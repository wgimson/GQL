package gqlMain;

import com.db4o.*;
import com.db4o.query.Query;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class NodeOrEdgeObject {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private Argument headArg;
    private Predicate predicate;
    private boolean hasPredicate, isNode, isEdge;
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////
    public NodeOrEdgeObject(Argument head) {
        this.headArg = head;
        this.predicate = null;
        this.hasPredicate = false;
    }
    
    public NodeOrEdgeObject(Argument head, Predicate pred) {
        this.headArg = head;
        this.predicate = pred;
        this.hasPredicate = true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS 
    ////////////////////////////////////////////////////////////////////////////
    public Argument getHeadArgument() {
        return this.headArg;
    }
    
    public Predicate getPredicate() {
        if (this.hasPredicate) {
            return this.predicate;
        } else {
            System.err.println("\nError: NodeOrEdgeObect has no Predicate.\n");
            return null;
        }
    }
    
    public Class getHeadArgumentClass() {
        if (this.headArg.getArgClass() == null) {
            System.err.println("\nError: head Argument '" + 
                    this.headArg.getArgName() + "' has no Class set.\n");
            return null;
        } else {
            return this.headArg.getArgClass();
        }
    }

    public boolean hasPredicate() {
        return this.hasPredicate;
    }
    
    public boolean isNode() {
        return this.isNode;
    }
    
    public boolean isEdge() {
        return this.isEdge;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setHeadArg(Argument arg) {
        this.headArg = arg;
    }
    
    public void setPredicate(Predicate pred) {
        this.predicate = pred;
        this.hasPredicate = true;
    }

    public void setHasPredicate(boolean bool) {
        this.hasPredicate = bool;
    }
    
    public void setIsNode(boolean bool) {
        this.isNode = bool;
        this.isEdge = !bool;
    }
    
    public void setIsEdge(boolean bool) {
        this.isEdge = bool;
        this.isNode = !bool;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public boolean loadSubclasses(String classPath, 
            ClassLoader subclassLoader) {
        if (this.hasPredicate) {
            if (this.predicate.hasSubclass()) {
                try {
                    String subclassName = this.predicate.getSubclass();
                    if (classPath.equals("")) {
                        subclassLoader.loadClass(subclassName);
                        return true;
                    } else {
                        // TODO must be dubugged
                        //
                        System.out.println("\nAttempting to access class path"
                                + classPath + "." + subclassName);
                        //
                        subclassLoader.loadClass(classPath + "." + subclassName);
                        return true;
                    } 
                } catch (ClassNotFoundException cnfe) {
                    return false;
                }
            } else {
                return true;
            } 
        } else {
            return true;
        }
    }

    public boolean setHeadArgClass
        (ListIterator<ComparableClass> classIter) {
        if (this.hasPredicate) {
            if (this.predicate.hasSubclass()) {
                String subclass = this.predicate.getSubclass();
                boolean headWasSet = 
                        this.traverseClassListAndSetHeadArgClass(classIter,
                        subclass);
                if (headWasSet) {
                    return true;
                } else {
                    System.err.println("\nError: subclass for head Argument: " +
                            this.getHeadArgument() + " failed to set.\n");
                    return false;
                }
            } else {
                boolean headWasSet = 
                        this.traverseClassListAndSetHeadArgClass(classIter);
                if (headWasSet) {
                    return true;
                } else {
                    System.err.println("\nError: subclass for head Argument: " +
                            this.getHeadArgument() + " failed to set.\n");
                    return false;
                }
            }
        } else {
            boolean headWasSet = 
                    this.traverseClassListAndSetHeadArgClass(classIter);
            if (headWasSet) {
                return true;
            } else {
                System.err.println("\nError: subclass for head Argument: " +
                        this.getHeadArgument() + " failed to set.\n");
                return false;
            }
        }
    }


    private boolean traverseClassListAndSetHeadArgClass
            (ListIterator<ComparableClass> listIter) {
        if (this.isNode) {
           if (listIter.hasNext()) {
                ComparableClass compClass = listIter.next();
                if (compClass.toString().equals("Node")) {
                    this.headArg.setArgClass(compClass.getCompClass());
                    return true;
                } else {
                    return this.traverseClassListAndSetHeadArgClass(listIter);
                }
            } else {
                System.err.println("\nError: Query has no subclass and object"
                    + " 'Node' does not appear in the database.\n");
                return false;
            }
        } else {
            if (listIter.hasNext()) {
                ComparableClass compClass = listIter.next();
                if (compClass.toString().equals("Edge")) {
                    this.headArg.setArgClass(compClass.getCompClass());
                    return true;
                } else {
                    return this.traverseClassListAndSetHeadArgClass(listIter);
                }
            } else {
                System.err.println("\nError: Query has no subclass and object"
                    + " 'Edge' does not appear in the database.\n");
                return false;
            }
        }
    }

    public boolean traverseClassListAndSetHeadArgClass
            (ListIterator<ComparableClass> listIter, 
            String subclass) {
        if (listIter.hasNext()) {
            ComparableClass compClass = listIter.next();
            if (compClass.toString().equals(subclass))  {
                this.headArg.setArgClass(compClass.getCompClass());
                return true;
            } else {
                return this.traverseClassListAndSetHeadArgClass(listIter,
                        subclass);
            }
        } else {
            System.err.println("\nError: Query subclass '" + subclass +
                    "' does not appear in database.\n");
            return false;
        }
    }

    public boolean checkQueryTypes() {
        Class headArgClass = this.headArg.getArgClass();
        if (this.hasPredicate) {
            boolean typesMatch = 
                this.predicate.checkQueryTypes(headArgClass);
            if (typesMatch) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // EVALUATION
    ////////////////////////////////////////////////////////////////////////////
    public LinkedList evaluateQuery(ObjectContainer db) {
        ObjectSet resultWithUnwantedSubclasses;
        LinkedList result = new LinkedList();
        Query query = db.query();
        if (this.hasPredicate) {
            if (this.predicate.hasSubclass() && 
                    this.predicate.hasConditions()) {
                String subName = this.predicate.getSubclass();
                try {
                    Class  subclass = Class.forName(subName);
                    Iterator<Condition> condIter = 
                            this.predicate.getConditions().iterator();
                    while (condIter.hasNext()) {
                        Condition cond = condIter.next();
                        Argument lOp = cond.getLOp();
                        String comp = cond.getOperator();
                        Argument rOp = cond.getROp();
                        query.constrain(subclass);
                        if (rOp.isConstant()) {
                            resultWithUnwantedSubclasses = 
                                    switchConstantComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);
                        } else {
                            resultWithUnwantedSubclasses = 
                                    switchVariableComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);
                        }
                    }
                    return null;
                } catch (ClassNotFoundException cnfe) {
                    System.err.println("\nError: class '" + subName +
                            "' could not be located.\n");
                    return null;
                }
            } else if (this.predicate.hasSubclass()) {
                try {
                    Class subclass = Class.forName(this.predicate.getSubclass());
                    query.constrain(subclass);
                    resultWithUnwantedSubclasses = query.execute();
                    return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                            result);
                } catch (ClassNotFoundException cnfe) {
                    System.err.println("\nError: class '" + this.predicate.getSubclass()
                             + "' could not be found.\n");
                    return null;
                }
            } else {
                Iterator<Condition> condIter = 
                        this.predicate.getConditions().iterator();
                while (condIter.hasNext()) {
                    Condition cond = condIter.next();
                    Argument lOp = cond.getLOp();
                    String comp = cond.getOperator();
                    Argument rOp = cond.getROp();
                    if (rOp.isConstant()) {
                        if (this.isNode) {
                            query.constrain(Node.class);
                            resultWithUnwantedSubclasses = 
                                    switchConstantComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);      
                        } else {
                            query.constrain(Edge.class);
                            resultWithUnwantedSubclasses = 
                                    switchConstantComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);
                        } 
                    } else {
                        if (this.isNode) {
                            query.constrain(Node.class);
                            resultWithUnwantedSubclasses = 
                                    switchVariableComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);
                        } else {
                            query.constrain(Edge.class);
                            resultWithUnwantedSubclasses = 
                                    switchVariableComparison(query, comp, lOp, rOp);
                            return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                                    result);
                        } 
                    }
                }
                return null;
            }
        } else {
            if (this.isNode) {
                query.constrain(Node.class);
                resultWithUnwantedSubclasses = query.execute();
                return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                        result);
            } else if (this.isEdge) {
                query.constrain(Edge.class);
                resultWithUnwantedSubclasses = query.execute();
                return removeUnwantedSubclasses(resultWithUnwantedSubclasses, 
                        result);
            } else {
                System.err.println("\nError: NodeOrEdgeObject type not set.\n");
                return null;
            }
        }
    }
    
    private ObjectSet switchConstantComparison(Query query, String comp, 
            Argument lOp, Argument rOp) {
        switch (comp) {
            case "<":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .smaller();
                return query.execute();
            case ">":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .greater();
                return query.execute();
            case "=":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .equal();
                return query.execute();
            case "<>":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .equal().not();
                return query.execute();
            case "<=":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .smaller().equal();
                return query.execute();
            case ">=":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .greater().equal();
                return query.execute();
            default:
                System.err.println("\nError: invalid comparison.\n");
                return null;
        }
    }
    
    
    // TODO - This probably isn't correct
    private ObjectSet switchVariableComparison(Query query, String comp, 
            Argument lOp, Argument rOp) {
        switch (comp) {
            case "<":
                query.descend(lOp.getArgName())
                        .constrain(query.descend(rOp.getArgName()))
                        .smaller();
                return query.execute();
            case ">":
                query.descend(lOp.getArgName())
                        .constrain(rOp.getArgName())
                        .greater();
                return query.execute();
            case "=":
                query.descend(lOp.getArgName())
                        .constrain(rOp.getArgName())
                        .equal();
                return query.execute();
            case "<>":
                query.descend(lOp.getArgName())
                        .constrain(rOp.getArgName())
                        .equal().not();
                return query.execute();
            case "<=":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .smaller().equal();
                return query.execute();
            case ">=":
                query.descend(lOp.getArgName())
                        .constrain(Double.valueOf(rOp.getArgValue()))
                        .greater().equal();
                return query.execute();
            default:
                System.err.println("\nError: invalid comparison.\n");
                return null;
        }
    }
    
    private LinkedList removeUnwantedSubclasses(ObjectSet resultWithSubclasses,
             LinkedList result) {
        while (resultWithSubclasses.hasNext()) {
            Object dbObj = resultWithSubclasses.next();
            ComparableClass objectClass = 
                    new ComparableClass(dbObj.getClass());
            ComparableClass queryClass = 
                    new ComparableClass(Node.class);
            if (objectClass.equals(queryClass)) {
                result.add((Node) dbObj);
                }
        }
        return result;
    }
}