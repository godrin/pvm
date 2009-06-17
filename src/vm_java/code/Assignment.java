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
	public static final int LAST_RETURN = 11;
	public static final int RVALUE = 12;

	private ObjectName objectName;
	private BasicObject rValue;
	private int whatToDo;

	Assignment(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	@Deprecated
	public Assignment(VMContext context, ObjectName on, BasicObject bo)
			throws VMExceptionOutOfMemory {
		super(context);
		objectName = on;
		rValue = bo;
		whatToDo = RVALUE;
	}

	public Assignment(VMContext context, ObjectName on, int pWhatToDo)
			throws VMExceptionOutOfMemory {
		super(context);
		objectName = on;
		whatToDo = pWhatToDo;
	}

	public Result execute(VMScope scope) {
		if (whatToDo == LAST_RETURN) {
			scope.put(objectName, scope.getLastReturn());
		} else if (whatToDo == RVALUE) {
			scope.put(objectName, rValue);
		}
		return Result.NONE;
	}
}
