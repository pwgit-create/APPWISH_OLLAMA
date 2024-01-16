package pn.cg.app_wish;

import pn.cg.datastorage.constant.QuestionConstants;

public class QuestionBuilder {

    private final String appWish;

    public QuestionBuilder(String appWish) {
        this.appWish = appWish;
    }


    /**
     * Creates a questions for ollama model that should produce java codes of a feature
     *
     * @return String
     * @Respone-goal Java class/classes for a complete feature
     */
    public String createFeatureQuestion() {

        return QuestionConstants.APP_WISH_PREFIX_FEATURE + appWish+ QuestionConstants.WITH_MAIN_METHOD+ QuestionConstants.AND_CORRECT_IMPORTS+QuestionConstants.INCLUDE_IN_ONE_FILE + QuestionConstants.AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE + QuestionConstants.AND_MARK_START_AND_END_DELIMITER_CHARS  + QuestionConstants.AND_MAKE_SURE_IT_WORKS_ON_JAVA_19 ;
    }

    /**
     * Creates a questions for ollama model that should produce java code of a class (in a feature)
     *
     * @param classForFeature
     * @return String
     * @Respone-goal A single java class that is needed part for the feature
     */
    public String createClassQuestion(final String classForFeature) {
        return QuestionConstants.APP_WISH_PREFIX_CLASS + classForFeature;
    }

    /**
     * Creates a question for ollama model that should produce a correct implementation of class that did not compile
     *
     * @param compileErrorMessage
     * @return String
     * @Response-goal A class implementation that will compile successfully
     */
    public String createCompileErrorQuestion(String compileErrorMessage) {

        return QuestionConstants.CLASS_DID_NOT_COMPILE_PREFIX + "\n" + compileErrorMessage;
    }


    /**
     * Creates a question for ollama model that should produce a list of classes that is needed to create an app from the users wish
     *
     * @return String
     * @Response-goal A numbered list of all the java classes that are needed to build an app from the users wish
     */
    public String createClassesNeededForAppWishQuestion() {

        return QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX + appWish + QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_SUFFIX;
    }


}
