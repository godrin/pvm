/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public abstract class CodeStatement extends BasicObject {

	public static class SourceInfo {

		int lineNo;
		String filename;

		public SourceInfo(String pFilename, int lineNo2) {

			filename = pFilename;
			lineNo = lineNo2;
		}

		public String toString() {
			return "(" + filename.replaceAll(".*/", "") + ":" + lineNo + ")";
		}

	}

	SourceInfo sourceInfo;

	CodeStatement(VMContext pContext, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(pContext);
		sourceInfo = source;
	}

	SourceInfo info() {
		return sourceInfo;
	}

	protected void assertNotNull(ObjectName leftMember) throws VMException {
		if (leftMember == null)
			throw new VMException(this, "Var is null!");
	}

	public abstract void execute(VMScope scope) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

}
