package controller;

import patterns.structural.EntradaJugador;
import patterns.structural.FachadaJuego;

public class GameLoop60FPS {

    private static final double DT = 1.0 / 60.0;
    private final FachadaJuego fachadaJuego;
    private final EntradaJugador entrada;

    public GameLoop60FPS(FachadaJuego fachadaJuego, EntradaJugador entrada) {
        this.fachadaJuego = fachadaJuego;
        this.entrada = entrada;
    }

    public void ejecutar(int ticksMaximos) {
        int tick = 0;
        while (ticksMaximos <= 0 || tick < ticksMaximos) {
            long inicio = System.currentTimeMillis();
            
            fachadaJuego.tick(DT, entrada);
            
            long fin = System.currentTimeMillis();
            long espera = 16 - (fin - inicio);
            if (espera > 0) {
                try { Thread.sleep(espera); } catch (InterruptedException e) {}
            }
            tick++;
        }
    }
}
