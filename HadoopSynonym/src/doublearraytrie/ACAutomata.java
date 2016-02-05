// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ACAutomata.java

package doublearraytrie;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import sun.misc.Queue;

// Referenced classes of package doublearraytrie:
//			DoubleArrayUtility, Int, DoubleArrayCell, POS, 
//			FileUtility, TypeConverter

public final class ACAutomata
{

	private static final int BASE_OFFSET = 0;
	private static final int CHECK_OFFSET = 4;
	private static final int FAILURE_OFFSET = 8;
	private static final int OUTPUT_OFFSET = 12;
	private static final int BYTE_OFFSET = 16;
	private static char NUMBER_CHARACTER[] = {
		'\u96F6', '\u4E00', '\u4E8C', '\u4E09', '\u56DB', '\u4E94', '\u516D', '\u4E03', '\u516B', '\u4E5D', 
		'\u5341', '\u767E', '\u5343', '\u4E07', '\u4EBF'
	};
	private static int CH_NUMBER[];

	public ACAutomata()
	{
	}

	public static void acFailure(ArrayList daTrie)
		throws Exception
	{
		Queue state = new Queue();
		int root = DoubleArrayUtility.daGetRoot();
		int idmax = 12254;
		for (int i = 1; i < idmax; i++)
		{
			int next = DoubleArrayUtility.daGetNext(daTrie, root, i);
			int check = DoubleArrayUtility.daGetCheck(daTrie, next);
			if (check == root)
				state.enqueue(new Int(next));
		}

		while (!state.isEmpty()) 
		{
			Int s = (Int)state.dequeue();
			DoubleArrayCell daCell = DoubleArrayUtility.daGetCell(daTrie, s.val);
			for (int i = 1; i < idmax; i++)
			{
				int next = Math.abs(daCell.base) + i;
				if (next >= daTrie.size())
					break;
				DoubleArrayCell daNextCell = DoubleArrayUtility.daGetCell(daTrie, next);
				if (daNextCell.check == s.val)
				{
					state.enqueue(new Int(next));
					int failure = daCell.failure;
					boolean walk;
					for (walk = DoubleArrayUtility.daWalk(daTrie, failure, i); !walk; walk = DoubleArrayUtility.daWalk(daTrie, failure, i))
					{
						if (failure == DoubleArrayUtility.daGetRoot())
							break;
						failure = DoubleArrayUtility.daGetCell(daTrie, failure).failure;
					}

					if (walk)
					{
						next = DoubleArrayUtility.daGetNext(daTrie, failure, i);
						daNextCell.failure = next;
						daNextCell.mergeOuput(DoubleArrayUtility.daGetCell(daTrie, next));
					}
				}
			}

		}
	}

	public static DirectGraph acMatch(ArrayList daTrie, ArrayList entityList, String str)
		throws Exception
	{
		Int s = new Int(DoubleArrayUtility.daGetRoot());
		DirectGraph graph = new DirectGraph(str.length() + 1);
		Int lorm = new Int(POS.DEFAULT);
		Int len = new Int(0);
		for (int i = 0; i < str.length(); i++)
		{
			graph.addEdge(i, i + 1, 1, 0);
			int id = DoubleArrayUtility.getWordID(str.charAt(i));
			isLetterOrNumber(id, lorm, len, i, graph);
			if (i == str.length() - 1 && len.val > 0)
				graph.addEdge((i - len.val) + 1, i + 1, 1, lorm.val);
			while (!DoubleArrayUtility.daWalk(daTrie, s, id)) 
			{
				if (s.val == DoubleArrayUtility.daGetRoot())
					break;
				DoubleArrayCell daCell = DoubleArrayUtility.daGetCell(daTrie, s.val);
				s.val = daCell.failure;
			}
			DoubleArrayCell daOutputCell = DoubleArrayUtility.daGetCell(daTrie, s.val);
			int size = daOutputCell.output.size();
			if (size > 0)
			{
				for (int j = 0; j < size; j++)
				{
					Int matchWord = (Int)daOutputCell.output.get(j);
					String data = (String)entityList.get(matchWord.val);
					int temp = data.lastIndexOf(POS.SEP);
					String word = data.substring(0, temp);
					String strPos = data.substring(temp + 2);
					int pos = Integer.parseInt(strPos);
					if (word.length() == 1)
					{
						Edge edge = graph.getEdge(i, i + 1);
						if (edge != null)
							edge.type = pos;
						else
							graph.addEdge(i, i + 1, 1, pos);
					} else
					{
						graph.addEdge((i + 1) - word.length(), i + 1, 1, pos);
					}
				}

			}
		}

		return graph;
	}

