package vm_java.types.buildins;

import vm_java.context.VMContext;
import vm_java.types.basic.VMBuildinContextualModule;

public class VMCtxLog extends VMBuildinContextualModule {

	StringBuilder b=new StringBuilder();
	
	public VMCtxLog(VMContext pContext) {
		super(pContext);
	}
	
	public void add(String x) {
		b.append(x);
	}
	
	public String getContent() {
		return b.toString();
	}

}
