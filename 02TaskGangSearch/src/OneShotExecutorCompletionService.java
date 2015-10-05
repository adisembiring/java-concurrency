import java.util.concurrent.*;

/**
 * @class OneShotExecutorCompletionService
 *
 * @brief Customizes the SearchTaskGangCommon framework to process a
 *        one-shot List of tasks via a variable-sized pool of Threads
 *        created by the ExecutorService. The units of concurrency are
 *        a "task per search word" *and* the input Strings. The
 *        results processing model uses an Asynchronous Future model,
 *        which starts processing results immediately.
 */
public class OneShotExecutorCompletionService extends SearchTaskGangCommon {
    /**
     * Processes the results of Futures returned from the
     * Executor.submit() method.
     */
    protected ExecutorCompletionService<SearchResults> mCompletionService;

    protected OneShotExecutorCompletionService(String[] wordsToFind,
                                               String[][] stringsToSearch) {
        super(wordsToFind,
                stringsToSearch);
        setExecutor(Executors.newCachedThreadPool());

        mCompletionService = new ExecutorCompletionService<SearchResults>(getExecutor());
    }

    @Override
    protected void initiateTaskGang(int inputSize) {
        // Enqueue each item in the input List for execution in the
        // Executor's Thread pool.
        for (int i = 0; i < inputSize; ++i)
            getExecutor().execute(makeTask(i));

        // Process all the Futures concurrently via the
        // ExecutorCompletionService's completion queue.
        concurrentlyProcessQueuedFutures();
    }

    @Override
    protected boolean processInput(final String inputData) {
        // Iterate through each word and submit a Callable that will
        // search concurrently for this word in the inputData.
        for (final String word : mWordsToFind) {
            // This submit() call stores the Future result in the
            // ExecutorCompletionService            // processing.
            mCompletionService.submit(new Callable<SearchResults>() {
                @Override
                public SearchResults call() throws Exception {
                    return searchForWord(word, inputData);
                }
            });
        }
        return true;
    }

    /**
     * Uses the ExecutorCompletionService to concurrently process all
     * the queued Futures.
     */
    protected void concurrentlyProcessQueuedFutures() {
        // Need to account for all the input data and all the words
        // that were searched for.
        final int count = getInput().size() * mWordsToFind.length;
        for (int i = 0; i < count; i++) {
            try {
                Future<SearchResults> resultsFuture = mCompletionService.take();
                resultsFuture.get().print();
            } catch (Exception e) {
                System.out.println("get() exception");
            }
        }
    }
}
