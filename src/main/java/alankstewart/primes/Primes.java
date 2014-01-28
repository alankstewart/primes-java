package alankstewart.primes;

import static java.lang.System.out;

public class Primes {

    public static void main(final String[] args) {
        final Primes p = new Primes();
        p.eratosthenesSieve(100);
    }

    public void eratosthenesSieve(final int upperBound) {
        final boolean[] composite = new boolean[upperBound + 1];

        final int upperBoundSqrt = (int) Math.sqrt(upperBound);
        for (int i = 2; i <= upperBoundSqrt; i++) {
            if (!composite[i]) {
                for (int j = i * i; j <= upperBound; j += i) {
                    composite[j] = true;
                }
            }
        }
        for (int i = 2; i <= upperBound; i++) {
            if (!composite[i]) {
                out.format("%d ", i);
            }
        }
        out.format("\n");
    }
}
