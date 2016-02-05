package index.util;

import common.lib.Log;

public class BraceRemove {

	public BraceRemove() {
		// TODO Auto-generated constructor stub
	}
	
	public static String removeRecursive(String caption) {
		try {
			int times = 0;
			while(true) {
				String ret = remove(caption);
				if (ret.equals(caption)) {
					return ret;
				}
				times ++;
				caption = ret;
				if (times > 10) {
					break;
				}
			}
		} catch (Exception e) {
			Log.logger.error(Log.getErrorInfoFromException(e));
		}
		return caption;
	}
	
	public static String remove(String caption) {
		String ret = caption;
		
		int lastLeft = caption.lastIndexOf("(");
		if (lastLeft == -1) {
			return caption;
		}
		int firstright = caption.substring(lastLeft).indexOf(")");
		if (firstright == -1) {
			return caption;
		}
		System.out.println(""+lastLeft + "\t" + firstright);
		
		String leftPart = caption.substring(0, lastLeft);
		String rightPart = caption.substring(lastLeft + firstright + 1, caption.length()  );
		
		System.out.println("left:\t" + leftPart);
		System.out.println("right:\t" + rightPart);
		
		ret =  leftPart +" " + rightPart;
		return ret;
		
	}
	
	public static void main(String[] args) {
		String[]  words = {"abc(123)", "abc(123)def" , "(456)abc(123)"};
		for (String word: words) {
			String ret = BraceRemove.remove(word);
			System.out.println(word + "=>" + ret);
			String ret2 = BraceRemove.removeRecursive(word);
			System.out.println(word + "=>" + ret2);
		}
		
	}

}
