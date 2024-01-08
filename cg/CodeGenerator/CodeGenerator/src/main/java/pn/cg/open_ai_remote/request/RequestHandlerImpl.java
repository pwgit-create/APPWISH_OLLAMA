package pn.cg.open_ai_remote.request;


import pn.cg.datastorage.CodeGeneratorConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.utils.Options;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;

import java.io.IOException;
import java.util.List;

public class RequestHandlerImpl implements RequestHandler {

    private final String HOST="http://localhost:11434/";
    

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerImpl.class);

    public RequestHandlerImpl() {

    }


    @Override
    public  String sendQuestionToOllamaInstance(String question) {
        
        OllamaAPI api = new OllamaAPI(HOST);
 

        Options options =
                new OptionsBuilder()
                        .setNumCtx(8000)
                        .build();

    api.setRequestTimeoutSeconds(1000);
       log.info("Is server alive? -> "+ api.ping());
        CodeGeneratorConfig codeGeneratorConfig = new CodeGeneratorConfig();
        OllamaResult result = null;
        try {
            result = api.ask(codeGeneratorConfig.GetOllamaModelName(), question,options);
            
        } catch (OllamaBaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String outputFromOllamaAPI = (result.getResponse());
        log.debug(outputFromOllamaAPI);
        return outputFromOllamaAPI;
    }
}




