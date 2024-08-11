package pn.cg.datastorage;

import java.io.IOException;
import java.util.Properties;


import pn.cg.app_system.code_generation.model.OllamaConfig;
import pn.cg.util.PropUtil;

/**
 * @apiNote The code-comment text for the Ollama parameters variables are from the official Ollama Docs (<a href="https://github.com/ollama/ollama/blob/main/docs/modelfile.md#valid-parameters-and-values">...</a>)
 */
public class CodeGeneratorConfig {

    /**
     * Name of the Ollama AI Model to use with AppWish
     */
    private final String OLLAMA_MODEL;

    /**
     * @OllamaDocs Size of context window (default 2048)
     * @use-info Remember that a high context window demands more compute resources
     * @use-info It is important to check the documentation of the AI-Model you are using regarding the supported context window sizes
     */
    private final int NUM_CTX;

    /**
     * @OllamaDocs A high value will give more creative answers while a low value will give more conservative values (default 40)
     */
    private final int TOP_K;

    /**
     * @OllamaDocs Number of tokens to predict when generating text (default 128, -1 = infinite, -2 = fill context)
     */
    private final int NUM_PREDICT;

    /**
     * @OllamaDocs Increasing the temperature will make the model answer more creatively. (default 0.8)
     */
    private final float TEMPERATURE;

    /**
     * This config states wherever external libraries should be added to the classpath or not
     */
    private final boolean USE_EXTERNAL_LIBRARIES;

    public CodeGeneratorConfig() {
        // This method should only be called once (when app is initializing) constructor
        this.OLLAMA_MODEL = GetOllamaModelNameFromPropFile();

        // Options that derive from the config.props file (located in src/main/resources)
        final OllamaConfig ollamaConfig = CreateOllamaConfigDTOFromPropFile();

        // Designate immutable ollamaConfig variables as final variables to ensure that references remain unchangeable during hooking or debugging
        this.NUM_CTX = ollamaConfig.num_ctx();
        this.TOP_K = ollamaConfig.top_k();
        this.NUM_PREDICT = ollamaConfig.num_predict();
        this.TEMPERATURE = ollamaConfig.temperature();
        this.USE_EXTERNAL_LIBRARIES = ollamaConfig.useExternalLibraries();
    }

    /**
     * Get the value from the "ollama_model.props"
     *
     * @return String that contains the model name
     */
    private final String GetOllamaModelNameFromPropFile() {

        try {

            /*
              File name of the file containing the model name
             */
            final String PROPERTIES_FILE_NAME_FOR_MODEL = "ollama_model.props";

            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_MODEL);
            final String PROPERTY_NAME_MODEL = "MODEL_NAME";
            return properties.getProperty(PROPERTY_NAME_MODEL);
        } catch (IOException e) {
            System.err.println("Error fetching model name\nTry to correct the model name in the 'ollama_model.props' ");
        }
        return "Error";
    }

    /**
     * Creates an OllamaConfigDTO.
     *
     * @return Record with Ollama config options that are editable in appwish
     * @use-info In the event of an error, the record returned will only contain the default values
     */
    private final OllamaConfig CreateOllamaConfigDTOFromPropFile() {
        OllamaConfig newOllamaConfigRecord;
        try {

            /*
              The file that holds the Ollama configurations that AppWish allows you to modify
             */
            final String PROPERTIES_FILE_NAME_FOR_CONFIG = "config.props";

            /*
             The config.prop file's key values' names
             */
            final String PROPERTY_NAME_NUM_CTX = "NUM_CTX";
            final String PROPERTY_NAME_TOP_K = "TOP_K";
            final String PROPERTY_NAME_NUM_PREDICT = "NUM_PREDICT";
            final String PROPERTY_NAME_TEMPERATURE = "TEMPERATURE";
            final String PROPERTY_NAME_USE_EXTERNAL_LIBRARIES = "USE_EXTERNAL_LIBRARIES";

            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_CONFIG);

            newOllamaConfigRecord = new OllamaConfig(Integer.parseInt(properties.getProperty(PROPERTY_NAME_NUM_CTX)),
                    Integer.parseInt(properties.getProperty(PROPERTY_NAME_TOP_K)),
                    Integer.parseInt(properties.getProperty(PROPERTY_NAME_NUM_PREDICT)),
                    Float.parseFloat(properties.getProperty(PROPERTY_NAME_TEMPERATURE)),
                    convertExternalLibConfigIntoBooleanValue(properties.getProperty(PROPERTY_NAME_USE_EXTERNAL_LIBRARIES)));
        } catch (IOException e) {
            System.err.println("Error when fetching the ollama config file\nSetting default values for all config parameters..");
            // Default values should be returned if the ollama config file is corrupted
            newOllamaConfigRecord = new OllamaConfig(2048, 40, 128, 0.8f, false);
        }
        return newOllamaConfigRecord;
    }

    /**
     * Retrieves the name of the AI model that is being used with AppWish
     *
     * @return String
     */
    public final String getOllamaModel() {
        return this.OLLAMA_MODEL;
    }

    /**
     * Get the value of the context window
     *
     * @return int
     */
    public final int getNUM_CTX() {
        return this.NUM_CTX;
    }

    /**
     * Get the TOP_K value
     *
     * @return int
     */
    public final int getTOP_K() {
        return this.TOP_K;
    }

    /**
     * Get the number of tokens to predict
     *
     * @return int
     */
    public final int getNUM_PREDICT() {
        return NUM_PREDICT;
    }

    /**
     * Get the temperature that the AI Model uses
     *
     * @return float
     */
    public final float getTEMPERATURE() {
        return TEMPERATURE;
    }

    public boolean isUSE_EXTERNAL_LIBRARIES() {
        return USE_EXTERNAL_LIBRARIES;
    }

    private final boolean convertExternalLibConfigIntoBooleanValue(String value) {

        return value.equalsIgnoreCase("On");

    }
}
