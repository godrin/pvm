/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import vm_java.code.CodeExpression;
import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.code.IntermedResult.Result;
import vm_java.internal.VMLog;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMInteger;
import vm_java.types.VMString;

/**
 * 
 * @author davidkamphausen
 */
public class BasicObject implements CodeExpression {

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
		if(o instanceof BasicObject) {
			return (BasicObject)o;
		}
		else if (o instanceof java.lang.String) {
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


	public Object convertToJava(Object pk) {
		VMLog.debug("BASIC");
		VMLog.debug(this);
		return null;
	}

	@Override
	public IntermedResult compute(VMScope scope) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
		return new IntermedResult(this,Result.NONE);
	}

	
}
