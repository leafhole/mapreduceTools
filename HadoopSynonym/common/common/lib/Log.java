package common.lib;

import java.util.ResourceBundle;
import java.io.*;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

//import org.jetbrains.annotations.Nullable;

public class Log {
	@NotNull
	public static Logger logger;

	static {
		try {
			logger = Logger.getLogger("MRTOOLLOG");
			String log4jprop = ResourceBundle.getBundle("index").getString(
					"log4j");
			File f = new File(log4jprop);
			if (f.exists())
				PropertyConfigurator.configure(log4jprop);
		} catch (Exception e) {
			System.err.println("Info: π”√»± °log4j≈‰÷√");
		}
	}

	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		return logger;
	}

	@NotNull
	public static String getErrorInfoFromException(@NotNull Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e2) {
			return "bad getErrorInfoFromException " + e2.toString() + " \r\n"
					+ e.toString();
		}
	}

	@NotNull
	public static String getErrorInfoFromException(@NotNull Throwable t) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e2) {
			return "bad getErrorInfoFromException " + e2.toString() + " \r\n"
					+ t.toString();
		}
	}
}