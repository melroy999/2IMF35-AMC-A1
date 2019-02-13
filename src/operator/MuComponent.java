package operator;

import java.util.regex.Pattern;

public class MuComponent extends AbstractComponent {
    // The components of the and operator.
    private final String lhs;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[A-Z]");

    private MuComponent(String lhs, AbstractComponent rhs) {
        this.lhs = lhs;
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
        return "\\mu " + lhs + ".(" + rhs.toLatex() + ")";
    }
}
