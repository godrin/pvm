/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java;

import java.util.ArrayList;
import java.util.List;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class VM {

    public VM() {
        mContexts = new ArrayList<VMContext>();
    }

    synchronized VMContext createContext() {
        VMContext c = new VMContext(this);
        mContexts.add(c);
        return c;
    }
    List<VMContext> mContexts;
}
