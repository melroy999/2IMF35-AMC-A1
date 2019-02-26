package s2imf35;

import s2imf35.experiment.*;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static int verbose = 1;

    /**
     * Print a string if the verbosity level is equal or lower than the global verbosity level.
     *
     * @param input The input string to print when the verbosity requirements are met.
     * @param verbosity The verbosity of the string.
     */
    public static void print(String input, int verbosity) {
        if(verbose >= verbosity) {
            System.out.println(input);
        }
    }

    /**
     * Main call to the program.
     *
     * @param args The arguments given to the program, which are described in further detail in the report.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            runUnitTests();
        } else if(args.length == 1) {
            switch (args[0]) {
                case "experiment1":
                    new Experiment1().run();
                    break;
                case "experiment2":
                    new Experiment2().run();
                    break;
                case "experiment3":
                    new Experiment3().run();
                    break;
                case "experiment4":
                    new Experiment4().run();
                    break;
                case "unit":
                    runUnitTests();
                    break;
                default:
                    System.out.println("The provided arguments are invalid or unknown.");
                    System.exit(-1);
            }
        } else {
            // -formula="inputs/testcases/combined/form1.mcf" -graph="inputs/testcases/combined/test.aut" -mode=improved -verbose=2
            run(args);
        }
    }

    /**
     * Run a single formula-graph pair and output the results following the preferences in the arguments.
     *
     * @param args The arguments of the run, which should always include at least the formula and graph.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    private static void run(String[] args) throws IOException {
        String formulaFile = null;
        String graphFile = null;
        boolean improved = false;

        // Read all arguments and set the associated variables.
        for(String arg : args) {
            if(arg.startsWith("-formula")) {
                formulaFile = arg.substring(arg.indexOf("=") + 1, arg.length());
            } else if(arg.startsWith("-graph")) {
                graphFile = arg.substring(arg.indexOf("=") + 1, arg.length());
            } else if(arg.equals("-mode=improved") || arg.equals("-mode=1")) {
                improved = true;
            } else if(arg.startsWith("-verbose")) {
                try {
                    verbose = Integer.parseInt(arg.substring(arg.indexOf("=") + 1, arg.length()));
                } catch (NumberFormatException e) {
                    System.out.println("The verbose parameter value is invalid. Valid values are [0, 1, 2].");
                    System.exit(-1);
                }
            }
        }

        // Check if all parameters are filled in.
        if(formulaFile == null) {
            System.out.println("No formula file has been provided. The formula file can be targeted with the argument -formula=\"<f_path>\".");
        } else if(graphFile == null) {
            System.out.println("No graph file has been provided. The graph file can be targeted with the argument -graph=\"<g_path>\".");
        } else {
            // Everything is filled in. Call the solver with the correct configuration.
            AbstractComponent formula = Parser.parseFormulaFile(formulaFile);
            LTS graph = Parser.parseSystemFile(graphFile);

            // Print the formula and graph under certain verbosity levels.
            Main.print("Formula: \t[" + formula.toLatex() + "]", 1);
            Main.print("Graph: \t\t[" + graph.toString() + "]", 1);

            Set<Integer> solution;
            if(improved) {
                solution = Solver.solveEmersonLei(formula, graph);
            } else {
                solution = Solver.solveNaive(formula, graph);
            }

            // Print the solution under any verbosity level.
            Main.print("Evaluation:\t" + solution.contains(graph.firstState), 0);
        }
    }

    /**
     * Run all the unit tests provided in part I of the assignment.
     *
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    private static void runUnitTests() throws IOException {
        String rootPath = "inputs/testcases/";
        HashMap<String, Integer> groups = new HashMap<>();
        groups.put("boolean", 9);
        groups.put("combined", 5);
        groups.put("fixpoints_only", 5);
        groups.put("modal_operators", 5);

        for(Map.Entry<String, Integer> entry : groups.entrySet()) {
            System.out.println("Testing " + entry.getKey());
            LTS graph = Parser.parseSystemFile(rootPath + entry.getKey() + "/test.aut");

            for(int i = 1; i < entry.getValue() + 1; i++) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + entry.getKey() + "/form" + i + ".mcf");
                System.out.println(i + ".\t" + formula);

                Solver.solveNaive(formula, graph);
                Set<Integer> solution = Solver.solveEmersonLei(formula, graph);

                // Print the solution under any verbosity level.
                Main.print("Evaluation:\t" + solution.contains(graph.firstState) + "\n", 0);
            }
            System.out.println();
        }
    }
}
