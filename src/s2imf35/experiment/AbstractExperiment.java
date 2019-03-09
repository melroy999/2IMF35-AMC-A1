package s2imf35.experiment;

import s2imf35.*;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An abstract class representing an entire experiment in the exercise.
 */
public abstract class AbstractExperiment {
    /**
     * Run the formulas within the experiment and evaluate the results.
     *
     * @param argMap The arguments given for the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    public abstract void run(Map<String, Object> argMap) throws IOException;

    /**
     * Print a fancy header for the experiment.
     *
     * @param name The name of the experiment.
     */
    public void printHeader(String name) {
        int n = 56;
        String text = repeatText("=", n) + "\n"
                + "===" + repeatText(" ", (int) Math.floor((n - name.length()) / 2.0) - 3) + name.toUpperCase()
                + repeatText(" ", (int) Math.ceil((n - name.length()) / 2.0) - 3) + "===" + "\n"
                + repeatText("=", n) + "\n";

        System.out.println(text);
    }

    /**
     * Repeat a given character for the given number of times.
     *
     * @param symbol The symbol to repeat.
     * @param n The number of repetitions.
     * @return The string symbol repeated n times.
     */
    private String repeatText(String symbol, int n) {
        return new String(new char[n]).replaceAll("\0", symbol);
    }

    static Solution getSolution(Boolean mode, LTS graph, AbstractComponent formula) {
        Solution solution;
        if(mode == null) {
            solution = Solver.solveNaive(formula, graph);
            System.out.println("Naive Solution: " + solution);

            Solution solution2 = Solver.solveEmersonLei(formula, graph);
            System.out.println("Emerson-Lei Solution: " + solution2);

            if(!solution.states.equals(solution2.states)) {
                System.out.println("WARNING: THE SOLUTIONS OF THE NAIVE AND EMERSON-LEI ALGORITHMS ARE UNEQUAL!");
            }
        } else if(!mode) {
            solution = Solver.solveNaive(formula, graph);
            System.out.println("Naive Solution: " + solution);
        } else {
            solution = Solver.solveEmersonLei(formula, graph);
            System.out.println("Emerson-Lei Solution: " + solution);
        }

        return solution;
    }

    List<String> getFormulaPaths(File[] files) {
        return Arrays.stream(files).filter(e -> e.getName().endsWith(".mcf"))
                .map(File::getName).collect(Collectors.toList());
    }

    List<String> getGraphPaths(File[] files) {
        return Arrays.stream(files).filter(e -> e.getName().endsWith(".aut"))
                .map(File::getName).collect(Collectors.toList());
    }

    void runAllmethods(Boolean mode, String rootPath, List<String> formulaNames, List<String> graphNames, HashMap<String, HashMap<String, PerformanceCounter>> metrics) throws IOException {
        for(String formulaFile : formulaNames) {
            metrics.put(formulaFile, new HashMap<>());

            AbstractComponent formula = Parser.parseFormulaFile(rootPath + formulaFile);
            System.out.println("File '" + formulaFile + "': " + formula);
            System.out.println("Nesting depth: " + formula.nestingDepth());
            System.out.println("Alternation depth: " + formula.alternationDepth());
            System.out.println("Dependent Alternation depth: " + formula.dependentAlternationDepth());
            System.out.println();
        }

        // Print all the formulas for each graph file.
        for(String graphFile : graphNames) {
            System.out.println(">>> TESTING GRAPH FILE [" + graphFile.toUpperCase() + "] <<<");
            System.out.println("Loading graph file '" + graphFile.toUpperCase() + "'.\n");
            LTS graph = Parser.parseSystemFile(rootPath + graphFile);

            for(String formulaFile : formulaNames) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + formulaFile);
                System.out.println("File '" + formulaFile + "': " + formula);

                Solution solution = getSolution(mode, graph, formula);
                metrics.get(formulaFile).put(graphFile, solution.counter);

                // Print the solution under any verbosity level.
                System.out.println("Evaluation: " + solution.states.contains(graph.firstState));
                System.out.println();
            }

            System.out.println();
        }
    }
}
