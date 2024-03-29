package s2imf35.experiment;

import s2imf35.Main;
import s2imf35.Parser;
import s2imf35.Solution;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An experiment that runs all unit tests given for part I.
 */
public class UnitTest extends AbstractExperiment {

    @Override
    public void run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("PART I: Unit Tests");

        // Which method do we want to solve?
        Boolean mode = (Boolean) argMap.getOrDefault("-mode", null);

        String rootPath = "inputs/testcases/";
        Map<String, Integer> groups = new LinkedHashMap<>();
        groups.put("boolean", 9);
        groups.put("modal_operators", 5);
        groups.put("fixpoints_only", 5);
        groups.put("combined", 5);

        for(Map.Entry<String, Integer> entry : groups.entrySet()) {
            System.out.println(">>> TESTING FOLDER [" + entry.getKey().toUpperCase() + "] <<<");
            System.out.println("Loading graph file 'test.aut'.\n");

            LTS graph = Parser.parseSystemFile(rootPath + entry.getKey() + "/test.aut");

            for(int i = 1; i < entry.getValue() + 1; i++) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + entry.getKey() + "/form" + i + ".mcf");
                System.out.println("File 'form" + i + ".mcf': " + formula);

                Solution solution = getSolution(mode, graph, formula);

                // Print the solution under any verbosity level.
                System.out.println("Evaluation: " + solution.states.contains(graph.firstState) + "\n");
            }
            System.out.println();
        }
    }
}
