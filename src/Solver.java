import graph.LTS;
import operator.AbstractComponent;
import operator.MuComponent;
import operator.NuComponent;

import java.util.*;

public class Solver {
    public static Set<Integer> solveNaive(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, Set<Integer>> A = new HashMap<>();

        // Call the solver.
        return formula.naiveEvaluate(graph, A);
    }

    public static Set<Integer> solve(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, Set<Integer>> A = new HashMap<>();

        // Find all recursion variables and their bindings.
        List<AbstractComponent> bindings = formula.findVariableBindings(new HashSet<>());

        // Initialize A with the correct values.
        for(AbstractComponent c : bindings) {
            if(c instanceof MuComponent) {
                MuComponent mu = (MuComponent) c;
                A.put(mu.variable, new HashSet<>());
            } else if(c instanceof NuComponent) {
                NuComponent nu = (NuComponent) c;
                A.put(nu.variable, graph.S());
            } else {
                // Do nothing.
            }
        }

        // Call the solver.
        return formula.evaluate(graph, A, new Stack<>());
    }
}
