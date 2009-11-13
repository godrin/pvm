package vm_java.types.interfaces;

import vm_java.context.BasicObject;
import vm_java.types.basic.ObjectName;

public interface MemberProvider {
	BasicObject getStatic(ObjectName objectName);
}
