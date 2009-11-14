package vm_java.types.basic;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMCException extends BasicObject {

	private Exception enclosed;
	
	public VMCException(VMContext context,Exception e) throws VMExceptionOutOfMemory {
		super(context);
		setEnclosed(e);
	}

	@Override
	public String inspect() {
		return "[VMCException]";
	}

	private void setEnclosed(Exception enclosed) {
		this.enclosed = enclosed;
	}

	public Exception getEnclosed() {
		return enclosed;
	}

	@Override
	public Code toCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inlineCode() {
		// TODO Auto-generated method stub
		return null;
	}

}