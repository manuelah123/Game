package model;

public class Plataforma {

    private final double x;
    private final double y;
    private final double ancho;
    private final double alto;

    public Plataforma(double x, double y, double ancho, double alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Ancho y alto deben ser positivos.");
        }
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    // ── PIXEL ART (Ladrillo / Bloque) ────────────────────────
    private static final String _B="#c84800", _BL="#f8a840", _BD="#703000", _N=null;
    private static final String[][] TILE = {
        {_BL,_BL,_BL,_BL,_BL,_BL,_BL,_BL},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_B,_B,_B,_B,_B,_B,_BD},
        {_BL,_BD,_BD,_BD,_BD,_BD,_BD,_BD}
    };

    public String[][] getPixelFrame() {
        return TILE;
    }
}
