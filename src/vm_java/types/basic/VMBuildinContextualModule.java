package vm_java.types.basic;

import vm_java.context.VMContext;

public class VMBuildinContextualModule {
	private VMContext context;

	public VMBuildinContextualModule(VMContext pContext) {
		setContext(pContext);
	}

	private void setContext(VMContext context) {
		this.context = context;
	}

	public VMContext getContext() {
		return context;
	}
}
