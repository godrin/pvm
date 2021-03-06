package vm_java.llparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vm_java.code.SourceBased.SourceInfo;
import vm_java.internal.VMLog;
import vm_java.llparser.LineLexer2.Lex;
import vm_java.llparser.LineLexer2.Result;
import vm_java.llparser.LineLexer2.SYMBOLS;
import vm_java.llparser.ast.ASTArray;
import vm_java.llparser.ast.ASTAssign;
import vm_java.llparser.ast.ASTAssignFromMember;
import vm_java.llparser.ast.ASTAssignMember;
import vm_java.llparser.ast.ASTBlock;
import vm_java.llparser.ast.ASTBoolean;
import vm_java.llparser.ast.ASTClearVar;
import vm_java.llparser.ast.ASTDouble;
import vm_java.llparser.ast.ASTFunction;
import vm_java.llparser.ast.ASTIf;
import vm_java.llparser.ast.ASTInclude;
import vm_java.llparser.ast.ASTInteger;
import vm_java.llparser.ast.ASTMethodCall;
import vm_java.llparser.ast.ASTProgram;
import vm_java.llparser.ast.ASTRescue;
import vm_java.llparser.ast.ASTReturn;
import vm_java.llparser.ast.ASTRightValue;
import vm_java.llparser.ast.ASTSleep;
import vm_java.llparser.ast.ASTStatementInterface;
import vm_java.llparser.ast.ASTString;
import vm_java.llparser.ast.ASTTry;
import vm_java.llparser.ast.ASTVar;
import vm_java.llparser.ast.ASTWhile;

public class LLParser2 {

