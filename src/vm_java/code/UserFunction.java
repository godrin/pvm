/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.code.Statement.Result;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.Function;


/**
 *
 * @author davidkamphausen
 */
public class UserFunction extends Function {
    CodeBlock mBlock;

    UserFunction(CodeBlock pBlock) throws VMExceptionOutOfMemory {
        super(pBlock.getContext());
        mBlock = pBlock;
    }

    @Override
    public Result execute(VMScope pScope) throws VMException {
        return mBlock.execute(pScope);
    }
}
