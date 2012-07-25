package gqlMain;

public class Edge {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private String edgeName;
    private double edgeDouble;
    private Node from, to;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public Edge() {
        this.edgeName = null;
        this.from = null;
        this.to = null;
    }
    
    public Edge(String name) {
        this.edgeName = name;
        this.from = null;
        this.to = null;
    }
    
    public Edge(String name, Node fromNode) {
        this.edgeName = name;
        this.from = fromNode;
        this.to = null;
    }
    
    public Edge(String name, Node fromNode, Node toNode) {
        this.edgeName = name;
        this.from = fromNode;
        this.to = toNode;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS 
    ////////////////////////////////////////////////////////////////////////////
    public String getName() {
        return this.edgeName;
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
        this.edgeName = name;
    }
    
    public void setFrom(Node fromNode) {
        this.from = fromNode;
    }
    
    public void setTo(Node toNode) {
        this.to = toNode;
    }

    public String toString() {
        return "\nEdge: " + this.edgeName + "; Number: " + this.edgeDouble +
            "; From Node: " + this.from.toString() + "; To Node: " + this.to.toString() 
            + "\n";
    }
}
