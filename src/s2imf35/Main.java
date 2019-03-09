package s2imf35;

import s2imf35.experiment.*;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /**
     * Main call to the program.
     *
     * @param args The arguments given to the program, which are described in further detail in the report.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    public static void main(String[] args) throws IOException {
        // Create the mapping.
        Map<String, Object> argMap = parseArguments(args);

        // We output the unit test as our default.
        if(argMap.isEmpty()) {
            new UnitTest().run(argMap);
            return;
        }

        // Check which tests to perform. Here, single inputs have priority over experiment/unit tests.
        if(argMap.containsKey("-graph")) {
            run(argMap);
        } else {
            // Run the specified tests.
            for(String arg : argMap.keySet()) {
                switch (arg) {
                    case "-experiment1":
                        new Experiment1().run(argMap);
                        break;
                    case "-experiment2":
                        new Experiment2().run(argMap);
                        break;
                    case "-experiment3":
                        new Experiment3().run(argMap);
                        break;
                    case "-experiment4":
                        new Experiment4().run(argMap);
                        break;
                    case "-unit":
                        new UnitTest().run(argMap);
                        break;
                }
            }
        }
    }

    /**
     * Parse the array of arguments and convert it to a mapping for easy access.
     *
     * @param args The arguments to parse.
     * @return A mapping in which the value of each argument is stored.
     */
    private static Map<String, Object> parseArguments(String[] args) {
        Map<String, Object> data = new HashMap<>();

        // Read all of the arguments and parse to the desired format.
        for(String arg : args) {
            // Make the argument parser case insensitive.
            arg = arg.toLowerCase();

            if(arg.startsWith("-formula")) {
                data.put("-formula", arg.substring(arg.indexOf("=") + 1, arg.length()));
            } else if(arg.startsWith("-graph")) {
                data.put("-graph", arg.substring(arg.indexOf("=") + 1, arg.length()));
            } else if(arg.equals("-mode=improved") || arg.equals("-mode=1")) {
                data.put("-mode", true);
            } else if(arg.equals("-mode=naive") || arg.equals("-mode=0")) {
                data.put("-mode", false);
            } else if(arg.equals("-experiment1")) {
                data.put("-experiment1", true);
            } else if(arg.equals("-experiment2")) {
                data.put("-experiment2", true);
            } else if(arg.equals("-experiment3")) {
                data.put("-experiment3", true);
            } else if(arg.equals("-experiment4")) {
                data.put("-experiment4", true);
            } else if(arg.equals("-unit")) {
                data.put("-unit", true);
            } else if(arg.equals("-all")) {
                data.put("-unit", true);
                data.put("-experiment1", true);
                data.put("-experiment2", true);
                data.put("-experiment3", true);
                data.put("-experiment4", true);
            }
        }

        return data;
    }

    /**
     * Run a single formula-graph pair and output the results following the preferences in the arguments.
     *
     * @param args The arguments of the run, which should always include at least the formula and graph.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    private static void run(Map<String, Object> args) throws IOException {
        // Find the formula and graph file and default to null if the key is not present.
        String formulaFile = (String) args.getOrDefault("-formula", null);
        String graphFile = (String) args.getOrDefault("-graph", null);

        // Check whether we have all the required parameters.
        if(formulaFile == null || graphFile == null) {
            System.out.println("No formula or graph file has been provided. The files can be targeted with the " +
                    "arguments -formula=\"<f_path>\" -graph=\"<g_path>\".");
        }

        // Find the optional parameters.
        boolean improved = (boolean) args.getOrDefault("-mode", false);

        // Everything is filled in. Call the solver with the correct configuration.
        AbstractComponent formula = Parser.parseFormulaFile(formulaFile);
        LTS graph = Parser.parseSystemFile(graphFile);

        System.out.println("Formula: [" + formula.toLatex() + "]");
        System.out.println("Graph: [" + graph.toString() + "]");

        Solution solution;
        if(improved) {
            solution = Solver.solveEmersonLei(formula, graph);
            System.out.println("Emerson-Lei Solution: " + solution);
        } else {
            solution = Solver.solveNaive(formula, graph);
            System.out.println("Naive Solution: " + solution);
        }

        // Print the solution under any verbosity level.
        System.out.println("Evaluation: " + solution.states.contains(graph.firstState));
    }
}
