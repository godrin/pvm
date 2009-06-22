/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vm_java.runtime.MemberFunction;
import vm_java.types.Function;
import vm_java.types.Module;
import vm_java.types.VMObject;
import vm_java.code.CodeStatement;
import vm_java.code.FunctionCall;
import vm_java.code.UserFunction;
import vm_java.code.VMException;
import vm_java.code.lib.StdIO;
import vm_java.code.lib.VMPackage;
import vm_java.types.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class VMScope {
	Map<ObjectName, BasicObject> mReferences = new TreeMap<ObjectName, BasicObject>();
	VMScope mParentScope;
	VMContext mContext;
	Map<String, BasicObject> mArgs = null;
	Module selfModule = null;
	VMObject selfObject = null;

	public VMScope(VMContext pContext) throws VMExceptionOutOfMemory {
		mContext = pContext;
		selfModule = new Module(mContext);
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

	@Deprecated
	public VMScope(VMScope scope, UserFunction userFunction) {
		mContext = scope.getContext();
		mParentScope = scope;
		selfObject = scope.selfObject;
		selfModule = scope.selfModule;
	}

	public BasicObject get(ObjectName name) {
		BasicObject ret = null;
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
		VMLog.debug("get:" + name + ":" + ret);

		return ret;
	}

	public void setArgs(Map<String, BasicObject> pArgs) {
		mArgs = pArgs;
	}

	public VMContext getContext() {
		return mContext;
	}

	public CodeStatement getStatement() {
		return null; // FIXME
	}

	public Map<String, BasicObject> getFuncCallArgs() {
		return mArgs;
	}

	public void put(ObjectName objectName, BasicObject value) {
		mReferences.put(objectName, value);
	}

	public void addPackage(VMPackage pPackage) throws VMExceptionOutOfMemory {
		ObjectName name=pPackage.getName(mContext);
		System.out.println("adding package OName:"+name.getName());
		selfModule.put(name, pPackage);
	}

	public MemberFunction getFunction(ObjectName methodName) throws VMException {
		Function f = (Function) get(methodName);
		if (selfObject == null)
			return new MemberFunction(selfModule, f);
		else
			return new MemberFunction(selfObject, f);
	}

}
