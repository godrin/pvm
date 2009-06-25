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

// TODO: merge with Assignment
public class MemberAssignment extends CodeStatement {

	ObjectName lObject, lMember;
	BasicObject rObject;
	ObjectName rMember;

	public MemberAssignment(VMContext context, SourceInfo source,
			ObjectName self, ObjectName leftMember, BasicObject rightObject)
			throws VMExceptionOutOfMemory {
		super(context, source);
		lObject = self;
		lMember = leftMember;
		rObject = rightObject;
		rMember = null;
	}

	@Override
	public IntermedResult execute(VMScope scope) throws VMException, VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		BasicObject l = scope.get(lObject);
		BasicObject r = rObject;
		if (r instanceof ObjectName)
			r = scope.get((ObjectName) r);

		if (rMember != null) {
			if (r instanceof MemberContainer) {
				r = ((MemberContainer) r).get(rMember);
			}
		}
		r=resolve(r,scope);
		if (lMember != null) {
			if (l instanceof MemberContainer) {
				((MemberContainer) l).set(lMember, r);
				return new IntermedResult(lMember, Result.NONE);
			} else {
				VMLog.debug(lObject);
				VMLog.debug(l);
				throw new VMException(this, "l is not member container");
			}
		} else {
			
			scope.put(lObject, r);
			return new IntermedResult(lObject, Result.NONE);
		}
	}

	private BasicObject resolve(BasicObject o, VMScope scope) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
		if(o instanceof CodeResolveVar) {
			IntermedResult res=o.compute(scope);
			VMLog.debug("Trying to resolve:"+o);
			if(res.result()!=Result.NONE)
				return o;
			o=res.content();
			VMLog.debug("resolved:"+o);
		}
		return o;
	}
}
