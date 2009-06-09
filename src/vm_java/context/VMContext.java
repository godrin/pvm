/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.ArrayList;
import java.util.List;
import vm_java.VM;
import vm_java.code.Block;
import vm_java.code.Program;

/**
 *
 * @author davidkamphausen
 */
public class VMContext {

    List<BasicObject> mObjects;
    VM mVM;

    public VMContext(VM pVM) {
        mVM = pVM;
        mObjects = new ArrayList<BasicObject>();
    }

    public Block createBlock() {
        Block b = new Block(this);
        mObjects.add(b);
        return b;
    }

    synchronized public Program createProgram(Block pBlock) {
        Program p = new Program(this, pBlock);
        mObjects.add(p);
        return p;
    }

    synchronized void add(BasicObject pObject) {
        mObjects.add(pObject);
    }

    public synchronized VMScope createScope()
            {
        return new VMScope(this, null);
    }
}
