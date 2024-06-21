package pn.cg.datastorage.constant;

public record ScriptConstants() {

    /**
     * Compile script for Linux
     */
    public static final String JAVAC_SCRIPT_NAME = "compile_class.sh";


    /**
     * Folder for generated java applications
     */
    public static final String COMPILE_CLASS_STORAGE = PathConstants.RESOURCE_PATH+"java_source_code_classes_tmp";



}
