/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class FunctionCall extends Statement {

    public FunctionCall(VMContext pContext, String pFuncName) {
        super(pContext);

        mFuncName=pFuncName;
    }

    void execute(VMScope scope) {
        BasicObject bo=scope.get(mFuncName);
        if(bo instanceof Function)
            {
            Function f=(Function)bo;
            f.execute(scope);
        }
    }

    String mFuncName;
}
