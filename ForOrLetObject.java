package gqlMain;

public class ForOrLetObject {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES 
    ////////////////////////////////////////////////////////////////////////////
    private String variable;
    private GraphObject graphObj;
    private boolean isFor, isLet;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public ForOrLetObject(String var, GraphObject graph) {
        this.variable = var;
        this.graphObj = graph;
    }
    //////////////////////////////////////////////////////////////////////
    // DEBUG
    public ForOrLetObject() {
        this.variable = null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public String getVariable() {
        return this.variable;
    }
    
    public GraphObject getGraphObject() {
        return this.graphObj;
    }
    
    public boolean isFor() {
        return this.isFor;
    }
    
    public boolean isLet() {
        return this.isLet();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setVariable(String var) {
        this.variable = var;
    }
    
    public void setGraphObject(GraphObject graph) {
        this.graphObj = graph;
    }
    
    public void setIsFor(boolean bool) {
        this.isFor = bool;
        this.isLet = !bool;
    }
    
    public void setIsLet(boolean bool) {
        this.isLet = bool;
        this.isFor = !bool;
    }

    public String toString() {
        if (this.isFor) {
            return "For expression; variable: " + this.getVariable();
        } else {
            return "Let Expression; variable: " + this.getVariable();
        }
    }
}
