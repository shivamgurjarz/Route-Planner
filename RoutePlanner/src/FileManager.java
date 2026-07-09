import java.io.*;

public class FileManager {

    public static void saveGraph(Graph graph) {
        System.out.println("saveGraph() called");
        try {

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("data/routes.txt"));

            // Save Cities
            writer.write("CITIES");
            writer.newLine();

            for (City city : graph.getCities()) {
                writer.write(city.getName());
                writer.newLine();
            }

            // Save Roads
            writer.write("ROADS");
            writer.newLine();

            for (int i = 0; i < graph.getCities().size(); i++) {

                String source = graph.getCities().get(i).getName();

                for (Edge edge : graph.getGraph().get(i)) {

                    String destination =
                            graph.getCities()
                                 .get(edge.getDestination())
                                 .getName();

                    int distance = edge.getDistance();

                    writer.write(source + "," +
                                 destination + "," +
                                 distance);

                    writer.newLine();
                }
            }

            writer.close();

            System.out.println("Graph saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving file.");

        }

    }
}