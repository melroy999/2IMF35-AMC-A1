package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A class that represents the box modality operator node type.
 */
public class BoxModalityComponent extends AbstractComponent {
    // The components of the and operator.
    private final String label;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[a-z][a-z0-9_]*");

    // A mapping between each state and the edges that start in the state with the label associated with the operator.
    private HashMap<Integer, Set<Integer>> edgeLookupMapping = new HashMap<>();

    /**
     * Constructor for the default box modality component, used as a type detector.
     */
    BoxModalityComponent() {
        this.label = null;
        this.rhs = new TrueComponent();
    }

    /**
     * Create a box modality component with the given label and subtree.
     *
     * @param label The text between the [] symbols.
     * @param rhs The subtree over which the modality is defined.
     */
    private BoxModalityComponent(String label, AbstractComponent rhs) {
        this.label = label;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     * The input formula is only matches iff the formula starts with a string of the form '[[a-z][a-z0-9_]*]'.
     */
    public boolean isMatch(String input) {
        if(input.startsWith("[")) {
            // Find the label.
            int start = input.indexOf("[");
            int end = input.indexOf("]");
            String label = input.substring(start + 1, end);

            // Is the label valid?
            return p.matcher(label).find();
        }
        return false;
    }

    public AbstractComponent extract(String input) {
        // Find the label and formula.
        int start = input.indexOf("[");
        int end = input.indexOf("]");
        String label = input.substring(start + 1, end);
        String formula = input.substring(end + 1, input.length());

        // Resolve the sub-components and make a new node.
        return new BoxModalityComponent(label, parse(formula));
    }

    @Override
    public String toLatex() {
        return "[" + label + "]" + rhs.toLatex();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public BitSet emersonLei(LTS graph, Map<String, BitSet> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        // Evaluate the sub-formula.
        BitSet eval = rhs.emersonLei(graph, A, binderStack, counter);

        // For each state, check whether all transitions with the label satisfy the sub-formula.
        return findValidStates(graph, eval);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public BitSet naive(LTS graph, Map<String, BitSet> A, PerformanceCounter counter) {
        // Evaluate the sub-formula.
        BitSet eval = rhs.naive(graph, A, counter);

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
     * Find all the states that can reach all of the states that are valid under the sub-formula.
     *
     * @param graph The graph which we check the formula against against.
     * @param eval The evaluation of the sub-formula, given as a set of integers.
     * @return The set of states that can reach all of the states valid under the sub-formula.
     */
    private BitSet findValidStates(LTS graph, BitSet eval) {
        // The states in the result.
        BitSet result = new BitSet(graph.numberOfStates);

        for(int state = 0; state < graph.numberOfStates; state++) {
            // Find all transitions/endpoints starting at the state, with the given label.
            BitSet endPoints = graph.getEndpoints(state, label);

            // Check whether all endpoints are in eval. If it does, add the state to the result.
            if(containsAll(eval, endPoints)) {
                result.set(state);
            }
        }

        return result;
    }

    /**
     * Check whether all bits set true in the second BitSet are also true in the first BitSet.
     *
     * @param s1 The first BitSet.
     * @param s2 The second BitSet.
     * @return True when all set bits in @code{s2} are also set in @code{s1}.
     */
    private static boolean containsAll(BitSet s1, BitSet s2) {
        BitSet intersection = (BitSet) s1.clone();
        intersection.and(s2);
        return intersection.equals(s2);
    }
}
