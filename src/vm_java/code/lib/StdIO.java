/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code.lib;


/**
 *
 * @author davidkamphausen
 */
public class StdIO extends VMPackage {

    public StdIO() {
    }

    public void init() {
        addFunc("puts");
        addFunc("sleep");
    }
    
    public void puts(String i) {
        System.out.println(i);
    }
    
    /**
     * TODO
     * This one should not be used, because control should be given back to
     * VMThread instead, so that other things can be done
     * @param i
     * @throws InterruptedException
     */
    public void sleep(Integer i) throws InterruptedException {
    	Thread.sleep(i);
    }
}
