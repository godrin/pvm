/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.Log;
import vm_java.types.Function;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
@Deprecated
public class UserFunction extends Function {
	CodeBlock mBlock;
	List<ObjectName> mArgs;

	public UserFunction(VMContext pContext, CodeBlock pBlock,
			List<ObjectName> pArgs) throws VMExceptionOutOfMemory {
		super(pContext);
		mBlock = pBlock;
		mArgs = pArgs;
	}

	@Override
	public IntermedResult runFunction(VMScope scope, List<? extends BasicObject> args)
			throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		VMScope subScope = new VMScope(scope, this);
		int i=0;
		Log.debug("ARGSSIZE:"+args.size());
		if(args.size()!=mArgs.size())
			throw new VMException(null,"Argsize is different!");
		
		for(;i<args.size();i++) {
			subScope.put(mArgs.get(i), args.get(i));
			Log.debug("Adding to subscope:"+mArgs.get(i)+":="+args.get(i));
		}

		IntermedResult res=mBlock.execute(subScope);
		return res;
	}
}
