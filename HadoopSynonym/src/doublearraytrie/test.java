// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   test.java

package doublearraytrie;

import java.io.PrintStream;
import java.util.ArrayList;

public class test
{

	private static final int ASCII_MAX = 128;
	public static final int TRIE_STARTBASE = 1;
	private static final int GB2312_HIGH_MIN = 160;
	private static final int GB2312_HIGH_MAX = 254;
	private static final int GB2312_LOW_MIN = 160;
	private static final int GB2312_LOW_MAX = 254;

	public test()
	{
	}

	public static void main(String args[])
	{
		int ID_MAX = 12254;
		System.out.println(ID_MAX);
		long t = System.currentTimeMillis();
		for (int i = 0; i < 0x7fffffff; i++)
		{
			ArrayList list = new ArrayList();
			for (int j = 0; j < 1; j++)
				list.add(new Integer(10));

		}

		System.out.println(System.currentTimeMillis() - t);
	}
}
