/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vm_java.code.Code;
import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.BuildinFunction;
import vm_java.types.Function;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.interfaces.MemberContainer;
import vm_java.types.interfaces.MemberProvider;

/**
 * 
 * @author davidkamphausen
 */
public class VMModule extends VMBuildinObjectBase implements MemberContainer,
		FunctionProvider, MemberProvider {

	private List<VMModule> mMixins = new ArrayList<VMModule>();
	protected Map<ObjectName, BasicObject> mStaticObjects = new TreeMap<ObjectName, BasicObject>();
	protected Map<ObjectName, BasicObject> mInstanceObjects = new TreeMap<ObjectName, BasicObject>();
	private String mName;

	public VMModule(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public RuntimeFunction getFunction(ObjectName name)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		BasicObject bo = getStatic(name);

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

	protected BasicObject getStaticDirect(ObjectName name) {
		return mStaticObjects.get(name);

	}

	public BasicObject getStatic(ObjectName name) {
		VMLog.debug("searching:" + name);
		VMLog.debug(mStaticObjects.keySet());
		BasicObject o = getStaticDirect(name);
		if (o == null) {
			for (VMModule m : mMixins) {
				o = m.getStatic(name);
				if (o != null)
					break;
			}
		}
		
		if(o==null && getName().equals("root")) {
			return getInstance(name);
		}
		
		VMLog.debug("found:" + o);
		return o;
	}

	public void putStatic(ObjectName name, BasicObject bo) throws VMException {
		mStaticObjects.put(name, bo);
	}

	
	protected BasicObject getInstanceDirect(ObjectName name) {
		return mInstanceObjects.get(name);

	}
	public BasicObject getInstance(ObjectName name) {
		BasicObject o = getInstanceDirect(name);
		if (o == null) {
			for (VMModule m : mMixins) {
				o = m.getInstance(name);
				if (o != null)
					break;
			}
		}
		VMLog.debug("found:" + o);
		return o;
	}

	public void putInstance(ObjectName name, BasicObject bo) throws VMException {
		mInstanceObjects.put(name, bo);
	}
	
	
	@Override
	public String inspect() {
		return toCode().toString();
		// return "[Module]";
	}
	
	protected void addStaticsAsInstanceTo(VMModule k) throws VMException {
		for(Map.Entry<ObjectName, BasicObject> e:mStaticObjects.entrySet()) {
			k.putInstance(e.getKey(), e.getValue());
		}
	}

	
/*
	public VMModule _newModule(VMContext pContext)
			throws VMExceptionOutOfMemory, VMException {
		VMModule mod = new VMModule(pContext);
		mod.addFunctionsTo(this);
		return mod;
	}
	*/
/*
	protected void addFunctionsTo(VMModule mod) throws VMException {
		for (Map.Entry<ObjectName, BasicObject> entry : mObjects.entrySet()) {
			mod.put(entry.getKey(), entry.getValue());
		}
	}
*/
	public void include(VMModule mod) throws VMException {
		mMixins.add(mod);
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("module " + mName);
		Code i = new Code();
		for (VMModule m : mMixins) {
			i.add("included " + m.toCode().indent());
		}
		for (Map.Entry<ObjectName, BasicObject> e : mStaticObjects.entrySet()) {
			i.add("static:"+e.getKey().inlineCode() + "=" + e.getValue().inlineCode());
		}
		for (Map.Entry<ObjectName, BasicObject> e : mInstanceObjects.entrySet()) {
			i.add("inst:"+e.getKey().inlineCode() + "=" + e.getValue().inlineCode());
		}
		c.add(i.indent());
		c.add("end");

		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString();
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getName() {
		return mName;
	}

}
