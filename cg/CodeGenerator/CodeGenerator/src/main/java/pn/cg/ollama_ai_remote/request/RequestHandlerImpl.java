package pn.cg.ollama_ai_remote.request;


import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.datastorage.CodeGeneratorConfig;
import pn.cg.datastorage.constant.QuestionConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.utils.Options;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;
import io.github.amithkoujalgi.ollama4j.core.utils.PromptBuilder;
import pn.cg.util.CodeGeneratorUtil;
import pn.cg.util.StringUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static pn.cg.util.CodeGeneratorUtil.isThisACreateNewAppRequest;


public class RequestHandlerImpl implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerImpl.class);

    private final String HOST = "http://localhost:11434/";
    private final OllamaAPI api;
    private final Options options;
    private final CodeGeneratorConfig codeGeneratorConfig;


    public RequestHandlerImpl() {

        this.api =  new OllamaAPI(HOST);
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
                    .addLine(QuestionConstants.GREETING_TO_MODEL)
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
                    .addLine(QuestionConstants.GREETING_TO_MODEL)
                    .addLine(QuestionConstants.THANKS_FOR_ALL_GENERATED_APPS)
                    .addLine(QuestionConstants.FUNCTIONALITY_NEEDED);

            contentOfExistingJavaFile.forEach(line -> promptBuilder.addLine(line));

            promptBuilder
                    .addLine(QuestionConstants.LAST_LINE_OF_SOURCE_CODE)
                    .addLine(QuestionConstants.AFTER_LLM_MODEL_HAS_PARSED_THE_JAVA_CODE_THAT_WE_WANT_TO_MODIFY)
                    .addLine(question)
                    .addLine(QuestionConstants.NOTE_TO_KEEP_AS_MUCH_ORIGINAL_STRUCTURE_AS_POSSIBLE)
                    .addLine(QuestionConstants.NO_EXISTING_CODE_COMMENTS)
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
            result = api.generate(codeGeneratorConfig.getOllamaModel(), promptBuilder.build(), options);

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (result == null) throw new AssertionError();
        String outputFromOllamaAPI = (result.getResponse());
        log.debug(outputFromOllamaAPI);
        return outputFromOllamaAPI;
    }

    @Override
    public List<SuperApp> sendClassesNeededForSuperAppQuestionToOllamaInstance(String question) {

        List<String> classNames = GetListOfClasses(question);

        if(classNames.isEmpty())
            sendClassesNeededForSuperAppQuestionToOllamaInstance(question);

        return CodeGeneratorUtil.convertListOfStringClassNamesIntoAnListOfUnimplementedSuperAppClasses(classNames);
    }

    private List<String> GetListOfClasses(String question){

        List<String> classNames = new LinkedList<>();


        PromptBuilder initialPrompt = new PromptBuilder().addLine(QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_PREFIX)
                .addLine(question)
                .addLine(QuestionConstants.LAST_LINE_OF_SUPER_APP)
                .addLine(QuestionConstants.WHICH_CLASS_ARE_NEEDED_FOR_APP_RESPONSE_FORMAT_1)
                .addLine(QuestionConstants.ONLY_CLASS_NAMES_IN_ANSWER)
                .addLine(QuestionConstants.CLARIFY_THAT_NO_DOTS_OR_NUMBERS_SHOULD_BE_INCLUDED_IN_THE_RESPONSE);


        OllamaResult result = null;
        try {
            result = api.generate(codeGeneratorConfig.getOllamaModel(), initialPrompt.build(), options);

            classNames = StringUtil.GetListOfClassNamesInSuperAppGeneration(result.getResponse());

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            log.error("Error when sending the request to the local ollama server");
        }

        return classNames;

    }

}




