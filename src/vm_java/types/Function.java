/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;

import vm_java.code.Block;
import vm_java.code.Statement;
import vm_java.code.VMException;
import vm_java.code.Statement.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 * 
 * @author davidkamphausen
 */
public class Function extends BasicObject {

	public BasicObject execute(Context pContext) {
		return BasicObject.nil;
	}

	Map<ObjectName, Object> mArgs;
	Block mBlock;

	public BasicObject execute(VMContext context, Arguments args) {
		// TODO Auto-generated method stub
		return null;
	}

	public Function(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public Map<String, BasicObject> assignParameters(
			Collection<? extends BasicObject> pArgs) {
		Map<String, BasicObject> map = new TreeMap<String, BasicObject>();

		int c = 0;
		for (BasicObject o : pArgs) {
			map.put(Integer.toString(c), o);
			c += 1;
		}

		return map;
	}

	public Statement.Result execute(VMScope pScope) throws VMException {
		// FIXME
		// TODO
		return Result.NONE;
	}
}
