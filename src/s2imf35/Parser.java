package s2imf35;

import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A class that parses the input files and converts them to the desired object type.
 */
public class Parser {
    /**
     * Parse the given formula file and convert it to a parse tree.
     *
     * @param path The location of the input file.
     * @return A parse tree representing the formula in the file.
     * @throws IOException If the file cannot be found or read.
     */
    public static AbstractComponent parseFormulaFile(String path) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return parseFormula(contents);
    }

    /**
     * Parse the given graph file and convert it to an edge list representation.
     *
     * @param path The location of the input file.
     * @return A labelled transition system representing the graph defined in the input file.
     * @throws IOException If the file cannot be found or read.
     */
    public static LTS parseSystemFile(String path) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return parseSystem(contents);
    }

    /**
     * Parse the given formula and convert it to a parse tree.
     *
     * @param input The formula represented by a string.
     * @return A parse tree representing the formula given in the input string.
     */
    private static AbstractComponent parseFormula(String input) {
        // Convert the string to lines.
        String[] lines = input.split("\\r?\\n");

        // Remove all lines starting with a comment.
        input = Arrays.stream(lines).filter(e -> !e.startsWith("%")).collect(Collectors.joining());

        AbstractComponent formula = AbstractComponent.parse(input);

        // Ensure that the open variable tags are set correctly.
        formula.propagateOpenVariables();
        formula.propagateOpenSubFormulae();

        return formula;
    }

    /**
     * Parse the given graph and convert it to a parse tree.
     *
     * @param input The graph represented by a string.
     * @return A labelled transition system representing the formula given in the input string.
     */
    private static LTS parseSystem(String input) {
        return new LTS(input);
    }
}
