/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.List;

import vm_java.code.CodeExpression;
import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 * 
 * @author davidkamphausen
 */
public abstract class Function extends BasicObject implements CodeExpression {

	public Function(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	/*
	public Map<String, BasicObject> assignParameters(
			Collection<? extends BasicObject> pArgs) {
		Map<String, BasicObject> map = new TreeMap<String, BasicObject>();

		int c = 0;
		for (BasicObject o : pArgs) {
			map.put(Integer.toString(c), o);
			c += 1;
		}

		return map;
	}*/
	
    public abstract IntermedResult runFunction(VMScope pScope,List<? extends BasicObject> args) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

	
	@Override
	public IntermedResult compute(VMScope scope) {
		return new IntermedResult(this,vm_java.code.IntermedResult.Result.NONE);
	}
}
