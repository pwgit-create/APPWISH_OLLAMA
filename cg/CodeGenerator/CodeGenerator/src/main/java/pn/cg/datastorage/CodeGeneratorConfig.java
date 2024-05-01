package pn.cg.datastorage;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;


import pn.cg.util.PropUtil;

public class CodeGeneratorConfig {

    private final String PROPERTIES_FILE_NAME_FOR_MODEL = "ollama_model.props";
    private final String OLLAMA_MODEL;

    private final String PROPERTIES_FILE_NAME_FOR_CONFIG = "config.props";
    /**
     * Context Window size
     */
    private final int NUM_CTX;



    /**
     * OkHttp 3 connection session value in seconds
     */
    public final static Duration CONNECTION_TIMEOUT = Duration.ofSeconds(600L);

    public CodeGeneratorConfig() {

        this.OLLAMA_MODEL = GetOllamaModelNameFromPropFile();
        this.NUM_CTX = GetNumCTXFroMPropFile();
    }

    private String GetOllamaModelNameFromPropFile() {

        try {

            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_MODEL);
            final String PROPERTY_NAME_MODEL = "MODEL_NAME";
            return properties.getProperty(PROPERTY_NAME_MODEL);
        } catch (IOException e) {
            System.err.println("Error fetching model name");
        }
        return "Error";
    }

    private int GetNumCTXFroMPropFile() {

        try {

            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_CONFIG);
            final String PROPERTY_NAME_NUM_CTX = "NUM_CTX";
            return Integer.parseInt(properties.getProperty(PROPERTY_NAME_NUM_CTX));
        } catch (IOException e) {
            System.err.println("Error fetching num ctx");

        }

        return 2048; // Default value for ctx should be returned if the file is corrupted
    }

    public String getOllamaModel() {
        return this.OLLAMA_MODEL;
    }

    public int getNUM_CTX(){
        return this.NUM_CTX;
    }


}
