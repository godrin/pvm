/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types.basic;

import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.BuildinFunction;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class VMKlass extends VMModule {
	private VMKlass mParent;

	// dictionary ??

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

	public BasicObject getInstance(ObjectName name) {

		VMLog.debug("TRYING TO GET OUT OF CLASS:" + name);
		BasicObject o = super.getInstance(name);
		if (o == null) {
			if (mParent != null) {
				o = mParent.getInstance(name);
			}
		}
		return o;
	}

	public RuntimeFunction getFunction(ObjectName name)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {

		BasicObject bo = getStatic(name);

		return makeFunction(bo, this);
	}

	public RuntimeFunction getInstanceFunction(ObjectName name,
			BasicObject target) throws VMExceptionFunctionNotFound,
			VMExceptionOutOfMemory {

		BasicObject bo = getInstance(name);

		return makeFunction(bo, target);
	}

	private RuntimeFunction makeFunction(BasicObject bo, Object target)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {

		if (bo instanceof RuntimeFunction)
			return (RuntimeFunction) bo;
		if (bo instanceof BuildinFunction) {
			BuildinFunction bf = (BuildinFunction) bo;
			return new CodeBuildinFunction(target, bf.method);
		}

		if (bo instanceof Function) {
			return new RuntimeMemberFunction(this, (Function) bo);
		}
		return null;
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
			VMInternalException {
		VMKlass k = new VMKlass(pContext);

		addStaticsAsInstanceTo(k);

		return k;
	}

	public boolean isSubClassOf(VMKlass pKlass) {
		if (this == pKlass)
			return true;
		if (mParent != null)
			return mParent.isSubClassOf(pKlass);
		return false;
	}

}
