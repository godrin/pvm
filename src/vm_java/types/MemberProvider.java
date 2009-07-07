package vm_java.types;

import vm_java.context.BasicObject;

public interface MemberProvider {
	BasicObject get(ObjectName objectName);
}
