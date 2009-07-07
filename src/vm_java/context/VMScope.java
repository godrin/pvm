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
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.Buildin;
import vm_java.types.Function;
import vm_java.types.VMModule;
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
	VMModule selfModule = null;
	VMObject selfObject = null;
	BasicObject currentException = null;
	boolean returned = false;

	public final static String SELF = "nv_self";

	public VMScope(VMContext pContext) throws VMExceptionOutOfMemory {
		mContext = pContext;
		selfModule = new VMModule(mContext);

		try {
			Buildin.createBuildins(this);
		} catch (VMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public VMScope(VMScope scope, VMObject object) throws VMException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = object;
		selfModule = object.getKlass();

		deriveCapitals();
	}

	public VMScope(VMScope scope, VMModule module) throws VMException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = null;
		selfModule = module;

		deriveCapitals();
	}

	public VMScope(VMScope scope, UserFunction userFunction) throws VMException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = null; // scope.selfObject;
		selfModule = null; // scope.selfModule;

		deriveCapitals();
	}

	private void deriveCapitals() throws VMException {
		for (Entry<ObjectName, BasicObject> entry : mParentScope.mReferences
				.entrySet()) {
			if (entry.getKey().isCapital()) {
				put(entry.getKey(), entry.getValue());
			}
		}
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

		if (objectName == null)
			throw new VMException(null, "ojectName is null");

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

	public RuntimeMemberFunction getFunction(ObjectName methodName)
			throws VMException {
		Function f = (Function) get(methodName);

		if (selfObject == null)
			return new RuntimeMemberFunction(selfModule, f);
		else
			return new RuntimeMemberFunction(selfObject, f);
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
		StringBuilder sb = new StringBuilder();
		sb.append("[Scope:\n");
		for (Map.Entry<ObjectName, BasicObject> entry : mReferences.entrySet()) {
			sb.append("  " + entry.getKey().inspect() + " => "
					+ entry.getValue().inspect());
		}
		sb.append("  returned:" + returned + "\n");
		if (currentException != null)
			sb.append("  exception:" + currentException.inspect() + "\n");
		sb.append("]\n");

		// TODO Auto-generated method stub
		return sb.toString();
	}

}
