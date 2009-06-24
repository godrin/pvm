/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class ObjectName extends BasicObject implements Comparable<ObjectName> {

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

	@Override
	public int compareTo(ObjectName o) {
		// FIXME
		return mName.compareTo(o.mName);
	}

	public String toSymbolName() {
		return mName.substring(3);
	}
	
	public Object convertToJava(Object pk,VMScope scope) {
		return toString();
		//BasicObject v=scope.get(this);
		//return v.convertToJava(pk, scope);
	}
	
	public String toString() {
		return super.toString()+" ["+mName+"]";
	}

}
