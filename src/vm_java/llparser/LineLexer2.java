package vm_java.llparser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vm_java.internal.VMLog;

public class LineLexer2 {
	enum SYMBOLS {
		BEGIN, END, TYPE, PARAMETER,
		RETURN, VAR, EQUAL, ASSIGN, DOT, WHITESPACE, SYMBOL, STRING, ERROR, NEWLINE, INTEGER, 
		BRACKETS_OPEN, BRACKETS_CLOSE, 
		PARENT_OPEN, PARENT_CLOSE, COMMA,
		EOF,COMMENT, BRACES_OPEN, BRACES_CLOSE, CLEAR, SLEEP
	};

	public static class Lex {
		String pattern;
		SYMBOLS symbol;
		Pattern pat;

		public Lex(String p, SYMBOLS sym) {
			pattern = p;
			symbol = sym;
			pat = Pattern.compile("(" + pattern + ")(.*)", Pattern.MULTILINE);
		}
	}

	public static class Result {
		Lex lex;
		String string;

		Result(Lex l, String s) {
			lex = l;
			string = s;
		}
	}

	List<Lex> lexes = new ArrayList<Lex>();

	public LineLexer2() {
		init();
	}

	public void add(String pattern, SYMBOLS symbol) {
		lexes.add(new Lex(pattern, symbol));
	}

	private void init() {
		add("clear",SYMBOLS.CLEAR);
		add("begin", SYMBOLS.BEGIN);
		add("end", SYMBOLS.END);
		add("sleep", SYMBOLS.SLEEP);
		add("return", SYMBOLS.RETURN);
		add("parameter\\[[0-9]*\\]",SYMBOLS.PARAMETER);
		add("[a-zA-Z+-_][a-zA-Z0-9_]*", SYMBOLS.VAR);
		//add("[A-Z][a-zA-Z0-9_]*", SYMBOLS.TYPE);
		add("==", SYMBOLS.EQUAL);
		add("=", SYMBOLS.ASSIGN);
		add("\\.", SYMBOLS.DOT);
		add(" ", SYMBOLS.WHITESPACE);
		add("\n", SYMBOLS.NEWLINE);
		add("\r", SYMBOLS.NEWLINE);
		add("\t", SYMBOLS.WHITESPACE);
		add(":[a-zA-Z_][a-zA-Z0-9_]*", SYMBOLS.SYMBOL);
		add("\"[^\"]*\"", SYMBOLS.STRING);
		add("#",SYMBOLS.COMMENT);
		add("-?[0-9][0-9]*", SYMBOLS.INTEGER);
		add("\\[", SYMBOLS.BRACKETS_OPEN);
		add("\\]", SYMBOLS.BRACKETS_CLOSE);
		add("\\(", SYMBOLS.PARENT_OPEN);
		add("\\)", SYMBOLS.PARENT_CLOSE);
		add("\\{", SYMBOLS.BRACES_OPEN);
		add("\\}", SYMBOLS.BRACES_CLOSE);
		add(",", SYMBOLS.COMMA);
	}

	List<Result> lex(String s) {
		int p = 0;
		int old = p;
		List<Result> result = new ArrayList<Result>();
		while (p < s.length()) {
			for (Lex l : lexes) {
				Matcher m = l.pat.matcher(s.substring(p));

				if (m.matches()) {
					String match = m.group().substring(0,
							m.group(m.groupCount() - 1).length());
					if (match.length() > 0) {
						Result r = new Result(l, match);
						result.add(r);
						p += r.string.length();
						continue;
					}
				}
			}
			if (old == p) {
				result.add(new Result(new Lex("", SYMBOLS.ERROR), s
						.substring(p)));
				break;
			}
			old = p;
		}
		return result;
	}
	
	public static String loadFile(File f) throws IOException {
		String content = "";

		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);

		int buflen = 1024;
		int cur;
		byte[] buffer = new byte[buflen];
		while ((cur = bis.read(buffer)) > 0) {
			content += new String(buffer, 0, cur);
		}

		bis.close();
		fis.close();

		return content;
	}


	static String loadFile(String path) throws IOException {
		return loadFile(new File(path));
	}
	
	public static void output(Result r) {
		VMLog.debug(r.lex.symbol + "(" + r.string + ") ");
		
	}

	public static void output(List<Result> results) {
		for (Result r : results) {
			output(r);
		}
		VMLog.debug("");
	}

	public static void main(String[] args) throws IOException {
		LineLexer2 ll = new LineLexer2();
		//output(ll.lex("begin"));

		String curDir = System.getProperty("user.dir");
		VMLog.debug(curDir);
		String fn = curDir + File.separator + "src" + File.separator
				+ "vm_java" + File.separator + "examples" + File.separator
				+ "simple_function.pvm";
		String content = loadFile(fn);
		// Log.debug();
		for (String line : content.split("\n")) {
			output(ll.lex(line));
		}
		// output(ll.lex(content));
	}


}
