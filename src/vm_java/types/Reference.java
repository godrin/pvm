package vm_java.types;

import vm_java.code.Code;
import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.basic.ObjectName;

public class Reference extends BasicObject {
	private VMScope scope;
	private ObjectName name;

	public Reference(VMScope pOtherScope, ObjectName pOtherName)
			throws VMExceptionOutOfMemory, VMInternalException {

		super(pOtherScope.getContext());
		if (pOtherName == null)
			throw new VMInternalException(null, "Reference othername is null");

		name = pOtherName;
		scope = pOtherScope;
	}

	public void set(BasicObject b) throws VMInternalException {
		scope.put(name, b);
	}

	@Override
	public String inspect() {
		return "[Reference]";
	}

	@Override
	public Code toCode() {
		return name.toCode();
	}

	@Override
	public String inlineCode() {
		return "<reference to " + name.getName() + ">";
	}

}
