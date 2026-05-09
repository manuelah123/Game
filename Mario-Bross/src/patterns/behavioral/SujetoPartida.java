package patterns.behavioral;

import java.util.ArrayList;
import java.util.List;

public class SujetoPartida {

    private final List<ObservadorPartida> observadores = new ArrayList<>();

    public void agregarObservador(ObservadorPartida observador) {
        observadores.add(observador);
    }

    public void notificar(EstadoPartida estadoPartida) {
        for (ObservadorPartida observador : observadores) {
            observador.alActualizar(estadoPartida);
        }
    }
}
