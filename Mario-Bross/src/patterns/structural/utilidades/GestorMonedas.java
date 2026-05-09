package patterns.structural.utilidades;

import model.Personaje;
import model.Moneda;
import patterns.creational.ScoreManager;
import java.util.List;

/**
 * Gestiona la recolección de monedas y actualización del score
 * Patrón: Single Responsibility Principle
 * Responsabilidad única: Gestionar monedas
 */
public class GestorMonedas {

    private final ScoreManager scoreManager;

    public GestorMonedas() {
        this.scoreManager = ScoreManager.getInstancia();
    }

    /**
     * Procesa la colisión con monedas
     * Si el personaje toca una moneda, la recoge y suma puntos
     */
    public void procesarMonedas(Personaje personaje, List<Moneda> monedas) {
        for (Moneda moneda : monedas) {
            // Saltar monedas inactivas
            if (!moneda.isActiva()) {
                continue;
            }

            // Detectar colisión (moneda es 24x24)
            if (DetectorColisiones.intersecta(
                    personaje.getX(), personaje.getY(), 
                    personaje.getAncho(), personaje.getAlto(),
                    moneda.getX(), moneda.getY(), 
                    24, 24)) {

                // Intentar recoger la moneda
                if (moneda.recoger()) {
                    scoreManager.agregarPuntos(moneda.getValorPuntos());
                }
            }
        }
    }
}