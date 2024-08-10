package pn.cg.datastorage.constant;

public record ScriptConstants() {

    /**
     * Compile script for Linux
     */
    public static final String JAVAC_SCRIPT_NAME = "compile_class.sh";


    /**
     * Compile script for linux with an added classpath that points to the external libraries folder (in this project)
     */
    public static final String JAVAC_WITH_ADDED_CLASS_PATH_TO_EXTERNAL_LIBRARIES="compile_class_with_external_classpath.sh";

    /**
     * Folder for generated java applications
     */
    public static final String COMPILE_CLASS_STORAGE = PathConstants.RESOURCE_PATH+"java_source_code_classes_tmp";



}
