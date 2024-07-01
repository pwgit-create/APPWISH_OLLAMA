package pn.app_wish.model;

public record CodeBaseCmd(String classPath) {

    public final String[] GetCMDForRunningCodeBaseApplication() {
        final String JAVA_PATH = "/usr/bin/java";
        final String C_ARGUMENT_SUPER_APP="-cp";
        final String CLASS_NAME="Main";

        return new String[]{JAVA_PATH,C_ARGUMENT_SUPER_APP,classPath,CLASS_NAME};
    }
}
