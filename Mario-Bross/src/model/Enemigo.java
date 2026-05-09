package model;

public abstract class Enemigo {

    private final String id;
    private final String tipo;
    protected double x;
    protected double y;
    protected double ancho;
    protected double alto;
    protected double velocidad;
    protected final double minX;
    protected final double maxX;
    protected boolean activo;

    protected Enemigo(String id, String tipo, double x, double y, double minX, double maxX, double velocidad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del enemigo es obligatorio.");
        }
        if (maxX < minX) {
            throw new IllegalArgumentException("maxX debe ser mayor o igual a minX.");
        }
        this.id = id;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.minX = minX;
        this.maxX = maxX;
        this.velocidad = velocidad;
        this.activo = true;
    }

    public abstract void actualizarMovimiento(double dt);

    protected void moverPatrulla(double dt) {
        x += velocidad * dt;
        if (x <= minX || x >= maxX) {
            velocidad *= -1;
            x = Math.max(minX, Math.min(maxX, x));
        }
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
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

    public abstract String[][] getPixelFrame();
}
