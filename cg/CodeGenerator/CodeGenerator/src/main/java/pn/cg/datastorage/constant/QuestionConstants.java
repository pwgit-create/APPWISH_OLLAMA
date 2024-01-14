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
    public final static String CLASS_DID_NOT_COMPILE_PREFIX_2 = "The java code you’ve sent me did not compile ";


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


    /*
     * Add information regarding start and end delimiters chars for the java code to the LLM model 
     */
    public final static String AND_MARK_START_AND_END_DELIMITER_CHARS = " and mark the start of the java code with the text @START_HERE and dont use it again after that and the end of the java code with the text @END_HERE and dont use it again after that" ;

    public final static String AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE=" and make sure that the correct number of brace brackets are used at the end of the java code so it compiles without errors";

    
}
