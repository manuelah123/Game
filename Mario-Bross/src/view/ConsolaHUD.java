package view;

import patterns.behavioral.EstadoPartida;
import patterns.behavioral.ObservadorPartida;

public class ConsolaHUD implements VistaJuego, ObservadorPartida {

    @Override
    public void renderizar(EstadoPartida estadoPartida) {
        System.out.println(
                "Nivel=" + estadoPartida.getNivel()
                        + " | Vidas=" + estadoPartida.getVidas()
                        + " | Puntaje=" + estadoPartida.getPuntaje()
                        + " | Estado=" + estadoPartida.getMensaje());
    }

    @Override
    public void alActualizar(EstadoPartida estado) {
        renderizar(estado);
    }
}
