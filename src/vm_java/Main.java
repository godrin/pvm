/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java;

import vm_java.code.Assignment;
import vm_java.code.Block;
import vm_java.code.BlockIsFinalException;
import vm_java.code.FunctionCall;
import vm_java.code.IncludePackage;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.code.lib.StdIO;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.ObjectName;
import vm_java.types.VMString;

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
    public static void main(String[] args) throws VMExceptionOutOfMemory, VMException, BlockIsFinalException, InterruptedException {

      VM vm=new VM();

      VMContext context=vm.createContext();

      Block block=context.createBlock();

      block.add(new IncludePackage(context,StdIO.class));
      block.add(new FunctionCall(context,"puts", new String[]{"MUUkjhsdfUU"}));
      
      ObjectName on=context.intern("myVar");
      VMString s=new VMString(context);
      s.setContent("someString");
      block.add(new Assignment(context,on,s));
      block.add(new FunctionCall(context,"puts", new Object[]{on}));
      block.add(new FunctionCall(context,"sleep", new Object[]{2000}));
      block.add(new FunctionCall(context,"puts", new Object[]{on}));

      Program program=context.createProgram(block);

      vm.run(program);
      vm.run();
      System.out.println("running");
      vm.join();
      
//      program.execute(scope);
      
      System.out.println("MEM:"+vm.getMemoryUsage());


    }

}
