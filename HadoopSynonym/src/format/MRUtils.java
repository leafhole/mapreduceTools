package format;

import java.io.UnsupportedEncodingException;

import org.apache.hadoop.io.Text;

public class MRUtils {
	public static String getGBKString(Text text)
			throws UnsupportedEncodingException {
		return new String(text.getBytes(), 0, text.getLength(), "GB18030");
	}
	
	public static String getGBKString(String text)
			throws UnsupportedEncodingException {
		return new String(text.getBytes(), 0, text.length(), "GB18030");
	}
}
