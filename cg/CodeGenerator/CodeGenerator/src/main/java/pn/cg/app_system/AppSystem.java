package pn.cg.app_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.open_ai_remote.OllamaRemoteSystem;

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
    public static void StartCodeGenerator(String appWish, boolean isFirstRun, boolean appWishCompileResult) {
        log.debug("AppSystem");

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

            log.debug("App System has compiled your app successfully");
        }

        if (!appWishCompileResult) {

            //Sleep thread to give time to check if another thread has a successful compile result
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
            }
            if (!appWishCompileResult) {


                retryCounter++;
                log.debug("In CheckCompilationRetryCounter with counter -> " + retryCounter);

                appWishCompileResult = ollamaRemoteSystem.CreateApp(appWish, false);

                StartCodeGenerator(appWish, false, appWishCompileResult);
            }
        }
    }

}







