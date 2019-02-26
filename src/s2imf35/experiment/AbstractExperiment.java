package s2imf35.experiment;

import s2imf35.Main;

import java.io.IOException;
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
}
