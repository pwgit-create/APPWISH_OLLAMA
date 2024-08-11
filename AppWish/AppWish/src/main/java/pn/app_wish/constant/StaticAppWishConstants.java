package pn.app_wish.constant;

public record StaticAppWishConstants() {
    public static final String BASH_PATH = "/bin/bash";
    public static final String C_ARGUMENT = "-c";
    public static final String CP_ARGUMENT="-cp ";
    public static final String JAVA_TEXT = "java ";

    public static final String CP_PATH= ".:external_libs/* " ;


    public static final String MAIN_TEXT="Main";
    public static final String MAIN_DOT_JAVA="Main.java";
    public static final String NOTHING_STRING="";

    public final static String CONTINUE_ON_APPLICATION_FILTER_ON_JAVA_EXTENSION = "*.java";
    public final static String CONTINUE_ON_APPLICATION_FILTER_ON_JAVA_EXTENSION_DESCRIPTION = "A extension filter with purpose to only show .java files";
    public final static String FOLDER_NAME_OF_GENERATED_JAVA_APPLICATIONS = "java_source_code_classes_tmp";
}
