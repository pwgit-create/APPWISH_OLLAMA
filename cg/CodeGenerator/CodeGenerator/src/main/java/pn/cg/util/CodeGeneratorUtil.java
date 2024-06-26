package pn.cg.util;

import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.datastorage.DataStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pn.cg.datastorage.constant.ScriptConstants.COMPILE_CLASS_STORAGE;

public record CodeGeneratorUtil() {

    /**
     * Boolean method that checks if it's a new request or a continues app generation
     * Return false means that the request is sent to query the LLM Model further about an existing app
     */
    public static boolean isThisACreateNewAppRequest(String pathToJavaFileToModify, List<String> contentOfExistingJavaFile) {

        return (contentOfExistingJavaFile == null || contentOfExistingJavaFile.size() <= 1 || pathToJavaFileToModify == null);
    }

    /**
     * Converts a list of class names into unimplemented classes in the current super app generation
     *
     * @param classNames A list of strings that contains class names needed for the current super app generation
     * @return List<SuperApp>
     */
    public static List<SuperApp> convertListOfStringClassNamesIntoAnListOfUnimplementedSuperAppClasses(List<String> classNames) {

        List<SuperApp> superAppList = new LinkedList<>();
        classNames.forEach(className -> superAppList.add(new SuperApp(className, false)));

        return superAppList;
    }

    /**
     * Check the implementation status of the entire super app creation
     *
     * @param classList List of classes for the super app creation and their implementation status
     * @return true if all classes are implemented , else false
     */
    public static boolean areAllSuperClassesImplemented(List<SuperApp> classList) {
        return classList.stream().filter(superAppClass -> !superAppClass.isImplemented()).toList().isEmpty();
    }

    /**
     * Get a random unimplemented class from the input list
     * @return A random SuperApp from the input list with a status of unimplemented (if any)
     */
    public static SuperApp getARandomUnimplementedClass() throws NoSuchElementException {

        return DataStorage.getInstance().getListOfCurrentSuperAppClasses().stream().filter(e -> !e.isImplemented()).findAny().orElseThrow();
    }

    /**
     * Read the files in the COMPILE_CLASS_STORAGE folder and filter the current super apps directories and increment the number by one
     * @return String
     */
    public static String getIncrementedSuperAppDirectoryName() {
        final String SUPER_APP_FOLDER_NAME = "APP_";

        // The number that will be appended to the APP_ directory
        int appNumber = 1;

        //Placeholder list that will be filled with the appending number of existing APP_ directories (if any)
        List<Integer> listOfNumbersThatAreAppendedToExistingSuperAppsDirectories = new LinkedList<>();

        try {
            List<Path> superAppFolders = Files.list(new File(COMPILE_CLASS_STORAGE)
                            .toPath())
                    .filter(file -> file.getFileName()
                            .toString()
                            .startsWith(SUPER_APP_FOLDER_NAME))
                    .toList();

            if (superAppFolders.isEmpty()) {
                return SUPER_APP_FOLDER_NAME + appNumber;
            } else {

                superAppFolders.forEach(p -> listOfNumbersThatAreAppendedToExistingSuperAppsDirectories
                        .add(Integer.parseInt(p.getFileName().toString().split(SUPER_APP_FOLDER_NAME)[1])));

                // Take the highest and increment it by one
                int maxNumberOfExistingSuperAppDirectories = listOfNumbersThatAreAppendedToExistingSuperAppsDirectories
                        .stream()
                        .mapToInt(Integer::intValue)
                        .max()
                        .getAsInt();

                return SUPER_APP_FOLDER_NAME + (maxNumberOfExistingSuperAppDirectories + 1);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract methods from java source code text
     * @param javaSourceCode The input for the java source code
     * @return List<String>
     */
    public static List<String> ExtractMethodsFromJavaSourceCodeString(String javaSourceCode) {

        List<String> methods = new LinkedList<>();

        final String MAIN_METHOD="public static void main(String args[])";
        final String MAIN_METHOD_2= "public static void main(String...args)";

        // Check if the java source code contains a main method
        if(javaSourceCode.contains(MAIN_METHOD))
            methods.add(MAIN_METHOD);

        if(javaSourceCode.contains(MAIN_METHOD_2))
            methods.add(MAIN_METHOD_2);

        // Remove all occurrences of new instances from the local method java source code
        // so the regex pattern won´t catch them as methods
        String methodLocalParsedJavaSourceCode = javaSourceCode.replaceAll("new.*(?:\r\n)?","");

        Pattern inputParamPattern = Pattern.compile("\\b[a-zA-Z_$][a-zA-Z0-9_$]*\\s*\\([^)]*input.*?\\)");
        Matcher inputParamMatcher = inputParamPattern.matcher(methodLocalParsedJavaSourceCode);


        while (inputParamMatcher.find()) {
            // Method with input parameter
            if(!methods.contains(inputParamMatcher.group())) methods.add(inputParamMatcher.group());
        }


        Pattern noInputParamPattern = Pattern.compile("\\b[a-zA-Z_$][a-zA-Z0-9_$]*\\s*\\(\\s*\\)");
        Matcher noInputParamMatcher = noInputParamPattern.matcher(methodLocalParsedJavaSourceCode);

        while (noInputParamMatcher.find()) {
            // Method without input parameter
            if(!methods.contains(noInputParamMatcher.group())) methods.add(noInputParamMatcher.group());

        }
        return methods;
    }

    /**
     * Set the method list for an implemented SuperApp class in the shared singleton list
     * @param implementedClassName The name of the implemented class
     * @param javaSourceCode The source code for the implemented class
     */
    public static void SetMethodListForImplementedClass(String implementedClassName,String javaSourceCode){

        //tmp SOUT 1
        System.out.println("Implemented class name -> "+implementedClassName+"\n");


        DataStorage.getInstance()
                .getListOfCurrentSuperAppClasses()
                .stream()
                .filter(s -> s.getClassName()
                        .equals(implementedClassName))
                .findFirst()
                .get()
                .setMethods(CodeGeneratorUtil.ExtractMethodsFromJavaSourceCodeString(javaSourceCode));
    }
}