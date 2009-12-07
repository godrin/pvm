package vm_java.types.buildins;

import java.io.IOException;

import vm_java.types.DontExpose;
import vm_java.types.basic.VMBuildinStaticModule;

public class VMIO implements VMBuildinStaticModule {

	private static StringBuilder output = new StringBuilder();

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
	
	public static int getChar() throws IOException {
		return System.in.read();
	}

}
