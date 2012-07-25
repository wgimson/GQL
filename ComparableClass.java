package gqlMain;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComparableClass {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    Class compClass;

    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////////////////////////////////////////////
    public ComparableClass(Class cClass) {
        this.compClass = cClass;
    }

    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public Class getCompClass() {
        return this.compClass;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setCompClass(Class cClass) {
        this.compClass = cClass;
    }

    @Override
    public boolean equals(Object compClass) {
        ComparableClass cClass = (ComparableClass) compClass;
        if (this.getCompClass().getName().
                equals(cClass.getCompClass().getName())) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        //Pattern packagePatt = Pattern.compile("(.*[.])([^.]*)");
        String classPattStr = this.compClass.getName().toString();
        System.out.println(classPattStr);
        
        //Matcher packageMatch = packagePatt.matcher(classPattStr);
        String[] classPattArry = classPattStr.split("\\.");
        //System.out.println(classPattArry[0]);
        //System.out.println(classPattArry[1]);
 
        String classStr = classPattArry[classPattArry.length - 1];
        return classStr;
    }
}
