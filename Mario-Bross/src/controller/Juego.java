package controller;

import patterns.structural.FachadaJuego;
import view.ConsolaHUD;

public class Juego {

    private final FachadaJuego fachadaJuego;
    private final AdaptadorTecladoWeb entrada;
    private final GameLoop60FPS loop;
    private final ServidorWebJuego servidor;

    public Juego() {
        this.fachadaJuego = new FachadaJuego();
        this.entrada = new AdaptadorTecladoWeb();
        this.loop = new GameLoop60FPS(fachadaJuego, entrada);
        this.servidor = new ServidorWebJuego(fachadaJuego, entrada);

        this.fachadaJuego.agregarObservador(new ConsolaHUD());
    }

    public void iniciar() {
        try {
            servidor.iniciar(8080);
            System.out.println("Iniciando loop del juego...");
            loop.ejecutar(0); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
