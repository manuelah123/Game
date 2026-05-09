package patterns.creational;

import model.Nivel;
import model.Plataforma;
import model.Moneda;

/**
 * Construye los 3 niveles usando NivelBuilder (Builder Pattern).
 * Niveles más largos (mundo 2400px) con plataformas variadas.
 * La metaX (bandera) está al final del mundo.
 * Patrón: Builder + Director
 */
public class DirectorNiveles {

    private final FabricaEnemigos fab = new FabricaEnemigos();

    public Nivel construirNivel(int id) {
        switch (id) {
            case 1: return nivel1();
            case 2: return nivel2();
            case 3: return nivel3();
            default: return null;
        }
    }

    // ── NIVEL 1: Pradera — introducción suave ───────────────────
    private Nivel nivel1() {
        return new NivelBuilder()
            .conId(1).conMetaX(2200)

            // Suelo continuo
            .agregarPlataforma(new Plataforma(0,    440, 2400, 60))

            // Zona 1: escalera baja
            .agregarPlataforma(new Plataforma(180,  390,  90, 20))
            .agregarPlataforma(new Plataforma(310,  350, 100, 20))
            .agregarPlataforma(new Plataforma(460,  310, 110, 20))

            // Zona 2: plataformas medias
            .agregarPlataforma(new Plataforma(650,  360, 130, 20))
            .agregarPlataforma(new Plataforma(840,  300, 100, 20))
            .agregarPlataforma(new Plataforma(990,  260, 120, 20))

            // Zona 3: zigzag
            .agregarPlataforma(new Plataforma(1180, 330, 110, 20))
            .agregarPlataforma(new Plataforma(1340, 280, 100, 20))
            .agregarPlataforma(new Plataforma(1490, 340, 110, 20))
            .agregarPlataforma(new Plataforma(1650, 290, 100, 20))

            // Zona final
            .agregarPlataforma(new Plataforma(1820, 350, 130, 20))
            .agregarPlataforma(new Plataforma(1990, 300, 120, 20))

            // Monedas (encima de plataformas)
            .agregarMoneda(new Moneda("m1",  215, 358, 100))
            .agregarMoneda(new Moneda("m2",  345, 318, 100))
            .agregarMoneda(new Moneda("m3",  495, 278, 100))
            .agregarMoneda(new Moneda("m4",  685, 328, 100))
            .agregarMoneda(new Moneda("m5",  875, 268, 100))
            .agregarMoneda(new Moneda("m6", 1030, 228, 100))
            .agregarMoneda(new Moneda("m7", 1215, 298, 100))
            .agregarMoneda(new Moneda("m8", 1375, 248, 100))
            .agregarMoneda(new Moneda("m9", 1525, 308, 100))
            .agregarMoneda(new Moneda("m10",1855, 318, 100))

            // Enemigos
            .agregarEnemigo(fab.crear("Goomba","e1",  550, 406, 460, 640))
            .agregarEnemigo(fab.crear("Goomba","e2",  950, 406, 780, 1100))
            .agregarEnemigo(fab.crear("Goomba","e3", 1600, 406,1430, 1800))
            .agregarEnemigo(fab.crear("Goomba","e4", 2000, 406,1850, 2150))
            .construir();
    }

    // ── NIVEL 2: Cueva — plataformas más altas, Tortuga ────────
    private Nivel nivel2() {
        return new NivelBuilder()
            .conId(2).conMetaX(2300)

            .agregarPlataforma(new Plataforma(0,    440, 2500, 60))

            // Zona 1: subida
            .agregarPlataforma(new Plataforma(160,  380,  80, 20))
            .agregarPlataforma(new Plataforma(290,  330,  90, 20))
            .agregarPlataforma(new Plataforma(440,  280, 100, 20))
            .agregarPlataforma(new Plataforma(600,  240, 100, 20))

            // Zona 2: puente alto
            .agregarPlataforma(new Plataforma(760,  200, 160, 20))
            .agregarPlataforma(new Plataforma(980,  240, 100, 20))
            .agregarPlataforma(new Plataforma(1130, 290, 110, 20))

            // Zona 3: escalones descendentes
            .agregarPlataforma(new Plataforma(1300, 250, 100, 20))
            .agregarPlataforma(new Plataforma(1460, 300, 110, 20))
            .agregarPlataforma(new Plataforma(1620, 350, 120, 20))

            // Zona 4: final con pico
            .agregarPlataforma(new Plataforma(1810, 300, 110, 20))
            .agregarPlataforma(new Plataforma(1970, 240, 100, 20))
            .agregarPlataforma(new Plataforma(2120, 300, 120, 20))

            // Monedas
            .agregarMoneda(new Moneda("m1",  195, 348, 150))
            .agregarMoneda(new Moneda("m2",  325, 298, 150))
            .agregarMoneda(new Moneda("m3",  475, 248, 150))
            .agregarMoneda(new Moneda("m4",  635, 208, 150))
            .agregarMoneda(new Moneda("m5",  800, 168, 150))
            .agregarMoneda(new Moneda("m6", 1015, 208, 150))
            .agregarMoneda(new Moneda("m7", 1165, 258, 150))
            .agregarMoneda(new Moneda("m8", 1335, 218, 150))
            .agregarMoneda(new Moneda("m9", 1495, 268, 150))
            .agregarMoneda(new Moneda("m10",1845, 268, 150))
            .agregarMoneda(new Moneda("m11",2005, 208, 150))

            // Enemigos
            .agregarEnemigo(fab.crear("Goomba","e1",  370, 406,  280,  520))
            .agregarEnemigo(fab.crear("Tortuga","e2", 700, 406,  600,  920))
            .agregarEnemigo(fab.crear("Goomba","e3", 1200, 406, 1070, 1380))
            .agregarEnemigo(fab.crear("Tortuga","e4", 1700, 406,1560, 1930))
            .agregarEnemigo(fab.crear("Goomba","e5", 2050, 406,1950, 2280))
            .construir();
    }

