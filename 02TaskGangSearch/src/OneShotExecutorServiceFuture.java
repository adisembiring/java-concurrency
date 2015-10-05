import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * @class OneShotExecutorServiceFuture
 *
 * @brief Customizes the SearchTaskGangCommon framework to process a
 *        one-shot List of tasks via a variable-sized pool of Threads
 *        created by the ExecutorService. The unit of concurrency is a
 *        "task per search word". The results processing model uses
 *        the Synchronous Future model, which defers the results
 *        processing until all words to search for have been
 *        submitted.
 */
public class OneShotExecutorServiceFuture extends SearchTaskGangCommon {
    /**
     * A List of Futures that contain SearchResults.
     */
    protected List<Future<SearchResults>> mResultFutures;

    protected OneShotExecutorServiceFuture(String[] wordsToFind,
                                           String[][] stringsToSearch) {
        // Pass input to superclass constructor.
        super(wordsToFind,
                stringsToSearch);

        // Initialize the Executor with a cached pool of Threads,
        // which grow dynamically.
        setExecutor(Executors.newCachedThreadPool());
    }

    @Override
    protected void initiateTaskGang(int inputSize) {
        // Preallocate the List of Futures to hold all the
        // SearchResults.
        mResultFutures = new ArrayList<Future<SearchResults>>(inputSize * mWordsToFind.length);

        // Process each String of inputData via the processInput()
        // method.  Note that input Strings aren't run concurrently,
        // just each word that's being searched for.
        for (String inputData : getInput())
            processInput(inputData);

        // Process all the Futures.
        processFutureResults(mResultFutures);
    }

    @Override
    protected boolean processInput(final String inputData) {
        ExecutorService executorService = (ExecutorService) getExecutor();
        for (final String word : mWordsToFind) {
            // Submit a Callable that will search concurrently for
            // this word in the inputData & create a Future to store
            // the results.
            final Future<SearchResults> resultsFuture = executorService.submit(new Callable<SearchResults>() {
                @Override
                public SearchResults call() throws Exception {
                    return searchForWord(word, inputData);
                }
            });
            mResultFutures.add(resultsFuture);
        }
        return true;
    }

    protected void processFutureResults(List<Future<SearchResults>> resultFutures) {
        // Iterate through the List of Futures and print the search
        // results.
        for (Future<SearchResults> resultFuture : resultFutures) {
            try {
                resultFuture.get().print();
            } catch (Exception e) {
                System.out.println("get() exception");
            }
        }
    }
}