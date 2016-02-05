package common.lib;

import org.jetbrains.annotations.NotNull;

public class Characters {
	public final static String REG_DIGIT = "[0-9.]*";
	public final static String REG_DIGIT_ADDR = "([0-9]|[һ-ʮ])+(��|��Ժ)*";
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
		String[] ss = { "һ2һ��", "12��Ժ", "33��", "�й���", "�й�����4014810", "312223",
				"123.44", "0.315", "1.23", "11233", "abcd" };
		for (int i = 0; i < ss.length; i++) {
			if (isDigitAddr(ss[i]))
				Log.logger.info(ss[i] + " is ���ֵ�ַ");
			if (isDigit(ss[i])) {
				Log.logger.info(ss[i] + " is digit");
			} else if (isChar(ss[i])) {
				Log.logger.info(ss[i] + " is char");
			} else
				Log.logger.info(ss[i] + " is overload");
		}
	}

}
