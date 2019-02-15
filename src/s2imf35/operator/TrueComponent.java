package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.*;

public class TrueComponent extends AbstractComponent {

    public static AbstractComponent extract(String input) {
        return new TrueComponent();
    }

    public static Boolean isMatch(String input) {
        // Is the input true?
        return input.equals("true");
    }

    @Override
    public String toLatex() {
        return "true";
    }

    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        return graph.S();
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        return graph.S();
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
