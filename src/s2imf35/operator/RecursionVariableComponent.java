package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;
import s2imf35.data.BitSet;

import java.util.*;

/**
 * A class that represents the recursion variable symbol node type.
 */
public class RecursionVariableComponent extends AbstractComponent {
    // The components of the and operator.
    private final String name;

    /**
     * Constructor for the default recursion variable symbol, used as a type detector.
     */
    RecursionVariableComponent() {
        this.name = null;
    }

    /**
     * Create a recursion variable component with the desired name.
     *
     * @param name The name of the recursion variable.
     */
    private RecursionVariableComponent(String name) {
        this.name = name;
    }

    public AbstractComponent extract(String input) {
        return new RecursionVariableComponent(input.replaceAll("\\(\\)", ""));
    }

    /**
     * {@inheritDoc}
     * The input formula is only matches iff the given formula is a single capital letter [A-Z].
     */
    public boolean isMatch(String input) {
        // Is the input a single capital letter?
        return input.length() == 1 && Character.isUpperCase(input.charAt(0));
    }

    @Override
    public String toLatex() {
        return name;
    }

    @Override
    public BitSet emersonLei(LTS graph, Map<String, BitSet> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        return A.get(name);
    }

    @Override
    public BitSet naive(LTS graph, Map<String, BitSet> A, PerformanceCounter counter) {
        return A.get(name);
    }

    @Override
    public List<AbstractComponent> propagateOpenSubFormulae() {
        return new ArrayList<>();
    }

    @Override
    public Set<String> propagateOpenVariables() {
        return new HashSet<>(Collections.singletonList(name));
    }

    @Override
    public List<AbstractComponent> findVariableBindings(Set<String> boundVariables) {
        return boundVariables.contains(name) ? new ArrayList<>() : Collections.singletonList(this);
    }
}
