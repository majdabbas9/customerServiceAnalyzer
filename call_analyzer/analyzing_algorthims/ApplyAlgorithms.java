package call_analyzer.analyzing_algorthims;

import java.sql.Array;
import java.util.ArrayList;

import call_analyzer.interaction.Interaction;

public class ApplyAlgorithms {
    private ArrayList<Algorithm> algorithms = new ArrayList<>();

    public void addAlgorithm(Algorithm algorithm) {
        this.algorithms.add(algorithm);
    }

    public ArrayList<String> applyAll(Interaction interaction) {
        ArrayList<String> res = new ArrayList<>();
        for (Algorithm algorithm : algorithms) {
            res.add(algorithm.apply(interaction));
        }
        return res;
    }
}
