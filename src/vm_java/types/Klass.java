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
	
	public BasicObject get(ObjectName name) {
		BasicObject o=super.get(name);
		if(o==null) {
			if(mParent!=null) {
				o=mParent.get(name);
			}
		}
		return o;
	}


	/*
	IntermedResult send(VMScope scope,ObjectName pName, List<ObjectName> pArgs)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
		try {
			IntermedResult ret = super.send(scope,pName, pArgs);

			return ret;
		} catch (VMExceptionFunctionNotFound e) {
			if (mParent != null) {
				return mParent.send(scope, pName, pArgs);
			}
			throw e;
		}
	}*/

}
