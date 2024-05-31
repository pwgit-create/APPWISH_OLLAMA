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
     * File name of the file containing the model name
     */
    private final String PROPERTIES_FILE_NAME_FOR_MODEL = "ollama_model.props";
    /**
     * Name of the Ollama AI Model to use with appwish
     */
    private final String OLLAMA_MODEL;

    /**
     * File name of the file containing the ollama configs that you can modify in appwish
     */
    private final String PROPERTIES_FILE_NAME_FOR_CONFIG = "config.props";

    /**
     * @OllamaDocs Size of context window (default 2048)
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

    public CodeGeneratorConfig() {
        this.OLLAMA_MODEL = GetOllamaModelNameFromPropFile();

        // Placeholder record for the Ollama config options
        final OllamaConfig ollamaConfig = GetOllamaConfigsFromPropFile();

        // Making the Ollama config parameters variables immutable
        this.NUM_CTX = ollamaConfig.num_ctx();
        this.TOP_K = ollamaConfig.top_k();
        this.NUM_PREDICT = ollamaConfig.num_predict();
        this.TEMPERATURE = ollamaConfig.temperature();
    }

    /**
     * Get the value from the "ollama_model.props"
     * @return String that contains the model name
     */
    private final String GetOllamaModelNameFromPropFile() {

        try {

            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_MODEL);
            final String PROPERTY_NAME_MODEL = "MODEL_NAME";
            return properties.getProperty(PROPERTY_NAME_MODEL);
        } catch (IOException e) {
            System.err.println("Error fetching model name\nTry to correct the model name in the 'ollama_model.props' ");
        }
        return "Error";
    }

    /**
     * Gets the values from the "config.props" file. If an error occurs , the record returned will only include default values.
     *
     * @return Record with Ollama config options that are editable in appwish
     */
    private final OllamaConfig GetOllamaConfigsFromPropFile() {
        OllamaConfig methodLocalConfig;
        try {
            final Properties properties = PropUtil.ReadPropertiesFile(PROPERTIES_FILE_NAME_FOR_CONFIG);
            final String PROPERTY_NAME_NUM_CTX = "NUM_CTX";
            final String PROPERTY_NAME_TOP_K = "TOP_K";
            final String PROPERTY_NAME_NUM_PREDICT = "NUM_PREDICT";
            final String PROPERTY_NAME_TEMPERATURE = "TEMPERATURE";

            methodLocalConfig = new OllamaConfig(Integer.parseInt(properties.getProperty(PROPERTY_NAME_NUM_CTX)),
                    Integer.parseInt(properties.getProperty(PROPERTY_NAME_TOP_K)),
                    Integer.parseInt(properties.getProperty(PROPERTY_NAME_NUM_PREDICT)),
                    Float.parseFloat(properties.getProperty(PROPERTY_NAME_TEMPERATURE)));
        } catch (IOException e) {
            System.err.println("Error when fetching the ollama config file\nSetting default values on all configs..");
            // Default values should be returned if the ollama config file is corrupted
            methodLocalConfig = new OllamaConfig(2048, 40, 128, 0.8f);
        }
        return methodLocalConfig;
    }

    public final String getOllamaModel() {
        return this.OLLAMA_MODEL;
    }

    public final int getNUM_CTX() {
        return this.NUM_CTX;
    }

    public final int getTOP_K() {
        return this.TOP_K;
    }

    public final int getNUM_PREDICT() {
        return NUM_PREDICT;
    }

    public final float getTEMPERATURE() {
        return TEMPERATURE;
    }
}
