package pn.cg.ollama_ai_remote.request;

import pn.cg.app_system.code_generation.model.SuperApp;

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
     * Send a request to the AI-Model and ask which classes are needed for the super app creation
     * @param question Question that contains text that describes requirements and functionality of a new super app
     * @Recursive
     * @return List<SuperApp> that contains the class names for the super app (decided by the AI-model) and their implementation status
     */
    List<SuperApp> sendClassesNeededForSuperAppQuestionToOllamaInstance(String question);

    /**
     * Send a request to the AI-Model where we ask it to implement the input super class with java code
     * @param superAppClass A class in the super app project
     * @return String with meta information on the current progress
     */
    String sendSuperAppQuestionToOllamaInstance(SuperApp superAppClass);

}
