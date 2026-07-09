import java.util.*;

public class Dijkstra {

    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        
    }
    static class Node implements Comparable<Node> {

        int city;
        int distance;

        Node(int city, int distance) {
            this.city = city;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return this.distance - other.distance;
        }
    }

    public void shortestPath(String sourceCity, String destinationCity) {

        HashMap<String, Integer> cityMap = graph.getCityMap();
        
        if (!cityMap.containsKey(sourceCity) ||
                !cityMap.containsKey(destinationCity)) {

            System.out.println("City not found.");
            return;
        }

        int source = cityMap.get(sourceCity);
        int destination = cityMap.get(destinationCity);

        int n = graph.getCities().size();

        int[] distance = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Node> pq = new PriorityQueue<>();

        distance[source] = 0;
        pq.offer(new Node(source, 0));

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            int u = current.city;

            if (visited[u])
                continue;

            visited[u] = true;

            for (Edge edge : graph.getGraph().get(u)) {

                int v = edge.getDestination();
                int weight = edge.getDistance();

                if (!visited[v] && distance[u] + weight < distance[v]) {

                    distance[v] = distance[u] + weight;
                    parent[v] = u;

                    pq.offer(new Node(v, distance[v]));
                }
            }
        }

        if (distance[destination] == Integer.MAX_VALUE) {
            System.out.println("No path exists.");
            return;
        }
        //history.addRoute(sourceCity, destinationCity);
        printPath(parent, destination);

        System.out.println("\nTotal Distance : " + distance[destination] + " km");
    }

        private void printPath(int[] parent, int current) {

        if (current == -1)
            return;

        printPath(parent, parent[current]);

        System.out.print(graph.getCities().get(current).getName());

        if (parent[current] != -1)
            System.out.print(" -> ");
    }

}