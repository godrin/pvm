/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import vm_java.VM;
import vm_java.code.Code;
import vm_java.internal.VMLog;
import vm_java.types.foundation.VMInteger;
import vm_java.types.foundation.VMString;

/**
 * 
 * @author davidkamphausen
 */
public abstract class BasicObject implements Comparable<BasicObject> {

	private final long BASICOBJECT_OVERHEAD = 16;
	private Long mid = new Long(0);

	public BasicObject(VMContext pContext) throws VMExceptionOutOfMemory {
		mContext = pContext;
		mContext.add(this);
		mid = mContext.getNewID();
	}

	public VMContext getContext() {
		return mContext;
	}

	VMContext mContext;
	static public BasicObject nil = null;

	public long getMemoryUsage() {
		return BASICOBJECT_OVERHEAD;
	}
	
	public Long getID() {
		return mid;
	}

	public static BasicObject convert(VMContext context, Object o)
			throws VMExceptionOutOfMemory {
		if (o instanceof BasicObject) {
			return (BasicObject) o;
		} else if (o instanceof java.lang.String) {
			VMString s = new VMString(context);
			s.setContent((String) o);
			return s;
		} else if (o instanceof java.lang.Integer) {
			VMInteger i = new VMInteger(context);
			i.set((Integer) o);
			return i;
		}
		// TODO Auto-generated method stub
		return null;
	}

	public Object convertToJava(Object pk) {
		VMLog.debug("BASIC");
		VMLog.debug(this);
		return null;
	}

	public VM getVM() {
		return getContext().getVM();
	}

	public abstract String inspect();

	@Override
	public int compareTo(BasicObject o) {
		return mid.compareTo(o.mid);
	}

	abstract public Code toCode();

	abstract public String inlineCode();
	/*
	public String inlineCode() {
		return toCode().toString().replace("\\n","");
	}
*/
}
