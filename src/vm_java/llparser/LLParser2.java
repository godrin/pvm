package vm_java.llparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LineLexer2.Lex;
import vm_java.llparser.LineLexer2.Result;
import vm_java.llparser.LineLexer2.SYMBOLS;
import vm_java.llparser.ast.ASTString;
import vm_java.llparser.ast.Assign;
import vm_java.llparser.ast.ParsedBlock;
import vm_java.llparser.ast.Function;
import vm_java.llparser.ast.MethodCall;
import vm_java.llparser.ast.Program;
import vm_java.llparser.ast.RValue;
import vm_java.llparser.ast.Statement;
import vm_java.llparser.ast.Var;

public class LLParser2 {

	public static class ParseError extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -683205404466665718L;

	}

	private VMContext mContext;
	private Result mResult;
	private List<String> lines = new ArrayList<String>();
	private List<Result> results = new ArrayList<Result>();
	private LineLexer2 lexer;
	int lineNo = 0;

	public LLParser2(VMContext pContext) {
		mContext = pContext;
		lexer = new LineLexer2();
	}

	Program parse(String content) throws ParseError {
		for (String line : content.split("\n")) {
			lines.add(line);
		}
		Program program = new Program();
		ParsedBlock block = new ParsedBlock();
		program.add(block);
		fetchToken();
		while (mResult != null) {
			Statement s = parseLine();
			if (s != null)
				block.add(s);
		}

		return program;
	}

	private SYMBOLS fetchToken() throws ParseError {
		if (results.size() == 0) {
			if (lines.size() == 0) {
				mResult = null;
				return SYMBOLS.EOF;
			}
			String line = lines.get(0);
			lines.remove(0);
			lineNo += 1;
			results = lexer.lex(line);
			results = filterWhitespaces(results);

			Lex lex = new Lex("", SYMBOLS.NEWLINE);
			results.add(new Result(lex, "\n"));
		}
		mResult = results.get(0);
		results.remove(0);
		return mResult.lex.symbol;
	}

	private List<Result> filterWhitespaces(List<Result> results2) {
		List<Result> n = new ArrayList<Result>();
		for (Result x : results2) {
			if (x.lex.symbol != SYMBOLS.WHITESPACE) {
				n.add(x);
			}
		}
		return n;
	}

	private Statement parseLine() throws ParseError {
		Statement s = null;
		if (mResult.lex.symbol == SYMBOLS.VAR) {
			Var v = parseVar();
			SYMBOLS t = fetchToken();
			if (t == SYMBOLS.ASSIGN) {
				s = parseAssign(v);
			} else if (t == SYMBOLS.DOT) {
				s = parseMemberAccess(v);
			} else {
				parseError();
			}
		} else {
			parseError();
		}
		if (s == null)
			parseError();
		if (token() != SYMBOLS.NEWLINE)
			parseError(); //
		else if (token() == null)
			return null;
		else
			fetchToken();
		return s;
	}

	private Statement parseMemberAccess(Var v) throws ParseError {
		fetchToken();
		Var m = parseVar();
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.BRACES_OPEN) {
			List<Var> ps = parseParameters();
			fetchToken();
			return new MethodCall(v, m, ps);
		}
		parseError();
		// TODO Auto-generated method stub
		return null;
	}

	private void parseError() throws ParseError {
		System.out.println("Parse error in line " + lineNo);
		System.out.print("Before: ");
		LineLexer2.output(mResult);
		LineLexer2.output(results);
		throw new ParseError();
		// TODO Auto-generated method stub

	}

	SYMBOLS token() {
		return mResult.lex.symbol;
	}

	private Var parseVar() throws ParseError {
		Var v = new Var(mResult.string);
		return v;
	}

	private Assign parseAssign(Var lValue) throws ParseError {
		SYMBOLS t = fetchToken();
		Assign s = null;
		if (t == SYMBOLS.BEGIN) {
			s = new Assign(lValue, parseBegin());
		} else if (t == SYMBOLS.TYPE || t == SYMBOLS.VAR) {
			s = new Assign(lValue, parseRValue());
		} else if (t == SYMBOLS.STRING) {
			s = new Assign(lValue, new ASTString(mResult.string));
		} else {
			parseError();
		}
		if (s == null)
			parseError();
		fetchToken();
		return s;
	}

	private RValue parseRValue() throws ParseError {
		Var v = parseVar();
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.DOT) {
			fetchToken();
			Var m = parseVar();
			t = fetchToken();
			List<Var> parameters = null;
			if (t == SYMBOLS.BRACES_OPEN) {
				parameters = parseParameters();
				t = fetchToken();

			}
			if (t == SYMBOLS.NEWLINE) {
				return new RValue(v, m, parameters);
			} else {
				parseError();
			}
		} else {
			parseError();
		}
		parseError();
		// TODO Auto-generated method stub
		return null;

	}

	private Function parseBegin() throws ParseError {
		assertTrue(token() == SYMBOLS.BEGIN);
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.BRACES_OPEN) {

			List<Var> parameters = parseParameters();

			t = fetchToken();
			if (t == SYMBOLS.NEWLINE) {
				return new Function(parameters, parseBlock());
			} else {
				parseError();
			}
		} else {
			parseError();
		}
		parseError();
		return null;
	}

	private List<Var> parseParameters() throws ParseError {

		List<Var> parameters = new ArrayList<Var>();

		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.VAR || t == SYMBOLS.SYMBOL) {
			Var v = null;
			while (true) {
				if (token() == SYMBOLS.VAR || token() == SYMBOLS.SYMBOL)
					v = parseVar();
				else
					parseError();

				parameters.add(v);
				SYMBOLS t2 = fetchToken();
				if (t2 == SYMBOLS.COMMA) {
					t2 = fetchToken();
				} else if (t2 == SYMBOLS.BRACES_CLOSE) {
					break;
				} else {
					parseError();
				}

			}
		}
		return parameters;
	}

	private void assertTrue(boolean b) throws ParseError {
		if (!b)
			parseError();

	}

	private ParsedBlock parseBlock() throws ParseError {
		SYMBOLS t = fetchToken();
		ParsedBlock block = new ParsedBlock();
		if (t == SYMBOLS.END) {
			parseError();
		} else {
			block.add(parseLine());
		}
		parseError();

		return block;
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException,
			VMExceptionOutOfMemory, ParseError {
		VM vm = new VM();

		LLParser2 lp = new LLParser2(vm.createContext());
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);
		String fn = curDir + File.separator + "src" + File.separator
				+ "vm_java" + File.separator + "examples" + File.separator
				+ "very_simple.pvm";
		String content = LineLexer2.loadFile(fn);
		lp.parse(content);
	}
}
