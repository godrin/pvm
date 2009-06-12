/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import vm_java.code.VMException;

/**
 *
 * @author davidkamphausen
 */
public class VMExceptionOutOfMemory extends Exception {

    VMExceptionOutOfMemory() {
        super("Out of memory");
    }
}
