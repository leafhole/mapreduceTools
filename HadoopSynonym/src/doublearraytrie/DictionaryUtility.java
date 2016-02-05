// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DictionaryUtility.java

package doublearraytrie;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

// Referenced classes of package doublearraytrie:
//			FileUtility, DoubleArrayUtility, DoubleArrayTrie, ACAutomata, 
//			Int, LogInfo, POS

public final class DictionaryUtility
{

	private static char SPECIALSYMBOL_LIST[] = {
		'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', 
		'(', ')', '_', '-', '+', '=', '|', '\\', '{', '}', 
		'[', ']', ':', '"', '\'', '<', '>', ',', '.', '?', 
		'/', '\t', '`', '~', '\uFF01', '\267', '#', '\uFFE5', '%', '\u2026', 
		'\u2026', '\u2014', '*', '\uFF08', '\uFF09', '\u2014', '-', '+', '=', '|', 
		'\u3001', '{', '}', '[', ']', '\uFF1A', '\u201C', '\u201D', '\u300A', '\u300B', 
		'\u3009', '\u3008', '\uFF0C', '\u3002', '\uFF1F', '/', ' '
	};
	private static String SPECIALSYMBOL_STRING;
	private static String URL = "jdbc:oracle:thin:@10.11.26.60:1521:maprun";
	private static String USER = "lsp";
	private static String PASSWORD = "lspouter";
	public static String FILEPATH;
	public static String ENTITY_FILE;
	public static String DICT_FILE;
	public static String LOG_FILE;
	public static String RESULT_FILE;
	public static String WORD_FILE;
	public static String VOCABLE_FILE;

	public DictionaryUtility()
	{
	}

	public static void createDictionary()
		throws Exception
	{
		String sLogFile = LOG_FILE;
		java.io.OutputStream os = new FileOutputStream(sLogFile, false);
		PrintStream ps = new PrintStream(os);
		ArrayList daTrie = new ArrayList();
		ArrayList entityList = new ArrayList();
		getEntity(entityList, ps);
		FileUtility.writeTxt(entityList, ENTITY_FILE);
		DoubleArrayUtility.daInitialize(daTrie);
		DoubleArrayTrie.trieStore(daTrie, entityList, ps);
		ACAutomata.acFailure(daTrie);
		FileUtility.writeDictionary(daTrie, entityList, DICT_FILE, WORD_FILE);
		ps.close();
	}

	public static ArrayList queryDictionary(ArrayList daTrie, ArrayList entityList, String str)
		throws Exception
	{
		DirectGraph graph = ACAutomata.acMatch(daTrie, entityList, str);
		int n = str.length();
		int path[] = graph.getShorstPath(0, n);
		ArrayList alPath = new ArrayList();
		for (; n >= 0; n = path[n])
			alPath.add(new Int(n));

		ArrayList alTerm = new ArrayList();
		for (int i = alPath.size() - 1; i > 0; i--)
		{
			Int begin = (Int)alPath.get(i);
			Int end = (Int)alPath.get(i - 1);
			Edge edge = graph.getEdge(begin.val, end.val);
			String word = str.substring(begin.val, end.val);
			alTerm.add(new Term(word, edge.type));
		}

		return alTerm;
	}

	public static ArrayList queryDictionary(String dictFile, String wordFile, String str)
		throws Exception
	{
		RandomAccessFile datRf = new RandomAccessFile(dictFile, "r");
		RandomAccessFile wordRf = new RandomAccessFile(wordFile, "r");
		DirectGraph graph = ACAutomata.acMatch(datRf, wordRf, str);
		int n = str.length();
		int path[] = graph.getShorstPath(0, n);
		ArrayList alPath = new ArrayList();
		for (; n >= 0; n = path[n])
			alPath.add(new Int(n));

		ArrayList alTerm = new ArrayList();
		for (int i = alPath.size() - 1; i > 0; i--)
		{
			Int begin = (Int)alPath.get(i);
			Int end = (Int)alPath.get(i - 1);
			Edge edge = graph.getEdge(begin.val, end.val);
			String word = str.substring(begin.val, end.val);
			alTerm.add(new Term(word, edge.type));
		}

		datRf.close();
		wordRf.close();
		return alTerm;
	}

	public static ArrayList queryDictionary(String str)
		throws Exception
	{
		return queryDictionary(DICT_FILE, WORD_FILE, str);
	}

