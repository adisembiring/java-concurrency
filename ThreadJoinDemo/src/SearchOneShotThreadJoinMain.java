/**
 * Created by USER on 10/4/2015.
 */
public class SearchOneShotThreadJoinMain {
    private final static String[] mOneShotInputStrings =
            {"xreo", "xfao", "xmiomio", "xlao", "xtiotio", "xsoosoo", "xdoo", "xdoodoo"};

    // List of words to search for.
    private static String[] mWordList = {"do",
            "re",
            "mi",
            "fa",
            "so",
            "la",
            "ti",
            "do"};

    public static void main(String[] args) {
        System.out.println("Entering Main");
        System.out.println("Starting JOIN");
        new SearchOneShotThreadJoin(mWordList, mOneShotInputStrings);
        System.out.println("Ending JOIN");
        System.out.println("Leaving Main");
    }
}
