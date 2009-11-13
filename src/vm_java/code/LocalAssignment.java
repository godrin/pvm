/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 * 
 * 
 *         only simple assignment
 */
public class LocalAssignment extends CodeStatement {
	private ObjectName lObjectName;
	private BasicObject rObject;

	public LocalAssignment(VMContext context, SourceInfo source,
			ObjectName left, BasicObject right) throws VMExceptionOutOfMemory {
		super(context, source);
		lObjectName = left;
		rObject = right;
	}

	public void execute(VMScope scope, Task parentTask)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory,
			VMException {

		BasicObject bo = rObject;
		if (bo instanceof ObjectName)
			bo = scope.get((ObjectName) bo);

		scope.put(lObjectName, bo);

	}

	@Override
	public String inspect() {
		return "[LocalAssign:" + lObjectName.inspect() + "="
				+ rObject.inspect() + "]";
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		Code tmp = rObject.toCode();
		String r = "";
		if (tmp != null) {

			r = tmp.toString();
			if (r != null)
				r = r.replace("\\n", "");
		} else {
			r = "unknown";
		}
		c.add(lObjectName.inlineCode() + "=" + r);
		return c;
	}

}
