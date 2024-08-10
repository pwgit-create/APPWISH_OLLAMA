package pn.cg.datastorage;

import pn.cg.app_system.code_generation.model.CompilationJob;
import pn.cg.app_system.code_generation.model.SuperApp;

import java.nio.file.Path;
import java.util.List;

public class DataStorage {

    /**
     * Data holder for the compilation result of a Java class
     *
     * @Thread-Info This variable is shared between threads and is not cached
     */
    private volatile CompilationJob compilationJob;

    /**
     * Flag that tells if the entire super app creation is complete or not
     */
    private volatile boolean isSuperAppCreated;

    /**
     * List of super classes that are part of the super app creation
     */
    private List<SuperApp> listOfCurrentSuperAppClasses;

    /**
     * The execution path for regular apps creation apps
     */
    private String javaExecutionPath;


    /**
     * Data holder for the current Super App Directory name
     */
    private String superAppDirectoryName;

    /**
     * Data holder for the current Continue an App Directory
     */
    private String continueAnAppDirectoryName;
    /**
     * The class that is selected for implementation tries in a super app creation process
     */
    private volatile SuperApp currentSuperClass;

    /**
     * This is the list of tmp files that is scheduled for deletion when the entire super app generation is complete
     */
    private List<Path> listOfPathsToTmpFiles;

    private static DataStorage instance;

    /**
     * The path for the project root directory
     */
    private final Path PROJECT_ROOT_WORKING_DIR;

    private CodeGeneratorConfig codeGeneratorConfig;

    private  DataStorage() {

        this.compilationJob = getCompilationJob();
        this.isSuperAppCreated = false;
        this.PROJECT_ROOT_WORKING_DIR = Path.of(System.getProperty("user.dir"));
    }


     synchronized public static DataStorage getInstance() {
        if (instance == null) {

            instance = new DataStorage();
        }
        return instance;
    }


    public final CompilationJob getCompilationJob() {
        return compilationJob;
    }

    public final void setCompilationJob(CompilationJob compilationJob) {
        this.compilationJob = compilationJob;
    }

    public final String getJavaExecutionPath() {
        return javaExecutionPath;
    }

    public final void setJavaExecutionPath(String javaExecutionPath) {
        this.javaExecutionPath = javaExecutionPath;
    }

    public final SuperApp getCurrentSuperClass() {
        return currentSuperClass;
    }

    public final void setCurrentSuperClass(SuperApp currentSuperClass) {
        this.currentSuperClass = currentSuperClass;
    }

    public final boolean isSuperAppCreated() {
        return isSuperAppCreated;
    }

    public final void setSuperAppCreated(boolean superAppCreated) {
        isSuperAppCreated = superAppCreated;
    }

    public final String getSuperAppDirectoryName() {
        return superAppDirectoryName;
    }

    public final void setSuperAppDirectoryName(String superAppDirectoryName) {
        this.superAppDirectoryName = superAppDirectoryName;
    }

    public final List<SuperApp> getListOfCurrentSuperAppClasses() {
        return listOfCurrentSuperAppClasses;
    }

    public final void setListOfCurrentSuperAppClasses(List<SuperApp> listOfCurrentSuperAppClasses) {
        this.listOfCurrentSuperAppClasses = listOfCurrentSuperAppClasses;
    }

    public final List<Path> getListOfPathsToTmpFiles() {
        return listOfPathsToTmpFiles;
    }

    public final void setListOfPathsToTmpFiles(List<Path> listOfPathsToTmpFiles) {
        this.listOfPathsToTmpFiles = listOfPathsToTmpFiles;
    }

    public final void addPathToTmpFileList(Path path){

        this.listOfPathsToTmpFiles.add(path);
    }

    public final Path getPROJECT_ROOT_WORKING_DIR() {
        return PROJECT_ROOT_WORKING_DIR;
    }

    public final String getContinueAnAppDirectoryName() {
        return continueAnAppDirectoryName;
    }

    public final void setContinueAnAppDirectoryName(String continueAnAppDirectoryName) {
        this.continueAnAppDirectoryName = continueAnAppDirectoryName;
    }

    public void setCodeGeneratorConfig(CodeGeneratorConfig codeGeneratorConfig) {
        this.codeGeneratorConfig = codeGeneratorConfig;
    }

    public CodeGeneratorConfig getCodeGeneratorConfig() {
        return codeGeneratorConfig;
    }
}


