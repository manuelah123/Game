package model;

/**
 * Modelo del personaje Mario.
 * BUGFIX salto: guarda prevY antes de integrar para pasarlo al detector.
 * Toda la lógica de animación (frame) está en Java — JS solo pinta.
 */
public class Personaje {

    private final String id;
    private double x, y, prevY;
    private final double ancho = 40;
    private final double alto  = 56;
    private double vx, vy;
    private int vidas = 3;
    private boolean enAire = false;
    private int facing = 1;
    private long invulnerableHastaMs = 0;
    private int tickAnim = 0;
    private static final int PASOS = 7;

    public Personaje(String id, double x, double y) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id obligatorio");
        this.id = id; this.x = x; this.y = y; this.prevY = y;
    }

    // ── Física ──────────────────────────────────────────────────
    public void setVelocidadHorizontal(double vx) {
        this.vx = vx;
        if (vx > 0) facing = 1;
        else if (vx < 0) facing = -1;
    }

    public void saltar(double fuerza) {
        if (!enAire) { vy = -Math.abs(fuerza); enAire = true; }
    }

    public void aplicarGravedad(double g, double dt) { vy += g * dt; }

    public void integrar(double dt) {
        prevY = y;                        // guardar antes de mover
        x += vx * dt;
        y += vy * dt;
        if (Math.abs(vx) > 1 && !enAire) tickAnim++;
    }

    public void aterrizar(double platY) {
        y  = platY - alto;
        vy = 0;
        enAire = false;
    }

    public void marcarEnAire() { enAire = true; }

    public boolean recibirDanioSiCorresponde(long ahoraMs, long protMs) {
        if (ahoraMs < invulnerableHastaMs || vidas <= 0) return false;
        vidas = Math.max(0, vidas - 1);
        invulnerableHastaMs = ahoraMs + protMs;
        return true;
    }

    public boolean estaVivo()        { return vidas > 0; }
    public boolean isInvulnerable()  { return System.currentTimeMillis() < invulnerableHastaMs; }

    // ── Getters ─────────────────────────────────────────────────
    public String  getId()     { return id; }
    public double  getX()      { return x; }
    public double  getY()      { return y; }
    public double  getPrevY()  { return prevY; }
    public double  getAncho()  { return ancho; }
    public double  getAlto()   { return alto; }
    public double  getVx()     { return vx; }
    public double  getVy()     { return vy; }
    public int     getVidas()  { return vidas; }
    public int     getFacing() { return facing; }
    public boolean isEnAire()  { return enAire; }
    public void    setX(double x)   { this.x = x; }
    public void    setVy(double vy) { this.vy = vy; }

    // ── PÍXEL ART — Java elige el frame ─────────────────────────
    private static final String _R="#e52121",_S="#e89050",_B="#2838c8",_D="#7b3020",_N=null;

    private static final String[][] IDLE = {
        {_N,_N,_N,_R,_R,_R,_R,_R,_N,_N,_N,_N},{_N,_N,_R,_R,_R,_R,_R,_R,_R,_R,_N,_N},
        {_N,_N,_D,_D,_D,_S,_S,_D,_S,_N,_N,_N},{_N,_D,_S,_D,_S,_S,_D,_S,_S,_S,_N,_N},
        {_N,_D,_S,_D,_D,_S,_S,_D,_S,_S,_S,_N},{_N,_D,_D,_S,_S,_S,_D,_D,_D,_D,_N,_N},
        {_N,_N,_N,_S,_S,_S,_S,_S,_N,_N,_N,_N},{_N,_N,_B,_B,_R,_B,_B,_N,_N,_N,_N,_N},
        {_N,_B,_B,_B,_R,_B,_B,_B,_N,_N,_N,_N},{_B,_B,_B,_R,_R,_B,_B,_B,_B,_N,_N,_N},
        {_S,_S,_R,_S,_S,_R,_B,_S,_S,_N,_N,_N},{_S,_S,_R,_R,_R,_R,_S,_S,_N,_N,_N,_N},
        {_N,_R,_R,_R,_N,_R,_R,_R,_N,_N,_N,_N},{_D,_D,_D,_N,_N,_N,_D,_D,_D,_N,_N,_N},
        {_D,_D,_D,_D,_N,_D,_D,_D,_D,_N,_N,_N},{_D,_D,_D,_D,_N,_D,_D,_D,_D,_N,_N,_N}
    };
    private static final String[][] JUMP = {
        {_N,_N,_N,_N,_N,_R,_R,_R,_R,_R,_N,_N},{_N,_N,_N,_R,_R,_R,_R,_R,_R,_R,_R,_R},
        {_N,_N,_N,_D,_D,_D,_S,_S,_D,_S,_N,_N},{_N,_N,_D,_S,_D,_S,_S,_S,_D,_S,_S,_S},
        {_N,_N,_D,_S,_D,_D,_S,_S,_S,_D,_S,_S},{_N,_N,_D,_D,_S,_S,_S,_S,_D,_D,_D,_D},
        {_N,_N,_N,_N,_S,_S,_S,_S,_S,_S,_S,_N},{_N,_N,_R,_R,_B,_R,_R,_R,_N,_N,_N,_N},
        {_N,_R,_R,_R,_R,_B,_R,_R,_R,_N,_B,_B},{_N,_S,_S,_R,_R,_B,_B,_B,_B,_B,_B,_B},
        {_S,_S,_N,_B,_B,_B,_B,_B,_B,_B,_B,_B},{_S,_N,_N,_B,_B,_B,_B,_B,_B,_B,_N,_N},
        {_N,_N,_B,_B,_B,_N,_N,_N,_N,_N,_N,_N},{_N,_B,_B,_B,_N,_N,_N,_N,_N,_N,_N,_N},
        {_N,_B,_B,_N,_N,_N,_N,_N,_N,_N,_N,_N},{_N,_N,_N,_N,_N,_N,_N,_N,_N,_N,_N,_N}
    };
    private static final String[][] WALK_A = {
        {_N,_N,_N,_R,_R,_R,_R,_R,_N,_N,_N,_N},{_N,_N,_R,_R,_R,_R,_R,_R,_R,_R,_N,_N},
        {_N,_N,_D,_D,_D,_S,_S,_D,_S,_N,_N,_N},{_N,_D,_S,_D,_S,_S,_D,_S,_S,_S,_N,_N},
        {_N,_D,_S,_D,_D,_S,_S,_D,_S,_S,_S,_N},{_N,_D,_D,_S,_S,_S,_D,_D,_D,_D,_N,_N},
        {_N,_N,_N,_S,_S,_S,_S,_S,_N,_N,_N,_N},{_N,_N,_B,_B,_R,_B,_B,_N,_N,_N,_N,_N},
        {_N,_B,_B,_B,_R,_B,_B,_B,_N,_N,_N,_N},{_B,_B,_B,_R,_R,_B,_B,_B,_B,_N,_N,_N},
        {_S,_S,_R,_S,_S,_R,_B,_S,_S,_N,_N,_N},{_S,_S,_R,_R,_R,_R,_S,_S,_N,_N,_N,_N},
        {_N,_R,_R,_R,_N,_R,_R,_R,_N,_N,_N,_N},{_N,_D,_D,_N,_N,_N,_N,_D,_D,_N,_N,_N},
        {_D,_D,_N,_N,_N,_N,_D,_D,_D,_N,_N,_N},{_D,_D,_N,_N,_N,_N,_N,_D,_D,_N,_N,_N}
    };
    private static final String[][] WALK_B = {
        {_N,_N,_N,_R,_R,_R,_R,_R,_N,_N,_N,_N},{_N,_N,_R,_R,_R,_R,_R,_R,_R,_R,_N,_N},
        {_N,_N,_D,_D,_D,_S,_S,_D,_S,_N,_N,_N},{_N,_D,_S,_D,_S,_S,_D,_S,_S,_S,_N,_N},
        {_N,_D,_S,_D,_D,_S,_S,_D,_S,_S,_S,_N},{_N,_D,_D,_S,_S,_S,_D,_D,_D,_D,_N,_N},
        {_N,_N,_N,_S,_S,_S,_S,_S,_N,_N,_N,_N},{_N,_N,_B,_B,_R,_B,_B,_N,_N,_N,_N,_N},
        {_N,_B,_B,_B,_R,_B,_B,_B,_N,_N,_N,_N},{_B,_B,_B,_R,_R,_B,_B,_B,_B,_N,_N,_N},
        {_S,_S,_R,_S,_S,_R,_B,_S,_S,_N,_N,_N},{_S,_S,_R,_R,_R,_R,_S,_S,_N,_N,_N,_N},
        {_N,_R,_R,_R,_N,_R,_R,_R,_N,_N,_N,_N},{_N,_N,_D,_D,_N,_D,_D,_N,_N,_N,_N,_N},
        {_N,_N,_D,_D,_N,_D,_D,_N,_N,_N,_N,_N},{_N,_N,_N,_D,_D,_D,_N,_N,_N,_N,_N,_N}
    };

    public String[][] getPixelFrame() {
        if (enAire) return JUMP;
        if (Math.abs(vx) > 1) return ((tickAnim / PASOS) % 2 == 0) ? WALK_A : WALK_B;
        return IDLE;
    }
}
