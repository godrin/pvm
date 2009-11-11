package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LLParser2.ParseError;

public abstract class SourceBased extends BasicObject {
	public static class SourceInfo {

		int lineNo;
		String filename;

		public SourceInfo(String pFilename, int lineNo2) throws ParseError {
			filename = pFilename;
			lineNo = lineNo2;
			if(filename==null) {
				throw new ParseError();
			}
		}

		public String toString() {
			return "(" + filename.replaceAll(".*/", "") + ":" + lineNo + ")";
		}

	}

	SourceInfo sourceInfo;

	public SourceBased(VMContext context, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(context);
		sourceInfo = source;

		// TODO Auto-generated constructor stub
	}

	SourceInfo info() {
		return sourceInfo;
	}

	@Override
	public String inspect() {
		return "[SourceBase:" + sourceInfo + "]";
	}

}
