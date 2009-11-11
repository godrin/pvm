package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.DoReturn;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeMemberFunction;
import vm_java.types.foundation.ObjectName;

public class ASTReturn extends AST implements ASTStatementInterface {
	ASTVar mVar;
	Type type;

	public enum Type {
		LOCAL, FAR
	};

	public ASTReturn(SourceInfo source, ASTVar v, Type pType) {
		super(source);
		mVar = v;
		type = pType;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		ObjectName varName = context.intern(mVar.name);
		return new DoReturn(context, source,varName,type);
//		return new LocalAssignment(context, source, context
	//			.intern(RuntimeMemberFunction.RETVALUE), varName);
	}

}
