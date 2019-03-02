package s2imf35;

import s2imf35.data.BitSet;

import java.util.Arrays;

/**
 * A class that holds all information we have about a solution.
 */
public class Solution {
    // The states that have been marked as valid.
    public final BitSet states;

    // The performance counter associated with the solution.
    public final PerformanceCounter counter;

    public Solution(BitSet states, PerformanceCounter counter) {
        this.states = states;
        this.counter = counter;
    }

    @Override
    public String toString() {
        return counter.toString() + ", " + states + " in " + counter.duration + " milliseconds.";
    }
}
