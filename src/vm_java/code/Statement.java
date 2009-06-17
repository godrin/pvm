/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public abstract class Statement extends BasicObject {
	public enum Result { QUIT_BLOCK, QUIT_BREAK, QUIT_EXCEPTION, NONE, QUIT_FUNCTION };

    Statement(VMContext pContext) throws VMExceptionOutOfMemory {
        super(pContext);
    }

    public abstract Result execute(VMScope scope)
            throws VMException;
}
