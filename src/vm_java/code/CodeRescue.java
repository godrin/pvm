package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public class CodeRescue extends CodeStatement {

	ObjectName varName;
	List<ObjectName> typeNames;
	CodeBlock block;

	public CodeRescue(VMContext pContext, SourceInfo source,
			List<ObjectName> types, ObjectName objectName, CodeBlock pblock)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
		typeNames = types;
		varName = objectName;
		block = pblock;
	}

	public Task execution(VMScope scope, Task parentTask) {
		return block.execution(scope, parentTask);
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		throw new VMInternalException(this, "Dont use");
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		StringBuilder b = new StringBuilder();
		b.append("rescue");
		for (int i = 0; i < typeNames.size(); i++) {
			if (i > 0) {
				b.append(",");
			}
			b.append(" ");
			b.append(typeNames.get(i));
		}
		if (varName != null) {
			b.append(" => ").append(varName.inlineCode());
		}
		c.add(b.toString());
		if (block != null)
			c.add(block.toCode().indent());
		return c;
	}

	public boolean matches(VMScope scope, BasicObject exception)
			throws VMInternalException {
		BasicObject exceptionKlass = exception.getKlass();
		if (exceptionKlass == null) {
			throw new VMInternalException(this, "exception has no Class:"
					+ exception.inspect());
		}
		if (typeNames.size() == 0)
			return true;
		for (ObjectName name : typeNames) {
			BasicObject type = scope.get(name);
			if (exceptionKlass.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
