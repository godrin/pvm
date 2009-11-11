package vm_java.types.buildins;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.DontExpose;

public class VMIO extends BuildInKlass {

	private static StringBuilder output = new StringBuilder();

	public VMIO(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public static synchronized void puts(String s) {
		output.append(s).append("\n");
		System.out.println(s);
	}

	@DontExpose
	public static String content() {
		return output.toString();
	}

	@DontExpose
	public static void clear() {
		output = new StringBuilder();
	}

	@Override
	public Code toCode() {
		// TODO Auto-generated method stub
		return null;
	}


}
