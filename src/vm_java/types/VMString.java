package vm_java.types;

import vm_java.code.CodeExpression;
import vm_java.code.IntermedResult;
import vm_java.code.IntermedResult.Result;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 * 
 * @author davidkamphausen
 */
public class VMString extends Klass implements CodeExpression {
	private java.lang.String mContent = "";
	private final int OVERHEAD = 8;

	public VMString(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public VMString(VMContext context, String s) throws VMExceptionOutOfMemory {
		super(context);
		mContent=s;
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

	public Object convertToJava(Object pk,VMScope scope) {
		if (pk.equals(java.lang.String.class)) {
			return mContent;
		}
		return null;
	}

	@Override
	public IntermedResult compute(VMScope scope) {
		return new IntermedResult(this,Result.NONE);
	}

}
