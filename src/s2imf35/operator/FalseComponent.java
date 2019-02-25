package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

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
    public Set<Integer> emersonLei(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        return new HashSet<>();
    }

    @Override
    public Set<Integer> naive(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        return new HashSet<>();
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
