package vm_java.types;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMInteger extends BuildInKlass implements BuildInInterface {
	Integer i=null;

	public VMInteger(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}
	
	public VMInteger(VMContext context, int i2) throws VMExceptionOutOfMemory {
		super(context);
		i=i2;
	}

	public void set(int x) {
		i=x;
	}
	
	public Integer get() {
		return i;
	}
	
	public Object convertToJava(Object pk) {
		if (pk.equals(java.lang.Integer.class)) {
			return i;
		} else if(pk.equals(java.lang.String.class)) {
			return Integer.toString(i);
		}
		return null;
	}
	
	public VMInteger plus(VMInteger p) throws VMExceptionOutOfMemory {
		return new VMInteger(getContext(),i+p.get());
	}



}
