import java.util.Stack;

public class RouteHistory {

    private final Stack<String> history;

    public RouteHistory() {
        history = new Stack<>();
    }

    public void addRoute(String source, String destination) {
        history.push(source + " -> " + destination);
    }

    public void showHistory() {

        if (history.isEmpty()) {
            System.out.println("No routes searched yet.");
            return;
        }

        System.out.println("\n===== Route History =====");

        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.println(history.get(i));
        }
    }
}