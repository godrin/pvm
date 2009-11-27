package vm_java.types.foundation;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMBuildinObjectBase;

public class VMInteger extends VMBuildinObjectBase{
	Integer i = null;

	public VMInteger(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public VMInteger(VMContext context, int i2) throws VMExceptionOutOfMemory {
		super(context);
		i = i2;
	}

	public void set(int x) {
		i = x;
	}

	public Integer get() {
		return i;
	}

	public Object convertToJava(Object pk) {
		if (pk.equals(java.lang.Integer.class)) {
			return i;
		} else if (pk.equals(java.lang.String.class)) {
			return Integer.toString(i);
		}
		return null;
	}

	public VMInteger plus(VMInteger p) throws VMExceptionOutOfMemory {
		return new VMInteger(getContext(), i + p.get());
	}

	public VMBoolean lessThan(VMInteger i) throws VMExceptionOutOfMemory {
		return new VMBoolean(getContext(), get() < i.get());
	}

	public VMBoolean biggerThan(VMInteger i) throws VMExceptionOutOfMemory {
		return new VMBoolean(getContext(), get() > i.get());
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add(i.toString());
		return c;
	}

	@Override
	public String inlineCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inspect() {
		// TODO Auto-generated method stub
		return null;
	}
}
