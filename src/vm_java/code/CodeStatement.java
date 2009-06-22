/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 *
 * @author davidkamphausen
 */
public abstract class CodeStatement extends BasicObject {

    CodeStatement(VMContext pContext) throws VMExceptionOutOfMemory {
        super(pContext);
    }

    public abstract IntermedResult execute(VMScope scope)
            throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;
}
