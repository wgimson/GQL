package gqlMain;

import java.lang.reflect.Field;

public class Argument {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    // argValue only used for Constants - argName only used for Objects and 
    // member variables
    private String argName, argValue;    
    private boolean isConstant, isObject, isMemberVariable, hasParent, hasChild, 
            isSequence, isSequenceChild;
    private Argument child, parent;
    private int index;
    private Class argClass;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    // To create a Constant Argument, use the default Constructor and then 
    // invoke the setConstant() method, and then setIsConstant() to true
    public Argument() {
        this.argName = null;
        this.argValue = null;
        this.isConstant = true;
        this.isObject = false;
        this.hasParent = false;
        this.hasChild = false;
        this.isMemberVariable = false;
        this.isSequence = false;
        this.isSequenceChild = false;
        this.parent = null;
        this.child = null;
        this.index = -1;
        this.argClass = null;
    }
    
    // This Constructor creates an Arugment representing a Node, Edge, or 
    // sequence of such
    public Argument(String name) {
        this.argName = name;
        this.argValue = null;
        this.isConstant = false;
        this.isObject = false;
        this.isMemberVariable = false;
        this.hasParent = false;
        this.hasChild = false;
        this.isSequence = false;
        this.isSequenceChild = false;
        this.parent = null;
        this.child = null;
        this.index = -1;
        this.argClass = null;
    }
    
    // This Constructor is strictly for Arguments representing an index in a 
    // sequence
        public Argument(int position) {
        this.argName = null;
        this.argValue = null;
        this.isConstant = true;
        this.isObject = false;
        this.hasParent = true;
        this.hasChild = false;
        this.isMemberVariable = false;
        this.isSequence = false;
        this.isSequenceChild = true;
        this.parent = null;
        this.child = null;
        this.index = position;
        this.argClass = null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public String getArgName() {
        if (!this.isConstant()) {
            return this.argName;
        } else {
            System.err.println("\nError: Constant Argument.\n");
            return null;
        }
    }
    
    public String getArgValue() {
        if (this.isConstant()) {
            return this.argValue;
        } else {
            System.err.println("\nError: Not a Constant Argument.\n");
            return null;
        }
    }
    
    public Class getArgClass() {
        return this.argClass;
    }
    
    public Argument getParent() {
        if (this.hasParent()) {
            return this.parent;
        } else {
            System.err.println("\nError: No parent Argument found.\n");
            return null;
        }
    }
    
    public Argument getChild() {
        if (this.hasChild()) {
            return this.child;
        } else {
            System.err.println("\nError: No child Argument found.\n");
            return null;
        }
    }
    
    public int getIndex(String position) {
        if (this.index != -1) {
            return this.index;
        } else {
            System.err.println("\nError: Argument in not a sequence child.\n");
            return -1;
        }
    }
    
    public boolean isConstant() {
        return this.isConstant;
    }
    
    public boolean isObject() {
        return this.isObject;
    }
    
    public boolean isMemberVariable() {
        return this.isMemberVariable;
    }

    public boolean isSequence() {
        return this.isSequence;
    }
    
    public boolean isSequenceChild() {
        return this.isSequenceChild;
    }
    
    public boolean hasParent() {
        return this.hasParent;
    }
    
    public boolean hasChild() {
        return this.hasChild;
    }

    public Argument getFinalChild() {
        if (this.hasChild) {
            return this.child.getFinalChild();
        } else {
            return this;
        }
    }
    
    public Argument getFirstParent() {
        if (this.hasParent) {
            return this.parent.getFirstParent();
        } else {
            return this;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setArgName(String name) {
        this.argName = name;
    }
    
    public void setArgValue(String value) {
        this.argValue = value;
    }
    
    public void setIndex(int position) {
        if (this.isSequenceChild) {
            this.index = position;
        } else {
            System.err.println("\nError: Argument is not a sequence.\n");
        }
    }
    
    public void setIsConstant(boolean bool) {
        this.isConstant = bool;
    }
    
    public void setConstant(String cons) {
        this.argValue = cons;
    }

    public void setIsObject(boolean bool) {
        this.isObject = bool;
        if (bool == true) {
            this.isMemberVariable = false;
        }
    }
    
    public void setIsMemberVariable(boolean bool) {
        this.isMemberVariable = bool;
        if (bool == true) {
            this.isObject = false;
            this.isSequence = false;
        }
    }

    public void setIsSequence(boolean bool) {
        this.isSequence = bool;
        if (bool == true) {
            this.isObject = bool;
        }
    }
    
    public void setIsSequenceChild(boolean bool) {
        this.isSequenceChild = bool;
    }

    public void setParent(Argument par) {
        this.parent = par;
        this.hasParent = true;
    }
    
    public void setChild(Argument chil) {
        this.child = chil;
        this.hasChild = true;
    }
    
    public void setArgClassWithString(String argC) {
        try {
            this.argClass = Class.forName(argC);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("\nError: Argument Class could not be" +
                    " assigned.\n");
        }
    }
    
    public void setArgClass(Class cls) {
        this.argClass = cls;
    }

    public String toString() {
        if (this.isConstant) {
            return this.argValue;
        } else {
            return this.argName;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public Class checkQueryTypes(Class headArgClass) {
        if (this.hasChild) {
            try {
                Field argField = headArgClass.getDeclaredField(this.argName);
                argField.setAccessible(true);
                Class argFieldClass = (Class) argField.getType();
                this.setArgClass(argFieldClass);
                return this.getChild().checkQueryTypes(argFieldClass);
            } catch (NoSuchFieldException nsfe) {
                System.err.println("\nError: Field '" + this.argName + 
                        "' could not be found.\n");
                return null;
            }
        } else {
            if (this.isConstant) {
                try {
                    double argDouble = Double.parseDouble(this.getArgValue());
                    this.setArgClass(double.class);      
                    return this.getArgClass();
                } catch (NumberFormatException nfe) {
                    this.setArgClass(String.class);       
                    return this.getArgClass();
                }
            } else {
                try {
                    Field argField = headArgClass.getDeclaredField(this.argName);
                    argField.setAccessible(true);
                    Class argFieldClass = (Class) argField.getType();
                    this.setArgClass(argFieldClass);
                    return this.getArgClass();
                } catch (NoSuchFieldException nsfe) {
                    System.out.println("\nError: Field '" + this.argName +
                            "' could not be found.\n");
                    return null;
                }
             }
        }
    }
}
