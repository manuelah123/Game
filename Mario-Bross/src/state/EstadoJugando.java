package state;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public class EstadoJugando implements EstadoJuego {

    @Override
    public void actualizar(ContextoEstadoJuego contexto, FachadaJuego fachada, double deltaSegundos, EntradaJugador entrada) {
        fachada.actualizarMundo(deltaSegundos, entrada);
        if (fachada.esGameOver()) {
            contexto.cambiarEstado(new EstadoGameOver());
            fachada.notificar("GAME OVER");
            return;
        }
        if (fachada.seCompletoNivel()) {
            contexto.cambiarEstado(new EstadoNivelCompletado());
            fachada.notificar("NIVEL COMPLETADO");
        }
    }

    @Override
    public String nombre() {
        return "JUGANDO";
    }
}