	public static class ParseError extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -683205404466665718L;

	}

	private Result mResult;
	private List<String> lines = new ArrayList<String>();
	private List<Result> results = new ArrayList<Result>();
	private LineLexer2 lexer;
	int lineNo = 0;
	private String filename;

	public LLParser2() {
		lexer = new LineLexer2();
	}

	ASTProgram parse(String content) throws ParseError {
		int cLineNo = 1;
		for (String line : content.split("\n")) {
			lines.add(line);
			VMLog.debug(Integer.toString(cLineNo) + ": " + line);
			cLineNo++;
		}
		ASTProgram program = new ASTProgram();
		ASTBlock block = new ASTBlock(source());
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
			results = filterComments(results);

			Lex lex = new Lex("", SYMBOLS.NEWLINE);
			results.add(new Result(lex, "\n"));
		}
		mResult = results.get(0);
		results.remove(0);
		return mResult.lex.symbol;
	}

	private List<Result> filterComments(List<Result> results2) {
		List<Result> n = new ArrayList<Result>();
		boolean comment = false;
		for (Result x : results2) {
			if (comment) {
				if (x.lex.symbol == SYMBOLS.NEWLINE) {
					comment = false;
				}
			} else if (x.lex.symbol == SYMBOLS.COMMENT) {
				comment = true;
				// return n;
			} else {
				n.add(x);
			}
		}
		return n;
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
		LineLexer2.output(mResult);
		// LineLexer2.output(this.results);
		ASTStatementInterface s = null;
		if (mResult.lex.symbol == SYMBOLS.VAR) {
			ASTVar v = parseVar();
			SYMBOLS t = fetchToken();
			if (t == SYMBOLS.ASSIGN) {
				s = parseAssign(v);
			} else if (t == SYMBOLS.DOT) {
				s = parseMemberAccess(v);
			} else if (t == SYMBOLS.AT) {
				s = parseStaticMemberAccess(v);
			} else if (t == SYMBOLS.GLOBAL) {
				s = parseGlobalAccess(v);
			} else if (t == SYMBOLS.PARENT_OPEN) {
				s = parseFunctionCall(v);
			} else {
				parseError();
			}
		} else if (token() == SYMBOLS.LRETURN || token() == SYMBOLS.FRETURN
				|| token() == SYMBOLS.ERETURN) {
			SYMBOLS sym = token();
			fetchToken();
			ASTVar v = parseVar();
			ASTReturn.Type t;
			if (sym == SYMBOLS.ERETURN) {
				t = ASTReturn.Type.EXCEPTION;
			} else if (sym == SYMBOLS.LRETURN) {
				t = ASTReturn.Type.LOCAL;
			} else {
				t = ASTReturn.Type.FAR;
			}
			fetchToken();

			s = new ASTReturn(source(), v, t);
		} else if (token() == SYMBOLS.NEWLINE) {
			fetchToken();
			VMLog.debug("IGNORE EMPTY LINE");
			return null;
		} else if (token() == SYMBOLS.COMMENT) {
			while (token() != SYMBOLS.NEWLINE)

				fetchToken();
			return null;
		} else if (token() == SYMBOLS.CLEAR) {
			fetchToken();
			ASTVar v = parseVar();
			s = new ASTClearVar(source(), v);
			fetchToken();
		} else if (token() == SYMBOLS.SLEEP) {
			fetchToken();
			ASTVar v = parseVar();
			s = new ASTSleep(source(), v);
			fetchToken();
		} else if (token() == SYMBOLS.IF || token() == SYMBOLS.UNLESS) {
			s = parseIf();
		} else if (token() == SYMBOLS.WHILE) {
			s = parseWhile();
			/*
			 * }
			 * 
			 * else if (token() == SYMBOLS.RESCUE) { fetchToken(); ASTVar
			 * rescueFunction = parseVar(); s = new ASTRescue(source(),
			 * rescueFunction); fetchToken();
			 */
		} else if (token() == SYMBOLS.INCLUDE) {
			fetchToken();
			ASTVar moduleName = parseVar();
			s = new ASTInclude(source(), moduleName);
			fetchToken();
		} else if (token() == SYMBOLS.TRY) {
			s = parseTry();
		} else {

			parseError();
		}
		if (s == null)
			parseError();
		if (token() != SYMBOLS.NEWLINE) {
			VMLog.debug("Statement:" + s);
			parseError(); //
		} else if (token() == null)
			return null;
		else
			fetchToken();
		return s;
	}

	private ASTStatementInterface parseTry() throws ParseError {
		fetchToken();
		if (token() == SYMBOLS.NEWLINE) {
			ASTBlock block = parseBlock();
			List<ASTRescue> rescues = new ArrayList<ASTRescue>();

			eatNewlines();
			if (token() != SYMBOLS.RESCUE) {
				parseError();
			}
			while (token() == SYMBOLS.RESCUE) {
				rescues.add(parseRescue());
			}

			return new ASTTry(source(), block, rescues);
		} else {
			parseError();
		}
		// TODO Auto-generated method stub
		return null;
	}

	private ASTRescue parseRescue() throws ParseError {
		List<ASTVar> types = new ArrayList<ASTVar>();
		ASTVar varName = null;

		if (token() != SYMBOLS.RESCUE) {
			parseError();
		}
		fetchToken();

		while (token() == SYMBOLS.VAR) {
			types.add(parseVar());
			fetchToken();
			if (token() == SYMBOLS.COMMA) {
				fetchToken();
			}
		}
		if (token() == SYMBOLS.ARROW) {
			fetchToken();
			varName = parseVar();
		}
		// eatNewlines();
		ASTBlock block = parseBlock();
		return new ASTRescue(source(), block, types, varName);
	}

	private void eatNewlines() throws ParseError {
		while (token() == SYMBOLS.NEWLINE) {
			fetchToken();
		}
	}

	private ASTStatementInterface parseWhile() throws ParseError {
		boolean wantedValue = token() == SYMBOLS.WHILE;
		fetchToken();
		ASTVar cond = parseVar();
		if (fetchToken() != SYMBOLS.DO)
			parseError();
		fetchToken();
		ASTVar blockName = parseVar();
		fetchToken();
		return new ASTWhile(source(), cond, blockName, wantedValue);
	}

	private ASTStatementInterface parseIf() throws ParseError {
		boolean wantedValue = (token() == SYMBOLS.IF);
		fetchToken();
		ASTVar cond = parseVar();
		if (fetchToken() != SYMBOLS.THEN)
			parseError();
		fetchToken();
		ASTVar blockName = parseVar();
		fetchToken();
		return new ASTIf(source(), cond, blockName, wantedValue);
	}

	private ASTStatementInterface parseFunctionCall(ASTVar v) throws ParseError {
		List<ASTVar> ps = parseParameters();
		fetchToken();
		return new ASTMethodCall(source(), null, v, ps, true);
	}

	private ASTStatementInterface parseMemberAccess(ASTVar v) throws ParseError {
		return parseMemberAccess(v, false);
	}

	private ASTStatementInterface parseStaticMemberAccess(ASTVar v)
			throws ParseError {
		return parseMemberAccess(v, true);
	}

	private ASTStatementInterface parseGlobalAccess(ASTVar v) throws ParseError {
		// FIXME
		return parseMemberAccess(v, true);
	}

	private ASTStatementInterface parseMemberAccess(ASTVar v, boolean isstatic)
			throws ParseError {
		fetchToken();
		ASTVar m = parseVar();
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.PARENT_OPEN) {
			List<ASTVar> ps = parseParameters();
			fetchToken();
			return new ASTMethodCall(source(), v, m, ps, isstatic);
		} else if (t == SYMBOLS.ASSIGN) {
			fetchToken();
			ASTVar rv = parseVar();
			fetchToken();
			return new ASTAssignMember(source(), v, m, rv, isstatic);
		} else if (t == SYMBOLS.NEWLINE) {
			List<ASTVar> ps = new ArrayList<ASTVar>();
			return new ASTMethodCall(source(), v, m, ps, isstatic);
		}

		parseError();
		return null;
	}

	private void parseError() throws ParseError {
		VMLog.error("Parse error in line " + lineNo);
		VMLog.error("Before: ");
		LineLexer2.output(mResult);
		LineLexer2.output(results);
		throw new ParseError();
	}

	SYMBOLS token() {
		return mResult.lex.symbol;
	}

	private ASTVar parseVar() throws ParseError {
		if (token() != SYMBOLS.VAR)
			parseError();
		ASTVar v = new ASTVar(source(), mResult.getString());
		return v;
	}

	private ASTStatementInterface parseAssign(ASTVar lValue) throws ParseError {
		SYMBOLS t = fetchToken();
		ASTStatementInterface s = null;
		if (t == SYMBOLS.BEGIN) {
			s = new ASTAssign(source(), lValue, parseBegin());
		} else if (t == SYMBOLS.BEGIN_WITHSCOPE) { // FIXME
			s = new ASTAssign(source(), lValue, parseBegin());
		} else if (t == SYMBOLS.TYPE || t == SYMBOLS.VAR) {
			s = new ASTAssign(source(), lValue, parseRValue());
		} else if (t == SYMBOLS.GLOBAL) {
			s = new ASTAssign(source(), lValue, parseGlobal());
		} else if (t == SYMBOLS.STRING) {
			s = new ASTAssign(source(), lValue,
					new ASTString(source(), mResult.getString().substring(1,
							mResult.getString().length() - 1)));
			fetchToken();
		} else if (t == SYMBOLS.INTEGER) {
			s = new ASTAssign(source(), lValue, new ASTInteger(source(),
					mResult.getString()));
			fetchToken();
		} else if (t == SYMBOLS.BRACKETS_OPEN) {
			s = new ASTAssign(source(), lValue, parseArray());
		} else if (t == SYMBOLS.BOOLEAN) {
			s = new ASTAssign(source(), lValue, new ASTBoolean(source(),
					mResult));
			fetchToken();
		} else if (t == SYMBOLS.DOUBLE) {
			s = new ASTAssign(source(), lValue,
					new ASTDouble(source(), mResult));
			fetchToken();
		} else if (t == SYMBOLS.NILQ) {
			s = new ASTClearVar(source(), lValue);
			fetchToken();
		} else {
			parseError();
		}
		if (s == null)
			parseError();
		return s;
	}

	private ASTRightValue parseGlobal() throws ParseError {
		fetchToken();
		if (token() == SYMBOLS.VAR) {
			String s = mResult.getString();
			fetchToken();
			return new ASTVar(source(), s);
		} else {
			parseError();
			return null;
		}
	}

	private ASTRightValue parseArray() throws ParseError {
		if (token() != SYMBOLS.BRACKETS_OPEN)
			parseError();
		fetchToken();
		ASTArray array = new ASTArray(source());
		while (token() != SYMBOLS.BRACKETS_CLOSE) {
			ASTVar v = parseVar();
			array.add(v);
			if (fetchToken() == SYMBOLS.COMMA)
				fetchToken();

		}
		fetchToken();

		return array;
	}

	SourceInfo source() throws ParseError {
		return new SourceInfo(filename, lineNo);
	}

	private ASTRightValue parseRValue() throws ParseError {
		ASTVar v = parseVar();
		SYMBOLS t = fetchToken();

		// FIXME: t==SYMBOLS.AT for static access
		if (t == SYMBOLS.DOT) {
			fetchToken();
			ASTVar m = parseVar();
			t = fetchToken();
			List<ASTVar> parameters = null;
			if (t == SYMBOLS.PARENT_OPEN) {
				parameters = parseParameters();
				t = fetchToken();
			}
			if (t == SYMBOLS.NEWLINE) {
				if (parameters == null) {
					return new ASTAssignFromMember(source(), v, m);
				} else {
					return new ASTMethodCall(source(), v, m, parameters, false);
				}
			} else {
				parseError();
			}
		} else if (t == SYMBOLS.NEWLINE) {
			return v;
		} else if (t == SYMBOLS.BRACKETS_OPEN) {
			fetchToken();
			ASTVar index = parseVar();
			if (fetchToken() != SYMBOLS.BRACKETS_CLOSE)
				parseError();
			fetchToken();
			List<ASTVar> ps = new ArrayList<ASTVar>();
			ps.add(index);
			return new ASTMethodCall(source(), v, new ASTVar(source(),
					"__getitem"), ps, false);
		} else if (t == SYMBOLS.PARENT_OPEN) {
			// parseError();
			List<ASTVar> parameters = null;
			parameters = parseParameters();
			t = fetchToken();

			if (t == SYMBOLS.NEWLINE) {
				ASTVar x = new ASTVar(source(), "self");
				return new ASTMethodCall(source(), x, v, parameters, false); // FIXME
				// -
				// or
				// is
				// static
				// correct
				// here
				// ?
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

	private ASTRightValue parseBegin() throws ParseError {
		boolean withScope = (token() == SYMBOLS.BEGIN_WITHSCOPE);
		assertTrue(token() == SYMBOLS.BEGIN
				|| token() == SYMBOLS.BEGIN_WITHSCOPE);
		SYMBOLS t = fetchToken();
		if (t == SYMBOLS.PARENT_OPEN) {

			List<ASTVar> parameters = parseParameters();

			t = fetchToken();
			if (t == SYMBOLS.NEWLINE) {
				return new ASTFunction(source(), parameters, parseBlock(),
						withScope);
			} else {
				parseError();
			}
		} else if (t == SYMBOLS.NEWLINE) {
			return parseBlock();
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
				} else if (t2 == SYMBOLS.PARENT_CLOSE) {
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
		fetchToken();
		ASTBlock block = new ASTBlock(source());
		while (token() != SYMBOLS.END && token() != SYMBOLS.RESCUE) {
			ASTStatementInterface statement = parseLine();
			if (statement != null) {
				block.add(statement);
			}
		}
		if (token() == SYMBOLS.END) {
			if (fetchToken() != SYMBOLS.NEWLINE)
				parseError();
		}

		return block;
		// TODO Auto-generated method stub

	}

	/*
	 * private ASTProgram parseFile(String fn) throws ParseError, IOException {
	 * filename = fn; return parse(LineLexer2.loadFile(fn)); }
	 */
	public ASTProgram parseFile(File f) throws ParseError, IOException {
		filename = f.toString();
		return parse(LineLexer2.loadFile(f));
	}

	public ASTProgram parseData(String data, String pFilename)
			throws ParseError, IOException {
		filename = pFilename;
		return parse(data);
	}

}
