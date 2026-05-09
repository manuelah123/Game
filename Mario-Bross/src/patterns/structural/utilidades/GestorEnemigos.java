package patterns.structural.utilidades;

import model.Personaje;
import model.Enemigo;
import patterns.creational.ScoreManager;
import java.util.List;

/**
 * Gestiona los enemigos: movimiento y colisiones
 * Patrón: Single Responsibility Principle
 * Responsabilidad única: Gestionar enemigos
 */
public class GestorEnemigos {

    private static final int PUNTOS_STOMP = 200;
    private static final long PROTECCION_DANIO_MS = 900;

    private final ScoreManager scoreManager;

    public GestorEnemigos() {
        this.scoreManager = ScoreManager.getInstancia();
    }

    /**
     * Actualiza el movimiento de todos los enemigos activos
     */
    public void actualizarEnemigos(List<Enemigo> enemigos, double dt) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.isActivo()) {
                enemigo.actualizarMovimiento(dt);
            }
        }
    }

    /**
     * Procesa colisiones entre el personaje y enemigos
     * Dos casos:
     * 1. Stomp: El personaje salta sobre el enemigo desde arriba
     * 2. Daño: El personaje colisiona por el lado (pierde vida)
     */
    public void procesarColisionesEnemigos(Personaje personaje, List<Enemigo> enemigos) {
        long ahora = System.currentTimeMillis();

        for (Enemigo enemigo : enemigos) {
            // Saltar enemigos inactivos
            if (!enemigo.isActivo()) {
                continue;
            }

            // Detectar colisión AABB
            boolean colision = DetectorColisiones.intersecta(
                    personaje.getX(), personaje.getY(), 
                    personaje.getAncho(), personaje.getAlto(),
                    enemigo.getX(), enemigo.getY(), 
                    enemigo.getAncho(), enemigo.getAlto());

            if (!colision) {
                continue;
            }

            // CASO 1: Stomp (saltando sobre el enemigo)
            if (personaje.getVy() > 0 && DetectorColisiones.esStomped(
                    personaje.getY(), personaje.getAlto(), enemigo.getY())) {
                enemigo.desactivar();
                personaje.setVy(-220);  // Rebote al saltar
                scoreManager.agregarPuntos(PUNTOS_STOMP);
                continue;
            }

            // CASO 2: Daño (colisión por el lado)
            if (personaje.recibirDanioSiCorresponde(ahora, PROTECCION_DANIO_MS)) {
                // Daño fue aplicado exitosamente
                // (Se puede notificar aquí si es necesario)
            }
        }
    }
}