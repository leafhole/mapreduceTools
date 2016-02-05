package index.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


import org.jetbrains.annotations.NotNull;

public class Util {
	/**
	 * 全角转换成半角
	 *
	 * @return
	 */
	@NotNull
    public static String sbc2dbc(String src)
	{
		src = src.toLowerCase().trim();
		StringBuffer sb = new StringBuffer();

		for (int index = 0; index < src.length(); ++index)
		{
			int iChar = (int) src.charAt(index);
			if (12288 == iChar) 
				sb.append((char) 32);
			else if (65281 < iChar && iChar < 65374) 
				sb.append((char) (iChar - 65248));
			else 
				sb.append((char) iChar);
		}

		return sb.toString();
	}
	
	/**
	 * 过滤特殊字符
	 * @param src
	 * @return
	 */
	@NotNull
    public static String filterPunctuation(String src) {
		src = sbc2dbc(src);
		StringBuffer sb = new StringBuffer();
		
		for (int index = 0; index < src.length(); ++index) {
			int iChar = (int) src.charAt(index);
			
			/*
			 * U+0021 ! 叹号 
			 * U+0022 " 双引号
			 * U+0023 # 井号
			 * U+0024 $ 价钱／货币符号
			 * U+0025 % 百分比符号
			 * U+0026 & 英文「and」的简写符号
			 * U+0027 ' 引号
			 * U+0028 ( 开 圆括号
			 * U+0029 ) 关 圆括号
			 * U+002A * 星号
			 * U+002B + 加号
			 * U+002C , 逗号
			 * U+002D - 连字号／减号
			 * U+002E . 句号
			 * U+002F / 由右上至左下的斜线 
			 */
			if (iChar >= 0x21 && iChar <= 0x2F)
				continue;
			
			/*
			 * U+003A : 冒号
			 * U+003B ; 分号
			 * U+003C < 小於符号
			 * U+003D = 等於号
			 * U+003E > 大於符号
			 * U+003F ? 问号
			 * U+0040 @ 英文「at」的简写符号
			 */
			if (iChar >= 0x3A && iChar <= 0x40)
				continue;
			
			sb.append((char)iChar);
		}
		
		return sb.toString();
	}
	
	@NotNull
    public static Set load(String file) throws IOException {
		HashSet set = new HashSet();	
		FileInputStream is = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
		String line = reader.readLine();
		while (line != null) {
			line = line.trim();
			set.add(line);
			line = reader.readLine();
		}		
		is.close();	
		return set;
	}
	
	public static void main(String[] args) {
		/*
		for (int i = 0x21; i <= 0x2E; ++i)
			System.out.println((char)i);
		
		for (int i = 0x3A; i <= 0x40; ++i)
			System.out.println((char)i);
		String[] lines = {"１２３ａｂｃ","123ａｂｃ","我们ａｂｃ","京广\t京广线","会计所\t会计师事务所"};
		for(int i=0;i<lines.length;i++)
		{
			System.out.println(sbc2dbc(lines[i]));
		}*/
		
		String content = "";
		String[] lines = content.split("\n");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<lines.length;i++)
		{
			//System.out.println(sbc2dbc(lines[i]));
			sb.append(sbc2dbc(lines[i])+"\n");
			//if(i>100)break;
		}

	}
}
