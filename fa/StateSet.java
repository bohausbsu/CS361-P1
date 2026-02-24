package fa;

import java.util.HashSet;
import java.util.Iterator;

public class StateSet extends HashSet<State> {

    public StateSet() {
        super();
    }

    public State get(String name) {
            Iterator<State> iter = this.iterator();
            State curr;
            while(iter.hasNext()) {
                curr = iter.next();
                if (curr.getName().equals(name)) {
                    return curr;
                }
            }
            return null;
    }

    @Override
    public boolean contains(Object o) {
        Iterator<State> iter = this.iterator();
        State curr;
        if (o instanceof String) {
            while (iter.hasNext()) {
                curr = iter.next();
                if (curr.getName().equals(o)) {
                    return true;
                }
            }
            return false;
        } else if (o instanceof State) {
            while (iter.hasNext()) {
                curr = iter.next();
                if (curr.equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }


}
