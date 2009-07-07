/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.runtime.RuntimeFunction;

/**
 * 
 * @author davidkamphausen
 */
public class VMModule extends BasicObject implements MemberContainer,
		FunctionProvider, MemberProvider {

	private List<VMModule> mMixins = new ArrayList<VMModule>();
	private Map<ObjectName, BasicObject> mObjects = new TreeMap<ObjectName, BasicObject>();

	public VMModule(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public RuntimeFunction getFunction(ObjectName name) throws VMException,
			VMExceptionOutOfMemory {
		BasicObject bo = get(name);

		if (bo instanceof RuntimeFunction)
			return (RuntimeFunction) bo;
		if (bo instanceof BuildinFunction) {
			BuildinFunction bf = (BuildinFunction) bo;
			return new CodeBuildinFunction(this, bf.method);
		}

		if (bo instanceof Function) {
			return new RuntimeMemberFunction(this, (Function) bo);
		}
		return null;
	}

	protected BasicObject getDirect(ObjectName name) {
		return mObjects.get(name);

	}

	public BasicObject get(ObjectName name) {
		VMLog.debug("searching:" + name);
		VMLog.debug(mObjects.keySet());
		BasicObject o = getDirect(name);
		if (o == null) {
			for (VMModule m : mMixins) {
				o = m.get(name);
				if (o != null)
					break;
			}
		}
		VMLog.debug("found:" + o);
		return o;
	}

	public void put(ObjectName name, BasicObject bo) throws VMException {
		mObjects.put(name, bo);
	}

	@Override
	public void set(ObjectName memberName, BasicObject r) throws VMException {
		put(memberName, r);

	}

	@Override
	public String inspect() {
		return "[Module]";
	}

	public static VMModule _new(VMContext pContext)
			throws VMExceptionOutOfMemory {
		return new VMModule(pContext);
	}

}
