package vm_java.types.foundation;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMKlassBuiltin;

public class VMBoolean extends VMKlassBuiltin {
	private boolean b;

	public VMBoolean(VMContext context, boolean bool)
			throws VMExceptionOutOfMemory {
		super(context);
		b = bool;
	}

	public boolean get() {
		return b;
	}

	public Object convertToJava(Object pk) {
		if (pk.equals(java.lang.Integer.class)) {
			if (b) {
				return 1;
			} else {
				return 0;
			}
		} else if (pk.equals(java.lang.String.class)) {
			return Boolean.toString(b);
		}
		return null;
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		if (b) {
			c.add("true");
		} else {
			c.add("false");
		}
		return c;
	}

}
