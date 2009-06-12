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
import vm_java.code.VMException;
import vm_java.types.ObjectName;

/**
 *
 * @author davidkamphausen
 */
public class VMContext {

    private final long MAX_MEMORY = 1024 * 1024;
    private List<BasicObject> mObjects;
    private VM mVM;
    private long mMaxMemory;
    private long mLastCheckedMemorySize;

    public VMContext(VM pVM) {
        mVM = pVM;
        mObjects = new ArrayList<BasicObject>();
        mMaxMemory = MAX_MEMORY;
        mLastCheckedMemorySize = 0;
    }

    public Block createBlock() throws VMExceptionOutOfMemory {
        Block b = new Block(this);
        add(b);
        return b;
    }

    public Program createProgram(Block pBlock) throws VMExceptionOutOfMemory {
        Program p = new Program(this, pBlock);
        add(p);
        return p;
    }

    synchronized void add(BasicObject pObject) throws VMExceptionOutOfMemory {
        if (mLastCheckedMemorySize + pObject.getMemoryUsage() > mMaxMemory) {
            throw new VMExceptionOutOfMemory();
        }
        mObjects.add(pObject);
    }

    public VMScope createScope() {
        return new VMScope(this);
    }

	public long getMemoryUsage() {
		long s=0;
		for(BasicObject o:mObjects)
			s+=o.getMemoryUsage();
		return s;
	}

	public ObjectName intern(String string) throws VMExceptionOutOfMemory {
		ObjectName on=new ObjectName(this,string);
		add(on);
		return on;
	}

}
