package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public interface EstadoJuego {

    void actualizar(ContextoEstadoJuego contexto, FachadaJuego fachada, double deltaSegundos, EntradaJugador entrada);

    String nombre();
}
