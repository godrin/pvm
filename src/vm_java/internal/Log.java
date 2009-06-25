package vm_java.internal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Log {
	enum Level {DEBUG,WARN,ERROR};
	
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
			logLine(p);
		}
	}

	private static void logLine(String line) {
		Date d = GregorianCalendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println("[" + sdf.format(d) + "] " + line);
	}
}
