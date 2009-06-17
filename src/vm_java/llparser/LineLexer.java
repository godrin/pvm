package vm_java.llparser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class LineLexer {

	abstract class Token {

	}

	class Var extends Token {
		String name;

		Var(String i) {
			name = i;
		}
	}

	class Begin extends Token {

	}

	class End extends Token {

	}

	class Assign extends Token {

	}

	class Compare extends Token {

	}

	class Equals extends Compare {

	}

	@Lex(pattern = "begin", order = 1)
	public Begin begin(String input) {
		return new Begin();
	}

	@Lex(pattern = "end", order = 2)
	public End end(String input) {
		return new End();
	}

	@Lex(pattern = "[a-z][a-zA-Z0-9_]*", order = 2)
	public Var variableName(String input) {
		return new Var(input);
	}

	@Lex(pattern = "==", order = 3)
	public Compare equals(String input) {
		return new Equals();
	}

	@Lex(pattern = "=", order = 4)
	public Assign assign(String input) {
		return new Assign();
	}

	public void run() {
		for (Annotation a : getClass().getAnnotations()) {
			System.out.println(a);
		}
		System.out.println("MUH");
		for (Method m : getClass().getMethods()) {
			System.out.println(m);
			for (Annotation a : m.getAnnotations()) {
				System.out.println(m);
				System.out.println(a);
			}
		}
	}

	public static void main(String[] args) {
		LineLexer ll = new LineLexer();
		ll.run();
	}
}
