package patterns.creational;

/**
 * Singleton: gestiona el puntaje global
 * BUGFIX: Se agrega resetear() para que al iniciar un nuevo nivel
 * el puntaje no acumule de partidas anteriores si el jugador vuelve al menú
 */
public final class ScoreManager {

    private static final ScoreManager INSTANCIA = new ScoreManager();
    private int puntaje;

    private ScoreManager() {
        this.puntaje = 0;
    }

    public static ScoreManager getInstancia() {
        return INSTANCIA;
    }

    public synchronized void agregarPuntos(int puntos) {
        if (puntos > 0) {
            puntaje += puntos;
        }
    }

    public synchronized int getPuntaje() {
        return puntaje;
    }

    /** Reinicia el puntaje (al iniciar partida nueva desde menú) */
    public synchronized void resetear() {
        puntaje = 0;
    }
}
