/**
 * 
 */
package vm_java.types.basic;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * @author davidkamphausen
 *
 */
public class VMKlassBuiltin extends VMKlass {
	
	/**
	 * @param context
	 * @param c
	 * @throws VMExceptionOutOfMemory
	 */
	public VMKlassBuiltin(VMContext context)
			throws VMExceptionOutOfMemory {
		super(context);
	}
	
}