	public static void getEntity(ArrayList entityList, PrintStream ps)
		throws Exception
	{
		LogInfo.logOut("连接数据库", ps);
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement stmt = conn.createStatement(1003, 1007);
		ResultSet rs = null;
		String Sql = " select distinct t.caption, t.alias from lsp.t_lyr_entity t left join lsp.t_local_poirank tt on t.uniqueID = tt.uniqueID left join lsp.T_LYR_LAYERINFO a on t.layerid = a.id where ((t.category = '地名' and t.subcategory = '地名')  or t.category = '购物场所' or t.category = '街道地名' or t.category = '景点场馆' or t.category = '居民小区' or t.category = '楼宇办公' or t.category = '旅游景点' or t.category = '商业网点' or t.category = '体育场馆' or t.category = '学校科研' or t.category = '医疗卫生' or t.category = '娱乐休闲' or t.category = '政府机关' or t.subcategory = '道路' or t.subcategory = '国道' or t.subcategory = '立交桥' ) and a.caption like '%无缝%' ";
		LogInfo.logOut("查询数据库", ps);
		rs = stmt.executeQuery(Sql);
		LogInfo.logOut("读取实体", ps);
		readResultSet(entityList, rs, ps, POS.ENTITY);
		rs.close();
		Sql = " select distinct city,caption,alias from t_lyr_entity where layerid=(select id from t_lyr_layerinfo t where caption ='无缝中国_县区划') order by city,caption";
		ResultSet rsClass = null;
		LogInfo.logOut("查询数据库", ps);
		rsClass = stmt.executeQuery(Sql);
		LogInfo.logOut("读取县区域词", ps);
		readResultSet(entityList, rsClass, ps, POS.REGION);
		LogInfo.logOut("共读取词" + entityList.size() + " 个", ps);
		rsClass.close();
		Sql = "select t.caption,t.alias from t_util_citycode t";
		LogInfo.logOut("查询数据库", ps);
		ResultSet rsCity = stmt.executeQuery(Sql);
		LogInfo.logOut("读取市区域词", ps);
		readResultSet(entityList, rsCity, ps, POS.REGION);
		LogInfo.logOut("共读市区域词 " + entityList.size() + " 个", ps);
		rsCity.close();
		Sql = "select t.caption,t.alias from t_util_provincecode  t where t.caption not in ('北京市','天津市','上海市','重庆市')";
		LogInfo.logOut("查询数据库", ps);
		ResultSet rsPro = stmt.executeQuery(Sql);
		LogInfo.logOut("读取省区域词", ps);
		readResultSet(entityList, rsPro, ps, POS.REGION);
		LogInfo.logOut("共读省区域词 " + entityList.size() + " 个", ps);
		rsPro.close();
	}

	private static void readResultSet(ArrayList entityList, ResultSet rs, PrintStream ps, int pos)
		throws SQLException
	{
		while (rs.next()) 
		{
			String strCaption = rs.getString("caption");
			if (strCaption != null && strCaption.length() < 10 && strCaption.length() != 1)
			{
				entityList.add(strCaption + "##" + pos);
				if (entityList.size() % 10000 == 0)
					LogInfo.logOut("已读入实体模式串 " + entityList.size() + " 个", ps);
				String strAlias = rs.getString("alias");
				if (strAlias != null && !strAlias.equals("") && !strAlias.equals("0"))
				{
					String alias1[] = strAlias.split("，");
					for (int i = 0; i < alias1.length; i++)
					{
						String alias2[] = alias1[i].split(",");
						for (int j = 0; j < alias2.length; j++)
							if (alias2[j] != null || strAlias.equals("") || strAlias.equals("0"))
								entityList.add(alias2[j] + "##" + pos);

					}

				}
			}
		}
	}

	public static String queryFilter(String str)
	{
		String ret = "";
		int len = str.length();
		for (int i = 0; i < len; i++)
			if (-1 == SPECIALSYMBOL_STRING.indexOf(str.charAt(i)))
				ret = ret + str.charAt(i);

		return ret;
	}

	static 
	{
		SPECIALSYMBOL_STRING = String.valueOf(SPECIALSYMBOL_LIST);
		FILEPATH = System.getProperty("user.dir");
		ENTITY_FILE = FILEPATH + "\\dic\\entity.txt";
		DICT_FILE = FILEPATH + "\\dic\\dictionary.dct";
		LOG_FILE = FILEPATH + "\\dic\\log.txt";
		RESULT_FILE = FILEPATH + "\\dic\\result.txt";
		WORD_FILE = FILEPATH + "\\dic\\word.dct";
		VOCABLE_FILE = FILEPATH + "\\dic\\vocable.txt";
	}
}
