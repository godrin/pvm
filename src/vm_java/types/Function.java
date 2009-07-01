/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 * 
 * @author davidkamphausen
 */
public abstract class Function extends BasicObject{

	public Function(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}
    public abstract void runFunction(VMScope pScope,ObjectName returnName,List<? extends BasicObject> args) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

    public String inspect() {
    	return "[FUNCTION:FIXME]";
    }
    
}
