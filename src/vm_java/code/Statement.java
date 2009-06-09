/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public abstract class Statement extends BasicObject{

    Statement(VMContext pContext)
            {
        super(pContext);
    }

    abstract void execute(VMScope scope);
}
