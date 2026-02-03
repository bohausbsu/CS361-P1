package fa.dfa;

import fa.State;

public class DFAState extends State {

    public DFAState(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            return this.getName().equals(((State) obj).getName());
        } else if (obj instanceof String) {
            return this.getName().equals((String) obj);
        } else {
            return false;
        }
    }
}
