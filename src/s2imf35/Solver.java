package s2imf35;

import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;
import s2imf35.operator.MuComponent;
import s2imf35.operator.NuComponent;

import java.util.*;

public class Solver {
    public static Set<Integer> solveNaive(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, Set<Integer>> A = new HashMap<>();

        // Create a performance counter.
        PerformanceCounter counter = new PerformanceCounter();

        // Call the solver and report.
        Set<Integer> matches = formula.naiveEvaluate(graph, A, counter);
        System.out.println(counter + "\t" + Arrays.toString(matches.toArray()));

        return matches;
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

        // Create a performance counter.
        PerformanceCounter counter = new PerformanceCounter();

        // Call the solver and report.
        Set<Integer> matches = formula.evaluate(graph, A, new Stack<>(), counter);
        System.out.println(counter + "\t" + Arrays.toString(matches.toArray()));

        // Call the solver.
        return matches;
    }
}
