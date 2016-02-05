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
	 * ȫ��ת���ɰ��
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
	 * ���������ַ�
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
			 * U+0021 ! ̾�� 
			 * U+0022 " ˫����
			 * U+0023 # ����
			 * U+0024 $ ��Ǯ�����ҷ���
			 * U+0025 % �ٷֱȷ���
			 * U+0026 & Ӣ�ġ�and���ļ�д����
			 * U+0027 ' ����
			 * U+0028 ( �� Բ����
			 * U+0029 ) �� Բ����
			 * U+002A * �Ǻ�
			 * U+002B + �Ӻ�
			 * U+002C , ����
			 * U+002D - ���ֺţ�����
			 * U+002E . ���
			 * U+002F / �����������µ�б�� 
			 */
			if (iChar >= 0x21 && iChar <= 0x2F)
				continue;
			
			/*
			 * U+003A : ð��
			 * U+003B ; �ֺ�
			 * U+003C < С춷���
			 * U+003D = ��춺�
			 * U+003E > ��춷���
			 * U+003F ? �ʺ�
			 * U+0040 @ Ӣ�ġ�at���ļ�д����
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
		String[] lines = {"����������","123����","���ǣ���","����\t������","�����\t���ʦ������"};
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
