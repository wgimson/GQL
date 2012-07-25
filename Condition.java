package gqlMain;

public class Condition {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private String operator;
    private Argument lOp, rOp;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public Condition(String op, Argument lOpName, Argument rOpName) {
        this.operator = op;
        this.lOp = lOpName;
        this.rOp = rOpName;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public String getOperator() {
        return this.operator;
    }
    
    public Argument getLOp() {
        return this.lOp;
    }
    
    public Argument getROp() {
        return this.rOp;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setOperator(String op) {
        this.operator = op;
    }
    
    public void setLOp(Argument lOpName) {
        this.lOp = lOpName;
    }
    
    public void setROp(Argument rOpName) {
        this.rOp = rOpName;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public boolean checkQueryTypes(Class headArgClass) {
        Class lChildClass = 
            this.lOp.checkQueryTypes(headArgClass);
        Class rChildClass = 
            this.rOp.checkQueryTypes(headArgClass);
        if (lChildClass.isAssignableFrom(rChildClass)) {
            return true;
        } else {
            System.err.println("\nError: Query types: " + lChildClass.toString()
                    + " and " + rChildClass.toString() + " do not match.\n");
            return false;
        }
    }
}
