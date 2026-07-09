import java.util.*;

public class DFS {

    private final Graph graph;

    public DFS(Graph graph) {
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

        Stack<Integer> stack = new Stack<>();

        stack.push(start);

        System.out.println("\n===== DFS Traversal =====");

        while (!stack.isEmpty()) {

            int current = stack.pop();

            if (visited[current]) {
                continue;
            }

            visited[current] = true;

            System.out.println(graph.getCities().get(current).getName());

            List<Edge> neighbours = graph.getGraph().get(current);

            // Push in reverse order so traversal looks natural
            for (int i = neighbours.size() - 1; i >= 0; i--) {

                int next = neighbours.get(i).getDestination();

                if (!visited[next]) {
                    stack.push(next);
                }
            }
        }
    }
}