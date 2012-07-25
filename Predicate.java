package gqlMain;

import java.util.Vector;
import java.util.Iterator;

public class Predicate {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private String subClass;
    private Vector<Condition> conditions;
    private boolean hasSubclass, hasConditions;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public Predicate(String sub) {
        this.subClass = sub;
        this.conditions = new Vector();
        this.hasSubclass = true;
        this.hasConditions = false;
    }
    
    public Predicate(Vector<Condition> conds) {
        this.subClass = null;
        this.conditions = conds;
        this.hasSubclass = false;
        this.hasConditions = true;
    }
    
    public Predicate(String sub, Vector<Condition> conds) {
        this.subClass = sub;
        this.conditions = conds;
        this.hasSubclass = true;
        this.hasConditions = true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public String getSubclass() {
        if (this.hasSubclass) {
            return this.subClass;
        } else {
            System.err.println("\nError: Predicate has no subclass.\n");
            return null;
        }
    }
    
    public Vector<Condition> getConditions() {
        if (this.hasConditions) {
            return this.conditions;
        } else {
            System.err.println("\nError: Predicate has no Conditions.\n");
            return null;
        }
    }
    
    public boolean hasSubclass() {
        return this.hasSubclass;
    }
    
    public boolean hasConditions() {
        return this.hasConditions;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setSubclass(String sub) {
        this.subClass = sub;
        this.hasSubclass = true;
    }
    
    public void setConditions(Vector<Condition> conds) {
        this.conditions = conds;
        this.hasConditions = true;
    }
    
    public void setHasSubclass(boolean bool) {
        this.hasSubclass = bool;
    }
    
    public void setHasConditions(boolean bool) {
        this.hasConditions = bool;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public boolean checkQueryTypes(Class headArgClass) {
        Iterator<Condition> condIter = this.conditions.iterator();
        while (condIter.hasNext()) {
            Condition cond = condIter.next();
            boolean typesMatch = 
                cond.checkQueryTypes(headArgClass);
            if (!typesMatch) {
                return false;
            }
        }
        return true;
    }
            

}
