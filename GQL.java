package gqlMain;

import java.io.*;
import java.util.*;
import com.db4o.*;

public class GQL {
    ////////////////////////////////////////////////////////////////////////////
    // GLOBAL VARIABLES
    ////////////////////////////////////////////////////////////////////////////
    private static ObjectSet<Object> dbObjects;
    private static LinkedList<ComparableClass> classes = new LinkedList();
    private static String dbPath, packageName;
    private static ObjectContainer db;
    //private static ClassLoader coreClassLoader = 
            //ClassLoader.getSystemClassLoader();
    //private static ClassLoader subClassLoader = 
            //ClassLoader.getSystemClassLoader();
    
    public static void main(String[] args) {
        ////////////////////////////////////////////////////////////////////////
        // CREATE DATABASE OBJECT
        ////////////////////////////////////////////////////////////////////////
        // If no path to a database is provided on the command line, print
        // error and exit program
        if (args.length < 1) {
           System.err.println("\nError: no database path provided.\n");
           return;
        } else if (args.length > 1) {
           dbPath = args[0];
           // TODO - dubug command line classpath
           packageName = args[1];
           db = Db4o.openFile(dbPath);
        } else {     // We assume that the database Classes are stored somewhere
           dbPath = args[0];    // along the CLASSPATH, and therefore classPath
           packageName = "";      // can be left empty
           db = Db4o.openFile(dbPath);
        }

        System.out.print("GQL> ");

        // The prompt of the interpreter is within a do-while loop, which can
        // be terminated by entering "exit"
        do {
           try {

               /////////////////////////////////////////////////////////////////
               // READ IN QUERY FILE
               /////////////////////////////////////////////////////////////////
               // We create a Scanner object to read tokens from the standard in
               // stream - these will be our DLOG files provided by the user
               Scanner fileScanner = new Scanner(System.in); 
               String GQLFile = fileScanner.next();

               // Break loop and exit program if user enters "exit"
               if (GQLFile.equalsIgnoreCase("exit")) {
                   break;
                    
               // If the user input is not preceeded by "@" and teminated with 
               // ";" then the input is invalid - the user is prompted again
               } else if (!(GQLFile.substring(0,1).equals("@")) || 
                           !(GQLFile.substring(GQLFile.length()-1, 
                               GQLFile.length()).equals(";"))) {

                   System.out.println("\nInvalid input.\nUsage:     "
                           + " @filename;\n");
                   System.out.print("GQL> ");
                   continue;

               } else { 

                   // Parse out the "@" and ";" from the user's input and send 
                   // this to a file Reader object
                   GQLFile = GQLFile.substring(1,GQLFile.length()-1);
               }

               // Now we create a reader object and give it the user's parsed 
               // input - in the event of a FileNotFoundException, the user is 
               // prompted again
               Reader reader;
               try {
                   reader = new FileReader(GQLFile);
               } catch (FileNotFoundException e) {
                   System.out.println("\nFile " + GQLFile + 
                           " does not exist.\n");
                   System.out.print("GQL> ");
                   continue;
               }

               /////////////////////////////////////////////////////////////////
               // PARSE QUERY
               /////////////////////////////////////////////////////////////////
               // The parser and Lexer objects are created in the parser.java 
               // and Lexer.java files, respectively - The parser takes the 
               // Lexer as an argument - the value variable generated by the
               // parse() method will return the topmost grammar construct, 
               // which in this case is a Query object
               parser p = new parser(new Lexer(reader));                       

               Query query = (Query) p.parse().value;
               /////////////////////////////////////////////////////////////////
               System.out.println("\n----------------------------Input Query-----" +
                       "-----------------------\n");

               System.out.println("\n                        SUCCESSFUL PARSE   " +
                       "                       \n");

               System.out.println("--------------------------------------" +
                       "------------------------------\n");
               /////////////////////////////////////////////////////////////////

               /////////////////////////////////////////////////////////////////
               // LOAD ALL CLASSES USED IN DATABASE INTO RUNTIME
               /////////////////////////////////////////////////////////////////
               // databse Classes should be kept on the CLASSPATH, or the path
               // to these classes should be provided as a second command line
               // argument
               /*boolean coreClassesLoaded = loadCoreClasses(coreClassLoader, 
                       classPath);
               if (!coreClassesLoaded) {
                   System.err.println("\nError: one or more of core Classes"
                           + "Node, Egge and SimplePath could not be found.\n");
                   db.close();
                   return;
               }
               // 
               System.out.println("Core classes loaded.\n");
               boolean subclassesLoaded = query.loadSubclasses(subClassLoader,
                       classPath);
               if (!subclassesLoaded) {
                   System.err.println("\nError: subclasses could not be" +
                           " loaded.\n");
                   db.close();
                   return;
               }
               //
               System.out.println("Subclasses loaded.\n");*/


               /////////////////////////////////////////////////////////////////
               // MAKE SURE THE DATABASE ACTUALLY CONTAINS SOME OBJECTS AND,
               // IF SO, PUT AN INSTANCE OF EACH CLASS REPRESENTED INTO THE 
               // LINKEDLIST CLASSLIST - SINCE WE LOADED THE DATABASE CLASSES
               // INTO THE RUNTIME ENVIRONMENT, OBJECTS RETURNED BY DATABASE
               // QUERIES WILL REMAIN TRUE TO THEIR TYPE; IF WE HADN'T DONE
               // THIS, THESE OBJECTS WOULD BE RETURNED AS TYPE GENERICOBJECT
               /////////////////////////////////////////////////////////////////
               dbObjects = db.queryByExample(Object.class);
               if (dbObjects.hasNext()) {
                  query.addClassesToList(dbObjects, classes);
               } else {
                   System.err.println("\nError: no objects in database.\n");
                   db.close();
                   return;
               }
               /////////////////////////////////////////////////////////////////
               //System.out.println(classes);

               /////////////////////////////////////////////////////////////////
               // SEMANTIC CHECKS                                             //
               /////////////////////////////////////////////////////////////////
               boolean headArgsAreSet = query.setHeadArgClasses(classes);     
               if (!headArgsAreSet) {                                         
                   db.close();
                   return;                                                    
               }                                                              
               boolean typesMatch = query.checkQueryTypes(db);          
               if (!typesMatch) {                                             
                   db.close();
                   return;
               }               
               
               /////////////////////////////////////////////////////////////////
               // EVALUATION
               /////////////////////////////////////////////////////////////////
               ArrayList<String> invalidQueries = new ArrayList();
               ArrayList results = query.evaluateQuery(db, 
                       invalidQueries);
               
               /////////////////////////////////////////////////////////////////
               // PRINT RESULTS
               /////////////////////////////////////////////////////////////////
               Iterator dbObjIter = results.iterator();
               ListIterator invalidQueryIter = invalidQueries.listIterator();
               while (invalidQueryIter.hasNext()) {
                   System.out.println("\nError: database object set '" +
                           invalidQueryIter.next() + "' not defined.\n");
               }
               while (dbObjIter.hasNext()) {
                   Object someQueryResult = dbObjIter.next();
                   System.out.println("\n" + someQueryResult);
               }
           } catch (Exception e) {
               System.out.println("\nSYNTAX ERROR\n");
               e.printStackTrace();
           }

           System.out.print("GQL> ");
        } while (true); 

        System.out.println("\nExiting...\n");
        db.close();
    }
    
    private static boolean loadCoreClasses(ClassLoader coreClassLoader, 
            String classPath) {
        try {
            coreClassLoader.loadClass("Node");
            coreClassLoader.loadClass("Edge");
            coreClassLoader.loadClass("SimplePath");
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
        return true;
    }
}