package operator;

import graph.LTS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class MuComponent extends AbstractComponent {
    // The components of the and operator.
    private final String variable;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[A-Z]");

    private MuComponent(String variable, AbstractComponent rhs) {
        this.variable = variable;
        this.rhs = rhs;
    }

    public static AbstractComponent extract(String input) {
        // Find the recursion variable.
        String variable = input.substring(3, 4);

        // Find the formula.
        String formula = input.substring(5, input.length());

        // Resolve the sub-components and make a new node.
        return new MuComponent(variable, parse(formula));
    }

    public static Boolean isMatch(String input) {
        if(input.startsWith("mu")) {
            // Find the recursion variable.
            String variable = input.substring(3, 4);

            // Is the label valid?
            return p.matcher(variable).find();
        }
        return false;
    }

    @Override
    public String toLatex() {
        return "\\mu " + variable + ".(" + rhs.toLatex() + ")";
    }

    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A) {
        return null;
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A) {
        // Start by filling A.
        A.put(variable, new HashSet<>());

        // Continue evaluating until A remains unchanged.
        Set<Integer> X;
        do {
            X = A.get(variable);
            A.put(variable, rhs.naiveEvaluate(graph, A));
        } while (!X.equals(A.get(variable)));

        return A.get(variable);
    }
}
