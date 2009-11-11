package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.types.foundation.ObjectName;

public class DoReturn extends CodeStatement {

	Type type;
	ObjectName name;

	public DoReturn(VMContext context, SourceInfo source, ObjectName varName,
			Type ptype) throws VMExceptionOutOfMemory {
		super(context, source);
		type = ptype;
		name = varName;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException {
		scope.setReturned();
	}

	@Override
	public String inspect() {
		return "[Return]";
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		if (type == Type.FAR) {
			c.add("freturn " + name.inlineCode());
		} else {
			c.add("lreturn " + name.inlineCode());

		}
		return c;
	}

}
