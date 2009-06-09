/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code.lib;

import java.util.ArrayList;
import java.util.List;
import vm_java.code.Function;
import vm_java.context.BasicObject;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class VMPackage {

    class PackageFunction extends Function {

        PackageFunction(VMContext pContext, String pName) {
            super(pContext);
            mName = pName;
        }

        @Override
        public void execute(VMScope pScope) {
            System.out.println("PackacgeFunction");
        }
        String mName;
    }

    VMPackage() {
    }

    void addFunc(String pName) {
        mFunctions.add(pName);
    }

    public void init() {
    }

    public BasicObject get(VMContext pContext, String pName) {

        if (mFunctions.contains(pName)) {
            return new PackageFunction(pContext, pName);
        }
        return null;
    }

    public void registerPackage(VMScope pScope) {
        pScope.addPackage(this);
    }
    List<String> mFunctions = new ArrayList<String>();
}
