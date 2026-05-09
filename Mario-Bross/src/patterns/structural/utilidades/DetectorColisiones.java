package patterns.structural.utilidades;

/**
 * AABB collision detection.
 * BUGFIX salto: tocandoDesdeArriba ahora recibe prevY para detectar
 * correctamente aunque el personaje viaje >20px en un tick.
 */
public class DetectorColisiones {

    public static boolean intersecta(double ax, double ay, double aw, double ah,
                                     double bx, double by, double bw, double bh) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    /**
     * El personaje aterriza en una plataforma si:
     *  - Hay overlap en X
     *  - La parte baja del personaje ANTES del tick estaba ENCIMA del tope
     *  - La parte baja AHORA está en o debajo del tope
     *  - Va cayendo (vy >= 0)
     */
    public static boolean tocandoDesdeArriba(double prevY, double currY,
                                              double alto, double vy, double platY) {
        double prevBottom = prevY + alto;
        double currBottom = currY + alto;
        return vy >= 0 && prevBottom <= platY + 1 && currBottom >= platY;
    }

    public static boolean esStomped(double py, double alto, double ey) {
        return (py + alto) - ey < 18;
    }
}
