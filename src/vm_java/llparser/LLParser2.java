package vm_java.llparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.code.lib.StdIO;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.LineLexer2.Lex;
import vm_java.llparser.LineLexer2.Result;
import vm_java.llparser.LineLexer2.SYMBOLS;
import vm_java.llparser.ast.ASTProgram;
import vm_java.llparser.ast.ASTString;
import vm_java.llparser.ast.ASTAssign;
import vm_java.llparser.ast.ASTMethodCall;
import vm_java.llparser.ast.ASTBlock;
import vm_java.llparser.ast.ASTFunction;
import vm_java.llparser.ast.RValue;
import vm_java.llparser.ast.ASTStatementInterface;
import vm_java.llparser.ast.ASTVar;
import vm_java.types.VMExceptionFunctionNotFound;

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

	ASTProgram parse(String content) throws ParseError {
		for (String line : content.split("\n")) {
			lines.add(line);
		}
		ASTProgram program = new ASTProgram();
		ASTBlock block = new ASTBlock();
		program.setBlock(block);
		fetchToken();
		while (mResult != null) {
			ASTStatementInterface s = parseLine();
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

	private ASTStatementInterface parseLine() throws ParseError {
		ASTStatementInterface s = null;
		if (mResult.lex.symbol == SYMBOLS.VAR) {
			ASTVar v = parseVar();
			SYMBOLS t = fetchToken();
			if (t == SYMBOLS.ASSIGN) {
				s = parseAssign(v);
			} else if (t == SYMBOLS.DOT) {
				s = parseMemberAccess(v);
			} else if (t == SYMBOLS.BRACES_OPEN) {
				s = parseFunctionCall(v);
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

	private ASTStatementInterface parseFunctionCall(ASTVar v) throws ParseError {
		List<ASTVar> ps = parseParameters();
		fetchToken();
		return new ASTMethodCall(null,v,ps);
	}

	private ASTStatementInterface parseMemberAccess(ASTVar v) throws ParseError {
		fetchToken();
		ASTVar m = parseVar();
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.BRACES_OPEN) {
			List<ASTVar> ps = parseParameters();
			fetchToken();
			return new ASTMethodCall(v, m, ps);
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

	private ASTVar parseVar() throws ParseError {
		ASTVar v = new ASTVar(mResult.string);
		return v;
	}

	private ASTAssign parseAssign(ASTVar lValue) throws ParseError {
		SYMBOLS t = fetchToken();
		ASTAssign s = null;
		if (t == SYMBOLS.BEGIN) {
			s = new ASTAssign(lValue, parseBegin());
		} else if (t == SYMBOLS.TYPE || t == SYMBOLS.VAR) {
			s = new ASTAssign(lValue, parseRValue());
		} else if (t == SYMBOLS.STRING) {
			s = new ASTAssign(lValue, new ASTString(mResult.string));
		} else {
			parseError();
		}
		if (s == null)
			parseError();
		fetchToken();
		return s;
	}

	private RValue parseRValue() throws ParseError {
		ASTVar v = parseVar();
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.DOT) {
			fetchToken();
			ASTVar m = parseVar();
			t = fetchToken();
			List<ASTVar> parameters = null;
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

	private ASTFunction parseBegin() throws ParseError {
		assertTrue(token() == SYMBOLS.BEGIN);
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.BRACES_OPEN) {

			List<ASTVar> parameters = parseParameters();

			t = fetchToken();
			if (t == SYMBOLS.NEWLINE) {
				return new ASTFunction(parameters, parseBlock());
			} else {
				parseError();
			}
		} else {
			parseError();
		}
		parseError();
		return null;
	}

	private List<ASTVar> parseParameters() throws ParseError {

		List<ASTVar> parameters = new ArrayList<ASTVar>();

		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.VAR || t == SYMBOLS.SYMBOL) {
			ASTVar v = null;
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

	private ASTBlock parseBlock() throws ParseError {
		SYMBOLS t = fetchToken();
		ASTBlock block = new ASTBlock();
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
			VMExceptionOutOfMemory, ParseError, BlockIsFinalException,
			VMException, VMExceptionFunctionNotFound {
		VM vm = new VM();
		VMContext vmc = vm.createContext();

		LLParser2 lp = new LLParser2(vmc);
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);
		String fn = curDir + File.separator + "src" + File.separator
				+ "vm_java" + File.separator + "examples" + File.separator
				+ "very_simple.pvm";
		String content = LineLexer2.loadFile(fn);
		ASTProgram astP = lp.parse(content);
		Program prg = astP.instantiate(vmc);
		VMScope scope = vmc.createScope();
		scope.addPackage(new StdIO(vmc));
		prg.execute(scope);
	}
}
