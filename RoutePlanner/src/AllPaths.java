import java.util.ArrayList;

public class AllPaths {

    private final Graph graph;

    public AllPaths(Graph graph) {
        this.graph = graph;
    }

    public void findAllPaths(String sourceCity, String destinationCity) {

        if (!graph.getCityMap().containsKey(sourceCity)
                || !graph.getCityMap().containsKey(destinationCity)) {

            System.out.println("City not found.");
            return;
        }

        int source = graph.getCityMap().get(sourceCity);
        int destination = graph.getCityMap().get(destinationCity);

        boolean[] visited = new boolean[graph.getCities().size()];

        ArrayList<Integer> path = new ArrayList<>();

        System.out.println("\n===== ALL POSSIBLE ROUTES =====");

        dfs(source, destination, visited, path, 0);
    }

    private void dfs(int current,
                     int destination,
                     boolean[] visited,
                     ArrayList<Integer> path,
                     int distance) {

        visited[current] = true;

        path.add(current);

        if (current == destination) {

            for (int i = 0; i < path.size(); i++) {

                System.out.print(graph.getCities().get(path.get(i)).getName());

                if (i != path.size() - 1)
                    System.out.print(" -> ");
            }

            System.out.println("\nDistance : " + distance + " km");
            System.out.println("---------------------------");

        } else {

            for (Edge edge : graph.getGraph().get(current)) {

                int next = edge.getDestination();

                if (!visited[next]) {

                    dfs(next,
                            destination,
                            visited,
                            path,
                            distance + edge.getDistance());
                }
            }

        }

        path.remove(path.size() - 1);

        visited[current] = false;

    }

}