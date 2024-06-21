package pn.cg.util;

import pn.cg.app_system.code_generation.model.SuperApp;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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
     *
     * @param classList List of classes for the super app creation and their implementation status
     * @return A random SuperApp from the input list with a status of unimplemented (if any)
     */
    public static SuperApp getARandomUnimplementedClass(List<SuperApp> classList) throws NoSuchElementException {

        return classList.stream().filter(e -> !e.isImplemented()).findAny().orElseThrow();
    }
}