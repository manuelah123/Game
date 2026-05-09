// ═══════════════════════════════════════════════════════════════
//  MARIO BROS — VISTA PURA (JavaScript)
//  JS solo pinta. Java decide toda la lógica.
//
//  BUGFIX CRÍTICO: buildLevelButtons() ya NO se llama en cada frame.
//  Solo se reconstruye cuando cambia nivelMaxDesbloqueado o al entrar al menú.
// ═══════════════════════════════════════════════════════════════

const canvas = document.getElementById("gameCanvas");
const ctx    = canvas.getContext("2d");
ctx.imageSmoothingEnabled = false;
const CW = 900, CH = 500;

const $ = id => document.getElementById(id);

// ── Estado local ──────────────────────────────────────────────
let gs        = null;
let paused    = false;
let muted     = false;
let prevUi    = null;
let prevUnlk  = -1;   // evita reconstruir botones si no cambió
let camX      = 0;

// ── Web Audio ─────────────────────────────────────────────────
let aCtx = null, masterGain = null, bgmTimer = null, bgmSched = 0;
const MELODY = [
    [659,.15],[659,.15],[0,.1],[659,.15],[0,.1],[523,.15],[659,.2],[784,.25],[0,.25],
    [392,.25],[0,.25],[523,.2],[0,.1],[392,.2],[0,.1],[330,.2],[0,.08],
    [440,.15],[494,.15],[0,.05],[466,.1],[440,.15],[0,.05],
    [392,.15],[659,.15],[784,.15],[880,.2],[698,.15],[784,.15],[0,.05],
    [659,.2],[0,.05],[523,.15],[587,.15],[494,.15],[0,.1]
];

function initAudio() {
    if (aCtx) return;
    try {
        aCtx = new (window.AudioContext || window.webkitAudioContext)();
        masterGain = aCtx.createGain();
        masterGain.gain.value = 0.18;
        masterGain.connect(aCtx.destination);
    } catch(e) {}
}
function nota(freq, t, dur, type="square", vol=0.14) {
    if (!aCtx || muted || freq === 0) return;
    const o = aCtx.createOscillator(), g = aCtx.createGain();
    o.type = type; o.frequency.value = freq;
    g.gain.setValueAtTime(vol, t);
    g.gain.exponentialRampToValueAtTime(0.001, t + dur * .9);
    o.connect(g); g.connect(masterGain); o.start(t); o.stop(t + dur);
}
function scheduleMelody(t0) {
    let t = t0;
    for (const [f, d] of MELODY) { nota(f, t, d * .85); t += d; }
    return t - t0;
}
function scheduleBass(t0) {
    const B = [131,131,0,131,0,131,131,196,196,0,196,0,165,0,196,0,
               220,247,0,233,220,0,196,330,392,440,349,392,0,330,0,262];
    let t = t0;
    for (const f of B) { nota(f, t, .18, "triangle", .08); t += .22; }
}
function loopBGM() {
    if (!aCtx || muted) return;
    const now = aCtx.currentTime;
    if (now < bgmSched - .4) return;
    const t0 = Math.max(now + .05, bgmSched);
    bgmSched = t0 + scheduleMelody(t0);
    scheduleBass(t0);
}
function musicPlay() {
    initAudio(); if (!aCtx) return;
    if (aCtx.state === "suspended") aCtx.resume();
    if (bgmTimer) return;
    bgmSched = 0; loopBGM();
    bgmTimer = setInterval(loopBGM, 800);
}
function musicStop() {
    if (bgmTimer) { clearInterval(bgmTimer); bgmTimer = null; }
    bgmSched = 0;
}

// ── SFX — un solo canal activo a la vez para evitar overlap ──
let sfxBloqueado = false;   // bloquea SFX reactivos durante transiciones de estado

