/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;

import vm_java.context.BasicObject;
import vm_java.types.basic.VMKlass;
import vm_java.types.foundation.ObjectName;
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

	@Override
	public BasicObject get(ObjectName objectName) {
		return mObjects.get(objectName);
	}

	@Override
	public void set(ObjectName memberName, BasicObject r) {
		mObjects.put(memberName,r);
	}
}
