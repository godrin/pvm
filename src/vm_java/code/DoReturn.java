package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

public class DoReturn extends CodeStatement {

	public DoReturn(VMContext context, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(context, source);
	}

	@Override
	public void execute(VMScope scope) throws VMException {
		scope.setReturned();
	}

	@Override
	public String inspect() {
		return "[Return]";
	}

}
