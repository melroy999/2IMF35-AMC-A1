package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiamondModalityComponent extends AbstractComponent {
    // The components of the and operator.
    private final String label;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[a-z][a-z0-9_]*");

    private DiamondModalityComponent(String label, AbstractComponent rhs) {
        this.label = label;
        this.rhs = rhs;
    }

    public static AbstractComponent extract(String input) {
        // Find the label and formula.
        int start = input.indexOf("<");
        int end = input.indexOf(">");
        String label = input.substring(start + 1, end);
        String formula = input.substring(end + 1, input.length());

        // Resolve the sub-components and make a new node.
        return new DiamondModalityComponent(label, parse(formula));
    }

    public static Boolean isMatch(String input) {
        if(input.startsWith("<")) {
            // Find the label.
            int start = input.indexOf("<");
            int end = input.indexOf(">");
            String label = input.substring(start + 1, end);

            // Is the label valid?
            return p.matcher(label).find();
        }
        return false;
    }

    @Override
    public String toLatex() {
        return "<" + label + ">" + rhs.toLatex();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        // Get the full set of states.
        Set<Integer> states = graph.S();

        // Evaluate the sub-formula.
        Set<Integer> eval = rhs.evaluate(graph, A, binderStack, counter);

        // The states in the result.
        Set<Integer> result = new HashSet<>();

        // For each state, check whether all transitions with the label satisfy the sub-formula.
        findValidStates(graph, states, eval, result);

        return result;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        // Get the full set of states.
        Set<Integer> states = graph.S();

        // Evaluate the sub-formula.
        Set<Integer> eval = rhs.naiveEvaluate(graph, A, counter);

        // The states in the result.
        Set<Integer> result = new HashSet<>();

        // For each state, check whether all transitions with the label satisfy the sub-formula.
        findValidStates(graph, states, eval, result);

        return result;
    }

    @Override
    public List<AbstractComponent> propagateOpenSubFormulae() {
        return rhs.propagateOpenSubFormulae();
    }

    @Override
    public Set<String> propagateOpenVariables() {
        return rhs.propagateOpenVariables();
    }

    @Override
    public List<AbstractComponent> findVariableBindings(Set<String> boundVariables) {
        return rhs.findVariableBindings(boundVariables);
    }

    private void findValidStates(LTS graph, Set<Integer> states, Set<Integer> eval, Set<Integer> result) {
        for(int state : states) {
            // Find all transitions/endpoints starting at the state, with the given label.
            Stream<LTS.Edge> edges = graph.start(state).stream().filter(e -> e.label.equals(label));
            Set<Integer> endPoints = edges.map(e -> e.endState).collect(Collectors.toSet());

            // Check whether any endpoints are in eval. If there are, add the state to the result.
            if(!Collections.disjoint(endPoints, eval)) {
                result.add(state);
            }
        }
    }
}
