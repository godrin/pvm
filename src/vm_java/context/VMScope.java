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
import vm_java.code.SourceBased.SourceInfo;
import vm_java.pruby.Authorizations;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.Buildin;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.basic.VMKlass;
import vm_java.types.basic.VMModule;
import vm_java.types.basic.VMObject;
import vm_java.types.foundation.VMArray;
import vm_java.types.foundation.VMString;

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

	public final static String SELF = "nv_self";

	public VMScope(VMContext pContext, Authorizations authorizations)
			throws VMExceptionOutOfMemory {
		mContext = pContext;
		selfModule = new VMModule(mContext);
		selfModule.setName("root");

		try {
			Buildin.createBuildins(this, authorizations);
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

	public BasicObject get(ObjectName name, boolean mstatic) {
		BasicObject ret = null;
		if (mstatic) {
			if (selfObject != null) {
				ret = selfObject.getStatic(name);
			}
			if (ret == null) {
				if (selfModule != null) {
					ret = selfModule.getStatic(name);
				}
			}
		} else {
			return get(name);
		}
		return ret;
	}

	public BasicObject get(ObjectName name) {
		BasicObject ret = null;

		if (SELF.equals(name.getName()))
			return self();

		ret = mReferences.get(name);

		if (ret == null) {
			if (selfObject != null) {
				ret = selfObject.getStatic(name);
			}
			if (ret == null) {
				if (selfModule != null) {
					ret = selfModule.getStatic(name);
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

	public void put(ObjectName objectName, BasicObject value)
			throws VMException {

		if (objectName == null)
			throw new VMException(null, "ojectName is null");

		if (SELF.equals(objectName.getName()))
			throw new VMException(null, "Self accessed!");

		mReferences.put(objectName, value);
	}

	public RuntimeMemberFunction getFunction(ObjectName methodName,
			boolean mstatic) throws VMExceptionFunctionNotFound {
		Function f = (Function) get(methodName, mstatic);

		if (selfObject == null)
			return new RuntimeMemberFunction(selfModule, f);
		else
			return new RuntimeMemberFunction(selfObject, f);
	}

	public void clear(ObjectName name) {
		mReferences.remove(name);
	}

	/*
	 * public boolean finished() { return returned != null; }
	 */

	public String inspect() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Scope:\n");
		for (Map.Entry<ObjectName, BasicObject> entry : mReferences.entrySet()) {
			sb.append("  " + entry.getKey().inspect() + " => "
					+ entry.getValue().inspect());
		}
		sb.append("]\n");

		return sb.toString();
	}

	public BasicObject exception(String name, String comment,
			SourceInfo sourceInfo) throws VMExceptionOutOfMemory, VMException {
		ObjectName iName = getContext().intern(name);

		BasicObject bo = get(iName);
		VMKlass klass = null;
		if (bo instanceof VMKlass)
			klass = (VMKlass) bo;
		if (klass == null) {
			klass = new VMKlass(getContext());
		}
		put(iName, klass);
		VMObject vmo = klass._new(getContext());
		vmo.putInstance(getContext().intern("what"), new VMString(getContext(),
				comment));
		VMArray stacktrace = new VMArray(getContext());
		stacktrace.add(new VMString(getContext(), sourceInfo.toString()));
		vmo.putInstance(getContext().intern("where"), stacktrace);
		return vmo;
	}
}
