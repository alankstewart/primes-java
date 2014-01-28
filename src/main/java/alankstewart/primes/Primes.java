package alankstewart.primes;

import java.io.Console;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Integer.parseInt;
import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;

public class Primes {

    private static final boolean DEBUG = Boolean.getBoolean("debug");

    public static void main(final String[] args) {
        final Console console = System.console();
        if (console == null) {
            err.format("No console\n");
            exit(1);
        }

        final int upperBound = args != null && args.length > 0 ? parseInt(args[0]) : 100;
        final Primes primes = new Primes();
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<boolean[]> future = executorService.submit(new Callable<boolean[]>() {
            @Override
            public boolean[] call() throws Exception {
                return primes.eratosthenesSieve(upperBound);
            }
        });

        try {
            out.format("Enter a number between 2 and %d: ", upperBound);
            final boolean[] composite = future.get();
            int number = parseInt(console.readLine());
            if (number < 2 || number > upperBound) {
                err.format("Number must be between 2 and %d\n", upperBound);
                exit(1);
            }

            if (composite[number]) {
                out.format("%d is not prime\n", number);
            } else {
                out.format("%d is prime\n", number);
            }

            if (DEBUG) {
                out.format("\n");
                for (int i = 2; i <= upperBound; i++) {
                    if (!composite[i]) {
                        out.format("%d ", i);
                    }
                }
                out.format("\n");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        } finally {
            executorService.shutdown();
        }
    }

    public boolean[] eratosthenesSieve(final int upperBound) {
        final boolean[] composite = new boolean[upperBound + 1];

        final int upperBoundSqrt = (int) Math.sqrt(upperBound);
        for (int i = 2; i <= upperBoundSqrt; i++) {
            if (!composite[i]) {
                for (int j = i * i; j <= upperBound; j += i) {
                    composite[j] = true;
                }
            }
        }

        return composite;
    }
}
