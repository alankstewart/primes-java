package alankstewart.primes;

import java.io.Console;
import java.util.BitSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class Primes {

    private static final boolean DEBUG = Boolean.getBoolean("DEBUG");

    public static void main(final String[] args) {
        final Primes primes = new Primes();
        primes.calculate(args != null && args.length > 0 ? parseInt(args[0]) : 100);
    }

    public void calculate(final int limit) {
        final Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("No console");
        }

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<BitSet> future = executorService.submit(new Callable<BitSet>() {
            @Override
            public BitSet call() throws Exception {
                return eratosthenesSieve(limit);
            }
        });

        out.format("Enter a number between 2 and %d: ", limit);

        final BitSet prime;
        try {
            prime = future.get();
        } catch (final InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        } finally {
            executorService.shutdown();
        }

        int number = parseInt(console.readLine());
        if (number < 2 || number > limit) {
            throw new IllegalArgumentException(String.format("Number must be between 2 and %d", limit));
        }

        out.format(prime.get(number) ? "%d is prime\n" : "%d is not prime\n", number);

        if (DEBUG) {
            out.format("\n");
            for (int i = 2; i <= limit; i++) {
                if (prime.get(i)) {
                    out.format("%d ", i);
                }
            }
            out.format("\n");
        }
    }

    public BitSet eratosthenesSieve(final int limit) {
        final BitSet prime = new BitSet(limit + 1);
        prime.set(2, limit + 1);
        final int limitSqrt = (int) Math.floor(Math.sqrt(limit));
        for (int i = 2; i <= limitSqrt; i++) {
            if (prime.get(i)) {
                for (int j = i * i; j <= limit; j += i) {
                    prime.clear(j);
                }
            }
        }
        return prime;
    }
}
