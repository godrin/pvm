package vm_java.types;

import java.util.Map;
import java.util.TreeMap;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMHash extends BuildInKlass {
	
	Map<BasicObject,BasicObject> map;

	public VMHash(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
		map=new TreeMap<BasicObject,BasicObject>();
	}
	
	public BasicObject get(BasicObject key) {
		return map.get(key);
	}
	public BasicObject set(BasicObject key,BasicObject value) {
		map.put(key,value);
		return value;
	}
	
	public static VMHash _new(VMContext pContext) throws VMExceptionOutOfMemory {
		return new VMHash(pContext);
	}

}
