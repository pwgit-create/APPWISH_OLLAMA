package pn.cg.app_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.datastorage.DataStorage;
import pn.cg.ollama_ai_remote.OllamaRemoteSystem;
import pn.cg.util.CodeGeneratorUtil;

import java.util.List;
import java.util.NoSuchElementException;

import static pn.cg.util.CodeGeneratorUtil.isThisACreateNewAppRequest;

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
     * @param superAppWish The App Wish from the user
     * @param isFirstRun Flag that shows if this is the first request attempt to ollama
     * @param appWishCompileResult The method will call itself recursively unless this is true
     * @param classList The data holder for the current super app generation implementation status
     * @param superAppCreationComplete A flag that shows if the entire super app creation is complete or not
     */
    public static void StartSuperAppGeneration(String superAppWish, boolean isFirstRun, boolean appWishCompileResult, List<SuperApp> classList,
                                               boolean superAppCreationComplete){
        OllamaRemoteSystem ollamaRemoteSystem = new OllamaRemoteSystem();

        // Get Class names for the super app generation
        if (isFirstRun) {
            log.info("Started the Super AppSystem (Long-time code base generation");
            retryCounter = 1;
            classList = ollamaRemoteSystem.GetClassListForSuperAppCreation(superAppWish);
            superAppCreationComplete = false;
            isFirstRun = false;

            // Generate the first class in the super app class list
            SuperApp selectedClassToCreate =  CodeGeneratorUtil.getARandomUnimplementedClass(classList);
            DataStorage.getInstance().setCurrentSuperClass(selectedClassToCreate);

        }


        // Continue with next class (on success) if any unimplemented classes remain, else end the super app creation successfully :)
        if (appWishCompileResult && !superAppCreationComplete) {

            log.info("Class Generated\nContinue...");

            try {
                // Select the next class to create (from the SuperApp list)
                SuperApp selectedClassToCreate =  CodeGeneratorUtil.getARandomUnimplementedClass(classList);
                DataStorage.getInstance().setCurrentSuperClass(selectedClassToCreate);
                StartSuperAppGeneration(superAppWish,false,false,classList,false);
            }
            // Success on the entire super generation!
            catch (NoSuchElementException noSuchElementException){

                log.info("No more classes to implement in super app creation");

                // Validate
                if(CodeGeneratorUtil.areAllSuperClassesImplemented(classList))
                    superAppCreationComplete = true;
            }

        }

        // Retry compilation for a selected class
        if (!appWishCompileResult && !superAppCreationComplete) {

            retryCounter++;

            // Make a call to

                appWishCompileResult = ollamaRemoteSystem.CreateSuperApp(DataStorage.getInstance().getCurrentSuperClass(), false);
                StartSuperAppGeneration(superAppWish,isFirstRun,appWishCompileResult,classList,false);
            }

    }

}







