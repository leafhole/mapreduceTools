package common.lib;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class Number {
	@NotNull
	public static String[] CH_BASE = {
			"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
	};
	@NotNull
	public static String[] CH_ADVANCE = {
			"", "十", "百", "千", "万", "十万", "百万", "千万", "亿"
	};
	@NotNull
    private static HashMap number = new HashMap();
	
	static {
		number.put("一", "1");
		number.put("二", "2");
		number.put("三", "3");
		number.put("四", "4");
		number.put("五", "5");
		number.put("六", "6");
		number.put("七", "7");
		number.put("八", "8");
		number.put("九", "9");
		number.put("零", "0");

		number.put("壹", "1");
		number.put("贰", "2");
		number.put("叁", "3");
		number.put("肆", "4");
		number.put("伍", "5");
		number.put("陆", "6");
		number.put("柒", "7");
		number.put("捌", "8");
		number.put("玖", "9");


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
				if (value.charAt(j) == '十' || value.charAt(j) == '拾') {
					buffer.append("10");
					isNumber = true;
					continue;
				}
			}

			if (j == 0 && (value.charAt(j) == '十' || value.charAt(j) == '拾')) {
				buffer.append("1");
			} else if (j == value.length() - 1 && (value.charAt(j) == '十' || value.charAt(j) == '拾')) {
				buffer.append("0");
			} else if (j == value.length() - 1 && (value.charAt(j) == '百' || value.charAt(j) == '佰')) {
				buffer.append("00");
			} else {
				String nb = String.valueOf(value.charAt(j));
				if (number.containsKey(nb)) {
					buffer.append(number.get(nb));
					isNumber = true;
				} else if (value.charAt(j) == '十' || value.charAt(j) == '百' || value.charAt(j) == '拾' || value.charAt(j) == '佰') {
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
		if (number.length() >= 7) // 电话号码
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
					 buffer.charAt(buffer.length() - 1) == '零'))) 
				buffer.append(CH_BASE[stepsNumber[i]]);
			
			if (stepsNumber[i] != 0)
				buffer.append(CH_ADVANCE[i]);
		}
		
		while (buffer.length() > 1 && 
				buffer.charAt(buffer.length() - 1) == '零') 
			buffer.deleteCharAt(buffer.length() - 1);
		
		StringBuffer buf1 = new StringBuffer();
		for (int i = 0; i < buffer.length() - 1; ++i) {
			char c = buffer.charAt(i);
			if (c != '十' && c != '百' &&  c != '千' && c != '万' && c != '亿')
				buf1.append(c);
		}
		buf1.append(buffer.charAt(buffer.length() - 1));
		
		StringBuffer buf2 = new StringBuffer();
		if (buffer.length() >= 2 &&
				buffer.charAt(0) == '一' &&
				buffer.charAt(1) == '十')
			buf2.append("十").append(buffer.substring(2));
		
		StringBuffer buf3 = new StringBuffer();
		for (int i = 0; i < buf1.length(); ++i) {
			if (buf1.charAt(i) == '零')
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
	 * 全角转换成半角
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
	 * 把一个输入串中中文转变为阿拉伯数字,只转百以内的数字
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
				if(!start)//只可以是数字一-九
				{
					if((c!='零' && c!='0' && number.containsKey(ks))||c=='十'||c=='拾')
					{
						b = i;
						e = i+1;
						start = true;
						continue;
					}
					
				}else//可以是十、百、千、万、零,拾、佰、仟
				{
					if(number.containsKey(ks)||c=='十'||c=='百'||c=='千'||c=='万'||c=='拾'||c=='佰'||c=='仟')
					{
						e = i+1;
						continue;
					}else//其他文字
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
			"叁佰零捌","拾","一拾捌","捌零",
			"一", "二", "三",
			"十", "十一", "一十二",
			"二十", "二十一", "一百",
			"一百零一", "1百", 
		};
				
		
		String[] testwords = {
				"解放军三千零5医院",
				"三零一一",
				"我们是三百零一医院的医生306三零八",
				
				"我是三零一",
				"地铁四号线",
				"地铁8号八八零三",
				"八佰buy",
				"一十九路",
				"十里堡",
				"北医捌十院",
				"太仆寺街;大木仓胡同;西长安街;东槐里胡同;大秤钩胡同;复兴门内大街;华远西街;华远北街;华远街;皮库胡同;力学胡同;大木仓北一巷;西牛角胡同;民丰胡同;兴隆街;西单北大街;高义伯胡同;背阴胡同;中京畿道;小石虎胡同;堂子胡同;横二条;武功卫胡同;宣武门内大街;西单商场;教育部; Go2mapLianSuo $$TP:TP10006$$,$$TP:TP10002$$,$$DG:点评美食-西餐-西式简餐$$,$$FB:餐饮美食-快餐小吃-西式快餐-巴贝拉$$,$$FB:餐饮美食-西餐$$,$$RC:巴贝拉-巴贝拉快餐-西式快餐$$,$$LS:巴贝拉$$,$$RC:西餐-俄式西餐-法式西餐-西餐自助-意式西餐-自助餐$$ Go2mapFanZhe Go2mapFan西单旗舰店 北京西单旗舰店 西单旗舰店 店 分店 总店"
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
