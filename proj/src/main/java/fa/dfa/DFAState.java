package fa.dfa;

import fa.State;

public class DFAState extends State {

    public DFAState(String name) {
        super(name);
    }

//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof DFAState) {
//            return this.getName().equals(((DFAState) obj).getName());
//        } else if (obj instanceof String) {
//            return this.getName().equals(obj);
//        } else {
//            return false;
//        }
//    }
}
