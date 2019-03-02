package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.Edge;
import s2imf35.graph.LTS;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that represents the diamond modality operator node type.
 */
public class DiamondModalityComponent extends AbstractComponent {
    // The components of the and operator.
    private final String label;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[a-z][a-z0-9_]*");

    // A mapping between each state and the edges that start in the state with the label associated with the operator.
    private HashMap<Integer, Set<Integer>> edgeLookupMapping = new HashMap<>();

    /**
     * Constructor for the default diamond modality component, used as a type detector.
     */
    DiamondModalityComponent() {
        this.label = null;
        this.rhs = new TrueComponent();
    }

    /**
     * Create a diamond modality component with the given label and subtree.
     *
     * @param label The text between the <> symbols.
     * @param rhs The subtree over which the modality is defined.
     */
    private DiamondModalityComponent(String label, AbstractComponent rhs) {
        this.label = label;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     * The input formula is only matches iff the formula starts with a string of the form '<[a-z][a-z0-9_]*>'.
     */
    public boolean isMatch(String input) {
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

    public AbstractComponent extract(String input) {
        // Find the label and formula.
        int start = input.indexOf("<");
        int end = input.indexOf(">");
        String label = input.substring(start + 1, end);
        String formula = input.substring(end + 1, input.length());

        // Resolve the sub-components and make a new node.
        return new DiamondModalityComponent(label, parse(formula));
    }

    @Override
    public String toLatex() {
        return "\\text{<}" + label + "\\text{>}" + rhs.toLatex();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> emersonLei(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        // Evaluate the sub-formula.
        Set<Integer> eval = rhs.emersonLei(graph, A, binderStack, counter);

        // For each state, check whether all transitions with the label satisfy the sub-formula.
        return findValidStates(graph, eval);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> naive(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        // Evaluate the sub-formula.
        Set<Integer> eval = rhs.naive(graph, A, counter);

        // For each state, check whether all transitions with the label satisfy the sub-formula.
        return findValidStates(graph, eval);
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

    /**
     * Find all the states that can reach at least one of the states that are valid under the sub-formula.
     *
     * @param graph The graph which we check the formula against against.
     * @param eval The evaluation of the sub-formula, given as a set of integers.
     * @return The set of states that can reach any of the states valid under the sub-formula.
     */
    private Set<Integer> findValidStates(LTS graph, Set<Integer> eval) {
        // The states in the result.
        Set<Integer> result = new HashSet<>();

        for(int state : graph.S()) {
            // Find all transitions/endpoints starting at the state, with the given label.
            Set<Integer> endPoints = graph.getEndpoints(state, label);

            // Check whether any endpoints are in eval. If there are, add the state to the result.
            // Note that eval has to proceed endPoints in parameter list to yield the best performance!
            if(!Collections.disjoint(eval, endPoints)) {
                result.add(state);
            }
        }

        return result;
    }
}
