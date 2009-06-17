package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.MemberContainer;
import vm_java.types.ObjectName;


// TODO: merge with Assignment
public class MemberAssignment extends Statement {

	ObjectName lObject, lMember;
	ObjectName rObject, rMember;

	public MemberAssignment(VMContext context, ObjectName self,
			ObjectName intern, ObjectName intern2)
			throws VMExceptionOutOfMemory {
		super(context);
		lObject = self;
		lMember = intern;
		rObject = intern2;
		rMember = null;
	}

	@Override
	public Result execute(VMScope scope) throws VMException {
		BasicObject l=scope.get(lObject);
		BasicObject r=scope.get(rObject);
		if(rMember!=null) {
			if(r instanceof MemberContainer) {
		  	  r=((MemberContainer)r).get(rMember);
			}
		}
		if(lMember!=null) {
			if(l instanceof MemberContainer) {
				((MemberContainer)l).set(lMember,r);
			} else {
				throw new VMException(this, "l is not member container");
			}
		} else {
			scope.put(lObject, r);
		}
		return null;
	}
}
