package gqlMain;

public class SubEdge {
     ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private String subEdgeName;
    private SubNode from, to;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public SubEdge() {
        this.subEdgeName = null;
        this.from = null;
        this.to = null;
    }
    
    public SubEdge(String name) {
        this.subEdgeName = name;
        this.from = null;
        this.to = null;
    }
    
    public SubEdge(String name, SubNode fromNode) {
        this.subEdgeName = name;
        this.from = fromNode;
        this.to = null;
    }
    
    public SubEdge(String name, SubNode fromNode, SubNode toNode) {
        this.subEdgeName = name;
        this.from = fromNode;
        this.to = toNode;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS 
    ////////////////////////////////////////////////////////////////////////////
    public String getName() {
        return this.subEdgeName;
    }
    
    public Node getFrom() {
        return this.from;
    }
    
    public Node getTo() {
        return this.to;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setName(String name) {
        this.subEdgeName = name;
    }
    
    public void setFrom(SubNode fromNode) {
        this.from = fromNode;
    }
    
    public void setTo(SubNode toNode) {
        this.to = toNode;
    }
}
