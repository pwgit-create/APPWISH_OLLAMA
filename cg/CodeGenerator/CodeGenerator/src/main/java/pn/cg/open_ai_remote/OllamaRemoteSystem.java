package pn.cg.open_ai_remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.app_system.code_generation.ClassCompiler;
import pn.cg.app_wish.QuestionBuilder;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.constant.QuestionConstants;
import pn.cg.open_ai_remote.request.RequestHandler;
import pn.cg.open_ai_remote.request.RequestHandlerImpl;
import pn.cg.util.FileUtil;
import pn.cg.util.StringUtil;
import pn.cg.util.TaskUtil;

import java.io.File;
import java.io.IOException;

import static pn.cg.datastorage.constant.CommonStringConstants.ERROR;
import static pn.cg.datastorage.constant.CommonStringConstants.JAVA_FILE_EXTENSION;

/**
 * This class holds the logic for the requests and responses to the OLLAMA AI API
 */
public class OllamaRemoteSystem {

    private final ClassCompiler classCompiler;

    private final RequestHandler requestHandler;
    private static final Logger log = LoggerFactory.getLogger(OllamaRemoteSystem.class);

    public OllamaRemoteSystem() {

        classCompiler = new ClassCompiler();
        requestHandler = new RequestHandlerImpl();
    }

    /**
     * Create an app with default strategy
     *
     * @param appWish (The app wish text input from the app-user)
     * @param firstRun (A flag that shows if the questions to OLLAMA is the first or a retry question
     * @Strategy Send only 1 question to OLLAMA and create only one .java file (if possible)
     */
    public synchronized boolean CreateApp(String appWish, boolean firstRun) {

        QuestionBuilder questionBuilder = new QuestionBuilder(appWish);
        String outputFromOLLMA = "";
        boolean isRetryCompilation;
        boolean tmpRetryCompilationValue;

        if(firstRun){
        isRetryCompilation = false;}

        else{
            tmpRetryCompilationValue = DataStorage.getInstance().getCompilationJob().isResult();
            isRetryCompilation = !tmpRetryCompilationValue;
        }
        // Fetch response from OllAMA remote api

        if (isRetryCompilation) {
            if(DataStorage.getInstance().getCompilationJob() != null && DataStorage.getInstance().getCompilationJob().getErrorMessage() != null) {
                log.error("Class did not compile\nSending new request... ");}
                if(DataStorage.getInstance().getCompilationJob() != null && !DataStorage.getInstance().getCompilationJob().isResult()) {
                    outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(QuestionConstants.CLASS_DID_NOT_COMPILE_PREFIX_2 + questionBuilder.createFeatureQuestion());
                }
            }

          if(firstRun) {
            outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(questionBuilder.createFeatureQuestion());
        }

        // Extract class name
        String className = StringUtil.extractClassNameFromTextWithJavaClasses(outputFromOLLMA);
log.debug("className -> "+className);
        if (className.equalsIgnoreCase(ERROR)) {
            //log.debug("Empty class name");
        }

        else {
            
            String javaSourceCode = outputFromOLLMA;

            javaSourceCode = StringUtil.IncludeEveryThingAfterStartChar(javaSourceCode);
        
            javaSourceCode = StringUtil.RemoveEveryThingAfterEndChar(javaSourceCode);

            log.info("Java source code after modification = " +javaSourceCode);
            // Create file instance with class name and file extension
            File file = new File(TaskUtil.addFilePathToClassName(className + JAVA_FILE_EXTENSION));

            // Save the path to shared storage (if user wants to execute the java app after the build
            DataStorage.getInstance().setJavaExecutionPath(file.getAbsolutePath());

            // Write the Java code provided from OLLAMA to file
            try {
                FileUtil.writeDataToFile(file, javaSourceCode);
            } catch (IOException e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }

            classCompiler.compileClass(className);

                while (DataStorage.getInstance().getCompilationJob().isResult() == null) {
                }
            }
           return DataStorage.getInstance().getCompilationJob().isResult();
        }
    }



