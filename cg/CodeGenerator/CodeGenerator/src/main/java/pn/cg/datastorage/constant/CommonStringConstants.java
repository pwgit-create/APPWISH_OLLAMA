package pn.cg.datastorage.constant;

/**
 * Holder of common message Strings
 */
public record CommonStringConstants() {

    /**
     * String that should be returned if a String value will be used for error logic
     */
    public static final String ERROR = "ERROR";

    /**
     * Java file extensions (.java)
     */
    public static final String JAVA_FILE_EXTENSION = ".java";

    /**
     * Class file extension
     */
    public static final String CLASS_FILE_EXTENSION=".class";

    /**
     * The start delimiter String that will be requested in the query to the LLM Model
     */
    public static final String JAVA_CODE_GENERATION_START_DELMITER_STRING = "@START_HERE";

    /**
     * The end delimiter String that will be requested in the query to the LLM Model
     */
    public static final String JAVA_CODE_GENERATION_END_DELMITER_STRING = "@END_HERE";


}
