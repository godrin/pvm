package vm_java.types.interfaces;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.types.basic.ObjectName;

public interface MemberContainer {
	public BasicObject getStatic(ObjectName objectName);
	public void putStatic(ObjectName memberName, BasicObject r) throws VMException;
	
	public BasicObject getInstance(ObjectName objectName);
	public void putInstance(ObjectName memberName, BasicObject r) throws VMException;

}
