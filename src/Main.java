import operator.AbstractComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            for(int i = 1; i < entry.getValue() + 1; i++) {
                System.out.println(i + ".\t" + Parser.parseFormulaFile(rootPath + entry.getKey() + "/form" + i + ".mcf"));
            }
            System.out.println(Parser.parseSystemFile(rootPath + entry.getKey() + "/test.aut"));
            System.out.println();
        }
    }
}
