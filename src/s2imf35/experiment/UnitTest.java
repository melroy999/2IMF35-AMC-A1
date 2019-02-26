package s2imf35.experiment;

import s2imf35.Main;
import s2imf35.Parser;
import s2imf35.Solver;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An experiment that runs all unit tests given for part I.
 */
public class UnitTest extends AbstractExperiment {

    @Override
    public void run(Map<String, Object> argMap) throws IOException {
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
