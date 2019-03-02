package s2imf35;

import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;
import s2imf35.operator.MuComponent;
import s2imf35.operator.NuComponent;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Solver {
    /**
     * Solve the given formula naively, using the given graph.
     *
     * @param formula The formula that has to be evaluated.
     * @param graph The graph that has to be used in the evaluation.
     * @return The set of states in the graph for which the formula holds.
     */
    public static Solution solveNaive(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, BitSet> A = new HashMap<>();

        // Create a performance counter.
        PerformanceCounter counter = new PerformanceCounter();
        Instant start = Instant.now();

        // Call the solver and report.
        BitSet matches = formula.naive(graph, A, counter);
        Instant finish = Instant.now();
        counter.duration = Duration.between(start, finish).toMillis();

        // Print an ending empty line.
        Main.print("", 2);

        return new Solution(matches, counter);
    }

    /**
     * Solve the given formula with the Emerson-Lei Algorithm, using the given graph.
     *
     * @param formula The formula that has to be evaluated.
     * @param graph The graph that has to be used in the evaluation.
     * @return The set of states in the graph for which the formula holds.
     */
    public static Solution solveEmersonLei(AbstractComponent formula, LTS graph) {
        // Create our data structure A.
        Map<String, BitSet> A = new HashMap<>();

        // Create a performance counter.
        PerformanceCounter counter = new PerformanceCounter();
        Instant start = Instant.now();

        // Find all recursion variables and their bindings.
        List<AbstractComponent> bindings = formula.findVariableBindings(new HashSet<>());

        // Initialize A with the correct values.
        for(AbstractComponent c : bindings) {
            if(c instanceof MuComponent) {
                MuComponent mu = (MuComponent) c;
                A.put(mu.variable, new BitSet(graph.numberOfStates));
            } else if(c instanceof NuComponent) {
                NuComponent nu = (NuComponent) c;
                A.put(nu.variable, graph.S());
            } else {
                // Do nothing.
            }
        }

        // Call the solver and report.
        BitSet matches = formula.emersonLei(graph, A, new Stack<>(), counter);
        Instant finish = Instant.now();
        counter.duration = Duration.between(start, finish).toMillis();

        // Print an ending empty line.
        Main.print("", 2);

        // Call the solver.
        return new Solution(matches, counter);
    }
}
