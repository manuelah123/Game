package controller;

import patterns.structural.EntradaJugador;

public class AdaptadorTecladoWeb implements EntradaJugador {
    private boolean izq, der, jump;

    public synchronized void actualizar(boolean izq, boolean der, boolean jump) {
        this.izq = izq;
        this.der = der;
        this.jump = jump;
    }

    @Override public synchronized boolean moverIzquierda() { return izq; }
    @Override public synchronized boolean moverDerecha() { return der; }
    @Override public synchronized boolean saltar() { return jump; }
}
