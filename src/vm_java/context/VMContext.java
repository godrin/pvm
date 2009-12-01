/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vm_java.VM;
import vm_java.code.CodeBlock;
import vm_java.code.Program;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.internal.VMLog;
import vm_java.pruby.Authorizations;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class VMContext {
	// FIXME : organize VMContext in hierarchy

	private final long MAX_MEMORY = 1024 * 1024;

	// TODO: maybe eleminate this array ???
	// so that GC does collect !!!
	private List<BasicObject> mObjects;
	private VM mVM;
	private long mMaxMemory;
	private long mLastCheckedMemorySize;
	private float mCpuTime;
	long lastId = 0;
	private List<SourcePath> sourcePaths = new ArrayList<SourcePath>();
	private Map<String,BasicObject> buildinTypes=new TreeMap<String,BasicObject>();

	public VMContext(VM pVM) {
		mVM = pVM;
		mObjects = new ArrayList<BasicObject>();
		mMaxMemory = MAX_MEMORY;
		mLastCheckedMemorySize = 0;
		mCpuTime = 0;
	}

	public CodeBlock createBlock(SourceInfo source)
			throws VMExceptionOutOfMemory {
		CodeBlock b = new CodeBlock(this, source);
		add(b);
		return b;
	}

	public Program createProgram(CodeBlock pBlock)
			throws VMExceptionOutOfMemory {
		Program p = new Program(this, pBlock);
		add(p);
		return p;
	}
	
	public void setBuildinType(String buildInKlassName,BasicObject klass) {
		buildinTypes.put(buildInKlassName,klass);
	}
	public BasicObject getBuildinType(String buildInKlassName) {
		return buildinTypes.get(buildInKlassName);
	}

	synchronized void add(BasicObject pObject) throws VMExceptionOutOfMemory {
		if (mLastCheckedMemorySize + pObject.getMemoryUsage() > mMaxMemory) {
			throw new VMExceptionOutOfMemory();
		}
		mObjects.add(pObject);
	}

	public VMScope createScope(Authorizations authorizations) throws VMExceptionOutOfMemory {
		return new VMScope(this,authorizations);
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
		this.mCpuTime += time;
	}

	public long getNewID() {
		return ++lastId;
	}

	public void addPath(SourcePath sourcePath) {
		sourcePaths.add(sourcePath);
	}

	public String loadSource(String filename) {
		for (SourcePath p : sourcePaths) {
			String c;
			try {
				c = p.getContent(filename);
				if (c != null)
					return c;
			} catch (IOException e) {
				VMLog.error(e);
			}
		}
		return null;
	}

}
