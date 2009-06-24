package vm_java.types;

import vm_java.code.VMException;
import vm_java.context.BasicObject;

public interface MemberContainer {
	public BasicObject get(ObjectName objectName);

	public void set(ObjectName memberName, BasicObject r) throws VMException;
}
