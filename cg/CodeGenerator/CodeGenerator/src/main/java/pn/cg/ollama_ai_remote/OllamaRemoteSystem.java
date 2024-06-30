package pn.cg.ollama_ai_remote;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.app_system.code_generation.ClassCompiler;
import pn.cg.app_system.code_generation.model.SuperApp;
import pn.cg.app_wish.QuestionBuilder;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.constant.CommonStringConstants;
import pn.cg.datastorage.constant.PathConstants;
import pn.cg.datastorage.constant.QuestionConstants;
import pn.cg.datastorage.constant.ScriptConstants;
import pn.cg.ollama_ai_remote.request.*;
import pn.cg.util.CodeGeneratorUtil;
import pn.cg.util.FileUtil;
import pn.cg.util.StringUtil;
import pn.cg.util.TaskUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static pn.cg.datastorage.constant.CommonStringConstants.*;


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
     * @param appWish  (The app wish text input from the app-user)
     * @param firstRun (A flag that shows if the questions to OLLAMA is the first or a retry question
     * @Strategy Send only 1 question to OLLAMA and create only one .java file (if possible)
     */
    public synchronized boolean CreateApp(String appWish, boolean firstRun, String ifJavaAppShouldBeModifiedPath, List<String> contentOfJavaFileIfModifyRequest) {

        QuestionBuilder questionBuilder = new QuestionBuilder(appWish);
        String outputFromOLLMA = "";
        boolean isRetryCompilation;
        boolean tmpRetryCompilationValue;

        boolean isCreateNewApp = CodeGeneratorUtil.isThisACreateNewAppRequest(ifJavaAppShouldBeModifiedPath, contentOfJavaFileIfModifyRequest);

        if (firstRun) {
            isRetryCompilation = false;
        } else {
            tmpRetryCompilationValue = DataStorage.getInstance().getCompilationJob().isResult();
            isRetryCompilation = !tmpRetryCompilationValue;
        }
        // Fetch response from OllAMA remote api

        if (isRetryCompilation) {
            if (DataStorage.getInstance().getCompilationJob() != null && DataStorage.getInstance().getCompilationJob().getErrorMessage() != null) {
                log.error("Class did not compile\nSending new request... ");
            }
            if (DataStorage.getInstance().getCompilationJob() != null && !DataStorage.getInstance().getCompilationJob().isResult()) {
                if (isCreateNewApp)
                    outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(QuestionConstants.CLASS_DID_NOT_COMPILE_PREFIX_2 + questionBuilder.createFeatureQuestion());
                else {
                    outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(QuestionConstants.CLASS_DID_NOT_COMPILE_PREFIX_2 + appWish, ifJavaAppShouldBeModifiedPath, contentOfJavaFileIfModifyRequest);
                }
            }
        }

        if (firstRun) {
            if (isCreateNewApp)
                outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(questionBuilder.createFeatureQuestion());
            else outputFromOLLMA = requestHandler.sendQuestionToOllamaInstance(appWish, ifJavaAppShouldBeModifiedPath,
                    contentOfJavaFileIfModifyRequest);

        }

        // Extract class name
        String className = StringUtil.extractClassNameFromTextWithJavaClasses(outputFromOLLMA);
        log.debug("className -> {}", className);
        if (className.equalsIgnoreCase(ERROR)) {
            //log.debug("Empty class name");
        } else {

            String javaSourceCode = outputFromOLLMA;

            javaSourceCode = StringUtil.RemoveExtraStartDelimitersInResponse(javaSourceCode);

            javaSourceCode = StringUtil.RemoveExtraEndDelimitersInResponse(javaSourceCode);

            javaSourceCode = StringUtil.IncludeEveryThingAfterStartChar(javaSourceCode);

            javaSourceCode = StringUtil.RemoveEveryThingAfterEndChar(javaSourceCode);

            javaSourceCode = checkAndFixUnclosedBraceBuckets(javaSourceCode);
            javaSourceCode = StringUtil.RemoveCommonAdditionStringsFromAiModels(javaSourceCode);


            log.info("Java source code after modification = {}", javaSourceCode);
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

    /**
     * Get a list of classes needed for the creation of the super app and sets the output list as reference in the shared singleton
     *
     * @param superAppWish The requirements for the super app in text format
     * @return List<SuperApp>
     */
    public synchronized List<SuperApp> GetClassListForSuperAppCreation(String superAppWish) {
        List<SuperApp> superAppList = requestHandler.sendClassesNeededForSuperAppQuestionToOllamaInstance(superAppWish);
        DataStorage.getInstance().setListOfCurrentSuperAppClasses(superAppList);
        return superAppList;
    }

    public synchronized boolean CreateSuperApp(SuperApp classInSuperAppDesign, boolean isFirstRun) {

        String outputFromOLLMA = "";

        if (!isFirstRun) {

            if (DataStorage.getInstance().getCompilationJob() != null) {

                if (!DataStorage.getInstance().getCompilationJob().isResult()) {
                    log.error("Class did not compile\nSending new request... ");
                    outputFromOLLMA = requestHandler.sendSuperAppQuestionToOllamaInstance(classInSuperAppDesign);
                } else {
                    log.info("Previous class compiled successfully\nSending new request for the next class in the list... ");
                    outputFromOLLMA = requestHandler.sendSuperAppQuestionToOllamaInstance(classInSuperAppDesign);
                }
            }
        }

        if (isFirstRun) {
            log.info("First run in super app creation\nNo compile result is yet to exist!");
            outputFromOLLMA = requestHandler.sendSuperAppQuestionToOllamaInstance(classInSuperAppDesign);
        }


        // Extract class name
        String className = StringUtil.extractClassNameFromTextWithJavaClasses(outputFromOLLMA);
        log.debug("className -> {}", className);


        String javaSourceCode = outputFromOLLMA;

        javaSourceCode = StringUtil.RemoveExtraStartDelimitersInResponse(javaSourceCode);

        javaSourceCode = StringUtil.RemoveExtraEndDelimitersInResponse(javaSourceCode);

        javaSourceCode = StringUtil.IncludeEveryThingAfterStartChar(javaSourceCode);

        javaSourceCode = StringUtil.RemoveEveryThingAfterEndChar(javaSourceCode);

        javaSourceCode = checkAndFixUnclosedBraceBuckets(javaSourceCode);
        javaSourceCode = StringUtil.RemoveCommonAdditionStringsFromAiModels(javaSourceCode);


        log.info("Java source code after modification = {}", javaSourceCode);
        // Create file instance with class name and file extension
        File file = new File(TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName()));

        if(className.equals("Main")){
        // Save the path to shared storage (if user wants to execute the java app after the build
        DataStorage.getInstance().setJavaExecutionPath(file.getAbsolutePath());}

        // Write the Java code provided from OLLAMA to file
        try {
            FileUtil.writeDataToFile(file, javaSourceCode);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }

        DataStorage.getInstance().getCompilationJob().setResult(null);
        classCompiler.compileSuperClass(className, DataStorage.getInstance().getSuperAppDirectoryName());


        while (DataStorage.getInstance().getCompilationJob().isResult() == null) {
        }

        if (DataStorage.getInstance().getCompilationJob().isResult()) {

            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Set methods and constructors for the implemented class SuperApp Class representation
            try {
                CodeGeneratorUtil.SetMethodAndConstructorListForImplementedClass(className, Path.of(TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.CLASS_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName())));
            } catch (MalformedURLException | ClassNotFoundException e) {
              log.error("Could not set Methods or/ and constructors");
              classInSuperAppDesign.setConstructors(new LinkedList<String>());
              classInSuperAppDesign.setMethods(new LinkedList<String>());
            }

            // Copy class file to the script directory for the javac script, so it will be included in the class path
            try {
                Path destinationPathForTempFile= Path.of(DataStorage.getInstance().getPROJECT_ROOT_WORKING_DIR().toString()+File.separator+className+CLASS_FILE_EXTENSION);
                Files.deleteIfExists(destinationPathForTempFile);
                DataStorage.getInstance().addPathToTmpFileList(destinationPathForTempFile);
                Files.copy(Path.of(TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.CLASS_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName())),destinationPathForTempFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            log.info("Successful compilation of class, continue with next class");
        } else {
            log.info("Class did not compile , try again!");
        }

        return DataStorage.getInstance().getCompilationJob().isResult();
    }

    /**
     * Checks if the current brace buckets pairs are even and if they are not , Append brace buckets until they are
     *
     * @param input java code
     * @return String
     */
    private String checkAndFixUnclosedBraceBuckets(String input) {


        int unclosedBraceBuckets = StringUtil.GetUnbalancedBraceBracketsFromString(input);

        if (unclosedBraceBuckets > 0) {

            return StringUtil.AppendBraceBucketsAtEndofTheString(input, unclosedBraceBuckets);
        }
        return input;
    }
}



