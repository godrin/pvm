package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeMemberFunction;

@DontExpose
public class VMObject extends BasicObject implements FunctionProvider,
		MemberProvider, MemberContainer {
	private VMKlass mKlass;
	private Map<ObjectName, BasicObject> data = new TreeMap<ObjectName, BasicObject>();

	public VMObject(VMContext context, VMKlass klass)
			throws VMExceptionOutOfMemory {
		super(context);
		mKlass = klass;
	}

	public VMKlass getKlass() {
		return mKlass;
	}

	public BasicObject get(ObjectName name) {
		BasicObject o = data.get(name);
		if (o == null) {
			o = mKlass.get(name);
		}
		return o;
	}

	public RuntimeFunction getFunction(ObjectName name) throws VMExceptionFunctionNotFound,
			VMExceptionOutOfMemory {

		BasicObject bo = get(name);
		if (bo == null && mKlass != null)
			return mKlass.getFunction(name);
		if (bo instanceof Function) {
			return new RuntimeMemberFunction(this, (Function) bo);
		}
		return null;
	}

	@Override
	public String inspect() {
		return "[VMObject]";
	}

	@Override
	public void set(ObjectName memberName, BasicObject r) throws VMException {
		data.put(memberName,r);
	}
}
