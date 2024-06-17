package pn.cg.app_system.code_generation.model;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class serves as a data holder for the classes needed for an application and their implementation status*/
public class SuperApp
{
    // The first parameter is the class name in text and the second parameter is for its implementation status
    private final Dictionary<String,Boolean> classesNeededForApplicationDict;


    public SuperApp() {
        this.classesNeededForApplicationDict = new Hashtable<String,Boolean>();

    }



    public void AddSuccessfulImplementationToClass(String nameOfClass){
        this.classesNeededForApplicationDict.put(nameOfClass,true);

    }
}
