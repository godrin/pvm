/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * 
 * @author davidkamphausen
 */
public class VMString extends Klass {
	private java.lang.String mContent = "";
	private final int OVERHEAD = 8;

	public VMString(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public void setContent(java.lang.String pContent) {
		mContent = pContent;
	}

	public java.lang.String getContent() {
		return mContent;
	}

	public long getMemoryUsage() {
		if (mContent != null)
			return mContent.length() + OVERHEAD;
		return OVERHEAD;
	}

	public Object convertTo(Object pk) {
		if (pk.equals(java.lang.String.class)) {
			return mContent;
		}
		return null;
	}

}
