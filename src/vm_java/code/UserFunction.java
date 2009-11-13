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
import vm_java.machine.Task;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class UserFunction extends Function {
	CodeBlock mBlock;
	List<ObjectName> mArgs;

	public UserFunction(VMContext pContext, CodeBlock pBlock,
			List<ObjectName> pArgs) throws VMExceptionOutOfMemory {
		super(pContext);
		mBlock = pBlock;
		mArgs = pArgs;
	}

	@Override
	public void runFunction(VMScope scope, ObjectName returnName,
			List<? extends BasicObject> args, Task parentTask)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		if (args.size() != mArgs.size())
			throw new VMException(null, "Argsize is different!");

		// put arguments into scope
		for (int i = 0; i < mArgs.size(); i++) {
			scope.put(mArgs.get(i), args.get(i));
		}

		getVM().addJob(mBlock.execution(scope, parentTask));
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

	@Override
	public Code toCode() {
		Code c = new Code();

		c.add("begin_withscope("); // FIXME
		boolean first = true;
		for (ObjectName a : mArgs) {
			if (first) {
				first = false;
			} else {
				c.addToLastLine(", ");
			}
			c.addToLastLine(a.inlineCode());
		}
		c.addToLastLine(")");
		c.add(mBlock.toCode().indent()); // FIXME: add scope ??
		c.add("end");
		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString();
	}
}
