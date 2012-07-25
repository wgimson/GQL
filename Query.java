package gqlMain;

import com.db4o.*;
import com.db4o.Db4o.*;
import java.util.*;

public class Query {
    ////////////////////////////////////////////////////////////////////////////
    // MEMBER VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private Vector<ForOrLetObject> forC;
    private Vector<ForOrLetObject> letC; 
    private HashMap<Integer, Vector<Condition>> whereC;
    private LinkedList<Argument> returnC;
    private boolean hasLetClause;
    private boolean hasWhereClause;
    
    
    ////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////////////////////////////////////////////
    public Query(Vector<ForOrLetObject> fors, 
            LinkedList<Argument> returns) {
        this.forC = fors;
        this.returnC = returns;
        this.hasLetClause = false;
        this.hasWhereClause = false;
    }
    
    public Query(Vector<ForOrLetObject> let, Vector<ForOrLetObject> fors, 
            LinkedList<Argument> returns) {
        this.letC = let;
        this.forC = fors;
        this.returnC = returns;
        this.hasLetClause = true;
        this.hasWhereClause = false;
    }
    
    public Query(Vector<ForOrLetObject> fors, 
            HashMap<Integer, Vector<Condition>> where, 
            LinkedList<Argument> returns) {
        this.forC = fors;
        this.whereC = where;
        this.returnC = returns;
        this.hasLetClause = false;
        this.hasWhereClause = true;
    }
    
