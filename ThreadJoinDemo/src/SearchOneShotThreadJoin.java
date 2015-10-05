import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by USER on 10/4/2015.
 */
public class SearchOneShotThreadJoin {
    private volatile List<String> mInput;
    private final String[] mWordsToFind;
    private final List<Thread> mWorkerThreads;

    public SearchOneShotThreadJoin(String[] wordsToFind, String[] inputStrings) {
        this.mWordsToFind = wordsToFind;
        this.mInput = Arrays.asList(inputStrings);
        this.mWorkerThreads = new LinkedList<Thread>();

        //create and start Thread for each element
        for (int i = 0; i < mInput.size(); i++) {
            Thread t = new Thread(makeTask(i));
            mWorkerThreads.add(t);
            t.start();
        }

        //synchronize
        for (Thread t : mWorkerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("join() interuppted");
            }
        }
    }

    private Runnable makeTask(final int index) {
        return new Runnable() {
            @Override
            public void run() {
                String element = mInput.get(index);
                if (!processInput(element)) return;
            }
        };
    }

    private boolean processInput(String inputData) {
        for (String word : mWordsToFind)
            // Check to see how many times (if any) the word
            // appears in the input data.
            for (int i = inputData.indexOf(word, 0);
                 i != -1;
                 i = inputData.indexOf(word, i + word.length()))
                // Each time a match is found the processResults()
                // hook method is called to handle the results.
                processResults("in thread "
                        + Thread.currentThread().getId()
                        + " "
                        + word
                        + " was found at offset "
                        + i
                        + " in string "
                        + inputData);
        return true;
    }

    private void processResults(String results) {
        System.out.println(results);
    }
}
