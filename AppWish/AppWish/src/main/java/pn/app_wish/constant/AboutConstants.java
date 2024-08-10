package pn.app_wish.constant;


public record AboutConstants() {

    private static final String APP_WISH_VERSION = "Appwish Enterprise 2.1";
    private static final String ABOUT_APP_WISH = "AppWish (2) is a open source project that creates Java applications from text input with the help of AI models";
    private static final String DEVELOPED_BY = "Pwgit-Create / Peter Westin";
    private static final String CONTACT = "\nEmail: snow_900@outlook.com";
    private static final String LINK_TO_DISCUSSION = "https://github.com/pwgit-create/APPWISH_OLLAMA/discussions";
    private static final String LINK_TO_ISSUE_PORTAL="https://github.com/pwgit-create/APPWISH_OLLAMA/issues";
    private static final String IF_YOU_LIKE_THE_PROJECT = "\n\nIf you like AppWish and wants to show your support, please let me know by leaving a post";
    private static final String THANKS_FOR_USING_APP_WISH_ENTERPRISE = "\n\nThanks for taking the time to read the about section, you rock! :)";


    /**
     * Builds an About String that can be used in the About section
     * @return String
     */
    public static String BuildAboutString() {

        return "Version: " +
                APP_WISH_VERSION +
                "\n\nWhat is AppWish used for? " +
                ABOUT_APP_WISH +
                "\n\nDeveloper: " +
                DEVELOPED_BY +
                "\n\nQuestions or in need of support? " +
                CONTACT +
                "\nIssue Portal: " +
                LINK_TO_ISSUE_PORTAL +
                "\n\nJoin the discussion and see updates: " +
                LINK_TO_DISCUSSION +
                IF_YOU_LIKE_THE_PROJECT +
                THANKS_FOR_USING_APP_WISH_ENTERPRISE;

    }

    /*
    Get the header text for the about section
     */
    public static String GetHeaderText(){

        return "AppWish Enterprise";
    }

    /*
    Get the header text for the about section
     */
    public static String GetTitleTest(){

        return "About AppWish";
    }

}
