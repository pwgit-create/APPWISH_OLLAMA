package pn.cg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.app_system.code_generation.model.enum_.OPTION_METHOD_CONSTRUCTOR;
import pn.cg.datastorage.DataStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static pn.cg.datastorage.constant.ScriptConstants.COMPILE_CLASS_STORAGE;

public record CodeGeneratorUtil() {

    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorUtil.class);

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
     * @return A random SuperApp from the input list with a status of unimplemented (if any)
     */
    public static SuperApp getARandomUnimplementedClass() throws NoSuchElementException {

        return DataStorage.getInstance().getListOfCurrentSuperAppClasses().stream().filter(e -> !e.isImplemented()).findAny().orElseThrow();
    }

    /**
     * Read the files in the COMPILE_CLASS_STORAGE folder and filter the current super apps directories and increment the number by one
     *
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
     * Set the method list for an implemented SuperApp class in the shared singleton list
     *
     * @param implementedClassName     The name of the implemented class
     * @param pathToJavaClassDirectory The path to the directory where the class file is resided
     */
    public static void SetMethodAndConstructorListForImplementedClass(String implementedClassName, Path pathToJavaClassDirectory) throws MalformedURLException, ClassNotFoundException {

        try {
            SuperApp superapp = DataStorage.getInstance()
                    .getListOfCurrentSuperAppClasses()
                    .stream()
                    .filter(s -> s.getClassName()
                            .equals(implementedClassName))
                    .findFirst()
                    .get();

            superapp.setMethods(ExtractDeclaredMethodsFromClassFile(implementedClassName, pathToJavaClassDirectory));
            superapp.setConstructors(ExtractDeclaredConstructorsFromClassFile(implementedClassName, pathToJavaClassDirectory));
        } catch (Exception e) {
            log.error("Could not set Method and/or Constructors for a compiled class...");
        }

    }

    /**
     * Checks if all classes in the super app creation are unimplemented
     *
     * @param superAppList A list that contains SuperApp instances
     * @return boolean
     */
    public static boolean AreAllClassesUnImplemented(List<SuperApp> superAppList) {

        if (superAppList != null) return superAppList.stream().anyMatch(SuperApp::isImplemented);
        return false;
    }

    /**
     * Get a formatted String that will differ depending on wherever the super app contains methods or not
     * @Format-Style A String that is formatted for a query to an AI-Model
     * @param superApp A SuperApp instance
     * @return String
     */
    public static String GetFormattedListOfMethodsString(SuperApp superApp) {

        if (superApp.getMethods().isEmpty())
            return " ,with no public methods";
        else
            return " ,with methods: " + superApp.toStringForMethods();
    }

    /**
     * Get a formatted String that will differ depending on wherever the super app contains constructors or not
     * @Format-Style A String that is formatted for a query to an AI-Model
     * @param superApp A SuperApp instance
     * @return String
     */
    public static String GetFormattedListOfConstructorString(SuperApp superApp) {

        if (superApp.getConstructors().isEmpty())
            return ", " + "and it does not have any public constructors";
        else return ", " + " and constructors: " + superApp.toStringForConstructors() + ",";
    }

    /**
     * Get a formatted String for a class name
     * @Format-Style A String that is formatted for a query to an AI-Model
     * @param superApp A SuperApp instance
     * @return String
     */
    public static String GetFormattedStringForAClassName(SuperApp superApp){
        return "\nClass name: "+superApp.getClassName();
    }

    private static List<String> ExtractDeclaredConstructorsFromClassFile(String className, Path pathToClassFileDirectory) throws MalformedURLException, ClassNotFoundException {
        return ExtractDeclared(className, pathToClassFileDirectory, OPTION_METHOD_CONSTRUCTOR.CONSTRUCTOR);
    }

    private static List<String> ExtractDeclaredMethodsFromClassFile(String className, Path pathToClassFileDirectory) throws MalformedURLException, ClassNotFoundException {

        return ExtractDeclared(className, pathToClassFileDirectory, OPTION_METHOD_CONSTRUCTOR.METHOD);
    }

    private static List<String> ExtractDeclared(String className, Path pathToClassFileDirectory, OPTION_METHOD_CONSTRUCTOR option) throws MalformedURLException, ClassNotFoundException {

        List<String> returnList = new LinkedList<>();
        ;
        String filePath = pathToClassFileDirectory.toString() + className + ".class"; // Replace with the actual path to your class

        File file = new File(filePath);
        URL url = file.getParentFile().toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});

        Class<?> clazz = loader.loadClass(className);

        if (option == OPTION_METHOD_CONSTRUCTOR.METHOD) {
            System.out.println("Public methods:");
            for (Method method : clazz.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers())) {
                    System.out.println(method.toString());
                    returnList.add(method.toString());

                }
            }

            return returnList;
        } else if (option == OPTION_METHOD_CONSTRUCTOR.CONSTRUCTOR) {
            System.out.println("\nPublic constructors:");
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (Modifier.isPublic(constructor.getModifiers())) {
                    System.out.println(constructor.toString());
                    returnList.add(constructor.toString());
                }
            }

            return returnList;
        }
        // Return empty list
        return returnList;
    }

}