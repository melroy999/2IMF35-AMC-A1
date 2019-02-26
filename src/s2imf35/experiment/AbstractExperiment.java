package s2imf35.experiment;

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
}