    // ── NIVEL 3: Castillo — el más difícil ──────────────────────
    private Nivel nivel3() {
        return new NivelBuilder()
            .conId(3).conMetaX(2400)

            .agregarPlataforma(new Plataforma(0,    440, 2600, 60))

            // Zona 1: saltos cortos alternados
            .agregarPlataforma(new Plataforma(150,  400,  70, 20))
            .agregarPlataforma(new Plataforma(280,  360,  70, 20))
            .agregarPlataforma(new Plataforma(410,  310,  80, 20))
            .agregarPlataforma(new Plataforma(550,  270,  80, 20))

            // Zona 2: torre central alta
            .agregarPlataforma(new Plataforma(700,  380, 100, 20))
            .agregarPlataforma(new Plataforma(700,  300, 100, 20))
            .agregarPlataforma(new Plataforma(700,  220, 100, 20))
            .agregarPlataforma(new Plataforma(860,  260, 110, 20))

            // Zona 3: plataformas separadas (exige precisión)
            .agregarPlataforma(new Plataforma(1030, 310, 80, 20))
            .agregarPlataforma(new Plataforma(1180, 260, 80, 20))
            .agregarPlataforma(new Plataforma(1320, 200, 80, 20))
            .agregarPlataforma(new Plataforma(1460, 260, 80, 20))
            .agregarPlataforma(new Plataforma(1600, 310, 80, 20))

            // Zona 4: bajada con enemigos densos
            .agregarPlataforma(new Plataforma(1760, 270, 110, 20))
            .agregarPlataforma(new Plataforma(1930, 330, 120, 20))
            .agregarPlataforma(new Plataforma(2100, 280, 100, 20))
            .agregarPlataforma(new Plataforma(2260, 350, 130, 20))

            // Monedas (más difíciles de alcanzar)
            .agregarMoneda(new Moneda("m1",  185, 368, 200))
            .agregarMoneda(new Moneda("m2",  315, 328, 200))
            .agregarMoneda(new Moneda("m3",  445, 278, 200))
            .agregarMoneda(new Moneda("m4",  585, 238, 200))
            .agregarMoneda(new Moneda("m5",  735, 188, 200))
            .agregarMoneda(new Moneda("m6",  895, 228, 200))
            .agregarMoneda(new Moneda("m7", 1065, 278, 200))
            .agregarMoneda(new Moneda("m8", 1215, 228, 200))
            .agregarMoneda(new Moneda("m9", 1355, 168, 200))
            .agregarMoneda(new Moneda("m10",1635, 278, 200))
            .agregarMoneda(new Moneda("m11",1795, 238, 200))
            .agregarMoneda(new Moneda("m12",1965, 298, 200))
            .agregarMoneda(new Moneda("m13",2295, 318, 200))

            // Enemigos (más rápidos — heredan velocidad de Enemigo)
            .agregarEnemigo(fab.crear("Goomba","e1",  490, 406,  380,  620))
            .agregarEnemigo(fab.crear("Tortuga","e2", 950, 406,  820, 1120))
            .agregarEnemigo(fab.crear("Goomba","e3", 1250,406,  1100,1440))
            .agregarEnemigo(fab.crear("Tortuga","e4", 1500,406,  1400,1680))
            .agregarEnemigo(fab.crear("Goomba","e5", 1850,406,  1720,2000))
            .agregarEnemigo(fab.crear("Tortuga","e6", 2180,406,  2050,2350))
            .construir();
    }
}
