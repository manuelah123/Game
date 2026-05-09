package patterns.creational;

/**
 * Singleton — gestiona el progreso global de la sesión.
 * BUGFIX: registrarNivelCompletado ahora marca todosCompletados
 *         correctamente cuando se termina el último nivel.
 * Patrón: Singleton
 */
public final class ProgresionJuego {

    private static final ProgresionJuego INSTANCIA = new ProgresionJuego();
    public  static final int TOTAL_NIVELES = 3;

    private int  nivelMaxDesbloqueado = 1;
    private int  puntajeAcumulado     = 0;
    private boolean juegoTerminado    = false;

    private ProgresionJuego() {}

    public static ProgresionJuego getInstancia() { return INSTANCIA; }

    /**
     * Registra que el jugador completó un nivel.
     * Si era el último, marca juegoTerminado = true.
     */
    public void registrarNivelCompletado(int nivelCompletado, int puntajeNivel) {
        puntajeAcumulado += puntajeNivel;
        if (nivelCompletado >= TOTAL_NIVELES) {
            // Completó el último nivel
            juegoTerminado = true;
        } else {
            nivelMaxDesbloqueado = Math.max(nivelMaxDesbloqueado, nivelCompletado + 1);
        }
    }

    public void reiniciar() {
        nivelMaxDesbloqueado = 1;
        puntajeAcumulado     = 0;
        juegoTerminado       = false;
    }

    public int     getNivelMaxDesbloqueado() { return nivelMaxDesbloqueado; }
    public int     getPuntajeAcumulado()     { return puntajeAcumulado; }
    public boolean estaDesbloqueado(int lvl) { return lvl <= nivelMaxDesbloqueado; }
    public boolean todosCompletados()        { return juegoTerminado; }
}