function sfxCoin() {
    if (!aCtx||muted||sfxBloqueado) return;
    nota(1047,aCtx.currentTime,.06,"square",.18);
    nota(1319,aCtx.currentTime+.06,.09,"square",.15);
}
function sfxJump() {
    if (!aCtx||muted||sfxBloqueado) return;
    const o=aCtx.createOscillator(),g=aCtx.createGain();
    o.type="square"; o.frequency.setValueAtTime(260,aCtx.currentTime);
    o.frequency.exponentialRampToValueAtTime(580,aCtx.currentTime+.12);
    g.gain.setValueAtTime(.14,aCtx.currentTime); g.gain.exponentialRampToValueAtTime(.001,aCtx.currentTime+.18);
    o.connect(g); g.connect(masterGain); o.start(); o.stop(aCtx.currentTime+.18);
}
function sfxDmg() {
    if (!aCtx||muted||sfxBloqueado) return;
    const o=aCtx.createOscillator(),g=aCtx.createGain();
    o.type="sawtooth"; o.frequency.setValueAtTime(420,aCtx.currentTime);
    o.frequency.exponentialRampToValueAtTime(100,aCtx.currentTime+.28);
    g.gain.setValueAtTime(.18,aCtx.currentTime); g.gain.exponentialRampToValueAtTime(.001,aCtx.currentTime+.3);
    o.connect(g); g.connect(masterGain); o.start(); o.stop(aCtx.currentTime+.3);
}
// Fanfarria de victoria — bloquea SFX reactivos mientras suena
function sfxWin() {
    if (!aCtx||muted) return;
    sfxBloqueado = true;
    musicStop();
    [523,659,784,1047].forEach((f,i) => nota(f, aCtx.currentTime+i*.13, .16, "square", .18));
    setTimeout(() => { sfxBloqueado = false; }, 700);
}
// Melodía de game over
function sfxOver() {
    if (!aCtx||muted) return;
    sfxBloqueado = true;
    musicStop();
    [392,349,330,294,262].forEach((f,i) => nota(f, aCtx.currentTime+i*.2, .22, "sawtooth", .16));
    setTimeout(() => { sfxBloqueado = false; }, 1200);
}

// ── Comunicación con Java ─────────────────────────────────────
async function cmd(comando) {
    try {
        await fetch("/command", {
            method: "POST",
            headers: { "Content-Type": "text/plain" },
            body: comando
        });
    } catch(e) { console.error("[cmd]", e); }
}

// ── Input teclado → Java ──────────────────────────────────────
const keys = { ArrowLeft:false, ArrowRight:false, " ":false, ArrowUp:false };
let lastInputBody = "";
function sendInput() {
    if (paused) return;
    const body = `${keys.ArrowLeft},${keys.ArrowRight},${keys[" "]||keys.ArrowUp}`;
    if (body === lastInputBody) return;
    lastInputBody = body;
    fetch("/input", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body
    }).catch(()=>{});
}
document.addEventListener("keydown", e => {
    if (e.key in keys) { keys[e.key] = true; e.preventDefault(); }
    if (e.key==="p"||e.key==="P") {
        if (gs && (gs.uiState==="jugando"||paused)) togglePause();
    }
    sendInput();
});
document.addEventListener("keyup", e => {
    if (e.key in keys) keys[e.key] = false;
    sendInput();
});

// ── Botones ───────────────────────────────────────────────────
$("btnPause").onclick       = () => togglePause();
$("btnMute").onclick        = () => {
    initAudio();
    muted = !muted;
    if (masterGain) masterGain.gain.value = muted ? 0 : 0.18;
    $("btnMute").textContent = muted ? "🔇" : "🔊";
};
$("btnResume").onclick      = () => togglePause();
$("btnPauseMenu").onclick   = () => { paused=false; musicStop(); cmd("menu"); };
$("btnCompleteMenu").onclick    = () => { musicStop(); cmd("menu"); };
$("btnCompletedMenu").onclick   = () => { musicStop(); cmd("menu"); };
$("btnBackMenu").onclick        = () => { musicStop(); cmd("menu"); };
$("btnRestartLevel").onclick    = () => { if(gs){ musicPlay(); cmd("start:"+gs.nivel); } };
$("btnContinue").onclick        = () => { if(gs){ musicPlay(); cmd("start:"+(gs.nivel+1)); } };

function togglePause() {
    paused = !paused;
    $("btnPause").textContent = paused ? "▶" : "⏸";
    if (paused) { musicStop(); showOverlay("paused"); }
    else        { musicPlay(); showOverlay(gs ? gs.uiState : "menu"); }
}

// ── Menú: botones con candados ────────────────────────────────
// BUGFIX: Solo reconstruye cuando cambia nivelMaxDesbloqueado, NO en cada frame
function buildLevelButtons(unlk, total) {
    const $lb   = $("levelButtons");
    $lb.innerHTML = "";
    const names = ["Pradera Feliz","Cueva Oscura","Castillo Final"];

    for (let lvl = 1; lvl <= total; lvl++) {
        const open = lvl <= unlk;
        const btn  = document.createElement("button");
        btn.className = "btn-level" + (open ? "" : " locked");
        btn.disabled  = !open;

        if (open) {
            btn.innerHTML = `<span class="lvl-num">${lvl}</span>
                             <span class="lvl-name">${names[lvl-1]||"Nivel "+lvl}</span>`;
            const lvlCopy = lvl;   // closure correcta
            btn.addEventListener("click", () => {
                initAudio();
                musicPlay();
                cmd("start:" + lvlCopy);
            });
        } else {
            btn.innerHTML = `<span class="lvl-lock">🔒</span>
                             <span class="lvl-name">${names[lvl-1]||"Nivel "+lvl}</span>`;
        }
        $lb.appendChild(btn);
    }
    prevUnlk = unlk;
}

