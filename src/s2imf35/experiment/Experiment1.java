package s2imf35.experiment;

import s2imf35.Main;
import s2imf35.Parser;
import s2imf35.Solution;
import s2imf35.Solver;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Solution for exercise one of part II.
 */
public class Experiment1 extends AbstractExperiment {
    @Override
    public void run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("PART II: Dining Philosophers");

        // Which method do we want to solve?
        Boolean mode = (Boolean) argMap.getOrDefault("-mode", null);

        // Gather the required files.
        String rootPath = "inputs/experiment1/";
        File[] files = new File(rootPath).listFiles();
        List<String> formulaNames = getFormulaPaths(files);
        List<String> graphNames = getGraphPaths(files);


        // Print all the formulas for each graph file.
        for(String graphFile : graphNames) {
            Main.print(">>> TESTING GRAPH FILE [" + graphFile.toUpperCase() + "] <<<", 0);
            Main.print("Loading graph file '" + graphFile.toUpperCase() + "'.\n", 0);
            LTS graph = Parser.parseSystemFile(rootPath + graphFile);


            for(String formulaFile : formulaNames) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + formulaFile);
                Main.print("File '" + formulaFile + "': " + formula, 0);

                Solution solution = getSolution(mode, graph, formula);

                // Print the solution under any verbosity level.
                Main.print("Evaluation: " + solution.states.get(graph.firstState), 0);
                Main.print("", 0);
            }


            System.out.println();
        }
    }

    private List<String> getFormulaPaths(File[] files) {
        return Arrays.stream(files).filter(e -> e.getName().endsWith(".mcf"))
                .map(File::getName).collect(Collectors.toList());
    }

    private List<String> getGraphPaths(File[] files) {
        return Arrays.stream(files).filter(e -> e.getName().endsWith(".aut"))
                .sorted(Comparator.comparing(
                        o -> Integer.parseInt(o.getName().split("_")[1].replace(".aut", ""))
                )).map(File::getName).collect(Collectors.toList());
    }
}
