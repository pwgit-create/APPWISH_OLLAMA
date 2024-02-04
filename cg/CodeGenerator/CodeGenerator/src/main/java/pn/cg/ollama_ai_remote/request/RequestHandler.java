package pn.cg.ollama_ai_remote.request;

import java.util.List;


/**
 * Handles all requests to the OLLAMA Api (Running on Localhost)
 */
public interface RequestHandler {



    /**
     * Sends a question to OLLAMA api and gets a reply
     * @param question
     * @return String
     */
    String sendQuestionToOllamaInstance(String question);

    /**
     * Sends a question to OLLAMA api and gets a reply
     * @param question
     * @param pathToJavaFileToModify
     * @param contentOfExistingJavaFile
     * @return String
     */
    String sendQuestionToOllamaInstance(String question,String pathToJavaFileToModify,List<String> contentOfExistingJavaFile);

}
