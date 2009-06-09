/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;
import vm_java.context.BasicObject;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class Block extends BasicObject{

    public Block(VMContext pContext) {
        super(pContext);

        statements=new ArrayList<Statement>();
    }

    List<Statement> statements;

    public void add(Statement pStatement) {
        statements.add(pStatement);
    }

    void execute(VMScope pScope) {
        for(Statement s:statements)
            s.execute(pScope);
    }
}
