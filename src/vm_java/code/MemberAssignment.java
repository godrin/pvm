package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.interfaces.MemberContainer;

public class MemberAssignment extends CodeStatement {

	ObjectName lObject, lMember;
	ObjectName rObject;
	boolean mstatic;

	public MemberAssignment(VMContext context, SourceInfo source,
			ObjectName self, ObjectName leftMember, ObjectName rightObject,
			boolean pstatic) throws VMExceptionOutOfMemory, VMInternalException {
		super(context, source);
		mstatic = pstatic;

		assertNotNull(self);
		assertNotNull(leftMember);
		assertNotNull(rightObject);

		lObject = self;
		lMember = leftMember;
		rObject = rightObject;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMInternalException,
			VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		BasicObject l = scope.get(lObject);
		BasicObject r = scope.get(rObject);

		if (l instanceof MemberContainer) {
			if (mstatic) {
				((MemberContainer) l).putStatic(lMember, r);
			} else {
				((MemberContainer) l).putInstance(lMember, r);
			}
		} else {
			VMLog.debug(lObject);
			VMLog.debug(l);
			throw new VMInternalException(this, "l is not member container");
		}
	}

	@Override
	public String inspect() {
		return "[Assign:" + lObject.inspect() + "." + lMember.inspect() + "="
				+ rObject.inspect() + "]";
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add(lObject.inlineCode() + (mstatic?"@":".") + lMember.inlineCode() + "="
				+ rObject.inlineCode());
		return c;
	}

}
