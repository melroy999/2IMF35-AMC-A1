package s2imf35.experiment;

import s2imf35.*;
import s2imf35.graph.LTS;
import s2imf35.operator.AbstractComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
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

        // Track all the found performance metrics.
        HashMap<String, HashMap<String, PerformanceCounter>> metrics = new HashMap<>();

        for(String formula : formulaNames) {
            metrics.put(formula, new HashMap<>());
        }

        // Print all the formulas for each graph file.
        for(String graphFile : graphNames) {
            Main.print(">>> TESTING GRAPH FILE [" + graphFile.toUpperCase() + "] <<<", 0);
            Main.print("Loading graph file '" + graphFile.toUpperCase() + "'.\n", 0);
            LTS graph = Parser.parseSystemFile(rootPath + graphFile);

            for(String formulaFile : formulaNames) {
                AbstractComponent formula = Parser.parseFormulaFile(rootPath + formulaFile);
                Main.print("File '" + formulaFile + "': " + formula, 0);

                Solution solution = getSolution(mode, graph, formula);
                metrics.get(formulaFile).put(graphFile, solution.counter);

                // Print the solution under any verbosity level.
                Main.print("Evaluation: " + solution.states.contains(graph.firstState), 0);
                Main.print("", 0);
            }

            System.out.println();
        }

        getPerformanceDataString(metrics, mode);
    }

    private void getPerformanceDataString(HashMap<String, HashMap<String, PerformanceCounter>> metrics, Boolean mode) {
        String modeName = mode == null || !mode ? "naive" : "emerson_lei";

        HashMap<String, String> abbreviations = new HashMap<>();
        abbreviations.put("invariantly_inevitably_eat.mcf", "iie");
        abbreviations.put("invariantly_plato_starves.mcf", "ips");
        abbreviations.put("invariantly_possibly_eat.mcf", "ipe");
        abbreviations.put("plato_infinitely_often_can_eat.mcf", "pioce");

        for(String formula : metrics.keySet()) {
            HashMap<String, PerformanceCounter> measurements = metrics.get(formula);

            List<SimpleEntry<Object, Object>> duration_data = new ArrayList<>();
            List<SimpleEntry<Object, Object>> reset_data = new ArrayList<>();
            List<SimpleEntry<Object, Object>> iteration_data = new ArrayList<>();

            for(int i = 2; i < 12; i++) {
                PerformanceCounter counter = measurements.get("dining_" + i + ".aut");
                duration_data.add(new SimpleEntry<>(i, counter.duration));
                reset_data.add(new SimpleEntry<>(i, counter.resets));
                iteration_data.add(new SimpleEntry<>(i, counter.iterations));
            }

            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_duration.data", duration_data);
            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_resets.data", reset_data);
            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_iterations.data", iteration_data);
        }
    }

    private void writeFileContentsBlock(String title, List<SimpleEntry<Object, Object>> data) {
        try (PrintWriter out = new PrintWriter("data/experiment1/" + title)) {
            for(SimpleEntry<Object, Object> d : data) {
                out.println(d.getKey() + "\t" + d.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