    public Query(Vector<ForOrLetObject> let, Vector<ForOrLetObject> fors, 
            HashMap<Integer, Vector<Condition>> where, 
            LinkedList<Argument> returns) {
        this.letC = let;
        this.forC = fors;
        this.whereC = where;
        this.returnC = returns;
        this.hasLetClause = true;
        this.hasWhereClause = true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    ////////////////////////////////////////////////////////////////////////////
    public Vector<ForOrLetObject> getForClause() {
        return this.forC;
    }
    public Vector<ForOrLetObject> getLetClause() {
        return this.letC;
    }
    
    public HashMap<Integer, Vector<Condition>> getWhereClause() {
        return this.whereC;
    }
    
    public LinkedList<Argument> getReturnClause() {
        return this.returnC;
    }
    
    public boolean hasLetClause() {
        return this.hasLetClause;
    }
    
    public boolean hasWhereClause() {
        return this.hasLetClause;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////
    public void setForClause(Vector<ForOrLetObject> forClause) {
        this.forC = forClause;
    }
    
    public void setLetClause(Vector<ForOrLetObject> letClause) {
        this.letC = letClause;
    }
    
    public void setWhereClause(HashMap<Integer, 
            Vector<Condition>> whereClause) {
        this.whereC = whereClause;
    }
    
    public void setReturnClause(LinkedList<Argument> returnClause) {
        this.returnC = returnClause;
    }
    
    public void setHasLetClause(boolean bool) {
        this.hasLetClause = bool;
    }
    
    public void setHasWhereClause(boolean bool) {
        this.hasWhereClause = bool;
    }

    ////////////////////////////////////////////////////////////////////////////
    // SEMANTIC CHECK METHODS
    ////////////////////////////////////////////////////////////////////////////
    public boolean loadSubclasses(ClassLoader subclassLoader, 
            String classPath) {
        if (this.hasLetClause()) {
            Vector<ForOrLetObject> letC = this.getLetClause();
            Iterator<ForOrLetObject> letIter = letC.iterator();
            while (letIter.hasNext()) {
                ForOrLetObject let = letIter.next();
                GraphObject graphObj = let.getGraphObject();
                boolean classesSuccessfullyLoaded = 
                    graphObj.loadSubclasses(classPath, 
                        subclassLoader);
                if (!classesSuccessfullyLoaded) {
                    return false;
                }
            }
            Vector<ForOrLetObject> forC = this.getForClause();
            Iterator<ForOrLetObject> forIter = forC.iterator();
            while (forIter.hasNext()) {
                ForOrLetObject foR = forIter.next();
                GraphObject graphObj = foR.getGraphObject();
                boolean classesSuccessfullyLoaded = 
                    graphObj.loadSubclasses(classPath, 
                        subclassLoader);
                if (!classesSuccessfullyLoaded) {
                    return false;
                }
            }
            return true;
        } else {
            Vector<ForOrLetObject> forC = this.getForClause();
            Iterator<ForOrLetObject> forIter = forC.iterator();
            while (forIter.hasNext()) {
                ForOrLetObject foR = forIter.next();
                GraphObject graphObj = foR.getGraphObject();
                boolean classesSuccessfullyLoaded = 
                    graphObj.loadSubclasses(classPath, 
                        subclassLoader);
                if (!classesSuccessfullyLoaded) {
                    return false;
                }
            }
            return true;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // METHOD:        A recursive function to populate a global LinkedList    // 
    //                with each Class represented in the object database.     //
    //                                                                        //
    // PRECONDITION:  The ObjectSet contains at least one Object, i.e. the    // 
    //                database is not empty - the LinkedList parameter is     //
    //                empty before the *first* invocation of the recursive    //
    //                function.                                               //
    //                                                                        //
    // POSTCONDITION: When this recursive function completes execution, each  //
    //                Class represented in the database will have an instance //
    //                in the LinkedList parameter.                            //
    //                                                                        //
    // PARAMETERS:    An ObjectSet containing every Object in the database,   //
    //                and a LinkedList, containing anywhere from none to      //
    //                all of the Casses represented in the database.          //
    //                                                                        //
    // RETURNS:       Nothing; the modified LinkedList is a global data       //
    //                structure.                                              //
    ////////////////////////////////////////////////////////////////////////////
    public void addClassesToList(ObjectSet<Object> dbObjects,
            LinkedList classList) {
        Object dbObject = dbObjects.next();
        ComparableClass compClass = new ComparableClass(dbObject.getClass());
        if (classList.contains(compClass)) {
            if (dbObjects.hasNext()) {
                addClassesToList(dbObjects, classList);
            } else {
                return;
            }
        } else {
            classList.add(compClass);
            if (dbObjects.hasNext()) {
                addClassesToList(dbObjects, classList);
            } else {
                return;
            }
        }
    }
     
    public boolean setHeadArgClasses(LinkedList classes) {
        if (this.hasLetClause) {
            Iterator<ForOrLetObject> letCIter = this.letC.iterator();
            while(letCIter.hasNext()) {
                ListIterator<ComparableClass> classIter = classes.listIterator();
                GraphObject graphObj = letCIter.next().getGraphObject();
                boolean headWasSet = 
                        graphObj.setHeadArgClasses(classIter);
                if (!headWasSet) {
                    return false;
                }
            }
            Iterator<ForOrLetObject> forCIter = this.forC.iterator();
            while(forCIter.hasNext()) {
                ListIterator<ComparableClass> classIter = classes.listIterator();
                GraphObject graphObj = forCIter.next().getGraphObject();
                boolean headWasSet =
                        graphObj.setHeadArgClasses(classIter);
                if (!headWasSet) {
                    return false;
                }
            }
            return true;
        } else {
            Iterator<ForOrLetObject> forCIter = this.forC.iterator();
            while(forCIter.hasNext()) {
                ListIterator<ComparableClass> classIter = classes.listIterator();
                GraphObject graphObj = forCIter.next().getGraphObject();
                boolean headWasSet = 
                        graphObj.setHeadArgClasses(classIter);
                if (!headWasSet) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean checkQueryTypes(ObjectContainer db) {
        if (this.hasLetClause) {
            Iterator<ForOrLetObject> letCIter = this.letC.iterator();
            while (letCIter.hasNext()) {
                ForOrLetObject let = letCIter.next();
                GraphObject graphObj = let.getGraphObject();
                boolean typesMatch = graphObj.checkQueryTypes();
                if (!typesMatch) {
                    return false;
                }
            }
            Iterator<ForOrLetObject> forCIter = this.forC.iterator();
            while (forCIter.hasNext()) {
                ForOrLetObject foR = forCIter.next();
                GraphObject graphObj = foR.getGraphObject();
                boolean typesMatch = graphObj.checkQueryTypes();
                if (!typesMatch) {
                    return false;
                }
            }
            return true;
        } else {
            Iterator<ForOrLetObject> forCIter = this.forC.iterator();
            while (forCIter.hasNext()) {
                ForOrLetObject foR = forCIter.next();
                GraphObject graphObj = foR.getGraphObject();
                boolean typesMatch = graphObj.checkQueryTypes();
                if (!typesMatch) {
                    return false;
                }
            }
            return true;
        }
    }

    // EVALUATION
    ////////////////////////////////////////////////////////////////////////////
    // STUB - TODO- assumes a basic Node or Edge Query with only a For Clause and a
    // Return Clause; - also, must 
    // include where clause
    public ArrayList evaluateQuery(ObjectContainer db, 
            ArrayList<String> invalidQueryNames) {
        ArrayList results;
        if (this.hasLetClause) {
            HashMap<String, LinkedList> letQueries = new HashMap();
            HashMap<String, LinkedList> forQueries = new HashMap();
            Iterator<ForOrLetObject> letIter = this.letC.iterator();
            while (letIter.hasNext()) {
                ForOrLetObject let = letIter.next();
                String letResultName = let.getVariable();
                GraphObject graphObj = let.getGraphObject();
                LinkedList queryResults = graphObj.evaluateQuery(db);
                letQueries.put(letResultName, queryResults);
                ////////////////////////////////////////////////////////////////
                /*Iterator<ObjectSet> objSetIter = queryResults.iterator();
                while(objSetIter.hasNext()) {
                    ObjectSet result = objSetIter.next();
                    System.out.println(result);
                }*/
            }
            Iterator<ForOrLetObject> forIter = this.forC.iterator();
            while (forIter.hasNext()) {
                ForOrLetObject foR = forIter.next();
                GraphObject graphObj = foR.getGraphObject();
                if (graphObj.isNode() || graphObj.isEdge()) {
                  NodeOrEdgeObject nodeOrEdge = graphObj.getNodeOrEdge();
                  String forResultName = nodeOrEdge.getHeadArgument().getArgName();
                  if (letQueries.containsKey(forResultName)) {
                      // Switch hash key name so it will match with result request
                      LinkedList letQueriesList = letQueries.get(forResultName);
                      forResultName = foR.getVariable();
                      forQueries.put(forResultName, letQueriesList);
                  } else {
                      forResultName = foR.getVariable();
                      LinkedList queryResults = graphObj.evaluateQuery(db);
                      forQueries.put(forResultName, queryResults);
                  }
                } else if (graphObj.isPath()) {
                    SimplePath path = graphObj.getPath();
                    String forResultName = path.getHeadArgument().getArgName();
                    if (letQueries.containsKey(forResultName)) {
                    // Switch hash key name so it will match with result request
                        LinkedList letQueriesList = letQueries.get(forResultName);
                        forResultName = foR.getVariable();
                        forQueries.put(forResultName, letQueriesList);
                    } else {
                        forResultName = foR.getVariable();
                        LinkedList queryResults = graphObj.evaluateQuery(db);
                        forQueries.put(forResultName, queryResults);
                    }
                }
                // TODO - For now we will not allow further manipulation of object
                // sets which are defined in a let clause; either all query 
                // stipulations are defined in the let clause, or all query 
                // stipulations are defined in the for clause
                ////////////////////////////////////////////////////////////////
                /*Iterator<ObjectSet> objSetIter = queryResults.iterator();
                while(objSetIter.hasNext()) {
                    ObjectSet result = objSetIter.next();
                    System.out.println(result);
                }*/
            }
            results = this.returnResults(forQueries, this.returnC, 
                    invalidQueryNames);
        } else {
            HashMap<String, LinkedList> queries = new HashMap();
            Iterator<ForOrLetObject> forIter = this.forC.iterator();
            while (forIter.hasNext()) {
                ForOrLetObject foR = forIter.next();
                String forResultName = foR.getVariable();
                GraphObject graphObj = foR.getGraphObject();
                LinkedList queryResults = graphObj.evaluateQuery(db);
                queries.put(forResultName, queryResults);
            } 
            results = this.returnResults(queries, this.returnC, 
                    invalidQueryNames);
        }
        return results;
    }
    
    private ArrayList returnResults(HashMap<String, 
            LinkedList> queryResults, LinkedList<Argument> returnC, 
            ArrayList<String> invalidQueries) {
        ListIterator<Argument> returnIter = returnC.listIterator();
        ArrayList results = new ArrayList();
        while (returnIter.hasNext()) {
            Argument setToReturn = returnIter.next();
            String setToReturnName = setToReturn.getArgName();
            if (queryResults.containsKey(setToReturnName)) {
                results.addAll(queryResults.get(setToReturnName));
            } else {
                invalidQueries.add(setToReturnName);
            }
        }
        return results;
    }
    
    /*private ArrayList returnNoLetClauseResults(HashMap<String, LinkedList> 
            forQueries, LinkedList<Argument> returnC, ArrayList<String> invalidQueries) {
        ListIterator<Argument> returnIter = returnC.listIterator();
        ArrayList results = new ArrayList();
        while (returnIter.hasNext()) {
            Argument setToReturn = returnIter.next();
            String setToReturnName = setToReturn.getArgName();
            if (forQueries.containsKey(setToReturnName)) {
                results.addAll(forQueries.get(setToReturnName));
            } else {
                invalidQueries.add(setToReturnName);
            }
        }
        return results;
    }
    
    private ArrayList returnLetClauseResults(HashMap<String, LinkedList>
            forQueries, LinkedList<Argument> returnC, ArrayList<String> invalidQueries, 
            String returnArg) {
        ListIterator<Argument> returnIter = returnC.listIterator();
        ArrayList results = new ArrayList();
        while (returnIter.hasNext()) {
            Argument setToReturn = returnIter.next();
            String setToReturnName = setToReturn.getArgName();
            if (for)
            
        }
        
    }*/
}
