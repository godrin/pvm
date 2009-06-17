/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.logging.Level;
import java.util.logging.Logger;
import vm_java.code.lib.VMPackage;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class IncludePackage extends Statement {

    public IncludePackage(VMContext pContext,Class<? extends VMPackage> pClass) throws VMExceptionOutOfMemory {
        super(pContext);
        mClass=pClass;
    }

    @Override
	public
    Result execute(VMScope scope) {
        try {
            VMPackage pkg = mClass.newInstance();
            pkg.init();
            pkg.registerPackage(scope);
        } catch (InstantiationException ex) {
            Logger.getLogger(IncludePackage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(IncludePackage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Result.NONE;
    }

    Class<? extends VMPackage> mClass;
}
