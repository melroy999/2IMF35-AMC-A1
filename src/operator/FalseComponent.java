package operator;

import graph.LTS;

import java.util.*;

public class FalseComponent extends AbstractComponent {

    public static AbstractComponent extract(String input) {
        return new FalseComponent();
    }

    public static Boolean isMatch(String input) {
        // Is the input false?
        return input.equals("false");
    }

    @Override
    public String toLatex() {
        return "false";
    }

    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack) {
        return new HashSet<>();
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A) {
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
