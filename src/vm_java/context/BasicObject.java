/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import vm_java.types.VMInteger;
import vm_java.types.VMString;

/**
 * 
 * @author davidkamphausen
 */
public class BasicObject {

	private final long BASICOBJECT_OVERHEAD = 8;

	public BasicObject(VMContext pContext) throws VMExceptionOutOfMemory {
		mContext = pContext;
		mContext.add(this);
	}

	public VMContext getContext() {
		return mContext;
	}

	VMContext mContext;
	static public BasicObject nil = null;

	public long getMemoryUsage() {
		return BASICOBJECT_OVERHEAD;
	}

	public static BasicObject convert(VMContext context, Object o)
			throws VMExceptionOutOfMemory {
		if (o instanceof java.lang.String) {
			VMString s = new VMString(context);
			s.setContent((String) o);
			return s;
		} else if(o instanceof java.lang.Integer) {
			VMInteger i= new VMInteger(context);
			i.set((Integer)o);
			return i;
		}
		// TODO Auto-generated method stub
		return null;
	}


	public Object convertTo(Object pk) {
		System.out.println("BASIC");
		return null;
	}
}
