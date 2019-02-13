package operator;

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
}
