package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public interface ASTStatementInterface {

	CodeStatement instantiate(VMContext context) throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException;

}
