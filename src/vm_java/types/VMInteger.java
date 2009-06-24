package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

public class VMInteger extends BasicObject implements BuildInInterface {
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
	
	public Object convertToJava(Object pk,VMScope scope) {
		if (pk.equals(java.lang.Integer.class)) {
			return i;
		} 
		return null;
	}


}
