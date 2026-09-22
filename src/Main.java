import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    static Graph graph = new Graph();

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/cities", Main::handleCities);
        server.createContext("/api/roads", Main::handleRoads);
        server.createContext("/api/bfs", Main::handleBfs);
        server.createContext("/api/dfs", Main::handleDfs);
        server.createContext("/api/dijkstra", Main::handleDijkstra);
        server.createContext("/api/history", Main::handleHistory);
        server.createContext("/api/reset", Main::handleReset);
        server.createContext("/", Main::handleStatic);

        server.setExecutor(null);
        System.out.println("Smart Route Planner running on port " + port);
        server.start();
    }

    // ---------- Static file serving ----------
    static void handleStatic(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";
        Path filePath = Paths.get("public" + path);
        if (!Files.exists(filePath) || Files.isDirectory(filePath)) { notFound(ex); return; }
        byte[] bytes = Files.readAllBytes(filePath);
        ex.getResponseHeaders().set("Content-Type", guessContentType(path));
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    static String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }

    static void notFound(HttpExchange ex) throws IOException {
        byte[] bytes = "Not Found".getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(404, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    // ---------- Helpers ----------
    static Map<String, String> parseForm(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return parseQuery(body);
    }

    static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new LinkedHashMap<>();
        if (query == null || query.isEmpty()) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            try {
                String key = URLDecoder.decode(kv[0], "UTF-8");
                String value = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                map.put(key, value);
            } catch (Exception e) { /* ignore malformed pair */ }
        }
        return map;
    }

    static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    static String jsonArray(List<String> items) {
        return "[" + items.stream().map(Main::jsonString).collect(Collectors.joining(",")) + "]";
    }

    static String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    // ---------- Handlers ----------
    static void handleCities(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if (method.equals("GET")) {
            sendJson(ex, 200, jsonArray(new ArrayList<>(graph.getCities())));
        } else if (method.equals("POST")) {
            Map<String, String> form = parseForm(ex);
            String name = form.get("name");
            if (name == null || name.trim().isEmpty()) { sendJson(ex, 400, "{\"error\":\"name required\"}"); return; }
            graph.addCity(name.trim());
            sendJson(ex, 200, jsonArray(new ArrayList<>(graph.getCities())));
        } else {
            sendJson(ex, 405, "{\"error\":\"method not allowed\"}");
        }
    }

    static void handleRoads(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if (method.equals("GET")) {
            sendJson(ex, 200, graph.edgesToJson());
        } else if (method.equals("POST")) {
            Map<String, String> form = parseForm(ex);
            String from = form.get("from"), to = form.get("to");
            double dist;
            try { dist = Double.parseDouble(form.get("distance")); }
            catch (Exception e) { sendJson(ex, 400, "{\"error\":\"invalid distance\"}"); return; }
            boolean ok = graph.addRoad(from, to, dist);
            if (!ok) { sendJson(ex, 400, "{\"error\":\"both cities must exist\"}"); return; }
            sendJson(ex, 200, graph.edgesToJson());
        } else if (method.equals("PUT")) {
            Map<String, String> form = parseForm(ex);
            String from = form.get("from"), to = form.get("to");
            double dist;
            try { dist = Double.parseDouble(form.get("distance")); }
            catch (Exception e) { sendJson(ex, 400, "{\"error\":\"invalid distance\"}"); return; }
            boolean ok = graph.updateRoad(from, to, dist);
            if (!ok) { sendJson(ex, 404, "{\"error\":\"road not found\"}"); return; }
            sendJson(ex, 200, graph.edgesToJson());
        } else if (method.equals("DELETE")) {
            Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
            graph.removeRoad(q.get("from"), q.get("to"));
            sendJson(ex, 200, graph.edgesToJson());
        } else {
            sendJson(ex, 405, "{\"error\":\"method not allowed\"}");
        }
    }

    static void handleBfs(HttpExchange ex) throws IOException {
        Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
        String start = q.get("start");
        if (start == null || !graph.getCities().contains(start)) { sendJson(ex, 400, "{\"error\":\"invalid start city\"}"); return; }
        List<String> order = graph.bfs(start);
        graph.pushHistory("BFS from " + start + ": " + String.join(" -> ", order));
        sendJson(ex, 200, "{\"order\":" + jsonArray(order) + "}");
    }

    static void handleDfs(HttpExchange ex) throws IOException {
        Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
        String start = q.get("start");
        if (start == null || !graph.getCities().contains(start)) { sendJson(ex, 400, "{\"error\":\"invalid start city\"}"); return; }
        List<String> order = graph.dfs(start);
        graph.pushHistory("DFS from " + start + ": " + String.join(" -> ", order));
        sendJson(ex, 200, "{\"order\":" + jsonArray(order) + "}");
    }

    static void handleDijkstra(HttpExchange ex) throws IOException {
        Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
        String start = q.get("start"), end = q.get("end");
        if (start == null || end == null || !graph.getCities().contains(start) || !graph.getCities().contains(end)) {
            sendJson(ex, 400, "{\"error\":\"invalid start/end city\"}"); return;
        }
        Graph.PathResult result = graph.dijkstra(start, end);
        if (result == null) { sendJson(ex, 200, "{\"error\":\"no path found\"}"); return; }
        graph.pushHistory("Dijkstra " + start + " -> " + end + ": " + result.distance + " km via " + String.join(" -> ", result.path));
        sendJson(ex, 200, "{\"path\":" + jsonArray(result.path) + ",\"distance\":" + result.distance + "}");
    }

    static void handleHistory(HttpExchange ex) throws IOException {
        sendJson(ex, 200, jsonArray(graph.getHistory()));
    }

    static void handleReset(HttpExchange ex) throws IOException {
        graph.reset();
        sendJson(ex, 200, "{\"status\":\"reset\"}");
    }
}
