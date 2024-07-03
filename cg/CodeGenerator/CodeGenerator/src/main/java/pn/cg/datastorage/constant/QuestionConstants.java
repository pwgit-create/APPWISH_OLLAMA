package pn.cg.datastorage.constant;

import javax.print.DocFlavor;

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
    public final static String MARK_START_CHAR_DELIMITER = "Mark the start of the first line of java code including the import statements with the text @START_HERE";
    
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
     * Do not include libraries that requires external libraries that is not included in OpenJDK 19.
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

    public final static String NO_EXISTING_CODE_COMMENTS="I need the actual code without any comments or placeholders referencing existing code";

    public final static String THREAD_PACKAGE="If your code requires the Java Thread package use import java.lang.Thread";

    public final static String ONLY_CODE ="Your answer should only contain code, any further explanations should be kept as comments within the code";
    /// ***** ///

    /*
     * Super app - specific questions
     */
    /// ***** ///

    /**
     * Prefix that should be sent to fetch a list of classes from the ollama model
     *
     * @use-info The (super) appWish should be concatenated at the tail of this String
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 1/5
     * @SuperApp-Question
     */
    public final static String WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX = "Which classes do we need to implement ourself in java to make an application with the features that is provided in the next coming lines?";

    /**
     * @use-info This is the string that will tell the AI-Model that the line above is the last line of the (super) appWish
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 2/5
     * @SuperApp-Question
     */
    public final static String LAST_LINE_OF_SUPER_APP="The line above was the last line that included features of the app I need";

    /**
     *
     * @use-info Text that specifies the format of the response from the AI-model
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 3/5
     */
    public final static String WHICH_CLASS_ARE_NEEDED_FOR_APP_RESPONSE_FORMAT_1 = "Please provide the name of the first java class and then make a new line for the second java class and then continue until you have listed all the class names of the application I need";


    /**
     * @use-info Additional guidelines regarding format in the response from the AI-model
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 4/5
     */
    public final static String ONLY_CLASS_NAMES_IN_ANSWER="Your response should only contain the needed classes name for the application and there shall be one class name per line, your reply must not contain anything else";

    /**
     * @use-info Clarify that no numbers or dots should be included in the response from the AI-model
     * @part-of-question-chain WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX
     * @chain-order 5/5
     */
    public final static String CLARIFY_THAT_NO_DOTS_OR_NUMBERS_SHOULD_BE_INCLUDED_IN_THE_RESPONSE="Your response shall not include numbers or dots or spaces to state the order of the classes, it shall only be one class name per line";

    /**
     * This is the line that initiates the phase that happens after the classes for the super app has been stated
     * @use-info Use this when the classes for the super app has been decided as the first line to the AI-model
     */
    public final static String FIRST_LINE_AFTER_CLASSES_HAS_BEEN_STATED_BY_MODEL="You have previously stated several classes that is needed to make my application";

    /**
     * Provide java code for a class
     * @use-info Append the appWish at the tail of this question
     */
    public final static String PROVIDE_ME_JAVA_CODE_SUPER_APP_SPECIFIC=APP_WISH_PREFIX_FEATURE+"the class with name ";

    /***
     * Clarify the rules for the super app java class generation to the AI-model
     */
    public final static String  CLARIFY_JAVA_CODE_IN_SUPER_APP_RULES="Make sure that the generated code for the java class is compatible with the other classes that you stated was needed for the application , that includes correct variable names and correct imports and class names";

    /**
     * Share information to the AI-Model that it always should include a Main class that contains the Main Method
     */
    public final static String MAIN_CLASS_SHOULD_ALWAYS_BE_ADDED_TO_THE_CLASS_LIST="The last entry in the class list that you will provide , please add a class called Main";

    /**
     * Instruct the AI-Model that the Main class is used to start the application (To avoid an extra class in the code base)
     */
    public final static String EXPLAIN_PURPOSE_WITH_MAIN_CLASS="The purpose with Main is to start the application but do not add that information in your response";

    /**
     * Share information to the AI-Model about the requirements for the implementation of the Main class ( In a super app generation process)
     */
    public final static String IMPLEMENT_MAIN_CLASS_IN_SUPER_APP_CREATION=WITH_MAIN_METHOD +" and that the body of that main method is empty or invokes a method in one of the other classes that we generated before";

    /**
     * Instruct the AI-Model to not include a main method unless the class name is main
     */
    public final static String NO_MAIN_CLASS_UNLESS_THE_CLASS_NAME_IS_MAIN="Do not include a main method unless the class name is Main or main";

    /**
     * Remind the AI-Model that the current class generation should consider the other classes (of the entire super app)
     */
    public final static String MAKE_SURE_IT_ALIGNS_WITH_OTHER_CLASSES="Please make sure that the class you are generating now will work with the following classes that we generated previously and implemented and if you forgot their names they will be provided for you in the next lines along with the methods that it contains";

    /**
     * Just a line stating that for the AI-Model that the line above was the last line of the list of classes and their method members
     */
    public final static String THAT_WAS_THE_LAST_LINE_OF_REMEMBER_CLASSES="The line above was the last line of class names to consider when creating the class";

    /**
     * A line that only should be included if it is the first class of N in a super app generation
     * @chain-order 1/4
     */
    public final static String CONSIDER_ALL_CLASSES_IN_THIS_SUPER_APP_CREATION="Please note that the class that you will create is one of many in this application and you need to consider that when creating this class";

    /**
     * Clarify that the other classes are not implemented yet and can´t be referenced in the code yet
     * @chain-order 2/4
     */
    public final static String CLASSES_THAT_ARE_INCLUDED_IN_THE_SUPER_APP="The classes that you need to consider are not yet implemented so you can not use references to those classes in your code for this class unless you provide them as comments, but you can still consider what methods those classes might need from this class";

    /**
     * Start line before unimplemented classes are listed
     * @chain-order 3/4
     */
    public final static String THE_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP="These unimplemented classes are provided for you in the next coming lines";

    /**
     * End line after all unimplemented classes has been listed
     * @chain-order 4/4
     */
    public final static String LAST_LINE_OF_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP="The line above was the last line that included unimplemented classes that will be included in this application in the future. Remember that you can not use references to those classes in your code for this class unless you provide them as comments";

    /**
     * Instruct the AI-Model to not assume that classes contains methods or constructor parameters that they do not
     */
    public final static String DO_NOT_ASSUME_THAT_CLASSES_CONTAINS_METHODS_OR_CONSTRUCTORS_THAT_THEY_DO_NOT="Please do not assume parameter types in methods or constructors of other classes and include them in the code if it is not stated in the query that they exists";

    /// ***** ///



}
