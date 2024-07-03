package pn.app_wish.model;
import pn.app_wish.constant.StaticAppWishConstants;

public record AppCmd(String javaExecutablePath) {

    public final String[] GetCMDForRunningApp_Application() {

        return new String[]{StaticAppWishConstants.BASH_PATH,StaticAppWishConstants.C_ARGUMENT, StaticAppWishConstants.JAVA_TEXT + javaExecutablePath};
    }
}
