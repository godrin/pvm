/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.types.basic.ObjectName;
import vm_java.types.basic.VMKlass;
import vm_java.types.interfaces.MemberContainer;

/**
 * 
 * @author davidkamphausen
 */
public class CustomObject implements MemberContainer {
	Map<ObjectName, BasicObject> mObjects;
	VMKlass mClass;

	public CustomObject assign(CustomObject pObject) {

		return this;
	}

	public BasicObject getInstance(ObjectName objectName) {
		return mObjects.get(objectName);
	}

	public void putInstance(ObjectName memberName, BasicObject r) {
		mObjects.put(memberName,r);
	}

	public BasicObject getStatic(ObjectName objectName) {
		return mClass.getInstance(objectName);
	}

	public void putStatic(ObjectName memberName, BasicObject r)
			throws VMException {
		mClass.putInstance(memberName, r);
	}
}
