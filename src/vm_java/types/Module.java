/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.MemberFunction;

/**
 * 
 * @author davidkamphausen
 */
public class Module extends BasicObject {

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
	public MemberFunction getFunction(ObjectName name) throws VMException {
		BasicObject bo = get(name);
		if (bo instanceof Function) {
			return new MemberFunction(this, (Function) bo);
		}
		return null;
	}

	protected BasicObject getDirect(ObjectName name) {
		return mObjects.get(name);

	}

	public BasicObject get(ObjectName name) {
		BasicObject o = getDirect(name);
		if (o == null) {
			for (Module m : mMixins) {
				o = m.get(name);
				if (o != null)
					break;
			}
		}
		return o;
	}

	public void put(ObjectName name, BasicObject bo) {
		mObjects.put(name, bo);
	}
}
