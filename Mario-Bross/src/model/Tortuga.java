package model;

/**
 * Enemigo Tortuga/Koopa — sprite 46×44 píxeles
 * BUGFIX: Color verde corregido de "@00a800" a "#00a800"
 */
public class Tortuga extends Enemigo {

    public Tortuga(String id, double x, double y, double minX, double maxX) {
        super(id, "Tortuga", x, y, minX, maxX, 55);
        this.ancho = 46;
        this.alto  = 44;
    }

    @Override
    public void actualizarMovimiento(double dt) {
        moverPatrulla(dt);
    }

    // ── PIXEL ART 46×44 ─────────────────────────────────────────
    // BUGFIX: "_G" era "@00a800" (inválido). Corregido a "#00a800"
    private static final String _G  = "#00a800";
    private static final String _GD = "#007000";
    private static final String _Y  = "#f8c878";
    private static final String _E  = "#f8f8f8";
    private static final String _O  = "#c84800";
    private static final String _N  = null;

    private static final String[][] FRAME = {
        {_N,_N,_N,_G,_G,_G,_G,_G,_N,_N,_N,_N,_N,_N},
        {_N,_N,_G,_G,_GD,_GD,_GD,_G,_G,_N,_N,_N,_N,_N},
        {_N,_G,_G,_GD,_Y,_Y,_Y,_GD,_G,_G,_N,_N,_N,_N},
        {_G,_G,_GD,_Y,_E,_E,_Y,_Y,_GD,_G,_N,_N,_N,_N},
        {_G,_GD,_Y,_E,_N,_E,_Y,_Y,_GD,_G,_N,_N,_N,_N},
        {_G,_GD,_Y,_Y,_Y,_Y,_Y,_GD,_G,_N,_N,_N,_N,_N},
        {_G,_G,_GD,_GD,_GD,_GD,_G,_G,_N,_N,_N,_N,_N,_N},
        {_N,_G,_G,_G,_G,_G,_G,_N,_N,_N,_N,_N,_N,_N},
        {_N,_N,_Y,_Y,_Y,_Y,_Y,_Y,_N,_N,_N,_N,_N,_N},
        {_N,_Y,_Y,_O,_Y,_Y,_O,_Y,_Y,_N,_N,_N,_N,_N},
        {_Y,_Y,_Y,_Y,_Y,_Y,_Y,_Y,_Y,_N,_N,_N,_N,_N},
        {_Y,_Y,_E,_Y,_Y,_Y,_Y,_E,_Y,_N,_N,_N,_N,_N},
        {_Y,_Y,_N,_Y,_Y,_Y,_Y,_N,_Y,_N,_N,_N,_N,_N},
        {_N,_Y,_Y,_Y,_Y,_Y,_Y,_Y,_N,_N,_N,_N,_N,_N},
        {_N,_N,_Y,_Y,_Y,_Y,_Y,_N,_N,_N,_N,_N,_N,_N},
        {_N,_N,_N,_Y,_Y,_Y,_N,_N,_N,_N,_N,_N,_N,_N}
    };

    @Override
    public String[][] getPixelFrame() {
        return FRAME;
    }
}
