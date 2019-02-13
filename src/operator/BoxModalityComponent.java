package operator;

import java.util.regex.Pattern;

public class BoxModalityComponent extends AbstractComponent {
    // The components of the and operator.
    private final String lhs;
    private final AbstractComponent rhs;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[a-z][a-z0-9_]*");

    private BoxModalityComponent(String lhs, AbstractComponent rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public static AbstractComponent extract(String input) {
        // Find the label and formula.
        int start = input.indexOf("[");
        int end = input.indexOf("]");
        String label = input.substring(start + 1, end);
        String formula = input.substring(end + 1, input.length());

        // Resolve the sub-components and make a new node.
        return new BoxModalityComponent(label, parse(formula));
    }

    public static Boolean isMatch(String input) {
        if(input.startsWith("[")) {
            // Find the label.
            int start = input.indexOf("[");
            int end = input.indexOf("]");
            String label = input.substring(start + 1, end);

            // Is the label valid?
            return p.matcher(label).find();
        }
        return false;
    }

    @Override
    public String toLatex() {
        return "[" + lhs + "]" + rhs.toLatex();
    }
}
