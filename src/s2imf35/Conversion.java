package s2imf35;

import s2imf35.graph.LTS;

import java.io.IOException;
import java.io.PrintWriter;

public class Conversion {
    public static void main(String[] args) throws IOException {
        experiment1();
    }

    private static void experiment1() throws IOException {
        for(int i = 2; i <= 10; i++) {
            String aut = "inputs/experiment2/demanding_children_" + i + ".aut";
            String mcrl2 = "mcrl2/experiment2/experiment2_n" + i + ".mcrl2";

            LTS lts = Parser.parseSystemFile(aut);
            String mcrl2Spec = lts.toMCRL2Spec();

            try (PrintWriter out = new PrintWriter(mcrl2)) {
                out.println(mcrl2Spec);
            }
        }
    }
}
