import java.util.*;

public class Graph {

    private final ArrayList<City> cities;
    private final ArrayList<ArrayList<Edge>> graph;
    private final HashMap<String, Integer> cityMap;

    public Graph() {
        cities = new ArrayList<>();
        graph = new ArrayList<>();
        cityMap = new HashMap<>();
    }

    // ==========================
    // CITY METHODS
    // ==========================

    public void addCity(String name) {

        if (cityMap.containsKey(name)) {
            System.out.println("City already exists.");
            return;
        }

        cities.add(new City(name));
        graph.add(new ArrayList<>());
        cityMap.put(name, cities.size() - 1);

        System.out.println(name + " added successfully.");
    }

    public boolean containsCity(String cityName) {
        return cityMap.containsKey(cityName);
    }

    public void displayCities() {

        System.out.println("\n========== Cities ==========");

        for (City city : cities) {
            System.out.println(city.getName());
        }
    }

    // ==========================
    // ROAD METHODS
    // ==========================

    public void addRoad(String city1, String city2, int distance) {

        if (!containsCity(city1) || !containsCity(city2)) {
            System.out.println("One or both cities not found.");
            return;
        }

        int u = cityMap.get(city1);
        int v = cityMap.get(city2);

        graph.get(u).add(new Edge(v, distance));
        graph.get(v).add(new Edge(u, distance));

        System.out.println("Road added successfully.");
    }

    public void updateRoadDistance(String city1,
                                   String city2,
                                   int newDistance) {

        if (!containsCity(city1) || !containsCity(city2)) {
            System.out.println("City not found.");
            return;
        }

        int u = cityMap.get(city1);
        int v = cityMap.get(city2);

        for (Edge edge : graph.get(u)) {

            if (edge.getDestination() == v) {
                edge.setDistance(newDistance);
            }

        }

        for (Edge edge : graph.get(v)) {

            if (edge.getDestination() == u) {
                edge.setDistance(newDistance);
            }

        }

        System.out.println("Road updated successfully.");
    }

    public void removeRoad(String city1, String city2) {

        if (!containsCity(city1) || !containsCity(city2)) {
            System.out.println("City not found.");
            return;
        }

        int u = cityMap.get(city1);
        int v = cityMap.get(city2);

        graph.get(u).removeIf(edge -> edge.getDestination() == v);
        graph.get(v).removeIf(edge -> edge.getDestination() == u);

        System.out.println("Road removed successfully.");
    }

    public void displayRoads() {

        System.out.println("\n========== Roads ==========");

        for (int i = 0; i < cities.size(); i++) {

            for (Edge edge : graph.get(i)) {

                System.out.println(
                        cities.get(i).getName()
                                + " ---> "
                                + cities.get(edge.getDestination()).getName()
                                + " ("
                                + edge.getDistance()
                                + " km)");
            }

        }
    }

    // ==========================
    // GRAPH DISPLAY
    // ==========================

    public void displayGraph() {

        System.out.println("\n========== Route Map ==========");

        for (int i = 0; i < cities.size(); i++) {

            System.out.print(cities.get(i).getName() + " -> ");

            for (Edge edge : graph.get(i)) {

                System.out.print(
                        cities.get(edge.getDestination()).getName()
                                + "("
                                + edge.getDistance()
                                + " km)   ");
            }

            System.out.println();
        }
    }

    // ==========================
    // GETTERS
    // ==========================

    public ArrayList<ArrayList<Edge>> getGraph() {
        return graph;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public HashMap<String, Integer> getCityMap() {
        return cityMap;
    }
}