/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.Function;
import vm_java.types.ObjectName;
import vm_java.types.Reference;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class UserFunction extends Function {
	CodeBlock mBlock;
	List<ObjectName> mArgs;

	public static final String RETVALUE = "__retValue";

	public UserFunction(VMContext pContext, CodeBlock pBlock,
			List<ObjectName> pArgs) throws VMExceptionOutOfMemory {
		super(pContext);
		mBlock = pBlock;
		mArgs = pArgs;
	}

	@Override
	public void runFunction(VMScope scope, ObjectName returnName,
			List<? extends BasicObject> args) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		VMScope subScope = new VMScope(scope, this);
		int i = 0;
		if (args.size() != mArgs.size())
			throw new VMException(null, "Argsize is different!");

		for (; i < args.size(); i++) {
			subScope.put(mArgs.get(i), args.get(i));
		}
		if (returnName != null) {
			subScope.put(subScope.getContext().intern(RETVALUE), new Reference(
					scope, returnName));
		}

		getVM().addJob(mBlock.execution(subScope));
	}

	@Override
	public String inspect() {
		StringBuilder b = new StringBuilder();
		b.append("[UserFunction:");
		for (ObjectName a : mArgs)
			b.append(a.inspect()).append(", ");
		b.append("]");
		return b.toString();
	}
}
