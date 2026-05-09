package patterns.creational;

import model.Enemigo;
import model.Goomba;
import model.Tortuga;

public class FabricaEnemigos {

    public Enemigo crear(String tipo, String id, double x, double y, double minX, double maxX) {
        if ("Goomba".equalsIgnoreCase(tipo)) {
            return new Goomba(id, x, y, minX, maxX);
        }
        if ("Tortuga".equalsIgnoreCase(tipo)) {
            return new Tortuga(id, x, y, minX, maxX);
        }
        throw new IllegalArgumentException("Tipo de enemigo no soportado: " + tipo);
    }
}
