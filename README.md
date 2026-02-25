****************
* P1 / Deterministic Finite Automata
* CS361 Section 02 
* 2/24/25
* Bradlee Borgholthaus & Piper Worsham
****************
OVERVIEW:
This program includes a DFA library, and a demonstration test suite. 
INCLUDED FILES:
* 
e.g.
* Class1.java - source file
* Class2.java - source file
* README - this file
  COMPILING AND RUNNING:
  To compile the program in onyx you can use the command:
  javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java
  To run the program, you can use the command:
  java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/hamcrest.jar
  org.junit.runner.JUnitCore test.dfa.DFATest
  Console output will give the results after the program finishes.
  
  PROGRAM DESIGN AND IMPORTANT CONCEPTS:
  This is the sort of information someone who really wants to
  understand your program - possibly to make future enhancements -
  would want to know.
  Explain the main concepts and organization of your program so that
  the reader can understand how your program works. This is not a repeat
  of javadoc comments or an exhaustive listing of all methods, but an
  explanation of the critical algorithms and object interactions that make
  up the program.
  Explain the main responsibilities of the classes and interfaces that make
  up the program. Explain how the classes work together to achieve the
  program
  goals. If there are critical algorithms that a user should understand,
  explain them as well.
  If you were responsible for designing the program's classes and choosing
  how they work together, why did you design the program this way? What, if
  anything, could be improved?
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
  All sources used outside of lecture notes, slides, and the textbook need
  to
  be cited here. If you used websites, used GenAI, asked your dad or your
  boss
  or your roommate for help then you must cite those resources. I am not
  concerned if you use proper APA or MLA or another format as long as you
  include
  all relevant information. If it is a person or GenAI that you referenced,
  be
  sure to include who you talked to (or which AI you accessed), when you
  talked
  to them, and what help they provided (e.g. Student, Awesome. Private
  communication, 21 January 2026. Discussed how polymorphism allows the
  return
  types of methods implemented in a class to be different from the class
  specified
  in the interface as long as the <type in implementation> “is-a” <type in
  interface>.)
--------------------------------------------------------------------------
--
All content in a README file is expected to be written in clear English
with
proper grammar, spelling, and punctuation. If you are not a strong writer,
be sure to get someone else to help you with proofreading. Consider all
project
documentation to be professional writing for your boss and/or potential
customers.
