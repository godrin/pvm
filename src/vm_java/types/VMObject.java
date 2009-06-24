package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.MemberFunction;

public class VMObject extends BasicObject implements FunctionProvider {
	private Klass mKlass;
	private Map<ObjectName, BasicObject> data = new TreeMap<ObjectName, BasicObject>();

	public VMObject(VMContext context, Klass klass)
			throws VMExceptionOutOfMemory {
		super(context);
	}

	public Klass getKlass() {
		return mKlass;
	}

	public BasicObject get(ObjectName name) {
		BasicObject o=data.get(name);
		if(o==null) {
			o=mKlass.get(name);
		}
		return o;
	}
	
	public MemberFunction getFunction(ObjectName name) throws VMException {
		
		BasicObject bo=get(name);
		if(bo instanceof Function) {
			return new MemberFunction(this,(Function)bo);
		}
		return null;
	}
}
