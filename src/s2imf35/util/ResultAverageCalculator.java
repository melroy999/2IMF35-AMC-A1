package s2imf35.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Take the average of a given set of results.
 */
public class ResultAverageCalculator {
    public static void main(String[] args) throws IOException {
        String target = "experiment3";

        List<String> folders = new ArrayList<>();
        for(int i = 1; i <=5; i++) {
            folders.add(target + "_t" + i);
        }

        String rootPath = "data/" + folders.get(0) + "/";
        File[] files = new File(rootPath).listFiles();
        assert files != null;
        List<String> fileNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());

        for(String file : fileNames) {
            Map<Integer, List<Integer>> results = new HashMap<>();

            for(String folder : folders) {
                String path = "data/" + folder + "/" + file;
                String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                String[] lines = contents.split("\\r?\\n");

                for(String line : lines) {
                    String[] components = line.split("\\s+");
                    int n = Integer.parseInt(components[0]);
                    int v = Integer.parseInt(components[1]);

                    List<Integer> resultList = results.getOrDefault(n, new ArrayList<>());
                    resultList.add(v);
                    results.put(n, resultList);
                }
            }

            // Calculate the averages and output them to a new file.
            String path = "data/" + target + "_avg/" + file;
            List<Integer> keys = results.keySet().stream().sorted().collect(Collectors.toList());

            try (PrintWriter out = new PrintWriter(path)) {
                for(Integer key : keys) {
                    List<Integer> list = results.get(key);
                    int avg = list.stream().mapToInt(e -> e).sum() / list.size();
                    out.println(key + "\t" + avg);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
