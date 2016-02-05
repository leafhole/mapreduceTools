// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileUtility.java

package doublearraytrie;

import java.io.*;
import java.util.ArrayList;

// Referenced classes of package doublearraytrie:
//			DoubleArrayCell, Int, POS

public final class FileUtility
{

	private static final int INT_SIZE = 4;
	private static final int STATE_SIZE = 5;

	public FileUtility()
	{
	}

	public static void writeTxt(ArrayList al, String sFileName)
		throws FileNotFoundException
	{
		java.io.OutputStream os = new FileOutputStream(sFileName, false);
		PrintStream ps = new PrintStream(os);
		String strOld = "";
		for (int i = 0; i < al.size(); i++)
		{
			String strWord = (String)al.get(i);
			if (!strWord.equals(strOld))
			{
				strOld = strWord;
				ps.println(strWord);
			}
		}

		ps.close();
	}

	public static void readTxt(ArrayList al, String sFileName)
		throws IOException
	{
		FileInputStream is = new FileInputStream(sFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		for (String line = reader.readLine(); line != null; line = reader.readLine())
			al.add(line);

		is.close();
	}

	public static void writeDictionary(ArrayList daTrie, ArrayList entityList, String sDatFile, String sWordFile)
		throws IOException
	{
		RandomAccessFile datRf = new RandomAccessFile(sDatFile, "rw");
		RandomAccessFile wordRf = new RandomAccessFile(sWordFile, "rw");
		int offset = 0;
		for (int i = 0; i != daTrie.size(); i++)
		{
			DoubleArrayCell daCell = (DoubleArrayCell)daTrie.get(i);
			datRf.writeInt(daCell.base);
			datRf.writeInt(daCell.check);
			datRf.writeInt(daCell.failure);
			datRf.writeInt(offset);
			ArrayList output = daCell.output;
			if (output.size() == 0)
			{
				datRf.writeInt(0);
			} else
			{
				int byteLen = 4;
				wordRf.writeInt(output.size());
				for (int j = 0; j < output.size(); j++)
				{
					Int wordIndex = (Int)output.get(j);
					String str = (String)entityList.get(wordIndex.val);
					int temp = str.indexOf(POS.SEP);
					String word = str.substring(0, temp);
					String strPos = str.substring(temp + 2);
					int pos = Integer.parseInt(strPos);
					wordRf.writeInt(word.length());
					wordRf.writeInt(pos);
					wordRf.writeChars(word);
					byteLen += word.length() * 2 + 8;
				}

				datRf.writeInt(byteLen);
				offset += byteLen;
			}
		}

		datRf.close();
		wordRf.close();
	}

	public static boolean randomAccess(RandomAccessFile rf, int index, Int base, Int check)
		throws IOException
	{
		if ((long)(index * 4 * 5) > rf.length())
		{
			return false;
		} else
		{
			rf.seek(index * 4 * 5);
			base.val = rf.readInt();
			check.val = rf.readInt();
			rf.seek(0L);
			return true;
		}
	}

	public static boolean readBytes(RandomAccessFile rf, int index, byte bytes[])
		throws IOException
	{
		if ((long)(index * 4 * 5) > rf.length())
		{
			return false;
		} else
		{
			rf.seek(index * 4 * 5);
			rf.read(bytes);
			rf.seek(0L);
			return true;
		}
	}
}
