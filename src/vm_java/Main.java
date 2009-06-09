/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java;

import vm_java.code.Block;
import vm_java.code.FunctionCall;
import vm_java.code.IncludePackage;
import vm_java.code.Program;
import vm_java.code.lib.StdIO;
import vm_java.context.VMContext;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

      VM vm=new VM();

      VMContext context=vm.createContext();

      Block block=context.createBlock();

      block.add(new IncludePackage(context,StdIO.class));
      block.add(new FunctionCall(context,"MUH"));

      Program program=context.createProgram(block);

      VMScope scope=context.createScope();

      program.execute(scope);


    }

}
