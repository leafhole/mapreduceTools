// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DoubleArrayTrie.java

package doublearraytrie;

import java.io.*;
import java.util.ArrayList;

// Referenced classes of package doublearraytrie:
//			DictionaryUtility, Int, DoubleArrayUtility, FileUtility, 
//			LogInfo, POS, DoubleArrayCell

public final class DoubleArrayTrie {
	public ArrayList daTrie;

	public DoubleArrayTrie() {
		daTrie = new ArrayList(2000);
	}

	public static void main(String args[]) {
		try {
			RandomAccessFile datRf = new RandomAccessFile(
					DictionaryUtility.DICT_FILE, "r");
			boolean re = trieRetrieve(datRf, "宋庄路");
			System.out.println(re);
			String str = "三0一中学";
			ArrayList alTerm = DictionaryUtility.queryDictionary(str);
			for (int i = 0; i < alTerm.size(); i++) {
				Term term = (Term) alTerm.get(i);
				System.out.println(term.wClass + ", " + term.value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean trieRetrieve(ArrayList daTrie, String str)
			throws Exception {
		Int s = new Int(DoubleArrayUtility.daGetRoot());
		for (int i = 0; i != str.length(); i++) {
			boolean bWalk = DoubleArrayUtility.daWalk(daTrie, s, str.charAt(i));
			if (!bWalk)
				return false;
		}

		int base = DoubleArrayUtility.daGetBase(daTrie, s.val);
		return base < 0;
	}

	public static boolean trieRetrieve(RandomAccessFile rf, String str)
			throws Exception {
		Int s = new Int(DoubleArrayUtility.daGetRoot());
		Int base = new Int();
		Int check = new Int();
		if (!FileUtility.randomAccess(rf, s.val, base, check))
			return false;
		for (int i = 0; i != str.length(); i++) {
			boolean bWalk = DoubleArrayUtility.daWalk(rf, base, s, str
					.charAt(i));
			if (!bWalk)
				return false;
		}

		if (!FileUtility.randomAccess(rf, s.val, base, check))
			return false;
		return base.val < 0;
	}

	public static void trieRetrieve(ArrayList daTrie) throws Exception {
		ArrayList al = new ArrayList();
		FileUtility.readTxt(al, DictionaryUtility.ENTITY_FILE);
		java.io.OutputStream os = new FileOutputStream(
				DictionaryUtility.RESULT_FILE, false);
		PrintStream ps = new PrintStream(os);
		int iRight = 0;
		int iError = 0;
		for (int i = 0; i < al.size(); i++) {
			String key = (String) al.get(i);
			boolean bWord = trieRetrieve(daTrie, key);
			ps.println("关键字:" + key + "," + "查询结果:" + bWord);
			if (bWord)
				iRight++;
			else
				iError++;
		}

		ps.println("TRUE:" + iRight + "   " + "FALSE:" + iError);
		ps.println("词库大小：" + daTrie.size());
		ps.close();
	}

	public static void trieRetrieve(RandomAccessFile rf) throws Exception {
		java.io.OutputStream os = new FileOutputStream(
				DictionaryUtility.RESULT_FILE, false);
		PrintStream ps = new PrintStream(os);
		int iRight = 0;
		int iError = 0;
		int iCount = 0;
		FileInputStream is = new FileInputStream(DictionaryUtility.ENTITY_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			line = line.substring(0, line.indexOf("##"));
			boolean bWord = trieRetrieve(rf, line);
			LogInfo.logOut("关键字:" + line + "," + "查询结果:" + bWord, ps);
			if (bWord)
				iRight++;
			else
				iError++;
		}

		is.close();
		iCount = iRight + iError;
		LogInfo.logOut("TRUE:" + iRight, ps);
		LogInfo.logOut("FALSE:" + iError, ps);
		LogInfo.logOut("词库大小：" + iCount, ps);
		LogInfo.logOut("此次查询准确率：" + (100F * (float) iRight) / (float) iCount
				+ "%", ps);
		ps.close();
	}

	public static void trieStore(ArrayList daTrie, ArrayList al, PrintStream ps)
			throws Exception {
		int index = DoubleArrayUtility.daGetRoot();
		int len = 0;
		int iError = 0;
		int iRight = 0;
		int ctnCount = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i != al.size(); i++) {
			String key = (String) al.get(i);
			int temp = key.indexOf(POS.SEP);
			key = key.substring(0, temp);
			Int Index = new Int(index);
			len += key.length();
			if (i % 1000 == 0)
				LogInfo.logOut("已构造 " + i + "个", ps);
			boolean bHasExist = true;
			for (int j = 0; j < key.length(); j++) {
				if (DoubleArrayUtility.daWalk(daTrie, Index, key.charAt(j))) {
					ctnCount++;
					continue;
				}
				String branch = key.substring(j, key.length());
				if (!DoubleArrayUtility
						.daInsertBranch(daTrie, Index, branch, i))
					iError++;
				else
					iRight++;
				bHasExist = false;
				break;
			}

			if (bHasExist) {
				DoubleArrayCell daCell = DoubleArrayUtility.daGetCell(daTrie,
						Index.val);
				daCell.base = -1 * daCell.base;
				daCell.output.add(new Int(i));
			}
		}

		long end = System.currentTimeMillis();
		LogInfo.logOut("构造总时间:" + (end - start) / 1000L + "秒.", ps);
		LogInfo.logOut("构造词数:" + al.size(), ps);
		LogInfo.logOut("字符总长度:" + len, ps);
		LogInfo.logOut("词平均长度:" + (float) len / (float) al.size(), ps);
		LogInfo.logOut("构造成功词数:" + iRight, ps);
		LogInfo.logOut("构造失败词数:" + iError, ps);
	}

	public static void trieStore(ArrayList daTrie, PrintStream ps)
			throws Exception {
		int index = DoubleArrayUtility.daGetRoot();
		int len = 0;
		int count = 0;
		int iError = 0;
		int iRight = 0;
		long start = System.currentTimeMillis();
		FileInputStream is = new FileInputStream(DictionaryUtility.ENTITY_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		for (String key = reader.readLine(); key != null; key = reader
				.readLine()) {
			count++;
			Int Index = new Int(index);
			len += key.length();
			if (count % 1000 == 0)
				LogInfo.logOut("构造 " + key + " (" + count + ")", ps);
			boolean bHasExist = true;
			for (int j = 0; j < key.length(); j++) {
				if (DoubleArrayUtility.daWalk(daTrie, Index, key.charAt(j)))
					continue;
				String branch = key.substring(j, key.length());
				if (!DoubleArrayUtility.daInsertBranch(daTrie, Index, branch,
						count - 1))
					iError++;
				else
					iRight++;
				bHasExist = false;
				break;
			}

			if (bHasExist) {
				DoubleArrayCell daCell = DoubleArrayUtility.daGetCell(daTrie,
						Index.val);
				daCell.base = -1 * daCell.base;
				daCell.output.add(new Int(count - 1));
			}
		}

		is.close();
		long end = System.currentTimeMillis();
		LogInfo.logOut("构造总时间:" + (end - start) / 1000L + "秒.", ps);
		LogInfo.logOut("构造词数:" + count, ps);
		LogInfo.logOut("字符总长度:" + len, ps);
		LogInfo.logOut("词平均长度:" + (float) len / (float) count, ps);
		LogInfo.logOut("构造成功词数:" + iRight, ps);
		LogInfo.logOut("构造失败词数:" + iError, ps);
	}
}
