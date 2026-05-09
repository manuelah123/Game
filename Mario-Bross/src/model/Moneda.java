package model;

public class Moneda {

    private final String id;
    private final double x;
    private final double y;
    private final int valorPuntos;
    private boolean activa;

    public Moneda(String id, double x, double y, int valorPuntos) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de moneda es obligatorio.");
        }
        if (valorPuntos <= 0) {
            throw new IllegalArgumentException("El valor de la moneda debe ser positivo.");
        }
        this.id = id;
        this.x = x;
        this.y = y;
        this.valorPuntos = valorPuntos;
        this.activa = true;
    }

    public boolean recoger() {
        if (!activa) {
            return false;
        }
        activa = false;
        return true;
    }

    public boolean isActiva() {
        return activa;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getValorPuntos() {
        return valorPuntos;
    }

    // ── PIXEL ART (En la clase respectiva) ──────────────────
    private static final String _Y="#f8d018", _YL="#fef870", _YD="#c8a000", _N=null;
    private static final String[][] FRAME = {
        {_N,_Y,_Y,_Y,_Y,_Y,_Y,_N},{_Y,_YL,_YL,_YL,_YL,_YL,_Y,_N},{_Y,_YL,_YD,_YD,_YD,_YL,_Y,_N},{_Y,_YL,_YD,_YL,_YD,_YL,_Y,_N},{_Y,_YL,_YD,_YD,_YD,_YL,_Y,_N},{_Y,_YL,_YL,_YL,_YL,_YL,_Y,_N},{_N,_Y,_Y,_Y,_Y,_Y,_Y,_N},{_N,_N,_N,_N,_N,_N,_N,_N}
    };

    public String[][] getPixelFrame() {
        return FRAME;
    }
}
