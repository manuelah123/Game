package model;

public class Goomba extends Enemigo {

    public Goomba(String id, double x, double y, double minX, double maxX) {
        super(id, "Goomba", x, y, minX, maxX, 70);
        this.ancho = 40;
        this.alto = 34;
    }

    @Override
    public void actualizarMovimiento(double dt) {
        moverPatrulla(dt);
    }

    // ── PIXEL ART (En la clase respectiva) ──────────────────
    private static final String _GB="#a83800", _GD="#703000", _GS="#f8c878", _GE="#f8f8f8", _GP="#f06030", _N=null;
    private static final String[][] FRAME = {
        {_N,_N,_GD,_GD,_GD,_GD,_GD,_GD,_N,_N},{_N,_GD,_GB,_GB,_GB,_GB,_GB,_GB,_GD,_N},{_GD,_GB,_GB,_GB,_GB,_GB,_GB,_GB,_GB,_GD},{_GB,_GE,_GE,_GB,_GB,_GE,_GE,_GB,_GB,_GB},{_GB,_GE,_N,_GB,_GB,_GE,_N,_GB,_GB,_GB},{_GB,_GB,_GP,_GP,_GP,_GP,_GB,_GB,_GB,_GB},{_N,_GS,_GS,_GS,_GS,_GS,_GS,_GS,_N,_N},{_GD,_GD,_GS,_GS,_GS,_GS,_GS,_GD,_GD,_N},{_GD,_GD,_GD,_GD,_GD,_GD,_GD,_GD,_GD,_N},{_N,_GD,_GD,_N,_N,_GD,_GD,_GD,_N,_N},{_GD,_GD,_N,_N,_N,_N,_GD,_GD,_N,_N},{_GD,_GD,_N,_N,_N,_N,_GD,_GD,_N,_N}
    };

    @Override
    public String[][] getPixelFrame() {
        return FRAME;
    }
}
