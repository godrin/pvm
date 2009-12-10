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
import vm_java.types.Reference;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class UserFunction extends Function {
	CodeBlock mBlock;
	List<ObjectName> mArgs;
	boolean mWithScope;

	public UserFunction(VMContext pContext, CodeBlock pBlock,
			List<ObjectName> pArgs, boolean pWithScope)
			throws VMExceptionOutOfMemory {
		super(pContext);
		mBlock = pBlock;
		mArgs = pArgs;
		mWithScope = pWithScope;
	}

	@Override
	public void runFunction(VMScope scope, Reference returnRef,
			List<? extends BasicObject> args, Task parentTask)
			throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		if (args.size() != mArgs.size())
			throw new VMInternalException(null, "Argsize is different!");

		// put arguments into scope
		for (int i = 0; i < mArgs.size(); i++) {
			scope.put(mArgs.get(i), args.get(i));
		}

		Task task = mBlock.execution(scope, parentTask, returnRef);
		if (mWithScope)
			task.setFarToLocal();

		getVM().addJob(task);
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
