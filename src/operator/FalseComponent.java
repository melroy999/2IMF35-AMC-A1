package operator;

import graph.LTS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A) {
        return new HashSet<>();
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A) {
        return new HashSet<>();
    }
}
