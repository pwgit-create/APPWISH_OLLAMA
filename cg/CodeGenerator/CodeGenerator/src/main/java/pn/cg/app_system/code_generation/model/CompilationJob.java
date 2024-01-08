package pn.cg.app_system.code_generation.model;

public class CompilationJob {


    private  String className;

    private int compilationTriesCounter;
    private Boolean result;

    private volatile String errorMessage;


    public CompilationJob(String className) {

        this.className = className;

        this.compilationTriesCounter = 0;
    }


    public int getCompilationTriesCounter() {
        return compilationTriesCounter;
    }

    public void setCompilationTriesCounter(int compilationTriesCounter) {
        this.compilationTriesCounter = compilationTriesCounter;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public synchronized void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean isResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     * Returns the error message if the compilation failed
     * Returns a default message if the compilation succeeded
     *
     * @return String
     */
    public String getErrorMessage() {

        if (errorMessage != null)
         return errorMessage;
        else return null;
    }
}
