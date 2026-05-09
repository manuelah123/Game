package state;

import patterns.creational.ProgresionJuego;
import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

/**
 * Estado nivel completado.
 * Java registra la progresión; JS muestra la pantalla.
 * Patrón: State
 */
public class EstadoNivelCompletado implements EstadoJuego {

    private boolean registrado = false;

    @Override
    public void actualizar(ContextoEstadoJuego ctx, FachadaJuego fachada,
                           double dt, EntradaJugador entrada) {
        // Registrar progresión una sola vez
        if (!registrado) {
            registrado = true;
            ProgresionJuego.getInstancia()
                .registrarNivelCompletado(fachada.getNivel(), fachada.getScore());

            // Si ya no hay más niveles → juego completado
            if (ProgresionJuego.getInstancia().todosCompletados()) {
                ctx.cambiarEstado(new EstadoJuegoCompletado());
                fachada.notificar("JUEGO COMPLETADO");
            }
        }
    }

    @Override
    public String nombre() { return "NIVEL_COMPLETADO"; }
}
