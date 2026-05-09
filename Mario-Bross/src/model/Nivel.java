package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nivel {

    private final int idNivel;
    private final double metaX;
    private final List<Plataforma> plataformas;
    private final List<Moneda> monedas;
    private final List<Enemigo> enemigos;

    public Nivel(int idNivel, double metaX, List<Plataforma> plataformas, List<Moneda> monedas, List<Enemigo> enemigos) {
        if (idNivel <= 0) {
            throw new IllegalArgumentException("El id de nivel debe iniciar en 1.");
        }
        if (metaX <= 0) {
            throw new IllegalArgumentException("La meta debe estar en una coordenada positiva.");
        }
        this.idNivel = idNivel;
        this.metaX = metaX;
        this.plataformas = new ArrayList<>(plataformas);
        this.monedas = new ArrayList<>(monedas);
        this.enemigos = new ArrayList<>(enemigos);
    }

    public int getIdNivel() {
        return idNivel;
    }

    public double getMetaX() {
        return metaX;
    }

    public List<Plataforma> getPlataformas() {
        return Collections.unmodifiableList(plataformas);
    }

    public List<Moneda> getMonedas() {
        return monedas;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }
}
