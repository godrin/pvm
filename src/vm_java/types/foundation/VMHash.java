package vm_java.types.foundation;

import java.util.Map;
import java.util.TreeMap;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.VMBuildinObjectBase;

public class VMHash extends VMBuildinObjectBase {

	Map<BasicObject, BasicObject> map;

	public VMHash(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
		map = new TreeMap<BasicObject, BasicObject>();
	}

	public BasicObject get(BasicObject key) {
		return map.get(key);
	}

	public BasicObject set(BasicObject key, BasicObject value) {
		map.put(key, value);
		return value;
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("{");
		boolean first = true;
		for (Map.Entry<BasicObject, BasicObject> e : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				c.addToLastLine(", ");
			}
			c.addToLastLine(e.getKey().inlineCode() + "=>"
					+ e.getValue().inlineCode());

		}
		c.addToLastLine("}");
		return c;
	}

	@Override
	public String inlineCode() {
		return toCode().toString().replace("\n","");
	}

	@Override
	public String inspect() {
		return inlineCode();
	}

}