// ── Overlays ─────────────────────────────────────────────────
function showOverlay(estado) {
    $("menuOverlay")          .classList.toggle("hidden", estado !== "menu");
    $("pauseOverlay")         .classList.toggle("hidden", estado !== "paused");
    $("levelCompleteOverlay") .classList.toggle("hidden", estado !== "nivel_completado");
    $("gameCompletedOverlay") .classList.toggle("hidden", estado !== "juego_completado");
    $("gameOverOverlay")      .classList.toggle("hidden", estado !== "game_over");
    $("hud")                  .classList.toggle("hidden",
        estado === "menu" || estado === "juego_completado");
}

// ── Sync con Java (polling) ───────────────────────────────────
let syncing = false;

async function sync() {
    if (syncing || paused) return;
    syncing = true;
    try {
        const res  = await fetch("/state");
        if (!res.ok) { syncing = false; return; }
        const data = await res.json();

        // SFX reactivos solo si el estado no está cambiando en este tick
        const mismoEstado = gs && data.uiState === gs.uiState;
        if (gs && mismoEstado) {
            if (data.personaje.vidas  < gs.personaje.vidas)   sfxDmg();
            if (data.puntaje          > gs.puntaje)            sfxCoin();
            if (!gs.personaje.enAire && data.personaje.enAire) sfxJump();
        }

        gs = data;
        updateHUD();
        handleUiState();

    } catch(e) {}
    syncing = false;
}

function handleUiState() {
    if (!gs) return;
    const ui    = gs.uiState;
    const unlk  = gs.nivelMaxDesbloqueado || 1;
    const total = gs.totalNiveles || 3;

    // ── Menú: actualizar puntaje y botones solo si cambió algo ──
    if (ui === "menu") {
        $("menuScore").textContent = String(gs.puntajeAcumulado || 0).padStart(6, "0");
        // Solo reconstruir botones si cambió el nivel desbloqueado
        if (unlk !== prevUnlk) {
            buildLevelButtons(unlk, total);
        }
    }

    // ── Solo cambiar overlay/SFX cuando cambia el estado ────────
    if (ui === prevUi) return;
    const anterior = prevUi;
    prevUi = ui;

    showOverlay(ui);

    if (ui === "nivel_completado") {
        sfxWin();   // sfxWin ya llama musicStop internamente
        $("statNivel").textContent   = gs.nivel;
        $("statPuntaje").textContent = gs.puntaje;
        $("statVidas").textContent   = "❤️".repeat(Math.max(0, gs.personaje?.vidas||0));
    }
    if (ui === "juego_completado") {
        sfxWin();   // sfxWin ya llama musicStop internamente
        $("finalScore").textContent = gs.puntajeAcumulado || gs.puntaje;
    }
    if (ui === "game_over")  { sfxOver(); }  // sfxOver ya llama musicStop internamente
    if (ui === "jugando" && anterior !== "jugando") {
        musicPlay();
        camX = 0;
    }
}

function updateHUD() {
    if (!gs) return;
    $("puntaje").textContent = gs.puntaje || 0;
    $("nivel").textContent   = gs.nivel   || 1;
    const hh = $("hudHearts");
    hh.innerHTML = "";
    const vidas = gs.personaje?.vidas ?? 3;
    for (let i = 0; i < 3; i++) {
        const h = document.createElement("span");
        h.className   = "heart" + (i >= vidas ? " dead" : "");
        h.textContent = "❤️";
        hh.appendChild(h);
    }
}

// ── Cámara ────────────────────────────────────────────────────
function updateCam() {
    if (!gs?.personaje) return;
    const target = Math.max(0, gs.personaje.x - CW * 0.35);
    camX += (target - camX) * 0.12;
}

// ── Píxeles (matriz viene de Java) ────────────────────────────
function drawPixels(matrix, wx, wy, dw, dh, flip=false) {
    if (!matrix||!matrix.length) return;
    const rows=matrix.length, cols=matrix[0].length;
    const pw=dw/cols, ph=dh/rows;
    ctx.save();
    ctx.translate(wx - camX, wy);
    if (flip) { ctx.translate(dw,0); ctx.scale(-1,1); }
    for (let r=0; r<rows; r++) {
        for (let c=0; c<cols; c++) {
            const color = matrix[r][c];
            if (!color) continue;
            ctx.fillStyle = color;
            ctx.fillRect(c*pw, r*ph, pw+0.5, ph+0.5);
        }
    }
    ctx.restore();
}

