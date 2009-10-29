/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeMemberFunction;

/**
 * 
 * @author davidkamphausen
 */
public class VMKlass extends VMModule {
	private VMKlass mParent;
	// these are klass-methods and klass-members
	private Map<ObjectName, BasicObject> mStaticObjects = new TreeMap<ObjectName, BasicObject>();
	private Class<? extends BasicObject> javaClass = null;

	public VMKlass(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public VMKlass(VMContext context, VMKlass pParent)
			throws VMExceptionOutOfMemory {
		super(context);
		mParent = pParent;
	}

	BasicObject create() {
		return BasicObject.nil;
	}

	public BasicObject get(ObjectName name) {

		VMLog.debug("TRYING TO GET OUT OF CLASS:" + name);
		BasicObject o = super.get(name);
		if (o == null) {
			if (mParent != null) {
				o = mParent.get(name);
			}
		}
		return o;
	}

	public RuntimeFunction getFunction(ObjectName name)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {

		BasicObject bo = getStatic(name);
		if (bo == null) // && getClass().equals(javaClass))
			bo = get(name);

		if (bo instanceof RuntimeFunction)
			return (RuntimeFunction) bo;
		if (bo instanceof BuildinFunction) {
			BuildinFunction bf = (BuildinFunction) bo;
			return new CodeBuildinFunction(this, bf.method);
		}

		if (bo instanceof Function) {
			return new RuntimeMemberFunction(this, (Function) bo);
		}
		return null;
	}

	private BasicObject getStatic(ObjectName name) {
		BasicObject bo = mStaticObjects.get(name);
		if (bo == null) {
			if (mParent != null)
				return mParent.getStatic(name);
		}
		return bo;
	}

	public void putStatic(ObjectName key, BuildinFunction value) {
		mStaticObjects.put(key, value);
	}

	public void setParent(BasicObject bo) {
		if (bo instanceof VMKlass) {
			mParent = (VMKlass) bo;
		}
	}

	public VMObject _new(VMContext pContext) throws VMExceptionOutOfMemory {
		return new VMObject(pContext, this);
	}

	public VMKlass _newKlass(VMContext pContext) throws VMExceptionOutOfMemory,
			VMException {
		VMKlass k = new VMKlass(pContext);

		VMLog.debug("ENTRIES static:");
		for (Map.Entry<ObjectName, BasicObject> entry : mStaticObjects
				.entrySet()) {
			VMLog.debug(entry.getKey());
		}
		VMLog.debug("ENTRIES :");
		for (Map.Entry<ObjectName, BasicObject> entry : super.mObjects
				.entrySet()) {
			VMLog.debug(entry.getKey());
		}

		addFunctionsTo(k);

		return k;
	}

	public void setJavaClass(Class<? extends BasicObject> c) {
		javaClass = c;
	}
}
