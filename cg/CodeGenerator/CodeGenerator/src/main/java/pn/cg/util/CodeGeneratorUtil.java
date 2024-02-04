package pn.cg.util;

import java.util.List;

public class CodeGeneratorUtil {

    /**
     * Boolean method that checks if it's a new request or a continues app generation
     * Return false means that the request is sent to query the LLM Model further about an existing app
     */
    public static boolean isThisACreateNewAppRequest(String pathToJavaFileToModify, List<String> contentOfExistingJavaFile){

        return (contentOfExistingJavaFile == null || contentOfExistingJavaFile.size() <= 1 || pathToJavaFileToModify == null);
    }
}
