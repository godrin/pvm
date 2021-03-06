package vm_java.types.foundation;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMBuildinObjectBase;

public class VMArray extends VMBuildinObjectBase {
	List<BasicObject> mArray;

	public VMArray(VMContext context)
			throws VMExceptionOutOfMemory {
		super(context);
		mArray = new ArrayList<BasicObject>();
	}

	public void add(BasicObject o) {
		mArray.add(o);
	}

	public BasicObject __getitem(VMInteger index) {

		return mArray.get(index.get());
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("[");
		boolean first = true;
		for (BasicObject bo : mArray) {
			if (first) {
				first = false;
			} else {
				c.addToLastLine(", ");
			}
			c.addToLastLine(bo.inlineCode());
		}
		c.addToLastLine("]");
		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString().replace("\n", "");
	}

	@Override
	public String inspect() {
		return inlineCode();
	}

}
