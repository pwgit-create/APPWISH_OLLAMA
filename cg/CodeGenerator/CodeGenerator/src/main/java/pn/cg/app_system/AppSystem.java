package pn.cg.app_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.ollama_ai_remote.OllamaRemoteSystem;
import java.util.List;

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
    private static void StartCodeGenerator(String appWish, boolean isFirstRun, boolean appWishCompileResult,boolean isCreateAppGeneration, 
                                            String javaClassName, List<String> javaFileContentInLines) {
        log.info("Started the AppSystem");

        OllamaRemoteSystem ollamaRemoteSystem = new OllamaRemoteSystem();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {

        }
        if (isFirstRun) {

            retryCounter = 1;
            appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, true);
        }

        if (appWishCompileResult) {

            log.info("App System has compiled your app successfully");
        }

        if (!appWishCompileResult) {

            //Sleep thread to give time to check if another thread has a successful compile result
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!appWishCompileResult) {


                retryCounter++;
                log.debug("In CheckCompilationRetryCounter with counter -> " + retryCounter);

                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, false);

                if(isCreateAppGeneration)
                StartCodeGenerator(appWish, false, appWishCompileResult,isCreateAppGeneration,"",null);
                else
                StartCodeGenerator(appWish,false,appWishCompileResult,isCreateAppGeneration,javaClassName,javaFileContentInLines);
            }
        }
    }

    /**
     * Create a new App with Code Generation
     */
   public static void StartCodeGenerator(String appWish){
                        StartCodeGenerator( appWish,true, false,true,"", null);
   }

    /**
     * Continue with improvments for an existing app
     */
     public static void StartCodeGenerator(String continueWithImprovmentText,String javaClassName, List<String> javaFileContentInLines){
    
                            StartCodeGenerator(continueWithImprovmentText,true, false ,false ,javaClassName,javaFileContentInLines);
   }

}







