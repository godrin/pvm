package vm_java.types.basic;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.Code;
import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.DontExpose;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.interfaces.MemberContainer;
import vm_java.types.interfaces.MemberProvider;

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

	public BasicObject getStatic(ObjectName name) {
		return mKlass.getInstance(name);
	}

	public RuntimeFunction getFunction(ObjectName name)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {

		BasicObject bo = getStatic(name);
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

	public void putInstance(ObjectName memberName, BasicObject r)
			throws VMException {
		data.put(memberName, r);
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("<Klass #" + mKlass.getID() + ">");
		Code i = new Code();
		for (Map.Entry<ObjectName, BasicObject> e : data.entrySet()) {
			i.add(e.getKey().inlineCode() + "=" + e.getValue().inlineCode());
		}
		c.add(i.indent());
		c.add("</Klass #" + mKlass.getID() + ">");
		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString();
	}

	public BasicObject getInstance(ObjectName objectName) {
		return data.get(objectName);
	}

	public void putStatic(ObjectName memberName, BasicObject r)
			throws VMException {
		mKlass.putInstance(memberName, r);
	}
}
