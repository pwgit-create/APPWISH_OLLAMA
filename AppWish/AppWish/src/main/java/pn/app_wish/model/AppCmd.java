package pn.app_wish.model;

import pn.app_wish.constant.StaticAppWishConstants;
import pn.cg.datastorage.DataStorage;


public record AppCmd(String javaExecutablePath) {

    public final String[] GetCMDForRunningApp_Application() {

        boolean isExternalLibraries = DataStorage.getInstance().getCodeGeneratorConfig().isUSE_EXTERNAL_LIBRARIES();
        if (isExternalLibraries) {
            return new String[]{StaticAppWishConstants.BASH_PATH, StaticAppWishConstants.C_ARGUMENT, StaticAppWishConstants.JAVA_TEXT + StaticAppWishConstants.CP_ARGUMENT + StaticAppWishConstants.CP_PATH + javaExecutablePath};

        } else {
            return new String[]{StaticAppWishConstants.BASH_PATH, StaticAppWishConstants.C_ARGUMENT, StaticAppWishConstants.JAVA_TEXT + javaExecutablePath};

        }
    }
}
