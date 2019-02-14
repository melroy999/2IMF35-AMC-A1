package graph;

import java.util.Arrays;

public class LTS {
    // Information about the LTS.
    private final int firstState;
    private final int numberOfTransitions;
    private final int numberOfStates;

    // The edges in the LTS.
    private final Edge[] edges;

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
        edges = new Edge[numberOfTransitions];
        for(int i = 1; i < lines.length; i++) {
            components = lines[i].substring(1, lines[i].length() - 1).split(",");

            int firstState = Integer.parseInt(components[0]);
            String label = components[1].substring(1, components[1].length() - 1);
            int endState = Integer.parseInt(components[2]);
            edges[i - 1] = new Edge(firstState, label, endState);
        }
    }

    @Override
    public String toString() {
        return "LTS{" +
                "firstState=" + firstState +
                ", numberOfTransitions=" + numberOfTransitions +
                ", numberOfStates=" + numberOfStates +
                ", edges=" + Arrays.toString(edges) +
                '}';
    }

    public class Edge {
        private final int startState;
        private final String label;
        private final int endState;

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
