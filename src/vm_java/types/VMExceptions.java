package vm_java.types;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMExceptions {

	public static class VMScriptException extends BasicObject {

		Exception enclosed;

		public VMScriptException(VMContext pContext, Exception pEnclosed)
				throws VMExceptionOutOfMemory {
			super(pContext);
			enclosed = pEnclosed;
		}

		@Override
		public String inlineCode() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String inspect() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Code toCode() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static BasicObject vmException(VMContext pContext, Exception e)
			throws VMExceptionOutOfMemory {
		return new VMScriptException(pContext, e);
	}

}
