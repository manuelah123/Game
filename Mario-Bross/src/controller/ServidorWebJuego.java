package controller;

import com.sun.net.httpserver.HttpServer;
import patterns.structural.FachadaJuego;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Servidor HTTP — sirve archivos estáticos y la API REST del juego.
 * BUGFIX: readBody ahora lee TODO el stream (no solo readLine)
 *         para que body sin salto de línea no retorne null.
 * Patrón: Adapter
 */
public class ServidorWebJuego {

    private final FachadaJuego        fachada;
    private final AdaptadorTecladoWeb entrada;

    public ServidorWebJuego(FachadaJuego fachada, AdaptadorTecladoWeb entrada) {
        this.fachada  = fachada;
        this.entrada  = entrada;
    }

    public void iniciar(int puerto) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);

        // GET / → archivos estáticos
        server.createContext("/", ex -> {
            String path = ex.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            File f = new File("web" + path);
            if (!f.exists()) { ex.sendResponseHeaders(404, -1); return; }
            byte[] bytes = Files.readAllBytes(f.toPath());
            ex.getResponseHeaders().set("Content-Type", contentType(path));
            ex.getResponseHeaders().set("Cache-Control", "no-cache");
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
        });

        // GET /state → JSON completo
        server.createContext("/state", ex -> {
            byte[] resp = JsonUtil.buildStateJson(fachada)
                                  .getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type",  "application/json");
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, resp.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(resp); }
        });

        // POST /input → "izq,der,saltar"
        server.createContext("/input", ex -> {
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                String body = readAll(ex.getRequestBody());
                if (body != null && !body.isEmpty()) {
                    String[] p = body.split(",");
                    if (p.length == 3)
                        entrada.actualizar(
                            "true".equals(p[0].trim()),
                            "true".equals(p[1].trim()),
                            "true".equals(p[2].trim()));
                }
            }
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(204, -1);
        });

        // POST /command → "start:N" | "menu"
        server.createContext("/command", ex -> {
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                String cmd = readAll(ex.getRequestBody());
                if (cmd != null) {
                    cmd = cmd.trim();
                    System.out.println("[CMD] recibido: '" + cmd + "'");
                    if (cmd.startsWith("start:")) {
                        try {
                            int lvl = Integer.parseInt(cmd.substring(6).trim());
                            System.out.println("[CMD] iniciarNivel(" + lvl + ")");
                            fachada.iniciarNivel(lvl);
                        } catch (NumberFormatException e) {
                            System.err.println("[CMD] numero invalido: " + cmd);
                        }
                    } else if ("menu".equals(cmd)) {
                        fachada.volverAlMenu();
                    }
                }
            }
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(204, -1);
        });

        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));
        server.start();
        System.out.println("=================================================");
        System.out.println("  ✅  http://localhost:" + puerto);
        System.out.println("  👉  Abre esa URL en tu navegador");
        System.out.println("=================================================");
        new Thread(() -> abrirNavegador(puerto)).start();
    }

    /** Lee TODO el body, no solo la primera línea. */
    private String readAll(InputStream is) {
        try {
            byte[] buf = is.readAllBytes();
            return new String(buf, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            return null;
        }
    }

    private String contentType(String path) {
        if (path.endsWith(".js"))  return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "text/html; charset=utf-8";
    }

    private void abrirNavegador(int puerto) {
        try {
            Thread.sleep(700);
            String url = "http://localhost:" + puerto;
            String os  = System.getProperty("os.name").toLowerCase();
            if      (os.contains("win"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else if (os.contains("mac"))
                Runtime.getRuntime().exec(new String[]{"open", url});
            else
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        } catch (Exception e) {
            System.err.println("Abre manualmente: http://localhost:" + puerto);
        }
    }
}
