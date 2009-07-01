package vm_java.types;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMIO extends BuildInKlass implements BuildInInterface {

	public VMIO(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}
	
	public static void puts(VMString s) {
		System.out.println(s.getContent());
	}

}