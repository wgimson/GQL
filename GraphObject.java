package gqlMain;

import java.util.ListIterator;
import com.db4o.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class GraphObject {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private boolean isPath, isNode, isEdge;
    private NodeOrEdgeObject nodeOrEdge;
    private SimplePath path;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTUCTOR
    ////////////////////////////////////////////////////////////////////////////
    public GraphObject(SimplePath sPath) {
        this.isPath = true;
        this.isNode = false;
        this.isEdge = false;
        this.nodeOrEdge = null;
        this.path = sPath;
    }
    
    // We don't know here whether the GraphObject is an Edge or a Node; only 
    // that it is not a Path - this is set manually in the CUP file
    public GraphObject(NodeOrEdgeObject nOE) {
        this.isPath = false;
        this.nodeOrEdge = nOE;
        this.path = null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public boolean isPath() {
        return isPath;
    }

    public boolean isNode() {
        return this.isNode;
    }

    public boolean isEdge() {
        return this.isEdge;
    }
    
    public NodeOrEdgeObject getNodeOrEdge() {
        if (!this.isPath) {
            return this.nodeOrEdge;
        } else {
            System.err.println("\nGraph Object is a Path.\n");
        }
        return null;
    }
    
    public SimplePath getPath() {
        if (this.isPath) {
            return this.path;
        } else {
            System.out.println("\nGraph Object is a Node or Edge Graph" +
                    " Object.\n");
        }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setIsPath(boolean bool) {
        this.isPath = bool;
        // this.getPath().setIsPath(bool);
        if (bool == true) {
            this.isNode = false;
            this.isEdge = false;
        }
    }

    public void setIsNode(boolean bool) {
        this.isNode = bool;
        this.getNodeOrEdge().setIsNode(bool);
        if (bool == true) {
            this.isEdge = false;
            this.getNodeOrEdge().setIsEdge(false);
            this.isPath = false;
        }
    }

    public void setIsEdge(boolean bool) {
        this.isEdge = bool;
        this.getNodeOrEdge().setIsEdge(bool);
        if (bool == true) {
            this.isNode = false;
            this.getNodeOrEdge().setIsNode(false);
            this.isPath = false;
        }
    }

    public void setNodeOrEdge(NodeOrEdgeObject nOE) {
        this.nodeOrEdge = nOE;
        this.path = null;
        this.isPath = false;
    }
    
    public void setPath(SimplePath sPath) {
        this.path = sPath;
        this.nodeOrEdge = null;
        this.isPath = true;
        this.isEdge = false;
        this.isNode = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public boolean loadSubclasses(String classPath, 
            ClassLoader subclassLoader) {
        if (this.isNode || this.isEdge) {
            boolean loadedSubclassFromNode = 
                this.nodeOrEdge.loadSubclasses(classPath, 
                    subclassLoader);
            if (loadedSubclassFromNode) { 
                return true;
            } else {
                return false;
            }
        } else if (this.isPath) {
            // TODO - as of this moment we don't need to worry about Path 
            // subclassese because there ARE none; this should be implemented
            // later but neccessitates altering the grammar
            NodeOrEdgeObject lNode = this.path.getLOp();
            NodeOrEdgeObject rNode = this.path.getROp();
            boolean loadedSubclassFromLNode = 
                lNode.loadSubclasses(classPath, 
                        subclassLoader);
            boolean loadedSubclassFromRNode =
                rNode.loadSubclasses(classPath,
                        subclassLoader);
            if (loadedSubclassFromLNode && loadedSubclassFromRNode) {
                return true;
            } else {
                return false;
            } 
        } else {
            return false;
        }
    }

    public boolean setHeadArgClasses
        (ListIterator<ComparableClass> classIter) {
        if (this.isNode || this.isEdge) {
            boolean nodeOrEdgeHeadArgWasSet = 
                this.nodeOrEdge.
                setHeadArgClass(classIter);
            if (nodeOrEdgeHeadArgWasSet) {
                return true;
            } else {
                return false;
            }
        } else if (this.isPath) {
            // First set head Argument of SimplePath obect; THEN set
            // head Arguments of respective Node objects
            boolean pathHeadArgWasSet = path.setHeadArgClass(classIter);
            if (!pathHeadArgWasSet) {
                return false;
            }
            // Reset classIter
            while (classIter.hasPrevious()) {
                classIter.previous();
            }
            NodeOrEdgeObject lNode = this.path.getLOp();
            NodeOrEdgeObject rNode = this.path.getROp();
            boolean lNodeOrEdgeHeadArgWasSet =
                lNode.setHeadArgClass(classIter);
            // reset classIter
            while (classIter.hasPrevious()) {
                classIter.previous();
            }
            boolean rNodeOrEdgeHeadArgWasSet = 
                rNode.setHeadArgClass(classIter);
            if (lNodeOrEdgeHeadArgWasSet && rNodeOrEdgeHeadArgWasSet) {
                return true;
            } else if (!lNodeOrEdgeHeadArgWasSet) {
                return false;
            } else {
                return false;
            }
        } else {
            System.err.println("\nError: Query is not of type Node, Edge, "
                    + "or Path, and has no subclass.\n");
            return false;
        }
    }

    public boolean checkQueryTypes() {
        if (this.isNode || this.isEdge) {
            NodeOrEdgeObject nodeOrEdge = this.getNodeOrEdge();
            boolean typesMatch  = nodeOrEdge.checkQueryTypes();
            if (typesMatch) {
                return true;
            } else {
                return false;
            }
        } else if (this.isPath) {
            // TODO - nothing to check for now, a Path must be a SimplePath, and
            // a SimplePath must have to Node objects, this is enforced by the
            // parser; later we will implement subclasses for Path and that
            // will require type checking
            NodeOrEdgeObject lNode = this.path.getLOp();
            NodeOrEdgeObject rNode = this.path.getROp();
            boolean lTypesMatch = lNode.checkQueryTypes();
            boolean rTypesMatch = rNode.checkQueryTypes();
            if (!lTypesMatch || !rTypesMatch) {
                return false;
            } else {
                return true;
            }
        } else {
            System.err.println("\nError: GraphObject type not set.\n");
            return false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // EVALUATION
    ////////////////////////////////////////////////////////////////////////////
    public LinkedList evaluateQuery(ObjectContainer db) {
        LinkedList queryResults = new LinkedList();
        if (this.isNode || this.isEdge) {
            LinkedList queryResult =  this.nodeOrEdge.evaluateQuery(db);
            queryResults.addAll(queryResult);
            return queryResults;
        } else {
            // This probably needs to be changed - SimplePath needs its own 
            // evaluateQuery method
            NodeOrEdgeObject lNode = this.path.getLOp();
            NodeOrEdgeObject rNode = this.path.getROp();
            LinkedList lQueryResult = lNode.evaluateQuery(db);
            LinkedList rQueryResult = rNode.evaluateQuery(db);
            queryResults.addAll(lQueryResult);
            queryResults.addAll(rQueryResult);
            return queryResults;
        }
    }
}
