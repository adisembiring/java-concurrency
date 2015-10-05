import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by USER on 10/4/2015.
 */
public class ThreadExecutorTest {
    final static int POOL_SIZE = 2;

    public static void main(String[] args) {
        System.out.println("Entering main()");
        final boolean daemonThread = args.length > 0;
        GCDRunnable runnableCommand = new GCDRunnable(daemonThread ? "daemon" : "user");

        final ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                if (daemonThread) t.setDaemon(true);

                return t;
            }
        };

        final Executor executor = Executors.newFixedThreadPool(POOL_SIZE, threadFactory);
        for (int i = 0; i < POOL_SIZE; i++) {
            executor.execute(runnableCommand);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Leaving main()");
    }
}
