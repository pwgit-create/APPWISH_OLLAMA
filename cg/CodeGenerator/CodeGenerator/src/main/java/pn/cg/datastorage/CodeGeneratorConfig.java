package pn.cg.datastorage;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;


import pn.cg.util.PropUtil;

public class CodeGeneratorConfig {

    private final String OLLAMA_MODEL;
    private final String PROPERTIES_FILE_NAME ="ollama_model.props";
    private final String PROPERTY_NAME_MODEL="MODEL_NAME";
  

    /**
     *  OkHttp 3 connection session value in seconds
     */
    public final static Duration CONNECTION_TIMEOUT=Duration.ofSeconds(600L);

    public CodeGeneratorConfig(){

        this.OLLAMA_MODEL = GetOllamaModelName();

    }

    public String GetOllamaModelName(){

      try {
        Properties properties =  PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME);
        return properties.getProperty(PROPERTY_NAME_MODEL);
    } catch (IOException e) {
        System.err.println("Error fetching model name");
        e.printStackTrace();
    }

    return "Error";
    }

    public String getOLLAMA_MODEL() {
        return OLLAMA_MODEL;
    }

    

}
