import java.util.*;

public class BFS {

    private final Graph graph;

    public BFS(Graph graph) {
        this.graph = graph;
    }

    public void traverse(String startCity) {

        HashMap<String, Integer> cityMap = graph.getCityMap();

        if (!cityMap.containsKey(startCity)) {
            System.out.println("City not found.");
            return;
        }

        int start = cityMap.get(startCity);

        boolean[] visited = new boolean[graph.getCities().size()];

        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        System.out.println("\n===== BFS Traversal =====");

        while (!queue.isEmpty()) {

            int current = queue.poll();

            System.out.println(graph.getCities().get(current).getName());

            for (Edge edge : graph.getGraph().get(current)) {

                int neighbour = edge.getDestination();

                if (!visited[neighbour]) {

                    visited[neighbour] = true;
                    queue.offer(neighbour);

                }
            }

        }

    }

}
