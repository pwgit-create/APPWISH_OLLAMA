package pn.cg.ollama_ai_remote.request;

import java.util.List;


/**
 * Handles all requests to the OLLAMA Api (Running on Localhost)
 */
public interface RequestHandler {



    /**
     * Sends a question to OLLAMA api and gets a reply
     * @param question Question that contains text that describes requirements and functionality of a new app
     * @return String
     */
    String sendQuestionToOllamaInstance(String question);

    /**
     * Sends a question to Ollama api and gets a reply
     * @param question Question that contains text that describes functionality that the user wants to add into an existing app
     * @param pathToJavaFileToModify The file-path of the java file that you want to add functionality into
     * @param contentOfExistingJavaFile The content of the Java file that you want to add functionality into
     * @return String
     */
    String sendQuestionToOllamaInstance(String question,String pathToJavaFileToModify,List<String> contentOfExistingJavaFile);

    /**
     *
     * @param question Question that contains text that describes requirements and functionality of a new super app
     * @return String
     */
    String sendSuperAppQuestionToOllamaInstance(String question);

}
