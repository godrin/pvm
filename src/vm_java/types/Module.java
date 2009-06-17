/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.List;
import java.util.Map;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * 
 * @author davidkamphausen
 */
public class Module extends BasicObject {

	private List<Module> mMixins;
	private Map<ObjectName, Function> mFunctions;

	public Module(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	// FIXME: maybe check "send"-function first
	BasicObject send(ObjectName pName, Arguments pArgs) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		Function f = mFunctions.get(pName);
		if (f == null) {

			for(Module mixin:mMixins) {
				try {
					BasicObject ret=mixin.send(pName,pArgs);
					return ret;
				} catch(VMExceptionFunctionNotFound e) {
					// just try next mixin
				}
			}
			
			ObjectName on = getContext().intern("method_missing");
			if (!pName.equals(on)) {
				Arguments args=new Arguments(pArgs);
				args.pushFront(pName);
				return send(on,args);
			}
			else {
				throw new VMExceptionFunctionNotFound();
			}
		}
		else {
			return f.execute(getContext(),pArgs);
		}
	}
}
