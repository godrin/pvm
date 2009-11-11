package vm_java.types.interfaces;

import vm_java.context.BasicObject;
import vm_java.types.foundation.ObjectName;

public interface MemberProvider {
	BasicObject get(ObjectName objectName);
}
