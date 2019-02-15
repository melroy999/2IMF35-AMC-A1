package s2imf35;

public class PerformanceCounter {
    public int iterations = 0;
    public int resets = 0;

    @Override
    public String toString() {
        return  "i=" + iterations +
                ", r=" + resets;
    }
}
