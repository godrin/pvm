package vm_java.types;

import vm_java.code.Code;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

public class Reference extends BasicObject {
	private VMScope otherScope;
	private ObjectName otherName;

	public Reference(VMScope pOtherScope, ObjectName pOtherName)
			throws VMExceptionOutOfMemory, VMException {

		super(pOtherScope.getContext());
		if(pOtherName==null)
			throw new VMException(null,"Reference othername is null");
		
		otherName = pOtherName;
		otherScope = pOtherScope;
	}

	public void set(BasicObject b) throws VMException {
		otherScope.put(otherName, b);
	}

	@Override
	public String inspect() {
		return "[Reference]";
	}

	@Override
	public Code toCode() {
		return otherName.toCode();
	}

}
