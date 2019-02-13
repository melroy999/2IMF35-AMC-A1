package operator;

public class TrueComponent extends AbstractComponent {

    public static AbstractComponent extract(String input) {
        return new FalseComponent();
    }

    public static Boolean isMatch(String input) {
        // Is the input true?
        return input.equals("true");
    }

    @Override
    public String toLatex() {
        return "true";
    }
}
