/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.interfaces.MemberProvider;

/**
 * 
 * @author davidkamphausen
 * 
 * 
 *         only simple assignment
 */
public class CodeAssignmentFromMember extends CodeStatement {
	private ObjectName lObjectName;
	private ObjectName rObjectName;
	private ObjectName rMemberName;

	public CodeAssignmentFromMember(VMContext context, SourceInfo source,
			ObjectName left, ObjectName right, ObjectName rightMember)
			throws VMExceptionOutOfMemory {
		super(context, source);
		lObjectName = left;
		rObjectName = right;
		rMemberName = rightMember;
	}

	public void execute(VMScope scope, Task parentTask)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory,
			VMException {

		BasicObject bo = scope.get(rObjectName);
		if (bo instanceof MemberProvider) {
			MemberProvider p = (MemberProvider) bo;

			scope.put(lObjectName, p.getStatic(rMemberName));
			return;
		}
		VMLog.debug("Right:" + bo);
		throw new VMException(this, "Right value was not found!");

	}

	@Override
	public String inspect() {
		return "[AssignFromMember:" + lObjectName.inspect() + "="
				+ rObjectName.inspect() + "." + rMemberName.inspect() + "]";
	}

	@Override
	public Code toCode() {
		StringBuilder b = new StringBuilder();
		b.append(lObjectName.inlineCode()).append("=");
		b.append(rObjectName.inlineCode());
		if (rMemberName != null) {
			b.append(".").append(rMemberName.inlineCode());
		}
		Code c=new Code();
		c.add(b.toString());
		return c;
	}

}
