/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.code.CodeBlock;
import vm_java.code.Program;
import vm_java.types.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class VMContext {

	private final long MAX_MEMORY = 1024 * 1024;

	// TODO: maybe eleminate this array ???
	// so that GC does collect !!!
	private List<BasicObject> mObjects;
	private VM mVM;
	private long mMaxMemory;
	private long mLastCheckedMemorySize;
	private float mCpuTime;

	public VMContext(VM pVM) {
		mVM = pVM;
		mObjects = new ArrayList<BasicObject>();
		mMaxMemory = MAX_MEMORY;
		mLastCheckedMemorySize = 0;
		mCpuTime = 0;
	}

	public CodeBlock createBlock() throws VMExceptionOutOfMemory {
		CodeBlock b = new CodeBlock(this);
		add(b);
		return b;
	}

	public Program createProgram(CodeBlock pBlock)
			throws VMExceptionOutOfMemory {
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

	public VMScope createScope() throws VMExceptionOutOfMemory {
		return new VMScope(this);
	}

	public long getMemoryUsage() {
		long s = 0;
		for (BasicObject o : mObjects)
			s += o.getMemoryUsage();
		return s;
	}

	public ObjectName intern(String string) throws VMExceptionOutOfMemory {
		if (string == null)
			return null;
		ObjectName on = new ObjectName(this, "nv_" + string);
		add(on);
		return on;
	}

	public ObjectName internal(String string) throws VMExceptionOutOfMemory {
		ObjectName on = new ObjectName(this, ":" + string);
		add(on);
		return on;
	}

	public VM getVM() {
		return mVM;
	}

	public void addTime(double time) {
		this.mCpuTime+=time;		
	}

}
