package pn.cg.datastorage;

import pn.cg.app_system.code_generation.model.CompilationJob;
import pn.cg.app_system.code_generation.model.SuperApp;

public class DataStorage {

    /**
     * Data holder for the compilation result of a Java class
     *
     * @Thread-Info This variable is shared between threads and is not cached
     */
    private volatile CompilationJob compilationJob;

    private boolean isSuperAppCreated;

    private String javaExecutionPath;

    /**
     * The class that is selected for implementation tries in a super app creation process
     */
    private volatile SuperApp currentSuperClass;

    private static DataStorage instance;

    private DataStorage() {

        this.compilationJob = getCompilationJob();
        this.isSuperAppCreated = false;
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
}