// ── Fondo animado ─────────────────────────────────────────────
let worldT = 0;
function drawBg() {
    worldT += 0.016;
    const lvl = gs?.nivel || 1;
    const paletas = [["#5b9bd5","#a8d8ea"],["#2d1b4e","#5a2a8a"],["#0a0a1a","#1a1a3a"]];
    const [c1,c2] = paletas[Math.min(lvl-1,2)];
    const g = ctx.createLinearGradient(0,0,0,CH);
    g.addColorStop(0,c1); g.addColorStop(1,c2);
    ctx.fillStyle=g; ctx.fillRect(0,0,CW,CH);
    if (lvl===1) drawClouds();
    else if (lvl===2) drawCaveGlow();
    else drawStars();
}
function drawClouds() {
    ctx.fillStyle="rgba(255,255,255,0.78)";
    [[120,70],[360,50],[650,80]].forEach(([cx,cy])=>{
        const ox=((cx-camX*0.12)%(CW+150)+CW+150)%(CW+150)-75;
        ctx.beginPath();
        ctx.arc(ox+45,cy+19,45,0,Math.PI*2);
        ctx.arc(ox+22,cy+24,24,0,Math.PI*2);
        ctx.arc(ox+68,cy+24,22,0,Math.PI*2);
        ctx.fill();
    });
}
function drawCaveGlow() {
    [[100,430],[300,420],[550,425],[750,430]].forEach(([cx,cy])=>{
        const a=0.1+Math.sin(worldT*2+cx)*0.06;
        ctx.save(); ctx.globalAlpha=a;
        const rg=ctx.createRadialGradient(cx,cy,2,cx,cy,50);
        rg.addColorStop(0,"#c084fc"); rg.addColorStop(1,"transparent");
        ctx.fillStyle=rg; ctx.beginPath(); ctx.arc(cx,cy,50,0,Math.PI*2); ctx.fill();
        ctx.restore();
    });
}
function drawStars() {
    [[50,30],[190,55],[370,22],[550,45],[710,30],[860,50]].forEach(([sx,sy])=>{
        const tw=0.35+Math.sin(worldT*3+sx)*0.55;
        ctx.save(); ctx.globalAlpha=tw;
        ctx.fillStyle="#fff"; ctx.beginPath(); ctx.arc(sx,sy,1.5,0,Math.PI*2); ctx.fill();
        ctx.restore();
    });
    ctx.save(); ctx.globalAlpha=0.9;
    ctx.fillStyle="#fffde7"; ctx.shadowBlur=18; ctx.shadowColor="#fffde7";
    ctx.beginPath(); ctx.arc(820,55,24,0,Math.PI*2); ctx.fill();
    ctx.fillStyle="#0a0a1a";
    ctx.beginPath(); ctx.arc(832,50,19,0,Math.PI*2); ctx.fill();
    ctx.restore();
}

function drawMeta() {
    if (!gs) return;
    const mx=gs.metaX-camX;
    if (mx<-40||mx>CW+40) return;
    ctx.fillStyle="#999"; ctx.fillRect(mx,255,6,185);
    ctx.fillStyle="#e8222e";
    ctx.beginPath();
    ctx.moveTo(mx+6,255);
    ctx.quadraticCurveTo(mx+38+Math.sin(worldT*4)*6,267,mx+6,280);
    ctx.fill();
    ctx.save();
    ctx.translate(mx+3,249+Math.sin(worldT*5)*4);
    ctx.fillStyle="#f9d22b"; ctx.font="18px serif";
    ctx.textAlign="center"; ctx.textBaseline="middle";
    ctx.fillText("⭐",0,0); ctx.restore();
}

function drawPlatforms() {
    if (!gs?.plataformas) return;
    for (const pl of gs.plataformas) {
        const sx=pl.x-camX;
        if (sx+pl.ancho<0||sx>CW) continue;
        ctx.save(); ctx.globalAlpha=.13;
        ctx.fillStyle="#000"; ctx.fillRect(sx+3,pl.y+3,pl.ancho,pl.alto);
        ctx.restore();
        const tileW=24;
        for (let tx=pl.x; tx<pl.x+pl.ancho; tx+=tileW) {
            drawPixels(pl.pixels,tx,pl.y,Math.min(tileW,pl.x+pl.ancho-tx),pl.alto);
        }
        const sh=0.4+Math.sin(worldT*2+pl.x*.01)*.28;
        ctx.save(); ctx.globalAlpha=sh*.5;
        ctx.fillStyle="#fff"; ctx.fillRect(sx,pl.y,pl.ancho,2);
        ctx.restore();
    }
}

