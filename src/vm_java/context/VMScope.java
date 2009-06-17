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

import vm_java.types.Function;
import vm_java.code.Statement;
import vm_java.code.lib.VMPackage;
import vm_java.types.ObjectName;

/**
 *
 * @author davidkamphausen
 */
public class VMScope {
    Map<String, BasicObject> mReferences = new TreeMap<String, BasicObject>();
    List<VMPackage> mPackages = new ArrayList<VMPackage>();
    VMScope mParentScope;
    VMContext mContext;
    Map<String, BasicObject> mArgs = null;
    /** return value of this block. */
    BasicObject mReturn=null;
    /** return value of last function call */
    BasicObject mLastReturn=null;

    public VMScope(VMContext pContext) {
        mContext = pContext;
    }

    public VMScope(VMScope scope, Function f) {
    	mContext = scope.getContext();
    	mParentScope = scope;
		// TODO Auto-generated constructor stub
	}
    
    public void setReturn(BasicObject pReturn) {
    	mReturn=pReturn;
    }
    
    public BasicObject getReturn() {
    	return mReturn;
    }

	public BasicObject get(String name) {
        BasicObject ret = null;
        ret = mReferences.get(name);

        if (ret == null) {
            Iterator<VMPackage> iterPackage = mPackages.iterator();
            while (iterPackage.hasNext()) {
                VMPackage mp=iterPackage.next();
                
                ret = mp.get(getContext(), name);
                if (ret != null) {
                    break;
                }

            }
        }
        VMLog.debug("get:"+name+":"+ret);
        
        return ret;
    }

    public void setArgs(Map<String, BasicObject> pArgs) {
    	mArgs=pArgs;
    }

    public void put(String pName, BasicObject pObject) {
        mReferences.put(pName, pObject);
    }

    public void addPackage(VMPackage pPackage) {
        mPackages.add(pPackage);
    }

    public VMContext getContext() {
        return mContext;
    }

    public Statement getStatement() {
        return null; // FIXME
    }
	public Map<String, BasicObject> getFuncCallArgs() {
		return mArgs;
	}

	public BasicObject get(ObjectName entry) {
		return get(entry.getName());
	}

	public void put(ObjectName objectName, BasicObject value) {
		mReferences.put(objectName.getName(), value);
	}

	/**
	 * @return the mLastReturn
	 */
	public synchronized final BasicObject getLastReturn() {
		return mLastReturn;
	}

	/**
	 * @param lastReturn the mLastReturn to set
	 */
	public synchronized final void setLastReturn(BasicObject lastReturn) {
		mLastReturn = lastReturn;
	}
	
	
}
