package operator;

public class RecursionVariableComponent extends AbstractComponent {
    // The components of the and operator.
    private final String name;

    private RecursionVariableComponent(String name) {
        this.name = name;
    }

    public static AbstractComponent extract(String input) {
        return new RecursionVariableComponent(input.replaceAll("\\(\\)", ""));
    }

    public static Boolean isMatch(String input) {
        // Is the input a single capital letter?
        return input.length() == 1 && Character.isUpperCase(input.charAt(0));
    }

    @Override
    public String toLatex() {
        return name;
    }
}
