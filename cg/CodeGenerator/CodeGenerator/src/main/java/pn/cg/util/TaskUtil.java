package pn.cg.util;

import pn.cg.datastorage.constant.ScriptConstants;

import java.io.File;

public class TaskUtil {

    /**
     * Adds the file path to the Java source code folder
     * @param className Name of the class
     * @return String
     */
    public static String addFilePathToClassName(String className){

        return ScriptConstants.COMPILE_CLASS_STORAGE+ File.separator+className;
    }
}
