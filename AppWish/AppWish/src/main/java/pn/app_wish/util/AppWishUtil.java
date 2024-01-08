package pn.app_wish.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class AppWishUtil {

    /**
     * Removes .class from a path
     * @param pathString
     * @return String
     */
    public static String removeClassPrefixFromString(String pathString){

        return pathString;
    }

    /**
     * Filter an array of files with he .class prefix predicate.
     * This is useful for separating .java files from .class files
     * @param javaAndClassFiles
     * @return File[]
     */
    public static List<File> filterOnClassPrefix(List<File> javaAndClassFiles){

   return javaAndClassFiles.stream().filter(f -> f.getPath().endsWith(".class")).collect(Collectors.toList());

    }

    public static boolean isLinux(){

       return Objects.equals(File.separator, "/");
    }
}
