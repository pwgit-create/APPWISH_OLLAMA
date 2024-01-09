package pn.cg.datastorage.constant;

public class QuestionConstants {

    /**
     * A prefix for a question that asks the ollama model to code a whole feature/app
     *
     * @use-info The app wish should be concatenated at the tail of this String
     */
    public final static String APP_WISH_PREFIX_FEATURE = "Provide me only java code on ";

    /**
     * A prefix for a question that will try to generate a single class
     *
     * @use-info Add a feature or functionality at the end
     */
    public final static String APP_WISH_PREFIX_CLASS = "Provide me a java class for";

    /**
     * Message that should be sent to the ollama model when a compilation of a class fails (with javac output)
     *
     * @use-info The error message from javac should be concatenated at the tail of this String
     */
    public final static String CLASS_DID_NOT_COMPILE_PREFIX = "The class you’ve sent me did not compile. Please rewrite it." +
            " Here is the error message I´ve revived from the compiler: ";

    /**
     * Message that should be sent to the ollama model when a compilation of a class fails (without javac output)
     *
     * @use-info as is
     */
    public final static String CLASS_DID_NOT_COMPILE_PREFIX_2 = "The java code you’ve sent me did not compile " +APP_WISH_PREFIX_FEATURE;


    /**
     * Prefix that should be sent to fetch a list of classes from the ollama model
     *
     * @use-info The app wish should be concatenated at the tail of this String
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 1/2
     */
    public final static String WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX = "Which classes are needed for java code on ";

    /**
     * Suffix for WHICH_CLASS_ARE_NEEDED_FOR_APP
     *
     * @use-info This String should be concatenated at the tail of the appWish (Instance variable in QuestionBuilder)
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 2/2
     */
    public final static String WHICH_CLASS_ARE_NEEDED_FOR_APP_SUFFIX = "=? please provide me the java classes in a numbered list ";

    /**
     * Optional Suffix to APP_WISH
     *
     * @use-info This String should be concatenated at the tail of the appWish (Instance variable in QuestionBuilder)
     */
    public final static String WITH_MAIN_METHOD = " with a main method";

    /**
     * Optional Suffix to APP_WISH
     *
     * @use-info This String should be concatenated at the tail of the WITH_MAIN_METHOD
     */

    public final static String AND_CORRECT_IMPORTS = " and correct imports that are added into the source code";

     public final static String INCLUDE_IN_ONE_FILE= " and include everything in one file";



}
