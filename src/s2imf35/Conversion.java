package s2imf35;

import s2imf35.graph.LTS;

import java.io.IOException;
import java.io.PrintWriter;

public class Conversion {
    public static void main(String[] args) throws IOException {
        experiment1();
    }

    private static void experiment1() throws IOException {
        for(int i = 2; i <= 5; i++) {
            String aut = "inputs/experiment3/german_linear_" + i + ".1.aut";
            String mcrl2 = "mcrl2/experiment3/experiment3_n" + i + ".mcrl2";

            LTS lts = Parser.parseSystemFile(aut);
            String mcrl2Spec = lts.toMCRL2Spec();

            try (PrintWriter out = new PrintWriter(mcrl2)) {
                out.println(mcrl2Spec);
            }
        }
    }
}
