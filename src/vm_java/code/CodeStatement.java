/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public abstract class CodeStatement extends BasicObject {

	public static class SourceInfo {

		int lineNo;

		public SourceInfo(int lineNo2) {

			lineNo = lineNo2;
		}
		public int getLineNumber() {
			return lineNo;
		}

	}
	
	SourceInfo sourceInfo;

	CodeStatement(VMContext pContext, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(pContext);
		sourceInfo=source;
	}
	
	SourceInfo info() {
		return sourceInfo;
	}

	public abstract IntermedResult execute(VMScope scope) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

}
