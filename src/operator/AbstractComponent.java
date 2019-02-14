package operator;

import graph.LTS;

import java.util.Map;
import java.util.Set;

public abstract class AbstractComponent {
    public abstract String toLatex();
    public abstract Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A);
    public abstract Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A);

    public static AbstractComponent parse(String input) {
        // Remove padding spaces.
        input = input.trim();

        // Find the pattern that matches.
        if(AndComponent.isMatch(input)) {
            return AndComponent.extract(input);
        } else if(OrComponent.isMatch(input)) {
            return OrComponent.extract(input);
        } else if(RecursionVariableComponent.isMatch(input)) {
            return RecursionVariableComponent.extract(input);
        } else if(TrueComponent.isMatch(input)) {
            return TrueComponent.extract(input);
        } else if(FalseComponent.isMatch(input)) {
            return FalseComponent.extract(input);
        } else if(DiamondModalityComponent.isMatch(input)) {
            return DiamondModalityComponent.extract(input);
        } else if(BoxModalityComponent.isMatch(input)) {
            return BoxModalityComponent.extract(input);
        } else if(NuComponent.isMatch(input)) {
            return NuComponent.extract(input);
        } else if(MuComponent.isMatch(input)) {
            return MuComponent.extract(input);
        }

        // We might have brackets to worry about. Remove the outer brackets and call parse again.
        if(!input.startsWith("(") && !input.endsWith(")")) {
            input = input.substring(1, input.length() - 1);
            return parse(input);
        }

        // We failed to parse the input, throw an exception.
        throw new RuntimeException("Failed to parse input \"" + input + "\"");
    }

    @Override
    public String toString() {
        return toLatex();
    }
}
