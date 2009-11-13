package vm_java.types.foundation;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMKlassBuiltin;
import vm_java.types.buildins.BuildInKlass;

public class VMDouble extends VMKlassBuiltin {
	Double i = null;

	public VMDouble(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public VMDouble(VMContext context, double i2) throws VMExceptionOutOfMemory {
		super(context);
		i = i2;
	}

	public void set(double x) {
		i = x;
	}

	public Double get() {
		return i;
	}

	public Object convertToJava(Object pk) {
		if (pk.equals(java.lang.Integer.class)) {
			return i;
		} else if (pk.equals(java.lang.String.class)) {
			return Double.toString(i);
		}
		return null;
	}

	public VMDouble plus(VMDouble p) throws VMExceptionOutOfMemory {
		return new VMDouble(getContext(), i + p.get());
	}

	public VMBoolean lessThan(VMDouble i) throws VMExceptionOutOfMemory {
		return new VMBoolean(getContext(), get() < i.get());
	}

	public VMBoolean biggerThan(VMDouble i) throws VMExceptionOutOfMemory {
		return new VMBoolean(getContext(), get() > i.get());
	}

	@Override
	public Code toCode() {
		Code c=new Code();
		c.add(i.toString());
		return c;
	}

}
