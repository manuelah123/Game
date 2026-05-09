package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public class EstadoGameOver implements EstadoJuego {

    @Override
    public void actualizar(ContextoEstadoJuego contexto, FachadaJuego fachada, double deltaSegundos, EntradaJugador entrada) {
        // Estado terminal: no avanza física ni input.
    }

    @Override
    public String nombre() {
        return "GAME_OVER";
    }
}
