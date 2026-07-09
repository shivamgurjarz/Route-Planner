import java.util.Scanner;

public class Menu {

    private final Graph graph;
    private final BFS bfs;
    private final DFS dfs;
    private final Dijkstra dijkstra;
    private final RouteHistory history;
    private final AllPaths allPaths;
    private final Scanner sc;
    
    public Menu(Graph graph) {

        this.graph = graph;
        this.bfs = new BFS(graph);
        this.dfs = new DFS(graph);
        this.dijkstra = new Dijkstra(graph);
        this.history = new RouteHistory();
        this.allPaths = new AllPaths(graph);

        sc = new Scanner(System.in);
    }

    public void start() {

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println("      SMART ROUTE PLANNER");
            System.out.println("======================================");
            System.out.println("1. Add City");
            System.out.println("2. Add Road");
            System.out.println("3. Display Cities");
            System.out.println("4. Display Roads");
            System.out.println("5. Display Route Map");
            System.out.println("6. BFS Traversal");
            System.out.println("7. DFS Traversal");
            System.out.println("8. Shortest Path");
            System.out.println("9. Update Road Distance");
            System.out.println("10. Remove Road");
            System.out.println("11. Route History");
            System.out.println("0. Exit");
            System.out.print("\nEnter Choice : ");

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:
                    addCity();
                    break;

                case 2:
                    addRoad();
                    break;

                case 3:
                    graph.displayCities();
                    break;

                case 4:
                    graph.displayRoads();
                    break;

                case 5:
                    graph.displayGraph();
                    break;

                case 6:
                    bfsTraversal();
                    break;

                case 7:
                    dfsTraversal();
                    break;

                case 8:
                    shortestPath();
                    break;

                case 9:
                    updateRoad();
                    break;

                case 10:
                    removeRoad();
                    break;
                
                case 11:
                    history.showHistory();
                    break;
                
                case 12:
                    allRoutes();
                    break;
                
                case 0:
                    System.out.println("Thank you for using Route Planner.");
                    break;

                default:
                    System.out.println("Invalid Choice.");
            }

        } while (choice != 0);

    }

    // ------------------------------

    private void addCity() {

        System.out.print("Enter City Name : ");
        String city = sc.nextLine();

        graph.addCity(city);
    }

    // ------------------------------

    private void addRoad() {

        System.out.print("Source City : ");
        String source = sc.nextLine();

        System.out.print("Destination City : ");
        String destination = sc.nextLine();

        System.out.print("Distance : ");
        int distance = sc.nextInt();

        sc.nextLine();

        graph.addRoad(source, destination, distance);

    }

    // ------------------------------

    private void bfsTraversal() {

        System.out.print("Start City : ");

        String city = sc.nextLine();

        bfs.traverse(city);

    }

    // ------------------------------

    private void dfsTraversal() {

        System.out.print("Start City : ");

        String city = sc.nextLine();

        dfs.traverse(city);

    }

    // ------------------------------

    private void shortestPath() {

        System.out.print("Source City : ");
        String source = sc.nextLine();

        System.out.print("Destination City : ");
        String destination = sc.nextLine();

        dijkstra.shortestPath(source, destination);

        history.addRoute(source, destination);
    }

    // ------------------------------

    private void updateRoad() {

        System.out.print("City 1 : ");
        String city1 = sc.nextLine();

        System.out.print("City 2 : ");
        String city2 = sc.nextLine();

        System.out.print("New Distance : ");
        int distance = sc.nextInt();

        sc.nextLine();

        graph.updateRoadDistance(city1, city2, distance);

    }

    // ------------------------------

    private void removeRoad() {

        System.out.print("City 1 : ");
        String city1 = sc.nextLine();

        System.out.print("City 2 : ");
        String city2 = sc.nextLine();

        graph.removeRoad(city1, city2);

    }

    // ------------------------------

    private void allRoutes() {

        System.out.print("Source City : ");
        String source = sc.nextLine();

        System.out.print("Destination City : ");
        String destination = sc.nextLine();

        allPaths.findAllPaths(source, destination);

    }

}