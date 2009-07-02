package vm_java.code;

import vm_java.context.VMScope;
import vm_java.machine.Task;

public interface LLTaskGenerator {
	public Task execution(VMScope scope,Task pParentTask);

}
