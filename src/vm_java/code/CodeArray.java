package vm_java.code;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.foundation.VMArray;

public class CodeArray extends CodeStatement {
	ObjectName targetName;
	List<ObjectName> fromNames;
	boolean fixed = false;

	public CodeArray(VMContext pContext, SourceInfo source,
			ObjectName objectName) throws VMExceptionOutOfMemory {
		super(pContext, source);
		targetName = objectName;
		fromNames = new ArrayList<ObjectName>();
	}

	public void add(ObjectName name) throws VMException {
		if (fixed) {
			throw new VMException(this, "CodeArray was already fixed");
		}
		fromNames.add(name);
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		VMArray value = new VMArray(scope.getContext());
		for (ObjectName name : fromNames) {
			value.add(scope.get(name));
		}
		scope.put(targetName, value);
	}

	@Override
	public Code toCode() {
		StringBuilder b = new StringBuilder();
		b.append(targetName).append("=[");
		boolean first = true;
		for (ObjectName n : fromNames) {
			if (first) {
				first = false;
			} else {
				b.append(", ");
			}

			b.append(n.inlineCode());

		}
		b.append("]");
		Code c=new Code();
		c.add(b.toString());
		return c;
	}

}
