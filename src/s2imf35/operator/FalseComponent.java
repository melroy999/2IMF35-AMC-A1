package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;
import s2imf35.data.BitSet;

import java.util.*;

/**
 * A class that represents the false symbol node type.
 */
public class FalseComponent extends AbstractComponent {

    /**
     * {@inheritDoc}
     * The input formula is only matches iff the formula equals 'false'.
     */
    public boolean isMatch(String input) {
        // Is the input false?
        return input.equals("false");
    }

    public AbstractComponent extract(String input) {
        return new FalseComponent();
    }

    @Override
    public String toLatex() {
        return "false";
    }

    @Override
    public BitSet emersonLei(LTS graph, Map<String, BitSet> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        return new BitSet(graph.numberOfStates);
    }

    @Override
    public BitSet naive(LTS graph, Map<String, BitSet> A, PerformanceCounter counter) {
        return new BitSet(graph.numberOfStates);
    }

    @Override
    public List<AbstractComponent> propagateOpenSubFormulae() {
        return new ArrayList<>();
    }

    @Override
    public Set<String> propagateOpenVariables() {
        return new HashSet<>();
    }

    @Override
    public List<AbstractComponent> findVariableBindings(Set<String> boundVariables) {
        return new ArrayList<>();
    }

}
