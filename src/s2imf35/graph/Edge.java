package s2imf35.graph;

/**
 * A class representing an edge in the labelled transition system.
 */
public class Edge {
    // The start and ending states of the edge.
    public final int startNode;
    public final int endNode;

    // The label on the edge.
    public final String label;

    /**
     * Create an edge between the two given nodes, with the given label.
     *
     * @param startNode The node the edge starts at.
     * @param label The label on the edge.
     * @param endNode The node the edge ends at.
     */
    public Edge(int startNode, String label, int endNode) {
        this.startNode = startNode;
        this.label = label;
        this.endNode = endNode;
    }

    @Override
    public String toString() {
        return "(" +
                startNode +
                ",\"" + label + "\"," +
                endNode +
                ')';
    }
}
