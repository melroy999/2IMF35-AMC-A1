package s2imf35;

import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Parser {
    public static AbstractComponent parseFormulaFile(String path) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return parseFormula(contents);
    }

    public static LTS parseSystemFile(String path) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return parseSystem(contents);
    }

    private static AbstractComponent parseFormula(String input) {
        AbstractComponent formula = AbstractComponent.parse(input);

        // Ensure that the open variable tags are set correctly.
        formula.propagateOpenVariables();
        formula.propagateOpenSubFormulae();
        return formula;
    }

    private static LTS parseSystem(String system) {
        return new LTS(system);
    }
}
