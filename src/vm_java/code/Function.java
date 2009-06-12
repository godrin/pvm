/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class Function extends BasicObject {

    public Function(VMContext pContext) throws VMExceptionOutOfMemory {
        super(pContext);
    }

    Map<String, BasicObject> assignParameters(Collection< ? extends BasicObject> pArgs) {
        Map<String, BasicObject> map=new TreeMap<String, BasicObject>();

        int c=0;
        for(BasicObject o:pArgs) {
        	map.put(Integer.toString(c), o);
        	c+=1;
        }
        
        
        return map;
    }

    public void execute(VMScope pScope) throws VMException {
    }
}
