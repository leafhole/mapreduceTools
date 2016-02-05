package common.lib;

import org.jetbrains.annotations.NotNull;

public class Characters {
	public final static String REG_DIGIT = "[0-9.]*";
	public final static String REG_DIGIT_ADDR = "([0-9]|[一-十])+(号|号院)*";
	public final static String REG_CHAR = "[a-zA-Z]*";

	// public Digit(){}
	public static boolean isDigitAddr(@NotNull String str) {
		return str.matches(REG_DIGIT_ADDR);
	}

	public static boolean isDigit(@NotNull String str) {
		return str.matches(REG_DIGIT);
	}

	public static boolean isChar(@NotNull String str) {
		return str.matches(REG_CHAR);
	}

	public static void main(String args[]) {
		// Characters t = new Characters();
		String[] ss = { "一2一号", "12号院", "33号", "中国人", "中国人民4014810", "312223",
				"123.44", "0.315", "1.23", "11233", "abcd" };
		for (int i = 0; i < ss.length; i++) {
			if (isDigitAddr(ss[i]))
				Log.logger.info(ss[i] + " is 数字地址");
			if (isDigit(ss[i])) {
				Log.logger.info(ss[i] + " is digit");
			} else if (isChar(ss[i])) {
				Log.logger.info(ss[i] + " is char");
			} else
				Log.logger.info(ss[i] + " is overload");
		}
	}

}
