package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public interface RV {

	BasicObject instantiate(VMContext context) throws VMExceptionOutOfMemory, BlockIsFinalException, VMException;

}
