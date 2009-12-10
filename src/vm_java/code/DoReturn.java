package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.types.basic.ObjectName;

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
	public void execute(VMScope scope, Task parentTask)
			throws VMInternalException, VMExceptionOutOfMemory {
		BasicObject object = scope.get(name);
		if (type == Type.EXCEPTION) {

			object = scope.encloseInException(sourceInfo, object);
		}
		parentTask.setReturn(type, object);
	}

	@Override
	public String inspect() {
		return "[Return]";
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		String cmd = "lreturn";
		if (type == Type.FAR) {
			cmd = "freturn";
		} else if (type == Type.EXCEPTION) {
			cmd = "ereturn";
		}

		c.add(cmd + " " + name.inlineCode());
		return c;
	}

}
