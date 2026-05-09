package patterns.behavioral;

public class EstadoPartida {

    private final int vidas;
    private final int puntaje;
    private final int nivel;
    private final String mensaje;

    public EstadoPartida(int vidas, int puntaje, int nivel, String mensaje) {
        this.vidas = vidas;
        this.puntaje = puntaje;
        this.nivel = nivel;
        this.mensaje = mensaje;
    }

    public int getVidas() {
        return vidas;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getNivel() {
        return nivel;
    }

    public String getMensaje() {
        return mensaje;
    }
}
