package pn.cg.app_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.datastorage.DataStorage;
import pn.cg.ollama_ai_remote.OllamaRemoteSystem;
import pn.cg.util.CodeGeneratorUtil;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static pn.cg.datastorage.constant.ScriptConstants.COMPILE_CLASS_STORAGE;

public class AppSystem {

    private static final Logger log = LoggerFactory.getLogger(AppSystem.class);

    private static int retryCounter = 1;


    /**
     * Makes a request to ollama and retries with a recursive strategy upon failure
     *
     * @param appWish              The App Wish from the user
     * @param isFirstRun           Flag that shows if this is the first request attempt to ollama
     * @param appWishCompileResult The method will call itself recursively unless this is true
     */
    private static void StartCodeGenerator(String appWish, boolean isFirstRun, boolean appWishCompileResult, boolean isCreateAppGeneration,
                                           String pathOfJavaFileIfModifyRequest, List<String> javaFileContentInLines) {


        OllamaRemoteSystem ollamaRemoteSystem = new OllamaRemoteSystem();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }


        if (isFirstRun) {
            log.info("Started the AppSystem");
            retryCounter = 1;
            if (isCreateAppGeneration) {
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, true, "", null);
            } else {
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, true, pathOfJavaFileIfModifyRequest, javaFileContentInLines);
            }
        }

        if (appWishCompileResult) {

            log.info("App System has compiled your app successfully");
        }

        if (!appWishCompileResult) {

            retryCounter++;
            log.debug("In CheckCompilationRetryCounter with counter -> {}", retryCounter);

            if (isCreateAppGeneration)
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, false, "", null);
            else
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, false, pathOfJavaFileIfModifyRequest, javaFileContentInLines);

            if (isCreateAppGeneration) {
                StartCodeGenerator(appWish, false, appWishCompileResult, isCreateAppGeneration, "", null);
            } else {
                StartCodeGenerator(appWish, false, appWishCompileResult, isCreateAppGeneration, pathOfJavaFileIfModifyRequest, javaFileContentInLines);
            }
        }
    }

    /**
     * Create a new App with Code Generation
     */
    public static void StartCodeGenerator(String appWish) {
        StartCodeGenerator(appWish, true, false, true, "", null);
    }

    /**
     * Continue with improvements for an existing app
     */
    public static void StartCodeGenerator(String continueWithImprovementText, String javaClassName, List<String> javaFileContentInLines) {

        StartCodeGenerator(continueWithImprovementText, true, false, false, javaClassName, javaFileContentInLines);
    }

    /**
     * Starts an app generation with the strategy to ask for each class that is needed and then compile each class in a new request until it compiles
     *
     * @param superAppWish             The App Wish from the user
     * @param isFirstRun               Flag that shows if this is the first request attempt to ollama
     * @param appWishCompileResult     A flag that states if the current compilation is successfull or not
     * @param classList                The data holder for the current super app generation implementation status
     * @param superAppCreationComplete A flag that shows if the entire super app creation is complete or not
     */
    public static void StartSuperAppGeneration(String superAppWish, boolean isFirstRun, boolean appWishCompileResult, List<SuperApp> classList,
                                               boolean superAppCreationComplete, OllamaRemoteSystem ollamaRemoteSystem) {

        // Get Class names for the super app generation
        if (isFirstRun) {
            log.info("Started the Super AppSystem (Long-time code base generation)");
            DataStorage.getInstance().setListOfPathsToTmpFiles(new LinkedList<Path>());
            ollamaRemoteSystem = new OllamaRemoteSystem();

            try {
                // List Super app directories (if any) and increment the number by one for next super app
                DataStorage.getInstance().setSuperAppDirectoryName(CodeGeneratorUtil.getIncrementedSuperAppDirectoryName());
                Files.createDirectory(new File(COMPILE_CLASS_STORAGE + File.separator + DataStorage.getInstance().getSuperAppDirectoryName()).toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            retryCounter = 1;
            classList = ollamaRemoteSystem.GetClassListForSuperAppCreation(superAppWish);
            superAppCreationComplete = false;

            // Set pointer class in singleton into one of the classes in the class list
            SuperApp selectedClassToCreate = CodeGeneratorUtil.getARandomUnimplementedClass();
            DataStorage.getInstance().setCurrentSuperClass(selectedClassToCreate);

            // Generate the first class in the super app class list
            appWishCompileResult = ollamaRemoteSystem.CreateSuperApp(selectedClassToCreate, true);

            if (appWishCompileResult) {
              SuperApp currentWorkingClass =DataStorage.getInstance().getCurrentSuperClass();
                DataStorage.getInstance().getListOfCurrentSuperAppClasses()
                        .stream()
                        .filter(e -> e.getClassName()
                                .equalsIgnoreCase(currentWorkingClass.getClassName()))
                        .findFirst()
                        .get()
                        .setImplemented(true);
            }
            StartSuperAppGeneration(superAppWish, false, appWishCompileResult, classList, false, ollamaRemoteSystem);
        }


        // Continue with next class (on success) if any unimplemented classes remain, else end the super app creation successfully :)
        if (appWishCompileResult && !isFirstRun && !superAppCreationComplete) {

            log.info("Class Generated\nContinue...");

            try {
                // Select the next class to create (from the super app list) or complete the super app creation if no unimplemented elements exist
                SuperApp selectedClassToCreate = CodeGeneratorUtil.getARandomUnimplementedClass();
                DataStorage.getInstance().setCurrentSuperClass(selectedClassToCreate);

                // Generate the next class in the list of the super app
                appWishCompileResult = ollamaRemoteSystem.CreateSuperApp(DataStorage.getInstance().getCurrentSuperClass(), false);

                if (appWishCompileResult) {
                    // Set the successfully compiled class to status implemented
                    SuperApp currentWorkingClass =DataStorage.getInstance().getCurrentSuperClass();

                    DataStorage.getInstance().getListOfCurrentSuperAppClasses()
                            .stream()
                            .filter(e -> e.getClassName()
                                    .equalsIgnoreCase(currentWorkingClass.getClassName()))
                            .findFirst()
                            .get()
                            .setImplemented(true);

                }
                StartSuperAppGeneration(superAppWish, false, appWishCompileResult, classList, DataStorage.getInstance().isSuperAppCreated(), ollamaRemoteSystem);
            }


            // Success on the entire super generation!
            catch (NoSuchElementException noSuchElementException) {

                log.info("No more classes to implement in super app creation");
                superAppCreationComplete = true;
                DataStorage.getInstance().setSuperAppCreated(superAppCreationComplete);

                // Write documentation file to the directory of the super app
                CodeGeneratorUtil.WriteDocFileToSuperApp(DataStorage.getInstance().getListOfCurrentSuperAppClasses(),
                        new File(CodeGeneratorUtil.GetDocFilePathForSuperApp()));

                // Delete tmp files that where added to the class path when compiling
                List<Path> pathOfTmpFilesInSuperAppCreation = DataStorage.getInstance().getListOfPathsToTmpFiles();

                pathOfTmpFilesInSuperAppCreation.forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

        }

        // Retry compilation for a selected class
        if (!appWishCompileResult && !DataStorage.getInstance().isSuperAppCreated()) {

            log.info("Class did not compile, try again....");

            appWishCompileResult = ollamaRemoteSystem.CreateSuperApp(DataStorage.getInstance().getCurrentSuperClass(), false);

            if (appWishCompileResult) {
                SuperApp currentWorkingClass =DataStorage.getInstance().getCurrentSuperClass();

                DataStorage.getInstance().getListOfCurrentSuperAppClasses()
                        .stream()
                        .filter(e -> e.getClassName()
                                .equalsIgnoreCase(currentWorkingClass.getClassName()))
                        .findFirst()
                        .get()
                        .setImplemented(true);

                try{
                SuperApp selectedClassToCreate = CodeGeneratorUtil.getARandomUnimplementedClass();
                DataStorage.getInstance().setCurrentSuperClass(selectedClassToCreate);}

                // Success on the entire super generation!
                catch (NoSuchElementException e){

                    log.info("No more classes to implement in super app creation");
                    superAppCreationComplete = true;
                    DataStorage.getInstance().setSuperAppCreated(superAppCreationComplete);

                    // Write documentation file to the directory of the super app
                    CodeGeneratorUtil.WriteDocFileToSuperApp(DataStorage.getInstance().getListOfCurrentSuperAppClasses(),
                            new File(CodeGeneratorUtil.GetDocFilePathForSuperApp()));

                    // Delete tmp files that where added to the class path when compiling
                    List<Path> pathOfTmpFilesInSuperAppCreation = DataStorage.getInstance().getListOfPathsToTmpFiles();
                    pathOfTmpFilesInSuperAppCreation.forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            }
            if(!DataStorage.getInstance().isSuperAppCreated()){
            StartSuperAppGeneration(superAppWish, false, appWishCompileResult, classList, DataStorage.getInstance().isSuperAppCreated(), ollamaRemoteSystem);}
        }
    }

}







