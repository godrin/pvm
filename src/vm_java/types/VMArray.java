package vm_java.types;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMArray extends BuildInKlass {
	List<BasicObject> mArray;

	public VMArray(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
		 mArray=new ArrayList<BasicObject>();
	}

	public void add(BasicObject o) {
		mArray.add(o);
	}
	
	public BasicObject __getitem(VMInteger index) {
		
		return mArray.get(index.get());
	}

}
