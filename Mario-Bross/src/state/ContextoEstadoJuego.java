package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public class ContextoEstadoJuego {

    private EstadoJuego estadoActual;

    public ContextoEstadoJuego() {
        this.estadoActual = new EstadoMenu();
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void actualizar(FachadaJuego fachada, double deltaSegundos, EntradaJugador entrada) {
        estadoActual.actualizar(this, fachada, deltaSegundos, entrada);
    }

    public String nombreEstado() {
        return estadoActual.nombre();
    }
}
