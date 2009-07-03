/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import vm_java.code.CodeStatement;
import vm_java.code.UserFunction;
import vm_java.code.VMException;
import vm_java.runtime.MemberFunction;
import vm_java.types.Buildin;
import vm_java.types.Function;
import vm_java.types.Module;
import vm_java.types.ObjectName;
import vm_java.types.Reference;
import vm_java.types.VMObject;

/**
 * 
 * @author davidkamphausen
 */
public class VMScope {
	Map<ObjectName, BasicObject> mReferences = new TreeMap<ObjectName, BasicObject>();
	VMScope mParentScope;
	VMContext mContext;
	Module selfModule = null;
	VMObject selfObject = null;
	BasicObject currentException = null;
	boolean returned = false;

	public final static String SELF = "nv_self";

	public VMScope(VMContext pContext) throws VMExceptionOutOfMemory {
		mContext = pContext;
		selfModule = new Module(mContext);

		try {
			Buildin.createBuildins(this);
		} catch (VMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// FIXME: get new self
	public VMScope(VMScope scope, VMObject object) {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = object;
		selfModule = object.getKlass();
	}

	public VMScope(VMScope scope, Module module) {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = null;
		selfModule = module;
	}

	public VMScope(VMScope scope, UserFunction userFunction) {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = scope.selfObject;
		selfModule = scope.selfModule;
	}

	public void setException(BasicObject bo) {
		currentException = bo;
	}

	public BasicObject getException() {
		return currentException;
	}



	public BasicObject get(ObjectName name) {
		BasicObject ret = null;

		if (SELF.equals(name.getName()))
			return self();

		ret = mReferences.get(name);

		if (ret == null) {
			if (selfObject != null) {
				ret = selfObject.get(name);
			}
			if (ret == null) {
				if (selfModule != null) {
					ret = selfModule.get(name);
				}
			}

		}
		return ret;
	}

	public BasicObject self() {
		if (selfObject != null)
			return selfObject;
		else
			return selfModule;
	}

	/*
	 * public void setArgs(Map<String, BasicObject> pArgs) { mArgs = pArgs; }
	 */
	public VMContext getContext() {
		return mContext;
	}

	public CodeStatement getStatement() {
		return null; // FIXME
	}

	/*
	 * public Map<String, BasicObject> getFuncCallArgs() { return mArgs; }
	 */
	public void put(ObjectName objectName, BasicObject value)
			throws VMException {
		
		if(objectName==null)
			throw new VMException(null,"ojectName is null");

		if (SELF.equals(objectName.getName()))
			throw new VMException(null, "Self accessed!");

		BasicObject old = mReferences.get(objectName);
		if (old instanceof Reference) {
			((Reference) old).set(value);
			setReturned();
		} else {
			mReferences.put(objectName, value);
		}
	}

	public MemberFunction getFunction(ObjectName methodName) throws VMException {
		Function f = (Function) get(methodName);

		if (selfObject == null)
			return new MemberFunction(selfModule, f);
		else
			return new MemberFunction(selfObject, f);
	}

	public void clear(ObjectName name) {
		mReferences.remove(name);
	}

	public boolean finished() {
		return returned || currentException != null;
	}

	public void setReturned() {
		returned = true;
	}

	public String inspect() {
		StringBuilder sb=new StringBuilder();
		sb.append("[Scope:\n");
		for(Map.Entry<ObjectName, BasicObject> entry:mReferences.entrySet()) {
			sb.append("  "+entry.getKey().inspect()+" => "+entry.getValue().inspect());
		}
		sb.append("  returned:"+returned+"\n");
		sb.append("  exception:"+currentException.inspect()+"\n");
		sb.append("]\n");
		
		// TODO Auto-generated method stub
		return sb.toString();
	}


}
