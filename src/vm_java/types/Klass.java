/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeMemberFunction;

/**
 * 
 * @author davidkamphausen
 */
public class Klass extends VMModule {
	private Klass mParent;
	// these are klass-methods and klass-members
	private Map<ObjectName, BasicObject> mObjects = new TreeMap<ObjectName, BasicObject>();

	public Klass(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	BasicObject create() {
		return BasicObject.nil;
	}

	public BasicObject get(ObjectName name) {

		VMLog.debug("TRYING TO GET OUT OF CLASS:" + name);
		BasicObject o = super.get(name);
		if (o == null) {
			if (mParent != null) {
				o = mParent.get(name);
			}
		}
		return o;
	}
	
	public RuntimeFunction getFunction(ObjectName name) throws VMException, VMExceptionOutOfMemory {
		BasicObject bo = getStatic(name);
		
		if(bo instanceof RuntimeFunction)
			return (RuntimeFunction)bo;
		if (bo instanceof BuildinFunction) {
			BuildinFunction bf=(BuildinFunction)bo;
			return new CodeBuildinFunction(this, bf.method);
		}
		
		if (bo instanceof Function) {
			return new RuntimeMemberFunction(this, (Function) bo);
		}
		return null;
	}

	private BasicObject getStatic(ObjectName name) {
		BasicObject bo=mObjects.get(name);
		if(bo==null) {
			if(mParent!=null)
				return mParent.getStatic(name);
		}
		return bo;
	}

	public void putStatic(ObjectName key, BuildinFunction value) {
		mObjects.put(key,value);
	}
}
