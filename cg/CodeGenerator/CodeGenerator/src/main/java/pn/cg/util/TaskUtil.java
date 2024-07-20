package pn.cg.util;

import pn.cg.datastorage.constant.ScriptConstants;

import java.io.File;
import java.nio.file.Path;

public record TaskUtil() {

    /**
     * Adds the class name to the path of the Java source code folder
     * @param className Name of the class
     * @return String
     */
    public static String addFilePathToClassName(String className){

        return ScriptConstants.COMPILE_CLASS_STORAGE+ File.separator+className;
    }

    /**
     * Adds the class name to the path of super app directory
     * @param className Name of the class
     * @param superAppDirectory Name of the directory for the super app
     * @return String
     */
    public static String addFilePathOfSuperAppToClassName(String className, String superAppDirectory){

        return ScriptConstants.COMPILE_CLASS_STORAGE+File.separator+superAppDirectory+File.separator +className;
    }

    /**
     *
     * @param className Name of the class
     * @param continueAppDirectory Name of the directory for the continue an application app
     * @return String
     */
    public static String AddFilePathToContinueOnApplication(String className, String continueAppDirectory){
        return ScriptConstants.COMPILE_CLASS_STORAGE+File.separator+continueAppDirectory+File.separator+className;
    }

}
