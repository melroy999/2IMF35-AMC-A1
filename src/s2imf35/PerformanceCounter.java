package s2imf35;

/**
 * A helper class that measures the performance of our algorithms.
 */
public class PerformanceCounter {
    // The number of iterations made when solving the mu and nu components.
    public int iterations = 0;

    // The number of resets made by the Emerson-Lei Algorithm.
    public int resets = 0;

    // The time the calculation took in milliseconds.
    public long duration = 0;

    @Override
    public String toString() {
        return  "i=" + iterations +
                ", r=" + resets;
    }
}
