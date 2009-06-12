/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class Assignment extends Statement {
	private ObjectName objectName;
	private BasicObject rValue;

	Assignment(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public Assignment(VMContext context, ObjectName on, BasicObject bo)
			throws VMExceptionOutOfMemory {
		super(context);
		objectName=on;
		rValue=bo;
	}

	public void execute(VMScope scope) {
scope.put(objectName, rValue);
	}
}
