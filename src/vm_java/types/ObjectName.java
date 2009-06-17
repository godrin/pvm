/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 *
 * @author davidkamphausen
 */
public class ObjectName extends BasicObject {

    public ObjectName(VMContext pContext,String string) throws VMExceptionOutOfMemory {
    	super(pContext);
        this.mName = string;
    }

    public String getName() {
        return mName;
    }
    
    public boolean isInternal() {
    	return mName.matches(":.*");
    }
    
    String mName;
}
