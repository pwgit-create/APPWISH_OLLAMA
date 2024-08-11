package pn.cg.app_system.code_generation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.ThreadPoolMaster;
import pn.cg.datastorage.constant.CommonStringConstants;
import pn.cg.datastorage.constant.ScriptConstants;
import pn.cg.task.CompileClassTask;
import pn.cg.util.TaskUtil;


import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

public class ClassCompiler {

    private static final Logger log = LoggerFactory.getLogger(ClassCompiler.class);

    public ClassCompiler() {
    }

    /**
     * Tries to compile a file with java source code into byte code
     *
     * @param className   Class name of the class that we will try to compile
     * @param superAppDir A String path that will be provided if it is a super app generation and left as null if not
     */
    private final void compile(String className, String superAppDir) {


        boolean isSuperAppGeneration;

        isSuperAppGeneration = superAppDir != null;

        ExecutorService executor = ThreadPoolMaster.getInstance().getExecutor();

        if (isSuperAppGeneration)
            log.info("Compiler path -> {}", TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName()));
        else
            log.info("Compiler path -> {}", TaskUtil.addFilePathToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION));

        String regularCompileScript = ScriptConstants.JAVAC_SCRIPT_NAME;
        String compileScriptWithAddedClassPath = ScriptConstants.JAVAC_WITH_ADDED_CLASS_PATH_TO_EXTERNAL_LIBRARIES;

        final boolean useExternalLibraries = DataStorage.getInstance().getCodeGeneratorConfig().isUSE_EXTERNAL_LIBRARIES();

        if (isSuperAppGeneration) {

            if (useExternalLibraries) {
                executor.execute(new CompileClassTask(compileScriptWithAddedClassPath, new String[]{TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName())}));
            } else {
                executor.execute(new CompileClassTask(regularCompileScript, new String[]{TaskUtil.addFilePathOfSuperAppToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION, DataStorage.getInstance().getSuperAppDirectoryName())}));
            }
        } else {

            if (useExternalLibraries) {
                executor.execute(new CompileClassTask(compileScriptWithAddedClassPath, new String[]{TaskUtil.addFilePathToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION)}));

            } else {
                executor.execute(new CompileClassTask(regularCompileScript, new String[]{TaskUtil.addFilePathToClassName(className + CommonStringConstants.JAVA_FILE_EXTENSION)}));
            }
        }
    }

    /**
     * Tries to compile a file with java source code into byte code
     *
     * @param className Class name of the class that we will try to compile
     */
    public void compileClass(String className) {
        compile(className, null);
    }

    /**
     * @param className   Class name of the class that we will try to compile
     * @param superAppDir The path to the directory of the super app
     */
    public void compileSuperClass(String className, String superAppDir) {
        compile(className, superAppDir);

    }


}
