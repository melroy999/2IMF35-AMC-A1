package operator;

import graph.LTS;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A) {
        return graph.S();
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A) {
        return graph.S();
    }
}
