package pn.cg.datastorage;

import pn.cg.app_system.code_generation.model.CompilationJob;

public class DataStorage {

    /**
     * Data holder for the compilation result of a Java class
     * @Thread-Info This variable is shared between threads and is not cached
     */
    private volatile CompilationJob compilationJob;

    private String javaExecutionPath;

    private static DataStorage instance;

    private DataStorage() {

        this.compilationJob = getCompilationJob();
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
}


