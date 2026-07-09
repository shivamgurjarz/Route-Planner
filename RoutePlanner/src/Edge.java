public class Edge {

    private final int destination;
    private int distance;

    public Edge(int destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }

    public int getDestination() {
        return destination;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}