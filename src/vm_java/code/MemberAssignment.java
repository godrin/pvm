package vm_java.code;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.types.MemberContainer;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class MemberAssignment extends CodeStatement {

	ObjectName lObject, lMember;
	ObjectName rObject;

	public MemberAssignment(VMContext context, SourceInfo source,
			ObjectName self, ObjectName leftMember, ObjectName rightObject)
			throws VMExceptionOutOfMemory, VMException {
		super(context, source);

		assertNotNull(self);
		assertNotNull(leftMember);
		assertNotNull(rightObject);

		lObject = self;
		lMember = leftMember;
		rObject = rightObject;
	}

	@Override
	public void execute(VMScope scope) throws VMException,
			VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		BasicObject l = scope.get(lObject);
		BasicObject r = scope.get(rObject);

		if (l instanceof MemberContainer) {
			((MemberContainer) l).set(lMember, r);
		} else {
			VMLog.debug(lObject);
			VMLog.debug(l);
			throw new VMException(this, "l is not member container");
		}
	}

	@Override
	public String inspect() {
		return "[Assign:"+lObject.inspect()+"."+lMember.inspect()+"="+rObject.inspect()+"]";
	}

}
