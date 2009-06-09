/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code.lib;

import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class StdIO extends VMPackage {

    public StdIO() {
    }

    public void init() {
        addFunc("puts");
    }

    public void puts(String i) {
        System.out.println(i);
    }
}
