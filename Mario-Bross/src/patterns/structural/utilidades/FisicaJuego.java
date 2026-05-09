package patterns.structural.utilidades;

import model.Personaje;
import model.Plataforma;
import java.util.List;

/**
 * Física del juego.
 * BUGFIX: resolverColisionesPlataformas usa prevY para detectar
 * correctamente el aterrizaje sin importar la velocidad del tick.
 */
public class FisicaJuego {

    private static final double GRAVEDAD    = 750;
    private static final double LIMITE_X    = 0;

    public void aplicarGravedad(Personaje p, double dt) {
        p.aplicarGravedad(GRAVEDAD, dt);
    }

    public void integrar(Personaje p, double dt) {
        p.integrar(dt);
    }

    /**
     * Resolución de colisiones con plataformas.
     * Usa prevY (posición antes del tick) para no perder colisiones en ticks rápidos.
     */
    public void resolverColisionesPlataformas(Personaje p, List<Plataforma> plataformas) {
        p.marcarEnAire();

        for (Plataforma pl : plataformas) {
            // 1. Overlap horizontal
            boolean overlapX = p.getX() + p.getAncho() > pl.getX()
                            && p.getX()               < pl.getX() + pl.getAncho();
            if (!overlapX) continue;

            // 2. Pasó por encima del tope entre el tick anterior y el actual
            if (DetectorColisiones.tocandoDesdeArriba(
                    p.getPrevY(), p.getY(), p.getAlto(), p.getVy(), pl.getY())) {
                p.aterrizar(pl.getY());
            }
        }

        p.setX(Math.max(LIMITE_X, p.getX()));
    }
}
