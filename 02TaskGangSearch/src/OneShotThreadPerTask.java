import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @class OneShotThreadPerTask
 *
 * @brief Customizes the SearchTaskGangCommon framework to process a
 *        one-shot List of tasks via an Executor that creates a Thread
 *        for each task. The Executor model is a Thread per task. The
 *        unit of concurrency is each input String. The results
 *        processing model is synchronous.
 */
public class OneShotThreadPerTask extends SearchTaskGangCommon {
    private final List<Thread> mWorkerThreads;

    public OneShotThreadPerTask(String[] wordsToFind, String[][] stringsToSearch) {
        super(wordsToFind, stringsToSearch);
        mWorkerThreads = new LinkedList<Thread>();
    }

    /**
     * Initiate the TaskGang to run each task in a separate Thread.
     */
    @Override
    protected void initiateTaskGang(int inputSize) {
        // Create a fixed-size Thread pool.
        if (getExecutor() == null) {
            setExecutor(new Executor() {
                @Override
                public void execute(Runnable command) {
                    Thread t = new Thread(command);
                    mWorkerThreads.add(t);
                    t.start();
                }
            });
        }

        // Enqueue each item in the input List for execution in a
        // separate Thread.
        for (int i = 0; i < inputSize; i++) {
            getExecutor().execute(makeTask(i));
        }
    }

    /**
     * Runs in a background Thread and searches the inputData for all
     * occurrences of the words to find.
     */
    @Override
    protected boolean processInput(String inputData) {
        // Iterate through each word we're searching for and try to
        // find it in the inputData.
        for (String word : mWordsToFind) {
            SearchResults results = searchForWord(word, inputData);
            synchronized(System.out) {
                results.print();
            }
        }
        return true;
    }


    @Override
    protected void awaitTasksDone() {
        System.out.println("Impl start awaitTasksDone()");
        for (Thread t : mWorkerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("awaitTaskDone interrupted");
            }
        }
        System.out.println("Impl end awaitTasksDone()");
    }

}
