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
     * @param input
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
     * If the AI returns a line that specifices that the output is java code - Remove it
     * @param input
     * @return
     */
    public static String ifFirstlineStartsWithJavaRemoveIt(String input){

        String returnValue = input;
        try{ String tmpValue; 
            tmpValue = input.split("\n",2)[1];
             if(tmpValue.startsWith("java"));
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

        try{ 
            returnValue = input.split(CommonStringConstants.JAVA_CODE_GENERATION_START_DELMITER_STRING)[1];
            
                }
        catch (Exception e){
            log.info("Could not include everything afterStartChar");
        }

        return returnValue;

    }


    public static String RemoveEveryThingAfterEndChar(String input){
        String returnValue = input;
        try{ 
            
            returnValue = input.split(CommonStringConstants.JAVA_CODE_GENERATION_END_DELMITER_STRING)[0];
        }
         
        catch (Exception e){
            log.info("Could not RemoveEveryThingAfterEndChar");
        }
        return returnValue;
    }

    public static String RemoveExtraStartDelimitersInResponse(String input){
          
        String returnValue = input;

        try{
           returnValue = removeDelimiters(returnValue,true);
        }

        catch(Exception e){

            log.debug("RemoveExtraStartDelimitersInResponse exception");
        }

        return returnValue;

    }

    public static String RemoveExtraEndDelimitersInResponse(String input){
          
        String returnValue = input;

        try{
           returnValue = removeDelimiters(returnValue, false);
        }

        catch(Exception e){

            log.debug("RemoveExtraEndDelimitersInResponse exception");
        }

        return returnValue;

    }




    private static String removeDelimiters(String input,boolean isStartDelimiter){

    String returnValue = input;
    String delimiter ="";
    
    try{
        
        if(isStartDelimiter){
           delimiter = CommonStringConstants.JAVA_CODE_GENERATION_START_DELMITER_STRING;
           returnValue = RemoveAllExceptTheFirstOccurrenceOfaAWord(returnValue, delimiter);
        }
            else if(!isStartDelimiter){
            delimiter = CommonStringConstants.JAVA_CODE_GENERATION_END_DELMITER_STRING;
            returnValue = RemoveAllExceptTheLastOccurrenceofAWord(returnValue, delimiter);
        }
        }
    catch (Exception e) {
    log.debug("Error on delimiter String removal process");
    }
        return returnValue;
} 

public static int GetUnbalancedBraceBracketsFromString(String input){
    int counter = 0;
    for (int i=0; i <input.length(); i++) {
        if (input.toCharArray()[i] == '{') counter++;
        else if (input.toCharArray()[i] == '}') counter--;
    
        if (counter < 0) break;
    }
    
   return counter;
    
}

/**
 * Appends brace buckets at the end of the String
 * @param input
 * @param numberOfBraceBackets
 * @return
 */
public static String AppendBraceBucketsAtEndofTheString(String input, int numberOfBraceBackets){

    String returnValue = input;

    try{
    for (int i = 0; i < numberOfBraceBackets;i++){
       returnValue = returnValue.concat("}");
    }
    }
    catch(Exception e){

        log.debug("Could not append braceBuckets at the end of the String");
    }
 
    return returnValue;

}
/**
 * Repleace all occurrences (with an empty String) except the first one of the delimiter word
 * @param input
 * @param delimiter
 * @return
 */
 private static String RemoveAllExceptTheFirstOccurrenceOfaAWord (String input, String delimiter){

    String firstPart = input.substring(input.indexOf(delimiter),input.indexOf(delimiter)+11);
    String afterStartDelimiterPartUnmodified = input.substring(input.indexOf(delimiter)+11);
    String cleanedafterStartDelimiterPart = afterStartDelimiterPartUnmodified.replaceAll(delimiter,"");
    String finalOutputString = firstPart.concat(cleanedafterStartDelimiterPart);
    
    return finalOutputString;
}

/**
 * Repleace all occurrences (with an empty String) except the last one of the delimiter word
 * @param input
 * @param delimiter
 * @return
 */
private static String RemoveAllExceptTheLastOccurrenceofAWord(String input, String delimiter){

    final String S=" ";

    String lastPart = input.substring(input.lastIndexOf(delimiter));
    String beforerEndDelimiterStringUnmodified = input.substring(0,input.lastIndexOf(delimiter)-1);
    String cleanedBeforerEndDelimiterString = beforerEndDelimiterStringUnmodified.replaceAll(delimiter, "");
    cleanedBeforerEndDelimiterString = cleanedBeforerEndDelimiterString.concat(S);
    String finalOutputString = cleanedBeforerEndDelimiterString.concat(lastPart);
   
    return finalOutputString;
}

}