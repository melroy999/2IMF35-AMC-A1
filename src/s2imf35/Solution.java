package s2imf35;

import java.util.Arrays;
import java.util.Set;

/**
 * A class that holds all information we have about a solution.
 */
public class Solution {
    // The states that have been marked as valid.
    public final Set<Integer> states;

    // The performance counter associated with the solution.
    public final PerformanceCounter counter;

    public Solution(Set<Integer> states, PerformanceCounter counter) {
        this.states = states;
        this.counter = counter;
    }

    @Override
    public String toString() {
        // Select only the first 20 states in the solution.
        Object[] first50States = states.stream().limit(20).toArray();
        String list = Arrays.toString(first50States);

        if(states.size() != first50States.length) {
            list = list.substring(0, list.length() - 1) + ", ...]";
        }

        return counter.toString() + ", " + list + " in " + counter.duration + " milliseconds.";
    }
}
