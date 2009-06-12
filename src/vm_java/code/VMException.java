/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

/**
 *
 * @author davidkamphausen
 */
public class VMException extends Exception {

    private Statement mStatement;
    private String what;

    public VMException(Statement aThis, String string) {
        mStatement = aThis;
        what = string;
    }
}
