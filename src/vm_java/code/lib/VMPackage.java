/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code.lib;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.runtime.PackageFunction;
import vm_java.types.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public class VMPackage extends BasicObject{



	VMPackage(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public List<String> getFunctions() {
		return mFunctions;
	}

	void addFunc(String pName) {
		mFunctions.add(pName);
	}

	public void init() {
	}

	/*
	public BasicObject get(VMContext pContext, String pName) {

		if (mFunctions.contains(pName)) {
			try {
				return new PackageFunction(this,pContext, pName);
			} catch (VMExceptionOutOfMemory ex) {
				Logger.getLogger(VMPackage.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		return null;
	}
*/
	public void registerPackage(VMScope pScope) throws VMExceptionOutOfMemory {
		pScope.addPackage(this);
	}

	List<String> mFunctions = new ArrayList<String>();

	public ObjectName getName(VMContext context) throws VMExceptionOutOfMemory {
		String name=getClass().toString().replaceAll(".*\\.","");
		return context.intern(name);
	}

	public PackageFunction getFunction(ObjectName methodName) throws VMExceptionOutOfMemory {
		String n=methodName.toSymbolName();
		return new PackageFunction(this,methodName.getContext(),n);
	}
}
