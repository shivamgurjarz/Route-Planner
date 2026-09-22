import java.util.*;

public class Graph {
    private final LinkedHashSet<String> cities = new LinkedHashSet<>();
    private final Map<String, List<Edge>> adjacency = new LinkedHashMap<>();
    private final Deque<String> history = new ArrayDeque<>();

    static class Edge {
        String to;
        double distance;
        Edge(String to, double distance) { this.to = to; this.distance = distance; }
    }

    public static class PathResult {
        List<String> path;
        double distance;
        PathResult(List<String> path, double distance) { this.path = path; this.distance = distance; }
    }

    public synchronized void addCity(String name) {
        cities.add(name);
        adjacency.putIfAbsent(name, new ArrayList<>());
    }

    public synchronized Set<String> getCities() { return cities; }

    public synchronized boolean addRoad(String from, String to, double distance) {
        if (!cities.contains(from) || !cities.contains(to)) return false;
        adjacency.get(from).add(new Edge(to, distance));
        adjacency.get(to).add(new Edge(from, distance));
        return true;
    }

    public synchronized boolean updateRoad(String from, String to, double distance) {
        boolean found = false;
        for (Edge e : adjacency.getOrDefault(from, Collections.emptyList())) {
            if (e.to.equals(to)) { e.distance = distance; found = true; }
        }
        for (Edge e : adjacency.getOrDefault(to, Collections.emptyList())) {
            if (e.to.equals(from)) { e.distance = distance; found = true; }
        }
        return found;
    }

    public synchronized void removeRoad(String from, String to) {
        if (adjacency.containsKey(from)) adjacency.get(from).removeIf(e -> e.to.equals(to));
        if (adjacency.containsKey(to)) adjacency.get(to).removeIf(e -> e.to.equals(from));
    }

    public synchronized String edgesToJson() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        Set<String> seen = new HashSet<>();
        for (String city : cities) {
            for (Edge e : adjacency.getOrDefault(city, Collections.emptyList())) {
                String key1 = city + "->" + e.to, key2 = e.to + "->" + city;
                if (seen.contains(key1) || seen.contains(key2)) continue;
                seen.add(key1);
                if (!first) sb.append(",");
                first = false;
                sb.append("{\"from\":\"").append(esc(city)).append("\",\"to\":\"").append(esc(e.to))
                  .append("\",\"distance\":").append(e.distance).append("}");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String esc(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }

    public synchronized List<String> bfs(String start) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start); visited.add(start);
        while (!queue.isEmpty()) {
            String cur = queue.poll();
            order.add(cur);
            for (Edge e : adjacency.getOrDefault(cur, Collections.emptyList())) {
                if (!visited.contains(e.to)) { visited.add(e.to); queue.add(e.to); }
            }
        }
        return order;
    }

    public synchronized List<String> dfs(String start) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            String cur = stack.pop();
            if (visited.contains(cur)) continue;
            visited.add(cur);
            order.add(cur);
            List<Edge> neighbors = adjacency.getOrDefault(cur, Collections.emptyList());
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                Edge e = neighbors.get(i);
                if (!visited.contains(e.to)) stack.push(e.to);
            }
        }
        return order;
    }

    public synchronized PathResult dijkstra(String start, String end) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String c : cities) dist.put(c, Double.POSITIVE_INFINITY);
        dist.put(start, 0.0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);
        Set<String> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            String cur = pq.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);
            if (cur.equals(end)) break;
            for (Edge e : adjacency.getOrDefault(cur, Collections.emptyList())) {
                double nd = dist.get(cur) + e.distance;
                if (nd < dist.getOrDefault(e.to, Double.POSITIVE_INFINITY)) {
                    dist.put(e.to, nd);
                    prev.put(e.to, cur);
                    pq.add(e.to);
                }
            }
        }
        Double endDist = dist.get(end);
        if (endDist == null || endDist == Double.POSITIVE_INFINITY) return null;
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) { path.add(0, cur); cur = prev.get(cur); }
        return new PathResult(path, endDist);
    }

    public synchronized void pushHistory(String entry) {
        history.push(entry);
        while (history.size() > 20) history.removeLast();
    }

    public synchronized List<String> getHistory() { return new ArrayList<>(history); }

    public synchronized void reset() {
        cities.clear();
        adjacency.clear();
        history.clear();
    }
}
