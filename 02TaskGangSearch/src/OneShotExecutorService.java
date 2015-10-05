import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @class OneShotExecutorService
 *
 * @brief Customizes the SearchTaskGangCommon framework to process a
 *        one-shot List of tasks via a fixed-size pool of Threads
 *        created by the ExecutorService, which is also used as a
 *        barrier synchronizer to wait for all the Threads in the pool
 *        to shutdown.  The unit of concurrency is invokeAll(), which
 *        creates a task for each input string.  The results
 *        processing model uses a LinkedBlockingQueue that stores
 *        results for immediate concurrent processing per cycle.
 */
public class OneShotExecutorService extends  SearchTaskGangCommon {
    /**
     * Controls when the framework exits.
     */
    protected CountDownLatch mExitBarrier = null;

    /**
     * Queue to store SearchResults of concurrent computations.
     */
    private BlockingQueue<SearchResults> mResultsQueue = new LinkedBlockingQueue<SearchResults>();

    /**
     * Number of Threads in the pool.
     */
    protected final int MAX_THREADS = 4;

    /**
     * Constructor initializes the superclass.
     */
    public OneShotExecutorService(String[] wordsToFind,
                                  String[][] stringsToSearch) {
        // Pass input to superclass constructor.
        super(wordsToFind,
                stringsToSearch);
    }

    /**
     * Hook method that initiates the gang of Threads by using a
     * fixed-size Thread pool managed by the ExecutorService.
     */
    @Override
    protected void initiateHook(int inputSize) {
        System.out.println("@@@ starting cycle "
                + currentCycle()
                + " with "
                + inputSize
                + " tasks@@@");
        mExitBarrier = new CountDownLatch(inputSize);
        if (getExecutor() == null)
            setExecutor(Executors.newFixedThreadPool(MAX_THREADS));
    }

    @Override
    protected void initiateTaskGang(int inputSize) {
        initiateHook(inputSize);

        // Create a new collection that will contain all the
        // Worker Runnables.
        List<Callable<Object>> workCollection = new ArrayList<Callable<Object>>(inputSize);

        // Create a Runnable for each item in the input List and add
        // it as a Callable adapter into the collection.
        for (int i = 0; i < inputSize; i++) {
            workCollection.add(Executors.callable(makeTask(i)));
        }

        try {
            //donwcast to get the executor service
            ExecutorService executorService = (ExecutorService) getExecutor();
            executorService.invokeAll(workCollection);
        } catch (InterruptedException e) {
            System.out.println("invokeAll() interrupted");
        }
    }

    @Override
    protected boolean processInput(String inputData) {
        for (String word : mWordsToFind) {
            mResultsQueue.add(searchForWord(word, inputData));
        }
        return true;
    }


    /**
     * Hook method called when a worker task is done.
     */
    @Override
    protected void taskDone(int index) throws IndexOutOfBoundsException {
        // Decrements the CountDownLatch, which releases the main
        // Thread when count drops to 0.
        mExitBarrier.countDown();
    }


    /**
     * Hook method that shuts down the ExecutorService's Thread pool
     * and waits for all the tasks to exit before returning.
     */
    @Override
    protected void awaitTasksDone() {
        do {
            processQueuedResults(getInput().size() * mWordsToFind.length);
            try {
                // Wait until the exit barrier has been tripped.
                mExitBarrier.await();
            } catch (InterruptedException e) {
                System.out.println("await() interrupted");
            }
        } while (advanceTaskToNextCycle());
    }

    protected void processQueuedResults(final int resultCount) {
        Runnable processQueuedResultsRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < resultCount; ++i)
                        // Extract each SearchResults from the
                        // queue (blocking if necessary) until
                        // we're done.
                        mResultsQueue.take().print();

                } catch (InterruptedException e) {
                    System.out.println("run() interrupted");
                }
            }
        };

        Thread t = new Thread(processQueuedResultsRunnable);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("processQueuedResults() interrupted");
        }
    }
}
