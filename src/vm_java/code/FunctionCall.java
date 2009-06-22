/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.Arguments;
import vm_java.types.ObjectName;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class FunctionCall extends CodeStatement {
	ObjectName mFuncName;
	Collection<? extends CodeExpression> mArgs;
	
	public FunctionCall(VMContext pContext, ObjectName pFuncName)
			throws VMExceptionOutOfMemory {
		super(pContext);

		mFuncName = pFuncName;
	}

	public FunctionCall(VMContext pContext, ObjectName pFuncName, Object[] pArgs)
			throws VMExceptionOutOfMemory {
		super(pContext);
		mFuncName = pFuncName;
		List<BasicObject> args = new ArrayList<BasicObject>();
		for (Object o : pArgs) {
			if (o instanceof CodeExpression) {
				args.add((BasicObject) o);
			}else if (o instanceof BasicObject) {
				args.add((BasicObject) o);
			} else {
				args.add(BasicObject.convert(pContext, o));
			}
		}
		mArgs = args;
	}

	public FunctionCall(VMContext pContext, ObjectName pFuncName,
			Collection<? extends BasicObject> pArgs)
			throws VMExceptionOutOfMemory {
		super(pContext);

		mFuncName = pFuncName;
		mArgs = pArgs;
	}

	/*
	 * public IntermedResult execute(VMScope scope) throws VMException {
	 * BasicObject bo = scope.get(mFuncName); if (bo instanceof Function) {
	 * Function f = (Function) bo; VMScope subScope = new VMScope(scope, f);
	 * 
	 * if (mArgs != null) { Map<String, BasicObject> map =
	 * f.assignParameters(mArgs); for (Entry<String, BasicObject> entry :
	 * map.entrySet()) { if (entry.getValue() instanceof ObjectName) {
	 * map.put(entry.getKey(), scope.get((ObjectName) entry .getValue())); } }
	 * subScope.setArgs(map); } IntermedResult res = f.compute(subScope); return
	 * res; } else { throw new VMException(this, mFuncName +
	 * " is not a valid funcName in scope " + scope); } }
	 */

	public static IntermedResult execute(VMScope scope, Function f, List<BasicObject> args)
			throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		return f.runFunction(scope, args);

	}



	@Override
	public IntermedResult execute(VMScope scope) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		BasicObject funcObject = scope.get(mFuncName);
		if (!(funcObject instanceof Function)) {
			// FIXME: fill in exception
			return new IntermedResult(BasicObject.nil, Result.QUIT_EXCEPTION);
		}
		Function f = (Function) funcObject;

		List<BasicObject> args = new ArrayList<BasicObject>();
		for (CodeExpression bo : mArgs) {
			IntermedResult res = bo.compute(scope);
			if (res.exception())
				return res;
			args.add(res.content());
		}

		return execute(scope, f, args);
	}
}
