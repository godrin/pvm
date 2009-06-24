package vm_java.llparser;


public class LLParser {
	/*
	class Rule {
		String methodName;
		List<SYMBOLS> symbols = new ArrayList<SYMBOLS>();

		Rule(String pMethodName, SYMBOLS[] pSymbols) {
			methodName = pMethodName;
			for (SYMBOLS sym : pSymbols) {
				symbols.add(sym);
			}
		}
	}

	private List<CodeBlock> blocks = new ArrayList<CodeBlock>();
	private List<Rule> rules = new ArrayList<Rule>();
	private Map<String, BasicObject> parameters = new TreeMap<String, BasicObject>();
	private VMContext mContext;
	private VMScope mScope;

	public LLParser(VMContext pContext) throws VMExceptionOutOfMemory {
		init();
		mContext = pContext;
		mScope = mContext.createScope();
	}

	public void startBlock(Result var, Result assign, Result begin)
			throws VMExceptionOutOfMemory {
		CodeBlock block = new CodeBlock(mContext);

		mScope.put(mContext.intern(var.string), block);
		blocks.add(block);
	}

	public void assignString(Result parameter, Result assign, Result str)
			throws VMExceptionOutOfMemory {
		VMString vms = new VMString(mContext);
		vms.setContent(str.string);
		parameters.put(parameter.string, vms);
	}

	public void assignFromCall(Result var, Result assign, Result klass,
			Result dot, Result newp) throws BlockIsFinalException,
			VMExceptionOutOfMemory, VMException {

		lastBlock()
				.add(
						new Assignment(mContext, source(), mContext.intern(var.string),
								new FunctionCall(mContext, mContext
										.intern(newp.string))));
	}
	
	SourceInfo source() {
		return new SourceInfo(lin)
	}

	public void setReturn(Result ret, Result var) throws BlockIsFinalException,
			VMExceptionOutOfMemory, VMException {
		lastBlock().add(new DoReturn(mContext, mContext.intern(var.string)));
	}

	public void end(Result end) {
		blocks.remove(blocks.size() - 1);
	}

	public void addFunction(Result pself, Result dot, Result addFunc,
			Result braceOpen, Result symbol, Result comma, Result block,
			Result braceClose) throws BlockIsFinalException,
			VMExceptionOutOfMemory, VMException {
		ObjectName self = mContext.internal("self");
		lastBlock().add(
				new MemberAssignment(mContext, self, mContext
						.intern(symbol.string.substring(1)), mContext
						.intern(block.string)));
	}

	public void assignFromParameter(Result var, Result assign, Result parameter) {

	}

	void init() {
		add("startBlock", "VAR ASSIGN BEGIN BRACES_OPEN PARAMETER (COMMA ");

		add("startBlock", new SYMBOLS[] { SYMBOLS.VAR, SYMBOLS.ASSIGN,
				SYMBOLS.BEGIN });
		add("assignString", new SYMBOLS[] { SYMBOLS.PARAMETER, SYMBOLS.ASSIGN,
				SYMBOLS.STRING });
		add("assignFromCall", new SYMBOLS[] { SYMBOLS.VAR, SYMBOLS.ASSIGN,
				SYMBOLS.TYPE, SYMBOLS.DOT, SYMBOLS.VAR });
		add("setReturn", new SYMBOLS[] { SYMBOLS.RETURN, SYMBOLS.VAR });
		add("end", new SYMBOLS[] { SYMBOLS.END });
		add("addFunction", new SYMBOLS[] { SYMBOLS.VAR, SYMBOLS.DOT,
				SYMBOLS.VAR, SYMBOLS.BRACES_OPEN, SYMBOLS.SYMBOL,
				SYMBOLS.COMMA, SYMBOLS.VAR, SYMBOLS.BRACES_CLOSE });

		// PARAMETER(parameter[0]) ASSIGN(=) STRING("hello")
	}

	private void add(String methodName, String pattern) {
		String[] pt = pattern.split(" ");

	}

	private CodeBlock lastBlock() {
		return blocks.get(blocks.size() - 1);
	}

	private void add(String string, SYMBOLS[] symbols) {
		rules.add(new Rule(string, symbols));

	}

	Program parse(String inStr) throws VMExceptionOutOfMemory {
		Program program = null;

		LineLexer2 ll = new LineLexer2();

		CodeBlock block = new CodeBlock(mContext);

		program = new Program(mContext, block);
		blocks.add(block);

		for (String line : inStr.split("\n")) {
			List<Result> result = ll.lex(line);
			result = filterWhitespaces(result);
			tryParseLine(result);
		}

		return program;
	}

	private List<Result> filterWhitespaces(List<Result> result) {
		List<Result> l = new ArrayList<Result>();
		for (Result r : result) {
			if (r.lex.symbol != SYMBOLS.WHITESPACE)
				l.add(r);
		}
		return l;
	}

	List<SYMBOLS> getSymbols(List<Result> tokens) {
		List<SYMBOLS> l = new ArrayList<SYMBOLS>();
		for (Result r : tokens) {
			if (r.lex.symbol != SYMBOLS.WHITESPACE)
				l.add(r.lex.symbol);
		}
		return l;
	}

	private void tryParseLine(List<Result> lineTokens) {

		List<SYMBOLS> toks = getSymbols(lineTokens);

		// System.out.println("TRY PARSE LINE");
		for (Rule rule : rules) {
			if (rule.symbols.equals(toks)) {
				try {
					Method m = getMethodByName(rule.methodName);
					try {
						Object[] rs = new Object[lineTokens.size()];
						for (int i = 0; i < rs.length; i++) {
							rs[i] = lineTokens.get(i);
						}
						m.invoke(this, rs);
						return;
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		System.out.println("Could not parse:");
		LineLexer2.output(lineTokens);
		// TODO Auto-generated method stub

	}

	private Method getMethodByName(String methodName) {
		for (Method m : getClass().getMethods()) {
			if (m.getName().equals(methodName))
				return m;
		}
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws IOException,
			VMExceptionOutOfMemory {
		VM vm = new VM();

		LLParser lp = new LLParser(vm.createContext());
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);
		String fn = curDir + File.separator + "src" + File.separator
				+ "vm_java" + File.separator + "examples" + File.separator
				+ "simple_function.pvm";
		String content = LineLexer2.loadFile(fn);
		lp.parse(content);
	}
*/
}
