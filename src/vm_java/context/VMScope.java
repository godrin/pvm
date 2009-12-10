/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

import vm_java.code.CodeStatement;
import vm_java.code.UserFunction;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.internal.VMLog;
import vm_java.pruby.Authorizations;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.Buildin;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.basic.VMException;
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
		} catch (VMInternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public VMScope(VMScope scope, VMObject object) throws VMInternalException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = object;
		selfModule = object.getKlass();

		deriveCapitals();
	}

	public VMScope(VMScope scope, VMModule module) throws VMInternalException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = null;
		selfModule = module;

		deriveCapitals();
	}

	public VMScope(VMScope scope, UserFunction userFunction)
			throws VMInternalException {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = null; // scope.selfObject;
		selfModule = null; // scope.selfModule;

		deriveCapitals();
	}

	private void deriveCapitals() throws VMInternalException {
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
			throws VMInternalException {

		if (objectName == null)
			throw new VMInternalException(null, "ojectName is null");

		if (SELF.equals(objectName.getName()))
			throw new VMInternalException(null, "Self accessed!");

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
			SourceInfo sourceInfo) throws VMExceptionOutOfMemory,
			VMInternalException {
		ObjectName iName = getContext().intern(name);

		VMLog.debug("create exception:" + name + "  " + comment);

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

	public VMKlass getExceptionKlass() throws VMInternalException {
		BasicObject bo = getContext().getBuildinType(
				VMException.class.getName());
		if (!(bo instanceof VMKlass)) {
			throw new VMInternalException(new TypeCheckError(
					"bo is not of type VMKlass", bo));
		}
		return (VMKlass) bo;
	}

	public VMObject basicException(SourceInfo sourceInfo, String name)
			throws VMInternalException, VMExceptionOutOfMemory {
		if (name == null)
			name = "RuntimeError";
		// first try to get the class "name" and check if it's an exception
		// klass

		VMKlass basicExceptionKlass = getExceptionKlass();
		BasicObject klassObject = get(getContext().intern(name));
		VMKlass exceptionKlass = null;
		if (klassObject instanceof VMKlass) {
			exceptionKlass = (VMKlass) klassObject;
			BasicObject bo = getContext().getBuildinType(
					VMException.class.getName());

			if (exceptionKlass.isSubClassOf(basicExceptionKlass)) {
				// ok
			} else {
				exceptionKlass = null;
			}

		}
		if (exceptionKlass == null) {
			// not ok, so create an object from VMException directly
			BasicObject bo = getContext().getBuildinType(
					VMException.class.getName());
			if (!(bo instanceof VMKlass)) {
				throw new VMInternalException(new TypeCheckError(
						"bo is not of type VMKlass", bo));
			}
			exceptionKlass = (VMKlass) bo;
		}
		VMObject vmo = exceptionKlass._new(getContext());
		// vmo.putInstance(getContext().intern("what"), new
		// VMString(getContext(),
		// comment));
		VMArray stacktrace = new VMArray(getContext());
		stacktrace.add(new VMString(getContext(), sourceInfo.toString()));
		vmo.putInstance(getContext().intern("where"), stacktrace);
		return vmo;
	}

	public BasicObject encloseInException(SourceInfo sourceInfo,
			BasicObject object) throws VMInternalException, VMExceptionOutOfMemory {
		if (!object.is_a(getExceptionKlass())) {
			VMObject vmo = basicException(sourceInfo, "RuntimeError");
			vmo.putInstance(getContext().intern("what"), object);
			object = vmo;

		}
		return object;
	}
}
