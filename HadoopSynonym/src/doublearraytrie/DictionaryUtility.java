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
		LogInfo.logOut("�������ݿ�", ps);
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement stmt = conn.createStatement(1003, 1007);
		ResultSet rs = null;
		String Sql = " select distinct t.caption, t.alias from lsp.t_lyr_entity t left join lsp.t_local_poirank tt on t.uniqueID = tt.uniqueID left join lsp.T_LYR_LAYERINFO a on t.layerid = a.id where ((t.category = '����' and t.subcategory = '����')  or t.category = '���ﳡ��' or t.category = '�ֵ�����' or t.category = '���㳡��' or t.category = '����С��' or t.category = '¥��칫' or t.category = '���ξ���' or t.category = '��ҵ����' or t.category = '��������' or t.category = 'ѧУ����' or t.category = 'ҽ������' or t.category = '��������' or t.category = '��������' or t.subcategory = '��·' or t.subcategory = '����' or t.subcategory = '������' ) and a.caption like '%�޷�%' ";
		LogInfo.logOut("��ѯ���ݿ�", ps);
		rs = stmt.executeQuery(Sql);
		LogInfo.logOut("��ȡʵ��", ps);
		readResultSet(entityList, rs, ps, POS.ENTITY);
		rs.close();
		Sql = " select distinct city,caption,alias from t_lyr_entity where layerid=(select id from t_lyr_layerinfo t where caption ='�޷��й�_������') order by city,caption";
		ResultSet rsClass = null;
		LogInfo.logOut("��ѯ���ݿ�", ps);
		rsClass = stmt.executeQuery(Sql);
		LogInfo.logOut("��ȡ�������", ps);
		readResultSet(entityList, rsClass, ps, POS.REGION);
		LogInfo.logOut("����ȡ��" + entityList.size() + " ��", ps);
		rsClass.close();
		Sql = "select t.caption,t.alias from t_util_citycode t";
		LogInfo.logOut("��ѯ���ݿ�", ps);
		ResultSet rsCity = stmt.executeQuery(Sql);
		LogInfo.logOut("��ȡ�������", ps);
		readResultSet(entityList, rsCity, ps, POS.REGION);
		LogInfo.logOut("����������� " + entityList.size() + " ��", ps);
		rsCity.close();
		Sql = "select t.caption,t.alias from t_util_provincecode  t where t.caption not in ('������','�����','�Ϻ���','������')";
		LogInfo.logOut("��ѯ���ݿ�", ps);
		ResultSet rsPro = stmt.executeQuery(Sql);
		LogInfo.logOut("��ȡʡ�����", ps);
		readResultSet(entityList, rsPro, ps, POS.REGION);
		LogInfo.logOut("����ʡ����� " + entityList.size() + " ��", ps);
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
					LogInfo.logOut("�Ѷ���ʵ��ģʽ�� " + entityList.size() + " ��", ps);
				String strAlias = rs.getString("alias");
				if (strAlias != null && !strAlias.equals("") && !strAlias.equals("0"))
				{
					String alias1[] = strAlias.split("��");
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
