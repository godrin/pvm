/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;
import javax.naming.Context;
import vm_java.code.Block;
import vm_java.context.BasicObject;

/**
 *
 * @author davidkamphausen
 */
public class Function {

  public BasicObject execute(Context pContext)
          {
      return BasicObject.nil;
  }
    
  Map<ObjectName,Object> mArgs;
  Block mBlock;
}
