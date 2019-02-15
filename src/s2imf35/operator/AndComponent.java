package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class AndComponent extends AbstractComponent {
    // The components of the and operator.
    private final AbstractComponent lhs, rhs;

    private AndComponent(AbstractComponent lhs, AbstractComponent rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public static AbstractComponent extract(String input) {
        // Remove the brackets at the start and end.
        input = input.substring(1, input.length() - 1);

        // Find the index of the operator.
        int i = findRootAndIndex(input);

        // Split on the operator.
        String lhs = input.substring(0, i).trim();
        String rhs = input.substring(i + 2, input.length()).trim();

        // Resolve the sub-components and make a new node.
        return new AndComponent(parse(lhs), parse(rhs));
    }

    public static Boolean isMatch(String input) {
        if(!input.startsWith("(") && !input.endsWith(")")) {
            // The and operator is always surrounded by brackets.
            return false;
        }

        // Remove the brackets at the start and end.
        input = input.substring(1, input.length() - 1);

        // Check whether there is an && not enclosed by brackets.
        return findRootAndIndex(input) >= 0;
    }

    @Override
    public String toLatex() {
        return "(" + lhs.toLatex() + " \\wedge " + rhs.toLatex() + ")";
    }

    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        Set<Integer> lhsResult = lhs.evaluate(graph, A, binderStack, counter);
        Set<Integer> rhsResult = rhs.evaluate(graph, A, binderStack, counter);
        lhsResult.retainAll(rhsResult);
        return lhsResult;
    }

    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        Set<Integer> lhsResult = lhs.naiveEvaluate(graph, A, counter);
        Set<Integer> rhsResult = rhs.naiveEvaluate(graph, A, counter);
        lhsResult.retainAll(rhsResult);
        return lhsResult;
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

    private static int findRootAndIndex(String input) {
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

            if(c == '&' && d == 0) {
                return i;
            }
        }

        return -1;
    }
}
