package patterns.creational;

import java.util.ArrayList;
import java.util.List;
import model.Enemigo;
import model.Moneda;
import model.Nivel;
import model.Plataforma;

public class NivelBuilder {

    private int idNivel;
    private double metaX;
    private final List<Plataforma> plataformas = new ArrayList<>();
    private final List<Moneda> monedas = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();

    public NivelBuilder conId(int idNivel) {
        this.idNivel = idNivel;
        return this;
    }

    public NivelBuilder conMetaX(double metaX) {
        this.metaX = metaX;
        return this;
    }

    public NivelBuilder agregarPlataforma(Plataforma plataforma) {
        this.plataformas.add(plataforma);
        return this;
    }

    public NivelBuilder agregarMoneda(Moneda moneda) {
        this.monedas.add(moneda);
        return this;
    }

    public NivelBuilder agregarEnemigo(Enemigo enemigo) {
        this.enemigos.add(enemigo);
        return this;
    }

    public Nivel construir() {
        return new Nivel(idNivel, metaX, plataformas, monedas, enemigos);
    }
}
