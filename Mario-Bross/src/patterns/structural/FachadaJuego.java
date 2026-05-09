package patterns.structural;

import model.Nivel;
import model.Personaje;
import patterns.behavioral.EstadoPartida;
import patterns.behavioral.ObservadorPartida;
import patterns.behavioral.SujetoPartida;
import patterns.creational.DirectorNiveles;
import patterns.creational.ProgresionJuego;
import patterns.creational.ScoreManager;
import patterns.structural.utilidades.FisicaJuego;
import patterns.structural.utilidades.GestorEnemigos;
import patterns.structural.utilidades.GestorMonedas;
import state.ContextoEstadoJuego;
import state.EstadoJugando;
import state.EstadoMenu;

/**
 * Fachada — orquesta todos los subsistemas.
 * Agrega getters de progresión para que JsonUtil los exponga al JS.
 * Patrón: Facade
 */
public class FachadaJuego {

    private static final double VELOCIDAD_X  = 180;
    private static final double FUERZA_SALTO = 420;   // subido para alcanzar plataformas altas

    private final DirectorNiveles     directorNiveles;
    private final ScoreManager        scoreManager;
    private final ProgresionJuego     progresion;
    private final SujetoPartida       sujetoPartida;
    private final ContextoEstadoJuego contextoEstado;
    private final FisicaJuego         fisica;
    private final GestorEnemigos      gestorEnemigos;
    private final GestorMonedas       gestorMonedas;

    private Personaje personaje;
    private Nivel     nivelActual;
    private int       nivel;
    private String    mensaje;

    public FachadaJuego() {
        this.directorNiveles = new DirectorNiveles();
        this.scoreManager    = ScoreManager.getInstancia();
        this.progresion      = ProgresionJuego.getInstancia();
        this.sujetoPartida   = new SujetoPartida();
        this.contextoEstado  = new ContextoEstadoJuego();
        this.personaje       = new Personaje("P1", 60, 370);
        this.mensaje         = "MENU";
        this.fisica          = new FisicaJuego();
        this.gestorEnemigos  = new GestorEnemigos();
        this.gestorMonedas   = new GestorMonedas();
    }

    // ── Getters básicos ─────────────────────────────────────────
    public Personaje getPersonaje()   { return personaje; }
    public Nivel     getNivelActual() { return nivelActual; }
    public int       getNivel()       { return nivel; }
    public int       getScore()       { return scoreManager.getPuntaje(); }
    public String    getMensaje()     { return mensaje; }
    public String    getUiState()     { return contextoEstado.nombreEstado(); }

    // ── Getters de progresión (para JS) ─────────────────────────
    public int  getNivelMaxDesbloqueado() { return progresion.getNivelMaxDesbloqueado(); }
    public int  getPuntajeAcumulado()     { return progresion.getPuntajeAcumulado(); }
    public int  getTotalNiveles()         { return ProgresionJuego.TOTAL_NIVELES; }

    // ── Loop ────────────────────────────────────────────────────
    public void tick(double dt, EntradaJugador entrada) {
        contextoEstado.actualizar(this, dt, entrada);
        publicarEstado();
    }

    // ── Lógica de mundo (llamado por EstadoJugando) ──────────────
    public void actualizarMundo(double dt, EntradaJugador entrada) {
        procesarEntrada(entrada);
        fisica.aplicarGravedad(personaje, dt);
        fisica.integrar(personaje, dt);
        fisica.resolverColisionesPlataformas(personaje, nivelActual.getPlataformas());
        gestorEnemigos.actualizarEnemigos(nivelActual.getEnemigos(), dt);
        gestorEnemigos.procesarColisionesEnemigos(personaje, nivelActual.getEnemigos());
        gestorMonedas.procesarMonedas(personaje, nivelActual.getMonedas());

        // Caída al vacío
        if (personaje.getY() > 540) {
            personaje.recibirDanioSiCorresponde(System.currentTimeMillis() - 2000, 100);
        }
    }

    // ── Iniciar nivel ────────────────────────────────────────────
    public void iniciarNivel(int idNivel) {
        if (!progresion.estaDesbloqueado(idNivel)) return;

        if (idNivel == 1) scoreManager.resetear();

        this.nivel       = idNivel;
        this.nivelActual = directorNiveles.construirNivel(idNivel);

        if (nivelActual == null) { mensaje = "SIN NIVEL"; return; }

        personaje = new Personaje("P1", 60, 370);
        mensaje   = "JUGANDO";
        contextoEstado.cambiarEstado(new EstadoJugando());
        publicarEstado();
    }

    public void volverAlMenu() {
        contextoEstado.cambiarEstado(new EstadoMenu());
        mensaje = "MENU";
        publicarEstado();
    }

    // ── Checks usados por estados ────────────────────────────────
    public boolean esGameOver()      { return !personaje.estaVivo(); }
    public boolean seCompletoNivel() { return nivelActual != null && personaje.getX() >= nivelActual.getMetaX(); }

    // ── Observer ─────────────────────────────────────────────────
    public void agregarObservador(ObservadorPartida obs) { sujetoPartida.agregarObservador(obs); }

    public void notificar(String msg) { this.mensaje = msg; publicarEstado(); }

    // ── Privados ─────────────────────────────────────────────────
    private void procesarEntrada(EntradaJugador entrada) {
        double vx = 0;
        if (entrada.moverIzquierda()) vx -= VELOCIDAD_X;
        if (entrada.moverDerecha())   vx += VELOCIDAD_X;
        personaje.setVelocidadHorizontal(vx);
        if (entrada.saltar()) personaje.saltar(FUERZA_SALTO);
    }

    private void publicarEstado() {
        sujetoPartida.notificar(new EstadoPartida(
                personaje.getVidas(), scoreManager.getPuntaje(),
                nivel, mensaje + " [" + contextoEstado.nombreEstado() + "]"));
    }
}
