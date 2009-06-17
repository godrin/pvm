/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * 
 * @author davidkamphausen
 */
public class Klass extends Module {
	private Klass mParent;

	public Klass(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}
	
	BasicObject create() {
		return BasicObject.nil;
	}

	BasicObject send(ObjectName pName, Arguments pArgs)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		try {
			BasicObject ret = super.send(pName, pArgs);

			return ret;
		} catch (VMExceptionFunctionNotFound e) {
			if (mParent != null) {
				return mParent.send(pName, pArgs);
			}
			throw e;
		}
	}

}
