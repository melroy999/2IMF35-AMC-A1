package s2imf35.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import s2imf35.data.BitSet;

/**
 * A class representing a labelled transition system, represented by a list of edges.
 */
public class LTS {
    // Generic information.
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

    // All endpoints of edges with the given label, starting at the given state.
    private final HashMap<String, HashMap<Integer, BitSet>> modalityMap = new HashMap<>();

    /**
     * Convert the given graph in string representation to a labelled transition system represented by edge lists.
     *
     * @param system The graph in string form in Aldebaran format.
     */
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

        // Populate the modality map.
        for(String label : labelToEdge.keySet()) {
            HashMap<Integer, BitSet> map = new HashMap<>();

            for(int state = 0; state < numberOfStates; state++) {
                Stream<Edge> edges = start(state).stream().filter(e -> e.label.equals(label));

                BitSet bitSet = new BitSet(numberOfStates);
                Set<Integer> endStates = edges.map(e -> e.endNode).collect(Collectors.toSet());
                endStates.forEach(bitSet::set);
                map.put(state, bitSet);
            }

            modalityMap.put(label, map);
        }
    }

    /**
     * Get all edges in the graph with the given label.
     *
     * @param label The desired label.
     * @return A list of all edges that have the given label.
     */
    public List<Edge> label(String label) {
        return labelToEdge.getOrDefault(label, new ArrayList<>());
    }

    /**
     * Get all edges in the graph that start in the given node.
     *
     * @param node The starting node.
     * @return A list of all edges that start in the given node.
     */
    public List<Edge> start(int node) {
        return startToEdge.getOrDefault(node, new ArrayList<>());
    }

    /**
     * Get all edges in the graph that end in the given node.
     *
     * @param node The end node.
     * @return A list of all edges that end in the given node.
     */
    public List<Edge> end(int node) {
        return endToEdge.getOrDefault(node, new ArrayList<>());
    }

    /**
     * Get all the end points reachable through one transition with the given label, starting at the given state.
     *
     * @param node The starting node.
     * @param label The desired label.
     * @return A set of all nodes reachable from @code{node} through an edge with the label @code{label}.
     */
    public BitSet getEndpoints(int node, String label) {
        return modalityMap.getOrDefault(label, new HashMap<>()).getOrDefault(node, new BitSet(numberOfTransitions));
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

    /**
     * Generate a set containing all states found in the graph.
     *
     * @return A set of integers in the range [0, |V| - 1].
     */
    public BitSet S() {
        BitSet result = new BitSet(numberOfStates);
        result.set();
        return result;
    }

    /**
     * Convert the given LTS to an mcrl2 specification.
     *
     * @return The graph in mCRL2 format.
     */
    public String toMCRL2Spec() {
        StringBuilder specification = new StringBuilder();

        specification.append("act\n");
        specification.append("\t");
        specification.append(labelToEdge.keySet().stream().collect(Collectors.joining(", "))).append(";\n");
        specification.append("proc\n");

        // Are there states that do not have any outgoing transitions?
        for(int state = 0; state < numberOfStates; state++) {
            specification.append("\tS").append(state).append(" = ");

            if(startToEdge.containsKey(state)) {
                specification.append(startToEdge.get(state).stream()
                        .map(e -> e.label + ".S" + e.endNode).collect(Collectors.joining(" + "))).append(";\n");
            } else {
                specification.append("delta;\n");
            }
        }

        specification.append("init\n");
        specification.append("\tS").append(firstState).append(";");
        String result = specification.toString();

        if(result.contains("tau")) {
            System.out.println("Warning, the LTS contains the label tau, which is invalid in mCRL2.");
            System.out.println("Replacing tau with t. Replace tau in the formulas manually!\n");
            result = result.replace("tau", "t");
        }

        return result;
    }
}
