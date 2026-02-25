package fa.dfa;

import fa.State;
import fa.StateSet;

import javax.print.DocFlavor;
import java.util.*;

public class DFA implements DFAInterface {

    private final Set<Character> alphabet;
    private final StateSet states;
    private State start;
    private final StateSet finalStates;
    private final TransitionTable transitionTable;
    /*
        2D Map:
        fromState -> <symb, toState>
     */

    private State currentState;

    public DFA() {
            this.alphabet = new LinkedHashSet<Character>();
            this.states = new StateSet();
            this.start = null;
            this.finalStates = new StateSet();
            this.transitionTable = new TransitionTable();
    }

    private DFA(Set<Character> sigma, StateSet Q, State q0, StateSet T, TransitionTable transitionTable) {
        this.alphabet = sigma;
        this.states = Q;
        this.start = q0;
        this.currentState = this.start;
        this.finalStates = T;
        // a new transition table must be made to correctly associate transitions to local states
        this.transitionTable = new TransitionTable(transitionTable);
    }

    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {

        // get local states by name
        State from = states.get(fromState);
        State to = states.get(toState);

        // confirm no invalid arguments
        if (fromState == null || toState == null || !alphabet.contains(onSymb)) {
            // illegal states or symbol
            return false;
        }

        // call transition table to manage transition
        return transitionTable.addTransition(from, onSymb, to);
    }

    @Override
    public DFA swap(char symb1, char symb2) {

        // create unique objects emulating existing 5 tuple for new DFA

        // sigma
        Set<Character> newSigma = new HashSet<>(alphabet);

        // Q
        StateSet newQ = new StateSet();
        for (State state : states) {
            newQ.add(new DFAState(state.getName()));
        }

        // q0
        State newQ0 = newQ.get(start.getName());

        // T
        StateSet newT = new StateSet();
        for (State state : finalStates) {
            newT.add(newQ.get(state.getName()));
        }

        // delta, with swapped transitions on symb1 and symb2
        TransitionTable temp = transitionTable.swap(symb1, symb2);
        return new DFA(newSigma, newQ, newQ0, newT, temp);

    }

    @Override
    public boolean addState(String name) {
        // confirm new state
        if (states.contains(name)) {
            return false;
        }
        // add new state to state set
        State newState = new DFAState(name);
        states.add(newState);
        // call to transition table to add new row
        return transitionTable.addState(newState);

    }

