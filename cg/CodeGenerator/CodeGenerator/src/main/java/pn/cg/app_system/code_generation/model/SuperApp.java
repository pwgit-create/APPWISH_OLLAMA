package pn.cg.app_system.code_generation.model;

import pn.cg.util.StringUtil;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class serves as a data holder for the classes needed for an application and their implementation status
 */
public class SuperApp {
    private final String className;
    private boolean isImplemented;

    /**
     * If the class is implemented and contains methods they are stored here
     */
    private List<String> methods;
    private List<String> constructors;


    public SuperApp(String className, boolean isImplemented) {
        this.className = className;
        this.isImplemented = isImplemented;

    }

    public String getClassName() {
        return className;
    }

    public boolean isImplemented() {
        return isImplemented;
    }

    public void setImplemented(boolean implemented) {
        isImplemented = implemented;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<String> constructors) {
        this.constructors = constructors;
    }

    /**
     * Gets a string with new lines that contains the methods if this instance is implemented and they exist
     * @return String
     */
    public String toStringForMethods(){
        StringBuilder sb = new StringBuilder();
        methods.forEach(m -> sb.append(m).append("\n"));
        return sb.toString().trim();
    }
    /**
     * Gets a string with new lines that contains any constructors if this instance is implemented and they exist
     * @return String
     */
    public String toStringForConstructors(){

        StringBuilder sb = new StringBuilder();
        constructors.forEach(c -> sb.append(c).append("\n"));
        return sb.toString().trim();
    }
}
