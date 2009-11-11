package vm_java.types.interfaces;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.types.foundation.ObjectName;

public interface MemberContainer {
	public BasicObject get(ObjectName objectName);

	public void set(ObjectName memberName, BasicObject r) throws VMException;
}