	public static DirectGraph acMatch(RandomAccessFile datRf, RandomAccessFile wordRf, String str)
		throws Exception
	{
		DirectGraph graph = new DirectGraph(str.length() + 1);
		byte bytes[] = new byte[20];
		int state = DoubleArrayUtility.daGetRoot();
		FileUtility.readBytes(datRf, state, bytes);
		int base = TypeConverter.byteToInt(bytes, 0, 4);
		int failure = TypeConverter.byteToInt(bytes, 8, 4);
		int rootbase = base;
		int rootfailure = DoubleArrayUtility.daGetRoot();
		Int lorm = new Int(POS.DEFAULT);
		Int len = new Int(0);
		for (int i = 0; i < str.length(); i++)
		{
			graph.addEdge(i, i + 1, 1, 0);
			int id = DoubleArrayUtility.getWordID(str.charAt(i));
			int next = Math.abs(base) + id;
			isLetterOrNumber(id, lorm, len, i, graph);
			if (i == str.length() - 1 && len.val > 0)
				graph.addEdge((i - len.val) + 1, i + 1, 1, lorm.val);
			FileUtility.readBytes(datRf, next, bytes);
			int nextcheck = TypeConverter.byteToInt(bytes, 4, 4);
			base = TypeConverter.byteToInt(bytes, 0, 4);
			boolean isStateExist = true;
			for (; nextcheck != state; nextcheck = TypeConverter.byteToInt(bytes, 4, 4))
			{
				if (state == DoubleArrayUtility.daGetRoot())
				{
					isStateExist = false;
					state = rootfailure;
					base = rootbase;
					break;
				}
				state = failure;
				FileUtility.readBytes(datRf, state, bytes);
				base = TypeConverter.byteToInt(bytes, 0, 4);
				failure = TypeConverter.byteToInt(bytes, 8, 4);
				next = Math.abs(base) + id;
				FileUtility.readBytes(datRf, next, bytes);
			}

			if (nextcheck == state)
			{
				state = next;
				base = TypeConverter.byteToInt(bytes, 0, 4);
				failure = TypeConverter.byteToInt(bytes, 8, 4);
			}
			if (isStateExist)
			{
				int size = TypeConverter.byteToInt(bytes, 16, 4);
				if (size > 0)
				{
					int outputoffset = TypeConverter.byteToInt(bytes, 12, 4);
					int bytelen = TypeConverter.byteToInt(bytes, 16, 4);
					byte byteword[] = new byte[bytelen];
					wordRf.seek(outputoffset);
					wordRf.read(byteword);
					int wordsize = TypeConverter.byteToInt(byteword, 0, 4);
					int off = 4;
					for (int j = 0; j < wordsize; j++)
					{
						int wordlen = TypeConverter.byteToInt(byteword, off, 4);
						int pos = TypeConverter.byteToInt(byteword, off + 4, 4);
						off += 8 + wordlen;
						if (wordlen == 1)
						{
							Edge edge = graph.getEdge(i, i + 1);
							if (edge != null)
								edge.type = pos;
							else
								graph.addEdge(i, i + 1, 1, pos);
						} else
						{
							graph.addEdge((i + 1) - wordlen, i + 1, 1, pos);
						}
					}

				}
			}
		}

		return graph;
	}

	private static int charType(int id)
	{
		if (id >= 48 && id <= 57)
			return POS.NUMBER;
		if (id >= 65 && id <= 90 || id >= 97 && id <= 122)
			return POS.LETTER;
		for (int i = 0; i < CH_NUMBER.length; i++)
			if (CH_NUMBER[i] == id)
				return POS.CHNUMBER;

		return POS.DEFAULT;
	}

	private static void isLetterOrNumber(int id, Int lorm, Int len, int index, DirectGraph graph)
	{
		int type = charType(id);
		if (type == POS.DEFAULT)
		{
			if (lorm.val != POS.DEFAULT)
				graph.addEdge(index - len.val, index, 1, lorm.val);
			lorm.val = POS.DEFAULT;
			len.val = 0;
		} else
		if (lorm.val != type)
		{
			if (lorm.val != POS.DEFAULT)
				graph.addEdge(index - len.val, index, 1, lorm.val);
			len.val = 1;
			lorm.val = type;
		} else
		{
			len.val++;
		}
	}

	static 
	{
		CH_NUMBER = new int[NUMBER_CHARACTER.length];
		for (int i = 0; i < CH_NUMBER.length; i++)
			try
			{
				CH_NUMBER[i] = DoubleArrayUtility.getWordID(NUMBER_CHARACTER[i]);
			}
			catch (Exception e)
			{
				CH_NUMBER[i] = 0;
			}

	}
}
