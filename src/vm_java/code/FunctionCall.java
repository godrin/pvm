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

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;
import vm_java.types.Function;

/**
 * 
 * @author davidkamphausen
 */
public class FunctionCall extends Statement {

	public FunctionCall(VMContext pContext, String pFuncName)
			throws VMExceptionOutOfMemory {
		super(pContext);

		mFuncName = pFuncName;
	}

	public FunctionCall(VMContext pContext, String pFuncName, Object[] pArgs)
			throws VMExceptionOutOfMemory {
		super(pContext);
		mFuncName = pFuncName;
		List<BasicObject> args = new ArrayList<BasicObject>();
		for (Object o : pArgs) {
			if (o instanceof BasicObject) {
				args.add((BasicObject) o);
			} else {
				args.add(BasicObject.convert(pContext, o));
			}
		}
		mArgs = args;
	}

	public FunctionCall(VMContext pContext, String pFuncName,
			Collection<? extends BasicObject> pArgs)
			throws VMExceptionOutOfMemory {
		super(pContext);

		mFuncName = pFuncName;
		mArgs = pArgs;
	}

	public Result execute(VMScope scope) throws VMException {
		BasicObject bo = scope.get(mFuncName);
		if (bo instanceof Function) {
			Function f = (Function) bo;
			VMScope subScope = new VMScope(scope, f);

			if (mArgs != null) {
				Map<String, BasicObject> map = f.assignParameters(mArgs);
				for (Entry<String, BasicObject> entry : map.entrySet()) {
					if (entry.getValue() instanceof ObjectName) {
						map.put(entry.getKey(), scope.get((ObjectName) entry
								.getValue()));
					}
				}
				subScope.setArgs(map);
			}
			Result res=f.execute(subScope);
			if(res==Result.NONE)
			scope.setLastReturn(subScope.getReturn());
			return res;
		} else {
			throw new VMException(this, mFuncName
					+ " is not a valid funcName in scope " + scope);
		}
	}

	String mFuncName;
	Collection<? extends BasicObject> mArgs;
}
