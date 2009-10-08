package vm_java.pruby;

public class PRubyParserJRuby implements PRubyParser {

	@Override
	public String parse(PRubySourceDef source) {
		// TODO Auto-generated method stub
		return null;
	}

	// public final static String PARSER_RB = "ruby/parser_test.rb";
	public final static String PARSER_RB = "ruby/compiler.rb";
	/*
	 * public PRubyParserJRuby() { }
	 * 
	 * @Override public String parse(PRubySourceDef def) { byte[] pRubySource =
	 * def.getData(); ScriptEngineManager factory = new ScriptEngineManager();
	 * 
	 * if (pRubySource == null) return null;
	 * 
	 * // Create a JRuby engine.
	 * 
	 * boolean useEngine = true;
	 * 
	 * ScriptEngine engine = factory.getEngineByName("jruby");
	 * 
	 * ByteArrayInputStream bais = new ByteArrayInputStream(pRubySource);
	 * InputStreamReader isr = new InputStreamReader(bais);
	 * 
	 * String parserSource; try { parserSource = FileTool.loadFileString(new
	 * File(PARSER_RB)); } catch (IOException e) { VMLog.error(e); return null;
	 * }
	 * 
	 * Object result = null; try { // if (useEngine) { result =
	 * engine.eval(isr); } else { / System.out.println("A"); RubyInstanceConfig
	 * ric=new RubyInstanceConfig(); System.out.println("A");
	 * ric.setJRubyHome("/Users/davidkamphausen/Downloads/jruby-1.1.3");
	 * System.out.println("A"); Ruby r = Ruby.newInstance(ric);
	 * System.out.println("A"); String s = r.evalScriptlet(parserSource);
	 * System.out.println("A"); System.out.println(s);
	 * 
	 * } } catch (ScriptException e) { e.printStackTrace(); }
	 * 
	 * System.out.println(result);
	 * 
	 * return null; }
	 */

}
