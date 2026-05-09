package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public class EstadoMenu implements EstadoJuego {

    @Override
    public void actualizar(ContextoEstadoJuego contexto, FachadaJuego fachada, double deltaSegundos, EntradaJugador entrada) {
        // En el menú no hay física, solo esperamos una acción externa (startLevel)
        // que vendrá por la Fachada.
    }

    @Override
    public String nombre() {
        return "MENU";
    }
}
