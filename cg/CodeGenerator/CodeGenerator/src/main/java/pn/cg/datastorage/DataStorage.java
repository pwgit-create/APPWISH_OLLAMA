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
    private String SuperAppDirectoryName;

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
    private DataStorage() {

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


    public CompilationJob getCompilationJob() {
        return compilationJob;
    }

    public void setCompilationJob(CompilationJob compilationJob) {
        this.compilationJob = compilationJob;
    }

    public String getJavaExecutionPath() {
        return javaExecutionPath;
    }

    public void setJavaExecutionPath(String javaExecutionPath) {
        this.javaExecutionPath = javaExecutionPath;
    }

    public SuperApp getCurrentSuperClass() {
        return currentSuperClass;
    }

    public void setCurrentSuperClass(SuperApp currentSuperClass) {
        this.currentSuperClass = currentSuperClass;
    }

    public boolean isSuperAppCreated() {
        return isSuperAppCreated;
    }

    public void setSuperAppCreated(boolean superAppCreated) {
        isSuperAppCreated = superAppCreated;
    }

    public String getSuperAppDirectoryName() {
        return SuperAppDirectoryName;
    }

    public void setSuperAppDirectoryName(String superAppDirectoryName) {
        SuperAppDirectoryName = superAppDirectoryName;
    }

    public List<SuperApp> getListOfCurrentSuperAppClasses() {
        return listOfCurrentSuperAppClasses;
    }

    public void setListOfCurrentSuperAppClasses(List<SuperApp> listOfCurrentSuperAppClasses) {
        this.listOfCurrentSuperAppClasses = listOfCurrentSuperAppClasses;
    }

    public List<Path> getListOfPathsToTmpFiles() {
        return listOfPathsToTmpFiles;
    }

    public void setListOfPathsToTmpFiles(List<Path> listOfPathsToTmpFiles) {
        this.listOfPathsToTmpFiles = listOfPathsToTmpFiles;
    }

    public void addPathToTmpFileList(Path path){

        this.listOfPathsToTmpFiles.add(path);
    }

    public Path getPROJECT_ROOT_WORKING_DIR() {
        return PROJECT_ROOT_WORKING_DIR;
    }
}


