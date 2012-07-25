package gqlMain;

import java.util.ListIterator;

public class SimplePath { 
   /////////////////////////////////////////////////////////////////////////////
   // MEMBER VARIABLES
   /////////////////////////////////////////////////////////////////////////////
   private Argument headArg;
   private String pathLength;
   private NodeOrEdgeObject lNodeOrEdge, rNodeOrEdge;
   private boolean isShortestPath, isEveryPath, 
           isFixedLengthPath;

   /////////////////////////////////////////////////////////////////////////////
   // CONSTRUCTORS
   /////////////////////////////////////////////////////////////////////////////
   public SimplePath(String headArgName, NodeOrEdgeObject lOp, 
           NodeOrEdgeObject rOp) {
       this.headArg = new Argument(headArgName);
       this.setHeadArgClassToSimplePath();
       this.pathLength = "1";
       this.lNodeOrEdge = lOp;
       this.rNodeOrEdge = rOp;
       this.isShortestPath = false;
       this.isEveryPath = false;
       this.isFixedLengthPath = false;
   }

   /////////////////////////////////////////////////////////////////////////////
   // GETTERS
   /////////////////////////////////////////////////////////////////////////////
   public String getPathLength() {
       return this.pathLength;
   }

   public NodeOrEdgeObject getLOp() {
       return this.lNodeOrEdge;
   }

   public NodeOrEdgeObject getROp() {
       return this.rNodeOrEdge;
   }

   public Argument getHeadArgument() {
       if (this.headArg != null) {
           return this.headArg;
       } else {
           System.err.println("\nError: SimplePath has no head Argument"
                   + " set.\n");
           return null;
       }
   }
   
   public boolean isShortestPath() {
       return this.isShortestPath;

   }
   
   public boolean isEveryPath() {
       return this.isEveryPath;
   }
   
   public boolean isFixedLengthPath() {
       return this.isFixedLengthPath;
   }

   /////////////////////////////////////////////////////////////////////////////
   // SETTERS
   /////////////////////////////////////////////////////////////////////////////
   public void setPathLength(String len) {
       this.pathLength = len;
   }

   public void setLOp(NodeOrEdgeObject newLOp) {
       this.lNodeOrEdge = newLOp;
   }

   public void setROp(NodeOrEdgeObject newROp) {
       this.rNodeOrEdge = newROp;
   }

   public void setHeadArg(Argument arg) {
       this.headArg = arg;
   }

   public void setHeadArgClassToSimplePath() {
       try {
           Class pathClass = Class.forName("SimplePath");
           this.headArg.setArgClass(pathClass);
       } catch (ClassNotFoundException cnfe) {
           System.err.println("\nError: Class: SimplePath could not be" +
                   " found.\n");
       }
   }

   public void setIsShortestPath(boolean bool) {
       this.isShortestPath = bool;
       if (bool == true) {
           this.isEveryPath = false;
           this.isFixedLengthPath  =false;
       }
   }
   
   public void setIsEveryPath(boolean bool) {
       this.isEveryPath = bool;
       if (bool == true) {
           this.isShortestPath = false;
           this.isFixedLengthPath = false;
       }
   }
   
   public void setIsFixedLengthPath(boolean bool) {
       this.isFixedLengthPath = true;
       if (bool == true) {
           this.isShortestPath = false;
           this.isEveryPath = false;
       }
   }
   //////////////////////////////////////////////////////////////////////////// 
   // SEMANTIC CHECK METHODS
   ////////////////////////////////////////////////////////////////////////////
   public boolean setHeadArgClass(ListIterator<ComparableClass> classIter) {
       if (classIter.hasNext()) {
           ComparableClass compClass = classIter.next();
           if (compClass.toString().equals("SimplePath")) {
               this.headArg.setArgClass(compClass.getCompClass());
               return true;
           } else {
               return this.setHeadArgClass(classIter);
           }
       } else {
           System.err.println("\nError: 'SimplePath' object does not appear in"
                   + " the database.\n");
           return false;
       }
   }
}
