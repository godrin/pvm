/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.logging.Level;
import java.util.logging.Logger;

import vm_java.code.IntermedResult.Result;
import vm_java.code.lib.VMPackage;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class IncludePackage extends CodeStatement {

    public IncludePackage(VMContext pContext,SourceInfo source,Class<? extends VMPackage> pClass) throws VMExceptionOutOfMemory {
        super(pContext,source);
        mClass=pClass;
    }

    @Override
	public
    IntermedResult execute(VMScope scope) throws VMExceptionOutOfMemory {
        try {
            VMPackage pkg = mClass.newInstance();
            pkg.init();
            pkg.registerPackage(scope);
        } catch (InstantiationException ex) {
            Logger.getLogger(IncludePackage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(IncludePackage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (VMException e) {
            Logger.getLogger(IncludePackage.class.getName()).log(Level.SEVERE, null, e);
		}
        return new IntermedResult(BasicObject.nil,Result.NONE);
    }

    Class<? extends VMPackage> mClass;
}
