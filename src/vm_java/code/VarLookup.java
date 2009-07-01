package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.ObjectName;

@Deprecated
public class VarLookup extends BasicObject{

	public VarLookup(VMContext context,ObjectName pName) throws VMExceptionOutOfMemory {
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	public String inspect() {
		return "[VarLookup]";
	}

}
