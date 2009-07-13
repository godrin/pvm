package vm_java.types;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMIO extends BuildInKlass implements BuildInInterface {

	private static StringBuilder output = new StringBuilder();;

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

}
