package pn.cg.app_system.code_generation.model;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class serves as a data holder for the classes needed for an application and their implementation status*/
public class SuperApp
{
    private final String className;
    private boolean isImplemented;

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
}
