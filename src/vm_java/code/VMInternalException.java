/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

/**
 * 
 * @author davidkamphausen
 */
public class VMInternalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CodeStatement mStatement;
	private String what;
	private Exception enclosed;

	public VMInternalException(CodeStatement aThis, String string) {
		mStatement = aThis;
		what = string;
	}

	public VMInternalException(Exception e) {
		enclosed = e;
	}

	public CodeStatement getStatement() {
		return mStatement;
	}

	public String what() {
		return what;
	}

	public String toString() {
		String s = super.toString();
		if (mStatement == null)
			return s;
		else
			return s + "\n" + what + "\n Statment:" + mStatement + " line:"
					+ mStatement.info();
	}

	public Exception getEnclosed() {
		return enclosed;
	}

}
