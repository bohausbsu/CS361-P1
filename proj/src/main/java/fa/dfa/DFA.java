package fa.dfa;

import fa.State;
import fa.StateSet;

import java.util.*;

public class DFA implements DFAInterface {




    // Alphabet, All states, start state, final states, transitions
    private final Set<Character> alphabet;
    private final StateSet states;
    private State start;
    private final StateSet finalStates;
//    private Map<String, Map<Character, State>> transitions;
    private final TransitionTable transitionTable;
    /*
        2D Map:
        fromState -> <symb, toState>


     */

    private State currentState;

    public DFA() {
            this.alphabet = new HashSet<Character>();
            this.states = new StateSet();
            this.start = new DFAState("");
            this. finalStates = new StateSet();
            this.transitionTable = new TransitionTable();
    }

    private DFA(Set<Character> sigma, StateSet Q, State q0, StateSet T, TransitionTable transitionTable) {
        this.alphabet = sigma;
        this.states = Q;
        this.start = q0;
        this.currentState = this.start;
        this.finalStates = T;
        // a new transition table must be made to correctly associate inner object
        this.transitionTable = new TransitionTable(transitionTable);
    }

    // TODO: Convert to using new StateSet
    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {

        State from = states.get(fromState);
        State to = states.get(toState);

        if (fromState == null || toState == null || !alphabet.contains(onSymb)) {
            // illegal states or symbol
            return false;
        }
            return transitionTable.addTransition(from, onSymb, to);
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        Set<Character> newSigma = new HashSet<>(alphabet);

        StateSet newQ = new StateSet();
        for (State state : states) {
            newQ.add(new DFAState(state.getName()));
        }

        State newQ0 = newQ.get(start.getName());

        StateSet newT = new StateSet();
        for (State state : finalStates) {
            newT.add(newQ.get(state.getName()));
        }

        DFA tempDFA = new DFA();

        TransitionTable temp = transitionTable.swap(symb1, symb2);
        return new DFA(newSigma, newQ, newQ0, newT, temp);

    }

    @Override
    public boolean addState(String name) {
        if (states.contains(name)) {
            return false;
        }
        State newState = new DFAState(name);
        states.add(newState);
        return transitionTable.addState(newState);

    }

    @Override
    public boolean setFinal(String name) {
        State state = states.get(name);
        if (state != null) {
            finalStates.add(state);
            return true;
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
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
        alphabet.add(symbol);
        transitionTable.addSymbol(symbol);
    }

    @Override
    public boolean accepts(String s) {
        boolean status = false;
        if (s.isEmpty()) {
            status = finalStates.contains(currentState);
            currentState = start;
            return status;
        }

        char currToken = s.charAt(0);
        if (!alphabet.contains(currToken)) {
            currentState = start;
            return status;
        }
        currentState = this.transitionTable.getTransition(currToken);
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

//    private Map<Character, State> getCurrentTransitions() {
//        return transitions.get(currentState);
//    }

    private class TransitionTable {

        private final Map<State, Map<Character, State>> table;

        public TransitionTable() {
            table = new HashMap<>();
        }

        private TransitionTable(TransitionTable source) {
            this.table = cloneFromTable(source);
        }

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
                transRow.put(symbol, null);
            }
            return true;
        }

        private State getTransition(char symbol) {
            return getTransition(currentState, symbol);
        }

        private State getTransition(State from, char symbol) {
            Map<Character, State> transRow = table.get(from);
            return transRow.get(symbol);
        }

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

        // used to create a trans table with local variable reference
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
            // TODO
            return "";
        }
    }
}


