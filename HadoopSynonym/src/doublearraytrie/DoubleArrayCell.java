// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DoubleArrayTrie.java

package doublearraytrie;

import java.util.ArrayList;

// Referenced classes of package doublearraytrie:
//			DoubleArrayUtility, Int

class DoubleArrayCell
{

	int base;
	int check;
	int failure;
	ArrayList output;

	DoubleArrayCell(int bs, int chk)
		throws Exception
	{
		base = bs;
		check = chk;
		failure = DoubleArrayUtility.daGetRoot();
		output = new ArrayList();
	}

	public void mergeOuput(DoubleArrayCell daOtherCell)
	{
		int count = daOtherCell.output.size();
		for (int i = 0; i < count; i++)
		{
			Int o = (Int)daOtherCell.output.get(i);
			output.add(new Int(o.val));
		}

	}
}
