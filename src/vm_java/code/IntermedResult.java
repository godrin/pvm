package vm_java.code;

import vm_java.context.BasicObject;

public class IntermedResult {
	public enum Result { QUIT_BLOCK, QUIT_BREAK, QUIT_EXCEPTION, NONE, QUIT_FUNCTION };
	
	private BasicObject content;
	private Result result;
	
	public IntermedResult(BasicObject bo,Result res) {
		content=bo;
		result=res;
	}
	
	public BasicObject content() {
		return content;
	}
	public Result result() {
		return result;
	}

	public boolean exception() {
		return result==Result.QUIT_EXCEPTION;
	}

	public boolean broken() {
		return result!=Result.NONE;
	}

}
