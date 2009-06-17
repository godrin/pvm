package vm_java.llparser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Lex {

	//public String value();

	public String pattern();

	public int order();

}
