package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMInteger extends BasicObject {
	Integer i=null;

	public VMInteger(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}
	
	public void set(int x) {
		i=x;
	}
	
	public Integer get() {
		return i;
	}
	
	public Object convertTo(Object pk) {
		if (pk.equals(java.lang.Integer.class)) {
			return i;
		} 
		return null;
	}

}
