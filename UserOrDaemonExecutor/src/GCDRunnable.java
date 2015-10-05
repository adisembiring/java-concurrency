import java.util.Random;

/**
 * Created by USER on 10/4/2015.
 */
public class GCDRunnable extends Random implements Runnable {
    private final int MAX_ITERATIONS = 100000000;

    public GCDRunnable(String name) {

    }

    private int computeGCD(int number1, int number2) {
        if (number2 == 0) return number1;
        return computeGCD(number2, number1 % number2);
    }

    @Override
    public void run() {
        final String threadString = " with thread id " + Thread.currentThread();
        System.out.println("Entering run() " + threadString);

        try {
            for(int i = 0; i < MAX_ITERATIONS; ++i) {
                int number1 = nextInt();
                int number2 = nextInt();

                // Check to see if the thread's been interrupted.
                if(Thread.interrupted())
                    throw new InterruptedException();
                // Print results every 10 million iterations.
                else if((i % 10000000) == 0)
                    System.out.println("In run()"
                            + threadString
                            + " the GCD of "
                            + number1
                            + " and "
                            + number2
                            + " is "
                            + computeGCD(number1,
                            number2));
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted " + threadString);
        } finally {
            System.out.println("Leaving run() " + threadString);
        }
    }
}
