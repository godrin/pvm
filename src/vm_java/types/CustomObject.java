/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.Map;

/**
 *
 * @author davidkamphausen
 */
public class CustomObject {


  public CustomObject assign(CustomObject pObject)
          {

      return this;
  }

  Map<ObjectName,CustomObject> mObjects;
  Class mClass;
}
