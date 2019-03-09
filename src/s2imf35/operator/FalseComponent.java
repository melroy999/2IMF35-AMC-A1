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
    public Set<Integer> emersonLei(LTS graph, Map<String, Set<Integer>> A, AbstractComponent lastBinder, PerformanceCounter counter) {
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

    @Override
    public int nestingDepth() {
        return 0;
    }

    @Override
    public int alternationDepth() {
        return 0;
    }

    @Override
    public int dependentAlternationDepth() {
        return 0;
    }

    @Override
    public void getRecursionVariables(Set<String> variables) {

    }

    @Override
    public void getMuFormulae(List<MuComponent> components) {

    }

    @Override
    public void getNuFormulae(List<NuComponent> components) {

    }

}
