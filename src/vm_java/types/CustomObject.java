/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;

import vm_java.context.BasicObject;

/**
 * 
 * @author davidkamphausen
 */
public class CustomObject implements MemberContainer {
	Map<ObjectName, BasicObject> mObjects;
	Klass mClass;

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
