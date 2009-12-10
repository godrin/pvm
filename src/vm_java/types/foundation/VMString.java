package vm_java.types.foundation;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMBuildinObjectBase;

/**
 * 
 * @author davidkamphausen
 */
public class VMString extends VMBuildinObjectBase {
	private java.lang.String mContent = "";
	private final int OVERHEAD = 8;

	public VMString(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public VMString(VMContext context, String s) throws VMExceptionOutOfMemory {
		super(context);
		mContent = s;
	}

	public boolean equals(Object o) {
		if (o instanceof VMString) {
			if (mContent != null)
				return mContent.equals(((VMString) o).mContent);
			else
				return ((VMString) o).mContent == null;
		} else if (o instanceof java.lang.String) {
			String s = (String) o;
			return s.equals(mContent);
		}
		return false;
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

	public VMString plus(VMString a) throws VMExceptionOutOfMemory {
		return new VMString(getContext(), getContent() + a.getContent());
	}

	/*
	 * public VMString concat(VMString a) throws VMExceptionOutOfMemory { return
	 * new VMString(getContext(),getContent()+a.getContent()); }
	 */

	public Object convertToJava(Object pk) {
		if (pk.equals(java.lang.String.class)) {
			return mContent;
		}
		if (pk.equals(java.lang.Integer.class)) {
			return Integer.parseInt(mContent);
		}
		return null;
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("\"" + mContent + "\"");
		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString().replace("\n", "");
	}

	@Override
	public String inspect() {
		return inlineCode();
	}
}
