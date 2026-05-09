package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

/**
 * Estado terminal: el jugador superó todos los niveles.
 * Patrón: State
 */
public class EstadoJuegoCompletado implements EstadoJuego {

    @Override
    public void actualizar(ContextoEstadoJuego ctx, FachadaJuego fachada,
                           double dt, EntradaJugador entrada) {
        // Terminal — espera comando de menú desde el frontend
    }

    @Override
    public String nombre() { return "JUEGO_COMPLETADO"; }
}
