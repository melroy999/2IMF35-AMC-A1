package s2imf35.experiment;

import s2imf35.PerformanceCounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Solution for exercise three of part II.
 */
public class Experiment3 extends AbstractExperiment {
    @Override
    public void run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("PART II: Cache Coherence");

        // Which method do we want to solve?
        Boolean mode = (Boolean) argMap.getOrDefault("-mode", null);

        // Gather the required files.
        String rootPath = "inputs/experiment3/";
        File[] files = new File(rootPath).listFiles();
        List<String> formulaNames = getFormulaPaths(files);
        List<String> graphNames = getGraphPaths(files);
        graphNames.sort(Comparator.comparing(
                o -> Integer.parseInt(o.split("_")[2].replace(".1.aut", ""))
        ));

        // Track all the found performance metrics.
        HashMap<String, HashMap<String, PerformanceCounter>> metrics = new HashMap<>();

        // Run all the formula/graph combinations.
        runAllmethods(mode, rootPath, formulaNames, graphNames, metrics);

        // Report on the performance of the algorithm.
        getPerformanceDataString(metrics, mode);
    }

    private void getPerformanceDataString(HashMap<String, HashMap<String, PerformanceCounter>> metrics, Boolean mode) {
        String modeName = mode == null || !mode ? "naive" : "emerson_lei";

        HashMap<String, String> abbreviations = new HashMap<>();
        abbreviations.put("infinite_run_no_access.mcf", "irna");
        abbreviations.put("infinitely_often_exclusive.mcf", "ioe");
        abbreviations.put("invariantly_eventually_fair_shared_access.mcf", "iefsa");
        abbreviations.put("invariantly_inevitably_exclusive_access.mcf", "iiea");
        abbreviations.put("invariantly_possibly_exclusive_access.mcf", "ipea");

        for(String formula : metrics.keySet()) {
            HashMap<String, PerformanceCounter> measurements = metrics.get(formula);

            List<AbstractMap.SimpleEntry<Object, Object>> duration_data = new ArrayList<>();
            List<AbstractMap.SimpleEntry<Object, Object>> reset_data = new ArrayList<>();
            List<AbstractMap.SimpleEntry<Object, Object>> iteration_data = new ArrayList<>();

            for(int i = 2; i <= 5; i++) {
                PerformanceCounter counter = measurements.get("german_linear_" + i + ".1.aut");
                duration_data.add(new AbstractMap.SimpleEntry<>(i, counter.duration == 0 ? 1 : counter.duration));
                reset_data.add(new AbstractMap.SimpleEntry<>(i, counter.resets));
                iteration_data.add(new AbstractMap.SimpleEntry<>(i, counter.iterations));
            }

            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_duration.data", duration_data);
            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_resets.data", reset_data);
            writeFileContentsBlock(abbreviations.get(formula) + "_" + modeName + "_iterations.data", iteration_data);
        }
    }

    private void writeFileContentsBlock(String title, List<AbstractMap.SimpleEntry<Object, Object>> data) {
        try (PrintWriter out = new PrintWriter("data/experiment3/" + title)) {
            for(AbstractMap.SimpleEntry<Object, Object> d : data) {
                out.println(d.getKey() + "\t" + d.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
