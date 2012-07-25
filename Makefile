GQL.class:							GQL.java Query.class parser.class Lexer.class
	javac GQL.java
Query.class:					    Query.java ForOrLetObject.class Condition.class Argument.class ComparableClass.class \
									Node.class Edge.class SimplePath.class
	javac Query.java
ForOrLetObject.class:				ForOrLetObject.java GraphObject.class
	javac ForOrLetObject.java
GraphObject.class:					GraphObject.java NodeOrEdgeObject.class SimplePath.class
	javac GraphObject.java
SimplePath.class:					SimplePath.java NodeOrEdgeObject.class
	javac SimplePath.java
NodeOrEdgeObject.class:				NodeOrEdgeObject.java Predicate.class
	javac NodeOrEdgeObject.java
Predicate.class:					Predicate.java Condition.class
	javac Predicate.java
Condition.class:					Condition.java Argument.class
	javac Condition.java
Argument.class:						Argument.java
	javac Argument.java
Lexer.class:						Lexer.java sym.class
	javac Lexer.java
Lexer.java:							GQL.flex
	java JFlex.Main GQL.flex
sym.class:							sym.java
	javac sym.java
sym.java:							GQL.cup
	java java_cup.Main GQL.cup
parser.class:						parser.java
	javac parser.java
parser.java:						GQL.cup
	java java_cup.Main GQL.cup
ComparableClass.class:				ComparableClass.java
	javac ComparableClass.java
Node.class:							Node.java
	javac Node.java
Edge.class: 						Edge.java
	javac Edge.java
SimplePath.class:					SimplePath.java
	javac SimplePath.java
clean:	
	rm Lexer.java Lexer.java~ sym.java parser.java *.class 
