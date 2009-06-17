package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMObject extends BasicObject {
	private Klass mKlass;
	private Map<ObjectName, BasicObject> data = new TreeMap<ObjectName, BasicObject>();

	public VMObject(VMContext context, Klass klass)
			throws VMExceptionOutOfMemory {
		super(context);
	}

	public Klass getKlass() {
		return mKlass;
	}

	public BasicObject send(ObjectName funcName, Arguments args)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		return mKlass.send(funcName, args);
	}

}
