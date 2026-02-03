package fa.dfa;

import fa.State;

import java.util.*;

public class DFA implements DFAInterface {

    // Alphabet, All states, start state, final states, transitions
    private List<String> alphabet;
    private List<State> states;
    private State start;
    private List<State> finalStates;
    private State[][] transitions;

    private State currentState;

    public DFA() {
        this(
                new ArrayList<String>(),
                new ArrayList<State>(),
                new DFAState(""),
                new ArrayList<State>(),
                new State[0][0]
        );
    }

    public DFA(List<String> alphabet, List<State> states, State start, List<State> finalStates, State[][] transitions) {

        this.alphabet = alphabet;
        this.states = states;
        this.start = start;
        this.finalStates = finalStates;
        this.transitions = transitions;

        // will initialize starter vars

        transitions = new State[states.size()][alphabet.size()];
        this.currentState = start;

    }


    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        if (!states.contains(fromState) || !states.contains(toState) || !alphabet.contains(onSymb)) {
            // handle error
            return false;
        } else {
            int fromStateIndex = states.indexOf(fromState);
            int symbIndex = alphabet.indexOf(onSymb);

            if (this.transitions[fromStateIndex][symbIndex] != null) {
                System.err.println("OVERRIDING EXISTING VALUE!");
            }
            this.transitions[fromStateIndex][symbIndex] = new DFAState(toState);
        }
        return true;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        return null;
    }

    @Override
    public boolean addState(String name) {
        return false;
    }

    @Override
    public boolean setFinal(String name) {
        return false;
    }

    @Override
    public boolean setStart(String name) {
        return false;
    }

    @Override
    public void addSigma(char symbol) {

    }

    @Override
    public boolean accepts(String s) {
        return false;
    }

    @Override
    public Set<Character> getSigma() {
        return Set.of();
    }

    @Override
    public State getState(String name) {
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        return false;
    }

    @Override
    public boolean isStart(String name) {
        return false;
    }

    private State transition(State current, String character) {

        return tTable[states.indexOf(current)][alphabet.indexOf(character)];

    }
}
