package common.lib;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class Number {
	@NotNull
	public static String[] CH_BASE = {
			"��", "һ", "��", "��", "��", "��", "��", "��", "��", "��"
	};
	@NotNull
	public static String[] CH_ADVANCE = {
			"", "ʮ", "��", "ǧ", "��", "ʮ��", "����", "ǧ��", "��"
	};
	@NotNull
    private static HashMap number = new HashMap();
	
	static {
		number.put("һ", "1");
		number.put("��", "2");
		number.put("��", "3");
		number.put("��", "4");
		number.put("��", "5");
		number.put("��", "6");
		number.put("��", "7");
		number.put("��", "8");
		number.put("��", "9");
		number.put("��", "0");

		number.put("Ҽ", "1");
		number.put("��", "2");
		number.put("��", "3");
		number.put("��", "4");
		number.put("��", "5");
		number.put("½", "6");
		number.put("��", "7");
		number.put("��", "8");
		number.put("��", "9");


		number.put("1", "1");
		number.put("2", "2");
		number.put("3", "3");
		number.put("4", "4");
		number.put("5", "5");
		number.put("6", "6");
		number.put("7", "7");
		number.put("8", "8");
		number.put("9", "9");
		number.put("0", "0");
	}

	public Number() {
	}
	
	@NotNull
	public static String chineseNumberToArabiaNumber(String chNumber) {
		StringBuffer buffer = new StringBuffer();
		boolean isNumber = false;
		String value = chNumber;

		for (int j = 0; j < value.length(); ++j) {
			if (value.length() == 1) {
				if (value.charAt(j) == 'ʮ' || value.charAt(j) == 'ʰ') {
					buffer.append("10");
					isNumber = true;
					continue;
				}
			}

			if (j == 0 && (value.charAt(j) == 'ʮ' || value.charAt(j) == 'ʰ')) {
				buffer.append("1");
			} else if (j == value.length() - 1 && (value.charAt(j) == 'ʮ' || value.charAt(j) == 'ʰ')) {
				buffer.append("0");
			} else if (j == value.length() - 1 && (value.charAt(j) == '��' || value.charAt(j) == '��')) {
				buffer.append("00");
			} else {
				String nb = String.valueOf(value.charAt(j));
				if (number.containsKey(nb)) {
					buffer.append(number.get(nb));
					isNumber = true;
				} else if (value.charAt(j) == 'ʮ' || value.charAt(j) == '��' || value.charAt(j) == 'ʰ' || value.charAt(j) == '��') {
					continue;
				} else {
					isNumber = false;
					break;
				}
			}
		}

		if (isNumber)
			return buffer.toString();
		else
			return "";
	}
	
	@NotNull
    public static String arabiaNumberToChineseNumber(@NotNull String number) {
		if (number.length() >= 7) // �绰����
			return number;
		int value = Integer.valueOf(number).intValue(); 
		StringBuffer buffer = new StringBuffer();
		number = String.valueOf(value);
		int steps = -1;
		int temp = value;
		int[] stepsNumber = new int[number.length()];
		
		do {
			stepsNumber[++steps] =  temp % 10;
			temp = temp / 10;
		} while (temp > 0);
		
		for (int i = stepsNumber.length - 1; i > -1; --i) {
			if (!(stepsNumber[i] == 0 & 
					(buffer.length() > 0 && 
					 buffer.charAt(buffer.length() - 1) == '��'))) 
				buffer.append(CH_BASE[stepsNumber[i]]);
			
			if (stepsNumber[i] != 0)
				buffer.append(CH_ADVANCE[i]);
		}
		
		while (buffer.length() > 1 && 
				buffer.charAt(buffer.length() - 1) == '��') 
			buffer.deleteCharAt(buffer.length() - 1);
		
		StringBuffer buf1 = new StringBuffer();
		for (int i = 0; i < buffer.length() - 1; ++i) {
			char c = buffer.charAt(i);
			if (c != 'ʮ' && c != '��' &&  c != 'ǧ' && c != '��' && c != '��')
				buf1.append(c);
		}
		buf1.append(buffer.charAt(buffer.length() - 1));
		
		StringBuffer buf2 = new StringBuffer();
		if (buffer.length() >= 2 &&
				buffer.charAt(0) == 'һ' &&
				buffer.charAt(1) == 'ʮ')
			buf2.append("ʮ").append(buffer.substring(2));
		
		StringBuffer buf3 = new StringBuffer();
		for (int i = 0; i < buf1.length(); ++i) {
			if (buf1.charAt(i) == '��')
				buf3.append('0');
			else
				buf3.append(buf1.charAt(i));
		}
		
		buffer.append(" " + buf2 + " " + buf1);
		if (!buf3.equals(buf1))
			buffer.append(" " + buf3);

		return buffer.toString();
	}
	

	/**
	 * ȫ��ת���ɰ��
	 * 
	 * @param sOriginName
	 * @return
	 */
	@NotNull
    public static String sbc2dbc(String sOriginName)
	{
		sOriginName = sOriginName.toLowerCase().trim();
		StringBuffer sb = new StringBuffer();

		for (int index = 0; index < sOriginName.length(); ++index)
		{
			int iChar = (int) sOriginName.charAt(index);
			if (12288 == iChar || 32 == iChar) continue;
			else if (65281 < iChar && iChar < 65374) sb.append((char) (iChar - 65248));
			else sb.append((char) iChar);
		}

		return sb.toString();
	}
	/*
	 * ��һ�����봮������ת��Ϊ����������,ֻת�����ڵ�����
	 * */
	@NotNull
    public static String transDigital(@NotNull String words)
	{
		try{
			int b = 0,e = 0;
			boolean start = false,trans=false;
			char c;
			char[] k = new char[1];
			String ks,chinawords;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<words.length();i++)
			{
				c = words.charAt(i);
				k[0] = c;
				ks = new String(k);
				if(!start)//ֻ����������һ-��
				{
					if((c!='��' && c!='0' && number.containsKey(ks))||c=='ʮ'||c=='ʰ')
					{
						b = i;
						e = i+1;
						start = true;
						continue;
					}
					
				}else//������ʮ���١�ǧ������,ʰ���ۡ�Ǫ
				{
					if(number.containsKey(ks)||c=='ʮ'||c=='��'||c=='ǧ'||c=='��'||c=='ʰ'||c=='��'||c=='Ǫ')
					{
						e = i+1;
						continue;
					}else//��������
					{
						if(start)
						{
							chinawords = words.substring(b,e);
							ks = chineseNumberToArabiaNumber(chinawords);
							if(ks.equals(""))
							{
								ks = chinawords;
							}
							sb.append(ks);
							//System.out.println("b:"+b+",e:"+e);
							trans = true;
							start = false;
						};						
					}
				}
				sb.append(c);							
			}
			if(start)
			{
				trans = true;
				sb.append(chineseNumberToArabiaNumber(words.substring(b,e)));
			}
			if(trans)
			{
				//Log.logger.debug("*****trans digit:"+words+"-->"+sb.toString());
			}
			return sb.toString();
		}catch(Exception e)
		{
			return words;
		}
	}
	
	public static void main(@NotNull String[] args) {
		for (int i = 101; i < 200; i++) {
			String chNumber = Number.arabiaNumberToChineseNumber(String.valueOf(i));
			System.out.println(chNumber);
		}
		String chNumber = Number.arabiaNumberToChineseNumber(String.valueOf(301));
		System.out.println(chNumber);
		
		String[] chineseNumbers = {
			"�������","ʰ","һʰ��","����",
			"һ", "��", "��",
			"ʮ", "ʮһ", "һʮ��",
			"��ʮ", "��ʮһ", "һ��",
			"һ����һ", "1��", 
		};
				
		
		String[] testwords = {
				"��ž���ǧ��5ҽԺ",
				"����һһ",
				"������������һҽԺ��ҽ��306�����",
				
				"��������һ",
				"�����ĺ���",
				"����8�Ű˰�����",
				"�˰�buy",
				"һʮ��·",
				"ʮ�ﱤ",
				"��ҽ��ʮԺ",
				"̫���½�;��ľ�ֺ�ͬ;��������;�������ͬ;��ӹ���ͬ;�������ڴ��;��Զ����;��Զ����;��Զ��;Ƥ���ͬ;��ѧ��ͬ;��ľ�ֱ�һ��;��ţ�Ǻ�ͬ;����ͬ;��¡��;���������;���岮��ͬ;������ͬ;�о��ܵ�;Сʯ����ͬ;���Ӻ�ͬ;�����;�书����ͬ;�������ڴ��;�����̳�;������; Go2mapLianSuo $$TP:TP10006$$,$$TP:TP10002$$,$$DG:������ʳ-����-��ʽ���$$,$$FB:������ʳ-���С��-��ʽ���-�ͱ���$$,$$FB:������ʳ-����$$,$$RC:�ͱ���-�ͱ������-��ʽ���$$,$$LS:�ͱ���$$,$$RC:����-��ʽ����-��ʽ����-��������-��ʽ����-������$$ Go2mapFanZhe Go2mapFan�����콢�� ���������콢�� �����콢�� �� �ֵ� �ܵ�"
		};		
		if(args.length>=1)
		{
			System.out.println("read:"+args[0]);
			String content = "";
			String[] lines = content.split("\n");
			String trans = null;
			for(int i=0;i<lines.length;i++)
			{
				trans = Number.transDigital(lines[i]);
				if(!trans.equals(lines[i]))
				{
					System.out.println(lines[i]);
				}
				System.out.println(trans);
			}
		}else
		{
			for (int i = 0; i < chineseNumbers.length; ++i)
				System.out.println(Number.chineseNumberToArabiaNumber(chineseNumbers[i]));
			for(int i =0;i<testwords.length;i++)
			{
				System.out.println(transDigital(testwords[i]));
			}
		}
	}
}
