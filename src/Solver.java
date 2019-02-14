import graph.LTS;
import operator.AbstractComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solver {
    public static Set<Integer> solveNaive(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, Set<Integer>> A = new HashMap<>();

        // Call the solver.
        return formula.naiveEvaluate(graph, A);
    }
}
