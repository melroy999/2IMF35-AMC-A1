package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;
import s2imf35.data.BitSet;

import java.util.*;

/**
 * A class that represents the or operator node type.
 */
public class OrComponent extends AbstractComponent {
    // The components of the and operator.
    private final AbstractComponent lhs, rhs;

    /**
     * Constructor for the default or component, used as a type detector.
     */
    OrComponent() {
        this.lhs = new TrueComponent();
        this.rhs = new TrueComponent();
    }

    /**
     * Create an or component between the two given subtrees.
     * @param lhs The left-hand side of the subtree.
     * @param rhs The right-hand side of the subtree.
     */
    private OrComponent(AbstractComponent lhs, AbstractComponent rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     * The input formula is only matches iff we have an or symbol surrounded by a balanced number of brackets. Note
     * that the outer brackets are not considered in the bracket count.
     */
    public boolean isMatch(String input) {
        if(!input.startsWith("(") && !input.endsWith(")")) {
            // The and operator is always surrounded by brackets.
            return false;
        }

        // Remove the brackets at the start and end.
        input = input.substring(1, input.length() - 1);

        // Check whether there is an && not enclosed by brackets.
        return findRootOrIndex(input) >= 0;
    }

    public AbstractComponent extract(String input) {
        // Remove the brackets at the start and end.
        input = input.substring(1, input.length() - 1);

        // Find the index of the operator.
        int i = findRootOrIndex(input);

        // Split on the operator.
        String lhs = input.substring(0, i).trim();
        String rhs = input.substring(i + 2, input.length()).trim();

        // Resolve the sub-components and make a new node.
        return new OrComponent(parse(lhs), parse(rhs));
    }

    @Override
    public String toLatex() {
        return "(" + lhs.toLatex() + " \\vee " + rhs.toLatex() + ")";
    }

    @Override
    public BitSet emersonLei(LTS graph, Map<String, BitSet> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        BitSet lhsResult = lhs.emersonLei(graph, A, binderStack, counter);
        BitSet rhsResult = rhs.emersonLei(graph, A, binderStack, counter);
        return lhsResult.or(rhsResult);
    }

    @Override
    public BitSet naive(LTS graph, Map<String, BitSet> A, PerformanceCounter counter) {
        BitSet lhsResult = lhs.naive(graph, A, counter);
        BitSet rhsResult = rhs.naive(graph, A, counter);
        return lhsResult.or(rhsResult);
    }

    @Override
    public List<AbstractComponent> propagateOpenSubFormulae() {
        // Find the open sub-formulae of both components.
        List<AbstractComponent> openLhs = lhs.propagateOpenSubFormulae();
        List<AbstractComponent> openRhs = rhs.propagateOpenSubFormulae();
        openLhs.addAll(openRhs);
        return openLhs;
    }

    @Override
    public Set<String> propagateOpenVariables() {
        Set<String> openLhs = lhs.propagateOpenVariables();
        Set<String> openRhs = rhs.propagateOpenVariables();
        openLhs.addAll(openRhs);
        return openLhs;
    }

    @Override
    public List<AbstractComponent> findVariableBindings(Set<String> boundVariables) {
        List<AbstractComponent>  openLhs = lhs.findVariableBindings(boundVariables);
        List<AbstractComponent>  openRhs = rhs.findVariableBindings(boundVariables);
        openLhs.addAll(openRhs);
        return openLhs;
    }

    /**
     * Find the index of an or operator surrounded by a balanced number of brackets (which places it at the root).
     *
     * @param input The input that potentially contains an and operator.
     * @return The index of the | character if an root and operator exists, -1 otherwise.
     */
    private static int findRootOrIndex(String input) {
        int d = 0;

        // Count the number of opening and closing brackets.
        for(int i = 0, n = input.length(); i < n; i++) {
            char c = input.charAt(i);

            if(c == '(') {
                d++;
            }

            if(c == ')') {
                d--;
            }

            if(c == '|' && d == 0) {
                return i;
            }
        }

        return -1;
    }
}
