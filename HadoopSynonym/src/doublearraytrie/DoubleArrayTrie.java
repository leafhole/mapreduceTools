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
			boolean re = trieRetrieve(datRf, "��ׯ·");
			System.out.println(re);
			String str = "��0һ��ѧ";
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
			ps.println("�ؼ���:" + key + "," + "��ѯ���:" + bWord);
			if (bWord)
				iRight++;
			else
				iError++;
		}

		ps.println("TRUE:" + iRight + "   " + "FALSE:" + iError);
		ps.println("�ʿ��С��" + daTrie.size());
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
			LogInfo.logOut("�ؼ���:" + line + "," + "��ѯ���:" + bWord, ps);
			if (bWord)
				iRight++;
			else
				iError++;
		}

		is.close();
		iCount = iRight + iError;
		LogInfo.logOut("TRUE:" + iRight, ps);
		LogInfo.logOut("FALSE:" + iError, ps);
		LogInfo.logOut("�ʿ��С��" + iCount, ps);
		LogInfo.logOut("�˴β�ѯ׼ȷ�ʣ�" + (100F * (float) iRight) / (float) iCount
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
				LogInfo.logOut("�ѹ��� " + i + "��", ps);
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
		LogInfo.logOut("������ʱ��:" + (end - start) / 1000L + "��.", ps);
		LogInfo.logOut("�������:" + al.size(), ps);
		LogInfo.logOut("�ַ��ܳ���:" + len, ps);
		LogInfo.logOut("��ƽ������:" + (float) len / (float) al.size(), ps);
		LogInfo.logOut("����ɹ�����:" + iRight, ps);
		LogInfo.logOut("����ʧ�ܴ���:" + iError, ps);
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
				LogInfo.logOut("���� " + key + " (" + count + ")", ps);
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
		LogInfo.logOut("������ʱ��:" + (end - start) / 1000L + "��.", ps);
		LogInfo.logOut("�������:" + count, ps);
		LogInfo.logOut("�ַ��ܳ���:" + len, ps);
		LogInfo.logOut("��ƽ������:" + (float) len / (float) count, ps);
		LogInfo.logOut("����ɹ�����:" + iRight, ps);
		LogInfo.logOut("����ʧ�ܴ���:" + iError, ps);
	}
}
