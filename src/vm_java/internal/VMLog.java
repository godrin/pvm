package vm_java.internal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

public class VMLog {
	public enum Level {DEBUG,WARN,ERROR};
	private static Set<Level> mLevels=new TreeSet<Level>();
	
	public static void setLogLevels(Level[] pLevels) {
		for(Level l:pLevels) {
			mLevels.add(l);
		}
	}
	
	public static void debug(Object x) {
		log(Level.DEBUG,x);
	}
	public static void warn(Object x) {
		log(Level.WARN,x);
	}
	public static void error(Object x) {
		log(Level.ERROR,x);
	}
	
	public static void log(Level l,Object x) {
		String s = x.toString();
		for (String p : s.split("\n")) {
			logLine(l,p);
		}
	}

	private static void logLine(Level l, String line) {
		if(!mLevels.contains(l))
			return;
		Date d = GregorianCalendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String level="["+l.toString().toLowerCase()+"] ";
		System.out.println("[" + sdf.format(d) + "] " +level+ line);
	}
}
