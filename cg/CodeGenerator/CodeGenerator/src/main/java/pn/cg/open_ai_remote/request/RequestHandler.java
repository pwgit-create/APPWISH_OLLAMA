package pn.cg.open_ai_remote.request;




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

}
