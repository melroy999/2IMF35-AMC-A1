package s2imf35.experiment;

import s2imf35.Main;
import s2imf35.Solution;
import s2imf35.Solver;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

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

        Main.print(text, 0);
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

    protected static Solution getSolution(Boolean mode, LTS graph, AbstractComponent formula) {
        Solution solution;
        if(mode == null) {
            solution = Solver.solveNaive(formula, graph);
            Main.print("Naive Solution: " + solution, 1);

            Solution solution2 = Solver.solveEmersonLei(formula, graph);
            Main.print("Emerson-Lei Solution: " + solution2, 1);

            if(!solution.states.equals(solution2.states)) {
                Main.print("WARNING: THE SOLUTIONS OF THE NAIVE AND EMERSON-LEI ALGORITHMS ARE UNEQUAL!", 0);
            }
        } else if(!mode) {
            solution = Solver.solveNaive(formula, graph);
            Main.print("Naive Solution: " + solution, 1);
        } else {
            solution = Solver.solveEmersonLei(formula, graph);
            Main.print("Emerson-Lei Solution: " + solution, 1);
        }

        return solution;
    }
}
