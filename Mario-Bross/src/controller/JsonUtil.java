package controller;

import model.*;
import patterns.creational.ProgresionJuego;
import patterns.structural.FachadaJuego;
import java.util.List;

/**
 * Serializa el estado completo del juego a JSON.
 * JS consume este JSON y solo pinta — toda lógica está en Java.
 * Agrega: nivelMaxDesbloqueado, puntajeAcumulado, totalNiveles.
 */
public class JsonUtil {

    public static String buildStateJson(FachadaJuego fachada) {
        Personaje p = fachada.getPersonaje();
        Nivel     n = fachada.getNivelActual();

        StringBuilder sb = new StringBuilder("{");

        // ── Personaje ────────────────────────────────────────────
        sb.append("\"personaje\":{");
        sb.append("\"x\":").append(round(p.getX())).append(",");
        sb.append("\"y\":").append(round(p.getY())).append(",");
        sb.append("\"vx\":").append(round(p.getVx())).append(",");
        sb.append("\"vy\":").append(round(p.getVy())).append(",");
        sb.append("\"facing\":").append(p.getFacing()).append(",");
        sb.append("\"enAire\":").append(p.isEnAire()).append(",");
        sb.append("\"vidas\":").append(p.getVidas()).append(",");
        sb.append("\"invulnerable\":").append(p.isInvulnerable()).append(",");
        sb.append("\"pixels\":").append(matrixToJson(p.getPixelFrame()));
        sb.append("},");

        // ── Globales ─────────────────────────────────────────────
        sb.append("\"nivel\":").append(fachada.getNivel()).append(",");
        sb.append("\"puntaje\":").append(fachada.getScore()).append(",");
        sb.append("\"uiState\":\"").append(fachada.getUiState().toLowerCase()).append("\",");

        // ── Progresión (para candados y menú) ────────────────────
        sb.append("\"nivelMaxDesbloqueado\":").append(fachada.getNivelMaxDesbloqueado()).append(",");
        sb.append("\"puntajeAcumulado\":").append(fachada.getPuntajeAcumulado()).append(",");
        sb.append("\"totalNiveles\":").append(fachada.getTotalNiveles()).append(",");

        // ── Nivel ────────────────────────────────────────────────
        if (n != null) {
            sb.append("\"metaX\":").append(n.getMetaX()).append(",");

            sb.append("\"plataformas\":[");
            List<Plataforma> plats = n.getPlataformas();
            for (int i = 0; i < plats.size(); i++) {
                Plataforma pl = plats.get(i);
                sb.append("{\"x\":").append(round(pl.getX())).append(",");
                sb.append("\"y\":").append(round(pl.getY())).append(",");
                sb.append("\"ancho\":").append(round(pl.getAncho())).append(",");
                sb.append("\"alto\":").append(round(pl.getAlto())).append(",");
                sb.append("\"pixels\":").append(matrixToJson(pl.getPixelFrame())).append("}");
                if (i < plats.size() - 1) sb.append(",");
            }
            sb.append("],");

            sb.append("\"monedas\":[");
            List<Moneda> mons = n.getMonedas();
            for (int i = 0; i < mons.size(); i++) {
                Moneda m = mons.get(i);
                sb.append("{\"id\":\"").append(m.getId()).append("\",");
                sb.append("\"x\":").append(round(m.getX())).append(",");
                sb.append("\"y\":").append(round(m.getY())).append(",");
                sb.append("\"activa\":").append(m.isActiva()).append(",");
                sb.append("\"pixels\":").append(matrixToJson(m.getPixelFrame())).append("}");
                if (i < mons.size() - 1) sb.append(",");
            }
            sb.append("],");

            sb.append("\"enemigos\":[");
            List<Enemigo> enes = n.getEnemigos();
            for (int i = 0; i < enes.size(); i++) {
                Enemigo e = enes.get(i);
                sb.append("{\"tipo\":\"").append(e.getTipo()).append("\",");
                sb.append("\"x\":").append(round(e.getX())).append(",");
                sb.append("\"y\":").append(round(e.getY())).append(",");
                sb.append("\"activo\":").append(e.isActivo()).append(",");
                sb.append("\"pixels\":").append(matrixToJson(e.getPixelFrame())).append("}");
                if (i < enes.size() - 1) sb.append(",");
            }
            sb.append("]");
        } else {
            sb.append("\"metaX\":0,\"plataformas\":[],\"monedas\":[],\"enemigos\":[]");
        }

        sb.append("}");
        return sb.toString();
    }

    private static double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private static String matrixToJson(String[][] m) {
        if (m == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int r = 0; r < m.length; r++) {
            sb.append("[");
            for (int c = 0; c < m[r].length; c++) {
                if (m[r][c] == null) sb.append("null");
                else sb.append("\"").append(m[r][c]).append("\"");
                if (c < m[r].length - 1) sb.append(",");
            }
            sb.append(r < m.length - 1 ? "]," : "]");
        }
        return sb.append("]").toString();
    }
}
