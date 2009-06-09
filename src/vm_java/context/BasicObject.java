/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

/**
 *
 * @author davidkamphausen
 */
public class BasicObject {

    public BasicObject(VMContext pContext) {
        mContext = pContext;
        mContext.add(this);
    }

    public VMContext getContext()
            {
        return mContext;
    }

    VMContext mContext;
    static public BasicObject nil = null;
}
