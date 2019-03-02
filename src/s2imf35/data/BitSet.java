package s2imf35.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class BitSet {
    // The internal representation is an array of longs, where the positions represent whether an integer is set or not.
    private final long[] data;

    // The length of the domain.
    private final int n;

    public BitSet(int n) {
        data = new long[(int) Math.ceil(0.015625 * n)];
        this.n = n;
    }

    public BitSet or(BitSet rhs) {
        BitSet result = new BitSet(n);
        for(int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] | rhs.data[i];
        }
        return result;
    }

    public BitSet and(BitSet rhs) {
        BitSet result = new BitSet(n);
        for(int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] & rhs.data[i];
        }
        return result;
    }

    public void set(int i) {
        data[i / 64] |= (1L << (i % 64));
    }

    public void set() {
        for(int i = 0; i < n / 64; i++) {
            data[i] = ~0L;
        }

        if(n % 64 != 0) {
            // We have to set the first n % 64 bits to 1.
            data[data.length - 1] |= ((1L << (n % 64)) - 1);
        }
    }

    public boolean intersects(BitSet rhs) {
        BitSet intersection = and(rhs);
        return Arrays.stream(intersection.data).anyMatch(e -> e != 0L);
    }

    /**
     * Check whether all bits set true in the given BitSet are also true in this BitSet.
     *
     * @param rhs The BitSet to check against.
     * @return True when all set bits in @code{s2} are also set in @code{this}.
     */
    public boolean containsAll(BitSet rhs) {
        BitSet intersection = and(rhs);
        return intersection.equals(rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitSet bitSet = (BitSet) o;

        if (n != bitSet.n) return false;
        return Arrays.equals(data, bitSet.data);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + n;
        return result;
    }

    public long cardinality() {
        return Arrays.stream(data).map(Long::bitCount).sum();
    }

    public boolean get(int i) {
        return ((data[i / 64] & (1L << (i % 64))) != 0);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder("[");
        List<Integer> results = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            if(get(i)) {
                results.add(i);
            }

            if(results.size() >= 20) {
                representation.append(results.stream().map(Object::toString).collect(Collectors.joining(", ")));
                representation.append(", ...]");
                return representation.toString();
            }
        }

        representation.append("]");
        return representation.toString();
    }
}