    @Override
    public boolean setFinal(String name) {
        // confirm valid state and add to final set if so
        State state = states.get(name);
        if (state != null) {
            finalStates.add(state);
            return true;
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        // confirm valid state and set start state if so
        State state = states.get(name);
        if (state != null) {
            start = state;
            currentState = start;
            return true;
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        // add symbol to sigma
        alphabet.add(symbol);
        // call transition table to add column
        transitionTable.addSymbol(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // use flag so that we can save terminating val
        boolean status = false;
        if (s.isEmpty()) {
            // terminate DFA and return acceptance state
            return terminate();
        }

        char currToken = s.charAt(0);
        if (!alphabet.contains(currToken)) {
            // restart DFA
            terminate();
            // illegal token, thus failure regardless of ending state.
            return false;
        }

        transition(currToken);
        if (currentState != null) {
            status = accepts(s.substring(1));
        }
        currentState = start;
        return status;
    }


    @Override
    public Set<Character> getSigma() {
        return alphabet;
    }

    @Override
    public State getState(String name) {
        return states.get(name);
    }

    @Override
    public boolean isFinal(String name) {
        State candidate = states.get(name);
        return finalStates.contains(candidate);
    }

    @Override
    public boolean isStart(String name) {
        State candidate = states.get(name);
        return start.equals(candidate);
    }

    /**
     * Checks to see if DFA is in valid finish state, and restarts DFA.
     * @return true if current state is valid, false otherwise
     */
    private boolean terminate() {
        boolean status = finalStates.contains(currentState);
        currentState = start;
        return status;
    }

    private boolean transition(char symb) {
        if (alphabet.contains(symb)) {
            currentState = transitionTable.getTransition(symb);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        //States
        StringBuilder q = new StringBuilder();
        //Alphabet
        StringBuilder sigma = new StringBuilder();
        //Final states
        StringBuilder f = new StringBuilder();
        //message
        StringBuilder message = new StringBuilder();

        q.append(" Q = { ");
        for (State state: states) {
            q.append(state + " ");
        }
        q.append("}\n");

        sigma.append("Sigma = { ");
        for (Character c: alphabet) {
            sigma.append(c +" ");
        }
        sigma.append("}\n");

        f.append("F = { ");
        for (State finalState: finalStates) {
            f.append(finalState +" ");
        }
        f.append("}");

        message.append(q);
        message.append(sigma);
        message.append("delta =\n" + transitionTable.toString());
        message.append("q0 = " + start + "\n");
        message.append(f);

        return message.toString();
    }

    /**
     * Represents a DFA transition table. Object must be instantiated
     * within related <code>DFA</code> to properly map to DFA's state
     * objects.
     */
    private class TransitionTable {

        private final Map<State, Map<Character, State>> table;

        public TransitionTable() {
            table = new HashMap<>();
        }

        private TransitionTable(TransitionTable source) {
            this.table = cloneFromTable(source);
        }

        /**
         * Adds the transition specified, given all arguments are valid members of encapsulating
         * DFA.
         * @param from source State
         * @param onSymb transition token
         * @param to destination State
         * @return true if transition was added, false if transition had illegal argument.
         */
        private boolean addTransition(State from, char onSymb, State to) {
            if (!states.contains(from) || !states.contains(to) || !alphabet.contains(onSymb)) {
                // illegal states or symbol
                return false;
            }
            Map<Character, State> transRow = table.get(from);
            if (transRow == null) {
                transRow = new HashMap<>();
                transRow.put(onSymb, to);
                table.put(from, transRow);
            } else {
                transRow.put(onSymb, to);
            }
            return true;
        }

        private boolean addState(State state) {
            Map<Character, State> transRow = new HashMap<>();
            for (Character c : alphabet) {
                transRow.put(c, null);
            }
            table.put(state, transRow);
            return true;
        }

        private boolean addSymbol(char symbol) {
            for (Map<Character, State> transRow : table.values()) {
                // method should return null unless value was overwritten
                if (transRow.put(symbol, null) != null) {
                    return false;
                };
            }
            return true;
        }

        /**
         * Returns transition on symbol from DFA's current state
         * @param symbol transition token
         * @return destination State
         */
        private State getTransition(char symbol) {
            return getTransition(currentState, symbol);
        }

        /**
         * Returns transition on symbol from provided DFA state
         * @param from State origin
         * @param symbol transition token
         * @return destination State
         */
        private State getTransition(State from, char symbol) {
            Map<Character, State> transRow = table.get(from);
            return transRow.get(symbol);
        }

        /**
         * Creates a new transition table where all transitions on <code>char1</code> instead
         * transition on <code>char2</code>, and vice versa
         * @param char1
         * @param char2
         * @return transformed transition table
         */
        private TransitionTable swap(char char1, char char2) {
            TransitionTable temp = new TransitionTable();
            for (State fromState : table.keySet()) {
                temp.addState(fromState);
                for (Character symb : alphabet) {
                    State toState = getTransition(fromState, symb);
                    if (symb.equals(char1)) {
                        temp.addTransition(fromState, char2, toState);
                    } else if (symb.equals(char2)) {
                        temp.addTransition(fromState, char1, toState);
                    } else {
                        temp.addTransition(fromState, symb, toState);
                    }
                }
            }
            return temp;
        }

        /**
         * Clones the transitions from another DFA to reference encapsulating instance's <code>State</code> objects,
         * contained in the <code>alphabet</code> field.
         * @param externalTable table with transitions to replicate
         * @return data structure for a new transition table object
         */
        private Map<State, Map<Character, State>> cloneFromTable(TransitionTable externalTable) {
            Map<State, Map<Character, State>> local = new HashMap<>();
            Map<State, Map<Character, State>> extTab = externalTable.table;
            for (State fromState : extTab.keySet()) {
                Map<Character, State> transRow = extTab.get(fromState);
                Map<Character, State> newRow = new HashMap<>();
                for(Character symb : transRow.keySet()) {
                    // gets external dest state
                    State extToState = transRow.get(symb);
                    // find local equivalent
                    State localToState = null;
                    if (extToState != null) {
                        localToState = states.get(extToState.getName());
                        }
                    newRow.put(symb, localToState);
                }
                State localFromState = states.get(fromState.toString());
                local.put(localFromState, newRow);
            }
            return local;
        }

        @Override
        public String toString() {
            StringBuilder tt = new StringBuilder();
            StringBuilder q = new StringBuilder();
            q.append("		");
            for (Character c: alphabet) {
                q.append(c + "	");
            }
            q.append("\n");

            StringBuilder a = new StringBuilder();
            a.append("	");

            for (State state: states) {
                a.append(state + "	");
                for (Character c: alphabet) {
                    State temp = getTransition(state, c);
                    a.append(temp + "	");
                }
                a.append("\n");
            }
            tt.append(q);
            tt.append(a);

            return tt.toString();
        }
    }
}


