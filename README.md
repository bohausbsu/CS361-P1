****************
* P1 / Deterministic Finite Automata
* CS361 Section 02 
* 2/24/25
* Bradlee Borgholthaus & Piper Worsham
****************
OVERVIEW:
This program includes a DFA library, and a demonstration test suite. 
INCLUDED FILES:
* DFA.java - source file
* DFAInterface.java - source file
* DFAState.java - source file
* FAInterface.java - source file
* State.java - source file
* StateSet.java - source file
* DFATest.java - test file
* README - this file

  COMPILING AND RUNNING:
  To compile the program in onyx you can use the command:
  javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java
  To run the program, you can use the command:
  java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/hamcrest.jar
  org.junit.runner.JUnitCore test.dfa.DFATest
  Console output will give the results after the program finishes.

  
  PROGRAM DESIGN AND IMPORTANT CONCEPTS:

The central code to the library exists in the DFA.java file. This creates a DFA as a java object. The DFA has DFA 5-tuple style 
instance variables. The states in a FA exist as singletons, contained within the alphabet field. That is, any reference to a state within
an object will and should always reference the same State object in memory. The State objects are unique to a single DFA object, meaning
two state objects with the same name in distinct DFA objects will not be equal through the java.Object's .equal method. States can, however,
be referenced by name via String parameters in DFA methods. The methods will correlate to an internal State object (if exists) to carry out
transitions and other DFA tasks.
The transition logic is maintained within an internal class in DFA.java called TransitionTable. The table, instantiated within an encapsulating
DFA object, can access the singleton states of the DFA and maintains a transition table through a 2D map.
The State.java file is an abstract datatype that provides universal logic applicable to multiple finite automata. The DFAState.java file
is simply a concrete extension of this, and does not add any additional logic.
The StateSet is a data structure that is used to easily manage singleton states and string querying among a finite automata's states.
A potential area of expansion in this library is within the transition table. The transition logic is almost entirely abstracted from the
inner values of a State object. Fully abstracting the logic would allow for possibly any object to be used as states or transition symbols.
For a DFA representation, this might not have much use. However, more complex applications may find multifaceted axes on the transition table
to be an incredibly powerful, while relatively simple, state machine


  TESTING:
  We tested our program with 3 different DFAs all different sizes and transitions.
  We tested to make sure all of our "add", "set", and "get" functions were working
  properly by testing the expected output and what we actually got. Then lastly, we
  tested out swap function by comparing the expected with the given.
  Our program can handle bad input, if it is an invalid token it will cause the DFA
  to fail. We cannot say that this program is idiot-proof because we did not do
  further testing that what was provided.
  We do not have any known issues or bugs remaining.
  DISCUSSION:
  One issue we had was we were using a HashSet for our states and alphabet. This caused
  each of the sets to be sorted when they were supposed to be in the order they were added.
  We ended up using Java library documentation to find another set option that did not sort
  and that was LinkedHashSet.
  The other issue we had was when the inner-class, TransitionTable, was instantiated, it
  kept a reference to the object in which it was instantiated. Which meant creating a copy
  of it to put into a new DFA, through the swap method, referenced different state objects than
  those that exist in the new DFA. To get around this, we allowed a transition table to be
  passed into the constructor and used that transition table to create a new instance of a
  transition table that referenced the local state objects.
  We found it challenging to navigate a nested map and debugging the swap problem.
  One thing that clicked for us was using Java library documentation to find a solution
  to our project needs.
  
  EXTRA CREDIT:
  No extra credit.
  SOURCES:
* java.util.LinkedHashSet documentation

--------------------------------------------------------------------------