let coinT=0;
function drawCoins() {
    if (!gs?.monedas) return;
    coinT+=.016;
    for (const m of gs.monedas) {
        if (!m.activa) continue;
        const sx=m.x-camX;
        if (sx<-30||sx>CW+30) continue;
        const bob=Math.sin(coinT*5.5+m.x*.01)*5;
        const spin=Math.abs(Math.cos(coinT*4+m.x*.02));
        const dw=Math.max(1,24*spin);
        ctx.save(); ctx.globalAlpha=.18+Math.sin(coinT*6+m.x)*.1;
        const ag=ctx.createRadialGradient(sx+12,m.y+12+bob,0,sx+12,m.y+12+bob,18);
        ag.addColorStop(0,"#f9d22b"); ag.addColorStop(1,"transparent");
        ctx.fillStyle=ag; ctx.beginPath(); ctx.arc(sx+12,m.y+12+bob,18,0,Math.PI*2); ctx.fill();
        ctx.restore();
        drawPixels(m.pixels,m.x-(dw-24)/2,m.y+bob,dw,24);
    }
}

let enemT=0;
function drawEnemigos() {
    if (!gs?.enemigos) return;
    enemT+=.016;
    for (const e of gs.enemigos) {
        if (!e.activo) continue;
        const sx=e.x-camX;
        if (sx<-60||sx>CW+60) continue;
        const w=e.tipo==="Tortuga"?46:40, h=e.tipo==="Tortuga"?44:40;
        const yb=Math.sin(enemT*10+e.x*.01)*2;
        ctx.save(); ctx.globalAlpha=.18; ctx.fillStyle="#000";
        ctx.beginPath(); ctx.ellipse(sx+w/2,e.y+h+2,w*.38,4,0,0,Math.PI*2); ctx.fill();
        ctx.restore();
        drawPixels(e.pixels,e.x,e.y+yb,w,h);
        ctx.save(); ctx.globalAlpha=.5+Math.sin(enemT*4+e.x)*.25;
        ctx.fillStyle="#ff1744"; ctx.shadowBlur=5; ctx.shadowColor="#ff1744";
        ctx.beginPath(); ctx.arc(sx+w*.3,e.y+h*.35+yb,2,0,Math.PI*2); ctx.fill();
        ctx.beginPath(); ctx.arc(sx+w*.7,e.y+h*.35+yb,2,0,Math.PI*2); ctx.fill();
        ctx.restore();
    }
}

let marioT=0;
function drawPersonaje() {
    if (!gs?.personaje) return;
    const p=gs.personaje;
    marioT+=.016;
    if (p.invulnerable && Math.floor(marioT*14)%2===0) return;
    const sx=p.x-camX;
    const isJump=p.enAire, isMove=Math.abs(p.vx)>1;
    const sqx=isMove&&!isJump?1+Math.sin(marioT*14)*.04:1;
    const sqy=isJump?1.1:1;
    const dw=40*sqx, dh=56*sqy;
    const byc=isMove&&!isJump?Math.sin(marioT*16)*3:0;
    const ss=isJump?Math.max(.2,1-(p.y-380)*.005):1;
    ctx.save(); ctx.globalAlpha=.18*ss; ctx.fillStyle="#000";
    ctx.beginPath(); ctx.ellipse(sx+20,p.y+56+3,18*ss,5*ss,0,0,Math.PI*2); ctx.fill();
    ctx.restore();
    drawPixels(p.pixels,p.x,p.y+byc,dw,dh,p.facing===-1);
}

// ── Render ────────────────────────────────────────────────────
function render() {
    sync();
    updateCam();
    ctx.clearRect(0,0,CW,CH);
    const ui      = gs?.uiState || "menu";
    const playing = ui==="jugando"||ui==="nivel_completado"||ui==="paused";
    drawBg();
    if (playing) {
        drawMeta();
        drawPlatforms();
        drawCoins();
        drawEnemigos();
        drawPersonaje();
        if (ui==="paused") {
            ctx.save(); ctx.globalAlpha=.45;
            ctx.fillStyle="#000"; ctx.fillRect(0,0,CW,CH); ctx.restore();
        }
    }
    requestAnimationFrame(render);
}

// ── Arranque ─────────────────────────────────────────────────
showOverlay("menu");
buildLevelButtons(1, 3);   // botones iniciales con solo nivel 1 desbloqueado
render();
