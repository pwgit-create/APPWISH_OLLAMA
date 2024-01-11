package pn.cg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.datastorage.constant.CommonStringConstants;

import java.util.Arrays;

public class StringUtil {

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);


    /**
     * Extracts the public class name from a text with java classes
     *
     * @param unparsedJavaClassText
     * @return String
     */
    public static String extractClassNameFromTextWithJavaClasses(String unparsedJavaClassText) {



        String[] lines = unparsedJavaClassText.split("\n");

        System.out.println(lines.length);

        String className = "";

        for (String line : lines) {


            if (line.contains("public class")) {

                className = line.replace("public class", "").replace("{","").trim();

               className = className.split("\\s")[0];
                break;
            }
        }


        if (className.isBlank()) {
           // log.error(ERROR_PREFIX_LOG_ERROR + unparsedJavaClassText);
            return CommonStringConstants.ERROR;
        } else
            //log.debug("Class name -> "+className);
            return className;

    }

    /**
     * Removes the first line of a String
     * @param inputrt
     * @return
     */
    public static String removeFirstLine(String input){
        
        String returnValue = input;
        try{ String tmpValue; 
            tmpValue = input.split("\n",2)[1];
             if(!tmpValue.startsWith("import"));
              returnValue = tmpValue;
        }
        catch (Exception e){
            log.info("Could not remove first line");
        }
        return returnValue;
    }



    /**
     * Gets the first line of a string
     * @param input
     * @return
     */
    public static String getFirstLine(String input){

        return input.split("\n",1)[0];
    }

    /**
     * Get the first lines of a String (up to 5)
     * @param input
     * @return
     */
    public static String getFirstFiveLines(String input){


     String[] fiveFirstLines =  input.split("\n",5);
     StringBuilder builder = new StringBuilder();

     Arrays.stream(fiveFirstLines).forEach(builder::append);

     return builder.toString();


    }

    /**
     * Removes chars that might cause ollama´s api to send 400
     * @param input
     * @return
     */
    public static String removeBadRequestChars(String input){


        return input.replace("^","").replace(":","");
    }

   
     public static String IncludeEveryThingAfterStartChar(String input){
        String returnValue = input;

        try{ returnValue = input.split("```java")[1];
            returnValue = input.split("```")[1];
                }
        catch (Exception e){
            log.info("Could not include everything afterStartChar");
        }
        return returnValue;

    }


    public static String RemoveEveryThingAfterEndChar(String input){
        String returnValue = input;
        try{ returnValue = input.split("```")[0];}
        catch (Exception e){
            log.info("Could not RemoveEveryThingAfterEndChar");
        }
        return returnValue;
    }




}