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
import vm_java.runtime.MemberFunction;
import vm_java.runtime.RuntimeFunction;

/**
 * 
 * @author davidkamphausen
 */
public class Module extends BasicObject implements MemberContainer,
		FunctionProvider {

	private List<Module> mMixins = new ArrayList<Module>();
	private Map<ObjectName, BasicObject> mObjects = new TreeMap<ObjectName, BasicObject>();

	public Module(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	/*
	 * // FIXME: maybe check "send"-function first IntermedResult send(VMScope
	 * scope, ObjectName pName, List<ObjectName> pArgs) throws
	 * VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
	 * Function f = mFunctions.get(pName); if (f == null) {
	 * 
	 * for (Module mixin : mMixins) { try { IntermedResult ret =
	 * mixin.send(scope, pName, pArgs); return ret; } catch
	 * (VMExceptionFunctionNotFound e) { // just try next mixin } }
	 * 
	 * ObjectName on = getContext().intern("method_missing"); if
	 * (!pName.equals(on)) { List<ObjectName> args = new
	 * ArrayList<ObjectName>(); args.add(pName); args.addAll(pArgs); return
	 * send(scope, on, pArgs); } else { throw new VMExceptionFunctionNotFound();
	 * } } else { return f.runFunction(scope, pArgs);
	 * 
	 * // return f.compute(getContext(),pArgs); } }
	 */
	public RuntimeFunction getFunction(ObjectName name) throws VMException, VMExceptionOutOfMemory {
		BasicObject bo = get(name);
		
		if(bo instanceof RuntimeFunction)
			return (RuntimeFunction)bo;
		if (bo instanceof BuildinFunction) {
			BuildinFunction bf=(BuildinFunction)bo;
			return new CodeBuildinFunction(this, bf.method);
		}
		
		if (bo instanceof Function) {
			return new MemberFunction(this, (Function) bo);
		}
		return null;
	}

	protected BasicObject getDirect(ObjectName name) {
		return mObjects.get(name);

	}

	public BasicObject get(ObjectName name) {
		VMLog.debug("searching:"+name);
		VMLog.debug(mObjects.keySet());
		BasicObject o = getDirect(name);
		if (o == null) {
			for (Module m : mMixins) {
				o = m.get(name);
				if (o != null)
					break;
			}
		}
		VMLog.debug("found:"+o);
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
}
