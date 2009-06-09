/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.types;

/**
 *
 * @author davidkamphausen
 */
public class ObjectName {

    public ObjectName(String pName) {
        this.mName = pName;
    }

    public String getName() {
        return mName;
    }
    String mName;
}
