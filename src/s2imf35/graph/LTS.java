package s2imf35.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LTS {
    // Information about the LTS.
    public final int firstState;
    public final int numberOfTransitions;
    public final int numberOfStates;

    // The edges in the LTS.
    private final List<Edge> edges = new ArrayList<>();

    // The edges grouped by label.
    private final HashMap<String, List<Edge>> labelToEdge = new HashMap<>();

    // The edges grouped by starting point.
    private final HashMap<Integer, List<Edge>> startToEdge = new HashMap<>();

    // The edges grouped by end point.
    private final HashMap<Integer, List<Edge>> endToEdge = new HashMap<>();

    public LTS(String system) {
        // Convert the string to lines.
        String[] lines = system.split("\\r?\\n");

        // The first line contains information about the first state, number of transitions and number of states.
        int startIndex = lines[0].indexOf("(");
        int endIndex = lines[0].indexOf(")");
        String component = lines[0].substring(startIndex + 1, endIndex);
        String[] components = component.split(",");

        firstState = Integer.parseInt(components[0]);
        numberOfTransitions = Integer.parseInt(components[1]);
        numberOfStates = Integer.parseInt(components[2]);

        // Import all the edges.
        for(int i = 1; i < lines.length; i++) {
            components = lines[i].substring(1, lines[i].length() - 1).split(",");

            int startState = Integer.parseInt(components[0]);
            String label = components[1].substring(1, components[1].length() - 1);
            int endState = Integer.parseInt(components[2]);

            Edge edge = new Edge(startState, label, endState);
            edges.add(edge);

            List<Edge> edgeList = labelToEdge.getOrDefault(label, new ArrayList<>());
            edgeList.add(edge);
            labelToEdge.put(label, edgeList);

            edgeList = startToEdge.getOrDefault(startState, new ArrayList<>());
            edgeList.add(edge);
            startToEdge.put(startState, edgeList);

            edgeList = endToEdge.getOrDefault(endState, new ArrayList<>());
            edgeList.add(edge);
            endToEdge.put(endState, edgeList);
        }
    }

    public List<Edge> label(String label) {
        return labelToEdge.getOrDefault(label, new ArrayList<>());
    }

    public List<Edge> start(int node) {
        return startToEdge.getOrDefault(node, new ArrayList<>());
    }

    public List<Edge> end(int node) {
        return endToEdge.getOrDefault(node, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "LTS{" +
                "firstState=" + firstState +
                ", numberOfTransitions=" + numberOfTransitions +
                ", numberOfStates=" + numberOfStates +
                ", edges=" + Arrays.toString(edges.toArray()) +
                '}';
    }

    public Set<Integer> S() {
        return IntStream.range(0, numberOfStates - 1).boxed().collect(Collectors.toSet());
    }

    public class Edge {
        public final int startState;
        public final String label;
        public final int endState;

        public Edge(int startState, String label, int endState) {
            this.startState = startState;
            this.label = label;
            this.endState = endState;
        }

        @Override
        public String toString() {
            return "(" +
                    startState +
                    ",\"" + label + "\"," +
                    endState +
                    ')';
        }
    }
}
