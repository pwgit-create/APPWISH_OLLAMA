package pn.cg.ollama_ai_remote.request;


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

import java.io.IOException;
import java.util.List;

import static pn.cg.util.CodeGeneratorUtil.isThisACreateNewAppRequest;


public class RequestHandlerImpl implements RequestHandler {

    private final String HOST = "http://localhost:11434/";


    private static final Logger log = LoggerFactory.getLogger(RequestHandlerImpl.class);

    public RequestHandlerImpl() {

    }

    @Override
    public String sendQuestionToOllamaInstance(String question) {
        // Create a new app questions
        return sendQuestionToOllamaInstance(question, "", null);

    }

    @Override
    public String sendQuestionToOllamaInstance(String question, String pathToJavaFileToModify, List<String> contentOfExistingJavaFile) {
        OllamaAPI api = new OllamaAPI(HOST);

        boolean isThisNewAppRequest = isThisACreateNewAppRequest(pathToJavaFileToModify, contentOfExistingJavaFile);
        CodeGeneratorConfig codeGeneratorConfig = new CodeGeneratorConfig();

        Options options =
                new OptionsBuilder()
                        .setNumCtx(codeGeneratorConfig.getNUM_CTX())
                        .build();


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

            contentOfExistingJavaFile.stream().forEach(line -> promptBuilder.addLine(line));

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
                    .addLine(QuestionConstants.ONLY_CODE)
                    .addLine(QuestionConstants.NO_PLACEHOLDER_FOR_EXISTING_LOGIC);

        }
        api.setRequestTimeoutSeconds(100000);
        log.info("Is OLLAMA server alive? -> " + api.ping()); // This is your local OLLAMA server running on localhost and is used for the LLM model

        OllamaResult result = null;
        try {
            result = api.ask(codeGeneratorConfig.getOllamaModel(), promptBuilder.build(), options);

        } catch (OllamaBaseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String outputFromOllamaAPI = (result.getResponse());
        log.debug(outputFromOllamaAPI);
        return outputFromOllamaAPI;
    }

}




