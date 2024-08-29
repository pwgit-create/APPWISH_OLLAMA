package pn.cg.ollama_ai_remote.request;


import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;

import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;
import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.datastorage.CodeGeneratorConfig;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.constant.QuestionConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import pn.cg.util.CodeGeneratorUtil;
import pn.cg.util.StringUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static pn.cg.datastorage.constant.QuestionConstants.*;
import static pn.cg.util.CodeGeneratorUtil.*;
import static pn.cg.util.CodeGeneratorUtil.GetFormattedStringForAClassName;


public class RequestHandlerImpl implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerImpl.class);

    private final String HOST = "http://localhost:11434/";
    private final OllamaAPI api;
    private final Options options;
    private final CodeGeneratorConfig codeGeneratorConfig;


    public RequestHandlerImpl() {

        this.api = new OllamaAPI(HOST);
        this.codeGeneratorConfig = new CodeGeneratorConfig();
        this.options = new OptionsBuilder()
                .setNumCtx(codeGeneratorConfig.getNUM_CTX())
                .setTopK(codeGeneratorConfig.getTOP_K())
                .setNumPredict(codeGeneratorConfig.getNUM_PREDICT())
                .setTemperature(codeGeneratorConfig.getTEMPERATURE())
                .build();

        api.setRequestTimeoutSeconds(100000);
        log.info("Is OLLAMA server alive? -> {}", api.ping()); // This is your local OLLAMA server running on localhost and is used for the LLM model
    }

    @Override
    public String sendQuestionToOllamaInstance(String question) {
        // Create a new app questions
        return sendQuestionToOllamaInstance(question, "", null);

    }

    @Override
    public String sendQuestionToOllamaInstance(String question, String pathToJavaFileToModify, List<String> contentOfExistingJavaFile) {


        boolean isThisNewAppRequest = isThisACreateNewAppRequest(pathToJavaFileToModify, contentOfExistingJavaFile);


        PromptBuilder promptBuilder;
        if (isThisNewAppRequest) {
            promptBuilder = new PromptBuilder()
                    .addLine(GREETING_TO_MODEL)
                    .addLine(question)
                    .addLine(QuestionConstants.WITH_MAIN_METHOD)
                    .addLine(QuestionConstants.AND_CORRECT_IMPORTS)
                    .addLine(QuestionConstants.INCLUDE_IN_ONE_FILE)
                    .addLine(QuestionConstants.AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE)
                    .addLine(QuestionConstants.MARK_START_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MARK_THE_END_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_START_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_END_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.NO_JAVA_FX)
                    .addLine(QuestionConstants.NO_SPECIAL_LIBRARIES)
                    .addLine(QuestionConstants.MAKE_SURE_IT_WORKS_ON_JAVA_19)
                    .addLine(QuestionConstants.IMPLEMENT_AS_MUCH_AS_POSSIBLE)
                    .addLine(QuestionConstants.THREAD_PACKAGE)
                    .addLine(QuestionConstants.ONLY_CODE);
        } else {

            promptBuilder = new PromptBuilder()
                    .addLine(GREETING_TO_MODEL)
                    .addLine(QuestionConstants.THANKS_FOR_ALL_GENERATED_APPS)
                    .addLine(QuestionConstants.FUNCTIONALITY_NEEDED);

            contentOfExistingJavaFile.forEach(line -> promptBuilder.addLine(line));

            promptBuilder
                    .addLine(QuestionConstants.LAST_LINE_OF_SOURCE_CODE)
                    .addLine(QuestionConstants.AFTER_LLM_MODEL_HAS_PARSED_THE_JAVA_CODE_THAT_WE_WANT_TO_MODIFY)
                    .addLine(question)
                    .addLine(QuestionConstants.NOTE_TO_KEEP_AS_MUCH_ORIGINAL_STRUCTURE_AS_POSSIBLE)
                    .addLine(QuestionConstants.NO_EXISTING_CODE_COMMENTS)
                    .add(QuestionConstants.NEVER_ADD_EXISTING_CODE_REFERENCE_COMMENTS)
                    .addLine(QuestionConstants.AND_CORRECT_IMPORTS)
                    .addLine(QuestionConstants.MARK_START_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MARK_THE_END_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_START_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_END_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.NO_JAVA_FX)
                    .addLine(QuestionConstants.NO_SPECIAL_LIBRARIES)
                    .addLine(QuestionConstants.MAKE_SURE_IT_WORKS_ON_JAVA_19)
                    .addLine(QuestionConstants.THREAD_PACKAGE)
                    .addLine(QuestionConstants.ONLY_CODE);

        }


        OllamaResult result = null;
        try {
            result = api.generate(codeGeneratorConfig.getOllamaModel(), promptBuilder.build(),false, options);

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            log.error("Error while sending a request to the local Ollama Server");
        }
        if (result == null) throw new AssertionError();
        String outputFromOllamaAPI = (result.getResponse());
        log.debug(outputFromOllamaAPI);
        return outputFromOllamaAPI;
    }

    @Override
    public List<SuperApp> sendClassesNeededForSuperAppQuestionToOllamaInstance(String question) {

      String responseFromAiModel  = GetListOfClasses(question);

        // Resend the question if validation fails (With recursive strategy)
        if (responseFromAiModel.isEmpty() ||
                !(CodeGeneratorUtil.ValidateResponseOnSuperAppGetAllClassesQuestion(responseFromAiModel))){
            sendClassesNeededForSuperAppQuestionToOllamaInstance(question);}

        // Convert response into list format
        List<String> classNames = StringUtil.GetListOfClassNamesInSuperAppGeneration(responseFromAiModel);

        // Resend the question (Extra validation after method invocation)
        if(classNames.size() < 2){
            sendClassesNeededForSuperAppQuestionToOllamaInstance(question);
        }

        List<SuperApp> unimplementedSuperAppClasses = CodeGeneratorUtil.convertListOfStringClassNamesIntoAnListOfUnimplementedSuperAppClasses(classNames);

        // (Extra validation after method invocation)
        if(unimplementedSuperAppClasses.size() < 2) {
            sendClassesNeededForSuperAppQuestionToOllamaInstance(question);
        }
        
        return unimplementedSuperAppClasses;
    }

    @Override
    public String sendSuperAppQuestionToOllamaInstance(SuperApp superAppClass) {
        PromptBuilder promptBuilder;
        List<SuperApp> superAppList = DataStorage.getInstance().getListOfCurrentSuperAppClasses();

        // Include the main method in the Main class
        if (superAppClass.getClassName().equals("Main") || superAppClass.getClassName().equalsIgnoreCase("MainClass")) {

            // Always include
            promptBuilder = new PromptBuilder()
                    .addLine(GREETING_TO_MODEL)
                    .addLine(PROVIDE_ME_JAVA_CODE_SUPER_APP_SPECIFIC + superAppClass.getClassName())
                    .addLine(IMPLEMENT_MAIN_CLASS_IN_SUPER_APP_CREATION)
                    .addLine(AND_CORRECT_IMPORTS)
                    .addLine(DO_NOT_ASSUME_PACKAGE_NAMES);


            // At last one class has been implemented
            if (superAppList.stream().anyMatch(SuperApp::isImplemented)) {
                promptBuilder.addLine(MAKE_SURE_IT_ALIGNS_WITH_OTHER_CLASSES);
                superAppList.stream().filter(SuperApp::isImplemented).forEach(s -> promptBuilder
                        .add(GetFormattedStringForAClassName(s) + CodeGeneratorUtil.GetFormattedListOfMethodsString(s)
                                + CodeGeneratorUtil.GetFormattedListOfConstructorString(s)));
                promptBuilder.addLine(THAT_WAS_THE_LAST_LINE_OF_REMEMBER_CLASSES)
                        .addLine(QuestionConstants.DO_NOT_ASSUME_THAT_CLASSES_CONTAINS_METHODS_OR_CONSTRUCTORS_THAT_THEY_DO_NOT);
            }

            // No class at all has been implemented
            else {

                promptBuilder.addLine(CONSIDER_ALL_CLASSES_IN_THIS_SUPER_APP_CREATION)
                        .addLine(CLASSES_THAT_ARE_INCLUDED_IN_THE_SUPER_APP)
                        .addLine(THE_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP);

                superAppList.stream().filter(s -> !(s.getClassName()
                                .equalsIgnoreCase(superAppClass.getClassName())))
                        .toList()
                        .forEach(c -> promptBuilder.addLine(c.getClassName()));
                promptBuilder
                        .addLine(LAST_LINE_OF_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP)
                        .addLine(QuestionConstants.DO_NOT_ASSUME_THAT_CLASSES_CONTAINS_METHODS_OR_CONSTRUCTORS_THAT_THEY_DO_NOT);
            }
            // Always include
            promptBuilder
                    .addLine(QuestionConstants.AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE)
                    .addLine(QuestionConstants.MARK_START_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MARK_THE_END_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_START_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_END_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.NO_JAVA_FX)
                    .addLine(QuestionConstants.NO_SPECIAL_LIBRARIES)
                    .addLine(QuestionConstants.MAKE_SURE_IT_WORKS_ON_JAVA_19)
                    .addLine(QuestionConstants.IMPLEMENT_AS_MUCH_AS_POSSIBLE)
                    .addLine(QuestionConstants.THREAD_PACKAGE)
                    .addLine(QuestionConstants.ONLY_CODE);

        }


        // Use this block for all other classes
        else {
            // Always include
            promptBuilder = new PromptBuilder()
                    .addLine(GREETING_TO_MODEL)
                    .addLine(PROVIDE_ME_JAVA_CODE_SUPER_APP_SPECIFIC + superAppClass.getClassName())
                    .addLine(QuestionConstants.AND_CORRECT_IMPORTS)
                    .addLine(NO_MAIN_CLASS_UNLESS_THE_CLASS_NAME_IS_MAIN)
                    .addLine(QuestionConstants.AND_MAKE_SURE_CORRECT_NUMBER_OF_BRACE_BRACKETS_ARE_USED_AT_THE_END_OF_THE_JAVA_CODE)
                    .addLine(QuestionConstants.MARK_START_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MARK_THE_END_CHAR_DELIMITER)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_START_DELIMITER_CHAR_IS_USED_ONCE)
                    .addLine(QuestionConstants.MAKE_SURE_THAT_END_DELIMITER_CHAR_IS_USED_ONCE);

            // At last one class has been implemented
            if (superAppList.stream().anyMatch(SuperApp::isImplemented)) {
                promptBuilder.addLine(MAKE_SURE_IT_ALIGNS_WITH_OTHER_CLASSES);
                superAppList.stream().filter(SuperApp::isImplemented).forEach(s -> promptBuilder.add(GetFormattedStringForAClassName(s)
                        + CodeGeneratorUtil.GetFormattedListOfMethodsString(s) + GetFormattedListOfConstructorString(s)));
                promptBuilder
                        .addLine(THAT_WAS_THE_LAST_LINE_OF_REMEMBER_CLASSES)
                        .addLine(DO_NOT_ASSUME_THAT_CLASSES_CONTAINS_METHODS_OR_CONSTRUCTORS_THAT_THEY_DO_NOT);
            }
            // No class at all has been implemented
            else {

                promptBuilder.addLine(CONSIDER_ALL_CLASSES_IN_THIS_SUPER_APP_CREATION)
                        .addLine(CLASSES_THAT_ARE_INCLUDED_IN_THE_SUPER_APP)
                        .addLine(THE_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP);

                superAppList.stream().filter(s -> !(s.getClassName()
                                .equalsIgnoreCase(superAppClass.getClassName())))
                        .toList()
                        .forEach(c -> promptBuilder.addLine(c.getClassName()));
                promptBuilder.addLine(LAST_LINE_OF_UNIMPLEMENTED_CLASSES_OF_THE_SUPER_APP);
            }
            // Always include
            promptBuilder
                    .addLine(QuestionConstants.NO_JAVA_FX)
                    .addLine(QuestionConstants.NO_SPECIAL_LIBRARIES)
                    .addLine(QuestionConstants.MAKE_SURE_IT_WORKS_ON_JAVA_19)
                    .addLine(QuestionConstants.IMPLEMENT_AS_MUCH_AS_POSSIBLE)
                    .addLine(QuestionConstants.THREAD_PACKAGE)
                    .addLine(QuestionConstants.ONLY_CODE);
        }

        OllamaResult result = null;
        try {
            result = api.generate(codeGeneratorConfig.getOllamaModel(), promptBuilder.build(),false ,options);

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            log.error("Error while sending a request to the local Ollama Server");
        }
        if (result == null) throw new AssertionError();
        String outputFromOllamaAPI = (result.getResponse());
        log.info(outputFromOllamaAPI);
        return outputFromOllamaAPI;
    }

    private String GetListOfClasses(String question) {

        List<String> classNames = new LinkedList<>();
        PromptBuilder initialPrompt = new PromptBuilder().addLine(QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX)
                .addLine(question)
                .addLine(QuestionConstants.LAST_LINE_OF_SUPER_APP)
                .addLine(QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_RESPONSE_FORMAT_1)
                .addLine(QuestionConstants.ONLY_CLASS_NAMES_IN_ANSWER)
                .addLine(QuestionConstants.CLARIFY_THAT_NO_DOTS_OR_NUMBERS_SHOULD_BE_INCLUDED_IN_THE_RESPONSE)
                .addLine(QuestionConstants.MAIN_CLASS_SHOULD_ALWAYS_BE_ADDED_TO_THE_CLASS_LIST)
                .addLine(QuestionConstants.THE_ORDER_OF_CLASSES_ARE_IMPORTANT);

        OllamaResult result = null;
        try {
            result = api.generate(codeGeneratorConfig.getOllamaModel(), initialPrompt.build(),false, options);


        } catch (OllamaBaseException | IOException | InterruptedException e) {
            log.error("Error when sending the request to the local ollama server");
        }

        return result.getResponse();

    }
}




