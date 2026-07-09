public class Main {

    public static void main(String[] args) {
        
        
        Graph graph = new Graph();
        
        // Sample Data
        graph.addCity("Delhi");
        graph.addCity("Jaipur");
        graph.addCity("Agra");
        graph.addCity("Mumbai");

        graph.addRoad("Delhi", "Jaipur", 280);
        graph.addRoad("Delhi", "Agra", 220);
        graph.addRoad("Jaipur", "Mumbai", 1150);
        graph.addRoad("Agra", "Mumbai", 1400);
        FileManager.saveGraph(graph);
        Menu menu = new Menu(graph);

        menu.start();
    }
}