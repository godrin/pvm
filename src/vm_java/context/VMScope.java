/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import vm_java.code.Block;
import vm_java.code.lib.VMPackage;

/**
 *
 * @author davidkamphausen
 */
public class VMScope {

    VMScope(VMContext pContext, Block mBlock) {
        mContext = pContext;
    }

    public BasicObject get(String name) {
        BasicObject ret = null;
        ret = mReferences.get(name);

        if (ret == null)
        {
            Iterator<VMPackage> iterPackage=mPackages.iterator();
            while(iterPackage.hasNext())
            {
                ret = iterPackage.next().get(getContext(),name);
                if (ret != null) {
                    break;
                }

            }
        }
        return ret;
    }

    void put(String pName, BasicObject pObject) {
        mReferences.put(pName, pObject);
    }

    public void addPackage(VMPackage pPackage) {
        mPackages.add(pPackage);
    }

    public VMContext getContext()
            {
        return mContext;
    }

    Map<String, BasicObject> mReferences = new TreeMap<String, BasicObject>();
    List<VMPackage> mPackages = new ArrayList<VMPackage>();
    VMScope mParentScope;
    VMContext mContext;
}
