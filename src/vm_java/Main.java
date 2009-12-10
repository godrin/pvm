/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java;

import vm_java.code.BlockIsFinalException;
import vm_java.code.VMInternalException;
import vm_java.context.VMExceptionOutOfMemory;

/**
 *
 * @author davidkamphausen
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws BlockIsFinalException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws VMExceptionOutOfMemory, VMInternalException, BlockIsFinalException, InterruptedException {
/*
      VM vm=new VM();

      VMContext context=vm.createContext();

      CodeBlock block=context.createBlock();

      block.add(new IncludePackage(context,StdIO.class));
      block.add(new FunctionCall(context,context.intern("puts"), new String[]{"MUUkjhsdfUU"}));
      
      ObjectName on=context.intern("myVar");
      VMString s=new VMString(context);
      s.setContent("someString");
      block.add(new Assignment(context,on,s));
      block.add(new FunctionCall(context,context.intern("puts"), new Object[]{on}));
      block.add(new FunctionCall(context,context.intern("sleep"), new Object[]{2000}));
      block.add(new FunctionCall(context,context.intern("puts"), new Object[]{on}));

      Program program=context.createProgram(block);

      vm.run(program);
      vm.run();
      Log.debug("running");
      vm.join();
      
//      program.execute(scope);
      
      Log.debug("MEM:"+vm.getMemoryUsage());

*/
    }

}
