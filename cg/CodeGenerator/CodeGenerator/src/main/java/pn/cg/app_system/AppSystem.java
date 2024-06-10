package pn.cg.app_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.ollama_ai_remote.OllamaRemoteSystem;
import java.util.List;

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
    private static void StartCodeGenerator(String appWish, boolean isFirstRun, boolean appWishCompileResult,boolean isCreateAppGeneration, 
                                            String pathOfJavaFileIfModifyRequest, List<String> javaFileContentInLines) {
        log.info("Started the AppSystem");

        OllamaRemoteSystem ollamaRemoteSystem = new OllamaRemoteSystem();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {

        }
        if (isFirstRun) {

            retryCounter = 1;
            if(isCreateAppGeneration){
            appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, true,"",null);
            }
            else{
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish,true,pathOfJavaFileIfModifyRequest,javaFileContentInLines);
            }
        }

        if (appWishCompileResult) {

            log.info("App System has compiled your app successfully");
        }

        if (!appWishCompileResult) {

            retryCounter++;
            log.debug("In CheckCompilationRetryCounter with counter -> {}", retryCounter);

            if(isCreateAppGeneration)
            appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, false,"",null);
            else
                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish,false,pathOfJavaFileIfModifyRequest,javaFileContentInLines);

            if(isCreateAppGeneration){
            StartCodeGenerator(appWish, false, appWishCompileResult,isCreateAppGeneration,"",null);}
            else{
            StartCodeGenerator(appWish,false,appWishCompileResult,isCreateAppGeneration,pathOfJavaFileIfModifyRequest,javaFileContentInLines);}
        }
    }

    /**
     * Create a new App with Code Generation
     */
   public static void StartCodeGenerator(String appWish){
                        StartCodeGenerator( appWish,true, false,true,"", null);
   }

    /**
     * Continue with improvements for an existing app
     */
     public static void StartCodeGenerator(String continueWithImprovementText,String javaClassName, List<String> javaFileContentInLines){
    
                            StartCodeGenerator(continueWithImprovementText,true, false ,false ,javaClassName,javaFileContentInLines);
   }

}







