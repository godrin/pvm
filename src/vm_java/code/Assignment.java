/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class Assignment extends CodeStatement {
	private ObjectName objectName;
	private BasicObject rValue;

	public Assignment(VMContext context, SourceInfo source, ObjectName on,
			BasicObject bo) throws VMExceptionOutOfMemory {
		super(context, source);
		objectName = on;
		rValue = bo;
	}

	/*
	 * public Assignment(VMContext context, ObjectName name, CodeExpression
	 * instantiate) throws VMExceptionOutOfMemory { super(context);
	 * objectName=name; rValue=null; rExpression=instantiate; }
	 */
	public IntermedResult execute(VMScope scope)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory,
			VMException {
		BasicObject bo;
		if (rValue instanceof CodeExpression) {
			CodeExpression rExpression = (CodeExpression) rValue;

			IntermedResult res = rExpression.compute(scope);
			if (!res.exception())
				bo = res.content();
			else
				return res;
		} else {
			bo = rValue;
		}
		if (bo instanceof CodeResolveVar) {
			IntermedResult res = ((CodeResolveVar) bo).compute(scope);
			if (res.exception())
				return res;
			bo = res.content();
		}

		scope.put(objectName, bo);
		return new IntermedResult(bo, Result.NONE);
	}
}
