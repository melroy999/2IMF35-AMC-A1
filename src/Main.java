import graph.LTS;
import operator.AbstractComponent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        String rootPath = "inputs/testcases/";
        HashMap<String, Integer> groups = new HashMap<>();
        groups.put("boolean", 9);
        groups.put("combined", 5);
        groups.put("fixpoints_only", 5);
        groups.put("modal_operators", 5);

        for(Map.Entry<String, Integer> entry : groups.entrySet()) {
            System.out.println("Testing " + entry.getKey());
            LTS graph = Parser.parseSystemFile(rootPath + entry.getKey() + "/test.aut");
            System.out.println(graph);

            for(int i = 1; i < entry.getValue() + 1; i++) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + entry.getKey() + "/form" + i + ".mcf");
                System.out.println(i + ".\t" + formula);

                Set<Integer> solution = Solver.solveNaive(formula, graph);
                System.out.println(Arrays.toString(solution.toArray()));
                System.out.println("Solution contains first state: " + solution.contains(graph.firstState));
            }
            System.out.println();
        }
    }
}
