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
        return counter.toString() + ", " + Arrays.toString(states.toArray());
    }
}
