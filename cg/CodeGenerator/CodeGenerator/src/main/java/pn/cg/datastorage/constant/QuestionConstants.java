package pn.cg.datastorage.constant;

public record QuestionConstants() {

    /**
     * A prefix for a question that asks the ollama model to code a whole feature/app
     *
     * @use-info The app wish should be concatenated at the tail of this String
     */
    public final static String APP_WISH_PREFIX_FEATURE = "Provide me java code for ";

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
    public final static String WITH_MAIN_METHOD = "Make sure it includes a main method";

    /**
     * Optional Suffix to APP_WISH
     *
     * @use-info This String should be concatenated at the tail of the WITH_MAIN_METHOD
     */

    public final static String AND_CORRECT_IMPORTS = "Make sure that correct imports are added into the source code";

    public final static String INCLUDE_IN_ONE_FILE = "Include everything in one file";


    /*
     * Add information regarding the start char delimiter of the java code provided by the LLM model
     */
    public final static String MARK_START_CHAR_DELIMITER = "Mark the start of the first lince of java code including the import statements with the text @START_HERE;";
    
     /*
     * Add information regarding the end char delimiter of the java code provided by the LLM model
     */
    public final static String MARK_THE_END_CHAR_DELIMITER =" Mark the end at the last line of java code that I will compile by marking it with the text @END_HERE";

    /**
     * Add information to double-check that the correct number of brace brackets are used by the LLM Model
     */
    public final static String AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE = "Make sure that the correct number of brace brackets are used at the end of the java code so it compiles without errors";
    
    /**
     * A clarification text to the LLM model regarding the start delimiter
     */
    public final static String MAKE_SURE_THAT_START_DELIMITER_CHAR_IS_USED_ONCE ="It is very important that you only use @START_HERE once according to my previous instruction";

    /**
     * A clarification text to the LLM model regarding the end delimiter
     */
    public final static String MAKE_SURE_THAT_END_DELIMITER_CHAR_IS_USED_ONCE ="It is very important that you only use @END_HERE once according to my previous instruction";

    // If instructions of a GUI app is provided , instruct model to not use the JavaFX library (It is not included in OpenJdk)
    public final static String NO_JAVA_FX="If I provided instructions that states that I want a GUI app you may only use the Swing library when providing me with code that I will compile";

    /**
     * Do not include libraries that requries external librariesd that is not included in OpenJDK 19.
     */
    public final static String NO_SPECIAL_LIBRARIES ="Do not generate code that contains external libraries that is not included in OpenJDK 19";

    /**
     * Include details about the output's compatibility with Java 19
     */
    public final static String MAKE_SURE_IT_WORKS_ON_JAVA_19 = "Ensure that it works with Java 19";

    /**
     * Implement as much as possible (Good for game app generation)
     */
    public final static String IMPLEMENT_AS_MUCH_AS_POSSIBLE="Do not use placeholders with instructions to handle an event or to implement logic";


    /**
     * A greeting to the AI model
     */
    public final static String GREETING_TO_MODEL="You are an expert coder and understand different programming languages";


    /*
    * Edit existing application specific questions
     */
    /// ***** ///
    public final static String THANKS_FOR_ALL_GENERATED_APPS="You have previously created Java code that I have  compiled successfully";

    public final static String FUNCTIONALITY_NEEDED ="I want to add more functionality to the java code I’m going to send you on the next coming lines";

    public final static String LAST_LINE_OF_SOURCE_CODE ="That line above was the last line of Java code";
    public final static String AFTER_LLM_MODEL_HAS_PARSED_THE_JAVA_CODE_THAT_WE_WANT_TO_MODIFY="Please add Java code that adds the following functionality to the code";

    public final static String NOTE_TO_KEEP_AS_MUCH_ORIGINAL_STRUCTURE_AS_POSSIBLE="Make sure that your generated code keep the current functionality and only add functionality to the existing code";

    public final static String NO_EXISTING_CODE_COMMENTS="Do not replace any code that i have sent with comment lines stating it is existing code. Keep all code that is needed for the application to compile";

    /// ***** ///
}
