package pn.cg.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.datastorage.constant.CommonStringConstants;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class StringUtil {

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);


    /**
     * Extracts the public class name from a text with java classes
     *
     * @param unparsedJavaClassText String that contains unparsed Java code
     * @return String
     */
    public static String extractClassNameFromTextWithJavaClasses(String unparsedJavaClassText) {
        String[] lines = unparsedJavaClassText.split("\n");

        System.out.println(lines.length);

        String className = "";

        for (String line : lines) {


            if (line.contains("public class")) {

                className = line.replace("public class", "").replace("{", "").trim();

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
     *
     * @param input input String
     * @return String
     */
    public static String removeFirstLine(String input) {

        String returnValue = input;
        try {
            String tmpValue;
            tmpValue = input.split("\n", 2)[1];
            if (!tmpValue.startsWith("import")) ;
            returnValue = tmpValue;
        } catch (Exception e) {
            log.info("Could not remove first line");
        }
        return returnValue;
    }

    /**
     * Gets the first line of a string
     *
     * @param input input String
     * @return String
     */
    public static String getFirstLine(String input) {

        return input.split("\n", 1)[0];
    }

    /**
     * Removes chars that might cause ollama´s api to send 400
     *
     * @param input input String
     * @return String
     */
    public static String removeBadRequestChars(String input) {


        return input.replace("^", "").replace(":", "");
    }


    public static String IncludeEveryThingAfterStartChar(String input) {
        String returnValue = input;

        try {
            returnValue = input.split(CommonStringConstants.JAVA_CODE_GENERATION_START_DELMITER_STRING)[1];

        } catch (Exception e) {
            log.info("Could not include everything afterStartChar");
        }

        return returnValue;

    }


    public static String RemoveEveryThingAfterEndChar(String input) {
        String returnValue = input;
        try {

            returnValue = input.split(CommonStringConstants.JAVA_CODE_GENERATION_END_DELMITER_STRING)[0];
        } catch (Exception e) {
            log.info("Could not RemoveEveryThingAfterEndChar");
        }
        return returnValue;
    }

    public static String RemoveExtraStartDelimitersInResponse(String input) {

        String returnValue = input;

        try {
            returnValue = removeDelimiters(returnValue, true);
        } catch (Exception e) {

            log.debug("RemoveExtraStartDelimitersInResponse exception");
        }

        return returnValue;

    }

    public static String RemoveExtraEndDelimitersInResponse(String input) {

        String returnValue = input;

        try {
            returnValue = removeDelimiters(returnValue, false);
        } catch (Exception e) {

            log.debug("RemoveExtraEndDelimitersInResponse exception");
        }

        return returnValue;
    }


    private static String removeDelimiters(String input, boolean isStartDelimiter) {

        String returnValue = input;
        String delimiter = "";

        try {

            if (isStartDelimiter) {
                delimiter = CommonStringConstants.JAVA_CODE_GENERATION_START_DELMITER_STRING;
                returnValue = RemoveAllExceptTheFirstOccurrenceOfaAWord(returnValue, delimiter);
            } else if (!isStartDelimiter) {
                delimiter = CommonStringConstants.JAVA_CODE_GENERATION_END_DELMITER_STRING;
                returnValue = RemoveAllExceptTheLastOccurrenceOfWord(returnValue, delimiter);
                returnValue = RemoveAllExceptTheLastOccurrenceOfWord(returnValue, delimiter + "\n");
            }
        } catch (Exception e) {
            log.debug("Error on delimiter String removal process");
        }
        return returnValue;
    }

    public static int GetUnbalancedBraceBracketsFromString(String input) {
        int counter = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.toCharArray()[i] == '{') counter++;
            else if (input.toCharArray()[i] == '}') counter--;
            if (counter < 0) break;
        }
        return counter;
    }

    /**
     * Appends brace buckets at the end of the String
     *
     * @param input                String Input
     * @param numberOfBraceBuckets int
     * @return input string appended with the given amount of brace buckets
     */
    public static String AppendBraceBucketsAtEndofTheString(String input, int numberOfBraceBuckets) {

        String returnValue = input;

        try {
            for (int i = 0; i < numberOfBraceBuckets; i++) {
                returnValue = returnValue.concat("}");
            }
        } catch (Exception e) {

            log.debug("Could not append braceBuckets at the end of the String");
        }
        return returnValue;
    }

    /**
     * Removes common strings that some AI-Models may add to the generated code
     *
     * @param input String Input
     * @return String
     */
    public static String RemoveCommonAdditionStringsFromAiModels(String input) {
        String returnValue = input;
        try {
            returnValue = returnValue.replace("```java", "")
                    .replace("```", "")
                    .replace("```Java", "")
                    .replace("```JAVA", "");
        } catch (Exception e) {

            log.error("Could not remove common addition string");
        }
        return returnValue;
    }

    /**
     * Takes an input string formated with new lines , parses it , and then converts it into a list
     *
     * @param input A string with class names delimited with new lines
     * @return List<String>
     */
    public static List<String> GetListOfClassNamesInSuperAppGeneration(String input) {
        try {
            String[] classNamesArr = input.split("\n");
            List<String> tmpClassNames = new LinkedList<>();

            for (String className : classNamesArr) {
                if (!className.contains(" ") || !Character.isLowerCase(className.codePointAt(0))) {
                    tmpClassNames.add(className);
                }
            }

           return tmpClassNames.stream().filter(line -> !(line.contains(" "))).toList();

        } catch (Exception e) {

            log.error("Could not extract class names from the response from the AI-model");
        }
        return new LinkedList<>();
    }

    /**
     * Replace all occurrences (with an empty String) except the first one of the delimiter word
     *
     * @param input     input String
     * @param delimiter delimiter String
     */
    private static String RemoveAllExceptTheFirstOccurrenceOfaAWord(String input, String delimiter) {

        String firstPart = input.substring(input.indexOf(delimiter), input.indexOf(delimiter) + 11);
        String afterStartDelimiterPartUnmodified = input.substring(input.indexOf(delimiter) + 11);
        String cleanedAfterStartDelimiterPart = afterStartDelimiterPartUnmodified.replaceAll(delimiter, "");

        return firstPart.concat(cleanedAfterStartDelimiterPart);
    }

    /**
     * Replace all occurrences (with an empty String) except the last one of the delimiter word
     *
     * @param input     input String
     * @param delimiter String
     */
    private static String RemoveAllExceptTheLastOccurrenceOfWord(String input, String delimiter) {

        final String S = " ";

        String lastPart = input.substring(input.lastIndexOf(delimiter));
        String beforeEndDelimiterStringUnmodified = input.substring(0, input.lastIndexOf(delimiter) - 1);
        String cleanedBeforerEndDelimiterString = beforeEndDelimiterStringUnmodified.replaceAll(delimiter, "");
        cleanedBeforerEndDelimiterString = cleanedBeforerEndDelimiterString.concat(S);

        return cleanedBeforerEndDelimiterString.concat(lastPart);
    }


}