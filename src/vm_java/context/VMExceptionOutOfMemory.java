/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;


/**
 *
 * @author davidkamphausen
 */
public class VMExceptionOutOfMemory extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VMExceptionOutOfMemory() {
        super("Out of memory");
    }
}
