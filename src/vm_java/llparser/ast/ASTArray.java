package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.VMArray;

public class ASTArray implements ASTRightValue {

	List<ASTVar> mArray=new ArrayList<ASTVar>();
	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		VMArray ar=new VMArray(context);
		
		for(ASTVar v:mArray) {
			ar.add(v.instantiate(context));
		}
		return ar;
	}
	public void add(ASTVar v) {
		mArray.add(v);
	}

}
