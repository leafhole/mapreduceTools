// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DoubleArrayUtility.java

package doublearraytrie;

import java.io.RandomAccessFile;
import java.util.ArrayList;

// Referenced classes of package doublearraytrie:
//			DoubleArrayCell, Int, FileUtility

public final class DoubleArrayUtility {

	private static final int ASCII_MAX = 128;
	public static final int TRIE_STARTBASE = 1;
	private static final int GB2312_HIGH_MIN = 160;
	private static final int GB2312_HIGH_MAX = 254;
	private static final int GB2312_LOW_MIN = 160;
	private static final int GB2312_LOW_MAX = 254;
	public static final int ID_MAX = 12254;
	private static final double BASE_COE = 1.3999999999999999D;

	public DoubleArrayUtility() {
	}

	public static int getWordID(char c) throws Exception {
		String str = String.valueOf(c);
		byte bytes[] = str.getBytes("GB18030");

		int nCode = -1;
		if (bytes.length == 1) {
			nCode = (256 + bytes[0]) % 256;
		} else if (bytes.length == 2) {
			int ch0 = (256 + bytes[0]) % 256;
			int ch1 = (256 + bytes[1]) % 256;
			if (ch0 >= 160 && ch0 <= 254 && ch1 >= 160 && ch1 <= 254)
				nCode = 128 + (ch0 - 160 << 7) + (ch1 - 160);
		}
		return nCode;
	}

	public static void daInitialize(ArrayList daTrie) throws Exception {
		int rootBase = daGetRoot();
		for (int i = 0; i != rootBase; i++)
			daTrie.add(new DoubleArrayCell(0, 0));

		daTrie.add(rootBase, new DoubleArrayCell(rootBase, 0));
	}

	public static void daExtend(ArrayList daTrie, int toIndex) throws Exception {
		for (; daTrie.size() <= toIndex; daTrie.add(new DoubleArrayCell(0, 0)))
			;
	}

	public static int daGetRoot() throws Exception {
		return 1;
	}

	public static void daSetBase(ArrayList daTrie, int index, int base)
			throws Exception {
		DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
		daCell.base = base;
	}

	public static void daSetCheck(ArrayList daTrie, int index, int check)
			throws Exception {
		DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
		daCell.check = check;
	}

	public static void daSetValue(ArrayList daTrie, int index, int base,
			int check) throws Exception {
		DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
		daCell.base = base;
		daCell.check = check;
	}

	public static DoubleArrayCell daGetCell(ArrayList daTrie, int index)
			throws Exception {
		return (DoubleArrayCell) daTrie.get(index);
	}

	public static int daGetNext(ArrayList daTrie, int index, int id)
			throws Exception {
		int base = daGetBase(daTrie, index);
		return Math.abs(base) + id;
	}

	public static boolean daWalk(ArrayList daTrie, int index, int id)
			throws Exception {
		int next = daGetNext(daTrie, index, id);
		int check = daGetCheck(daTrie, next);
		return check == index;
	}

	public static int daGetBase(ArrayList daTrie, int index) throws Exception {
		int base = 0;
		if (index < daTrie.size()) {
			DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
			base = daCell.base;
		}
		return base;
	}

	public static int daGetCheck(ArrayList daTrie, int index) throws Exception {
		int check = 0;
		if (index < daTrie.size()) {
			DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
			check = daCell.check;
		}
		return check;
	}

	public static boolean daWalk(ArrayList daTrie, Int Index, char in)
			throws Exception {
		return daWalk(daTrie, Index, getWordID(in));
	}

	public static boolean daWalk(ArrayList daTrie, Int Index, int id)
			throws Exception {
		int base = Math.abs(daGetBase(daTrie, Index.val));
		int next = base + id;
		int check = daGetCheck(daTrie, next);
		if (check == Index.val) {
			Index.val = next;
			return true;
		} else {
			return false;
		}
	}

	public static boolean daWalk(RandomAccessFile rf, Int Base, Int Index,
			char in) throws Exception {
		Int Check = new Int();
		int base = Math.abs(Base.val);
		int next = base + getWordID(in);
		if (!FileUtility.randomAccess(rf, next, Base, Check))
			return false;
		if (Check.val == Index.val) {
			Index.val = next;
			return true;
		} else {
			return false;
		}
	}

	public static boolean daCheckFreeCell(ArrayList daTrie, int base, int id)
			throws Exception {
		int next = base + id;
		daExtend(daTrie, next);
		return daGetCheck(daTrie, next) <= 0;
	}

	public static ArrayList daOutputSymbols(ArrayList daTrie, int index)
			throws Exception {
		ArrayList syms = new ArrayList();
		int base = Math.abs(daGetBase(daTrie, index));
		for (int i = 1; i <= 12254; i++)
			if (daGetCheck(daTrie, base + i) == index)
				syms.add(new Int(i));

		return syms;
	}

	public static boolean daFitSymbols(ArrayList daTrie, int base,
			ArrayList syms) throws Exception {
		for (int i = 0; i < syms.size(); i++) {
			Int code = (Int) syms.get(i);
			if (!daCheckFreeCell(daTrie, base, code.val))
				return false;
		}

		return true;
	}

	public static int daFindFreeBase(ArrayList daTrie, int base, ArrayList syms)
			throws Exception {
		int newbase;
		for (newbase = base; !daFitSymbols(daTrie, newbase, syms); newbase++)
			;
		return newbase;
	}

	public static boolean daInsertBranch(ArrayList daTrie, Int Index,
			String key, int output) throws Exception {
		int coe = 1;
		int oldbase = daGetBase(daTrie, Index.val);
		if (oldbase < 0)
			coe = -1;
		oldbase = Math.abs(oldbase);
		int newbase = oldbase;
		for (int i = 0; i < key.length(); i++) {
			int code = getWordID(key.charAt(i));
			if (code == -1)
				return false;
			newbase = oldbase;
			if (!daCheckFreeCell(daTrie, oldbase, code)) {
				ArrayList syms = daOutputSymbols(daTrie, Index.val);
				syms.add(new Int(code));
				newbase = daFindFreeBase(daTrie, oldbase, syms);
				daRelocateBase(daTrie, Index.val, code, newbase, syms);
				if (i == 0)
					daSetBase(daTrie, Index.val, coe * newbase);
				else
					daSetBase(daTrie, Index.val, newbase);
			}
			int b = (int) ((double) (newbase + code) / 1.3999999999999999D);
			daSetValue(daTrie, newbase + code, b, Index.val);
			oldbase = b;
			Index.val = newbase + code;
		}

		DoubleArrayCell daCell = daGetCell(daTrie, Index.val);
		daCell.base = -1 * daCell.base;
		daCell.output.add(new Int(output));
		return true;
	}

	public static void daRelocateBase(ArrayList daTrie, int index, int id,
			int newbase, ArrayList syms) throws Exception {
		for (int i = 0; i < syms.size(); i++) {
			Int code = (Int) syms.get(i);
			int oldbase = Math.abs(daGetBase(daTrie, index));
			int oldnext = oldbase + code.val;
			int newnext = newbase + code.val;
			if (code.val != id) {
				DoubleArrayCell daCell = daGetCell(daTrie, oldnext);
				int oldnextbase = daCell.base;
				daCopyCell(daTrie, oldnext, newnext);
				for (int j = 1; j <= 12254; j++) {
					int oldnextnext = Math.abs(oldnextbase) + j;
					if (daGetCheck(daTrie, oldnextnext) == oldnext)
						daSetCheck(daTrie, oldnextnext, newnext);
				}

				daFreeCell(daTrie, oldnext);
			}
		}

	}

	public static void daFreeCell(ArrayList daTrie, int index) throws Exception {
		DoubleArrayCell daCell = (DoubleArrayCell) daTrie.get(index);
		daCell.base = 0;
		daCell.check = 0;
		daCell.failure = daGetRoot();
		daCell.output.clear();
	}

	public static void daCopyCell(ArrayList daTrie, int fromIndex, int toIndex) {
		DoubleArrayCell daFromCell = (DoubleArrayCell) daTrie.get(fromIndex);
		DoubleArrayCell daToCell = (DoubleArrayCell) daTrie.get(toIndex);
		daToCell.base = daFromCell.base;
		daToCell.check = daFromCell.check;
		daToCell.failure = daFromCell.failure;
		ArrayList alFromOutput = daFromCell.output;
		ArrayList alToOutput = daToCell.output;
		alToOutput.clear();
		for (int i = 0; i < alFromOutput.size(); i++) {
			Int op = (Int) alFromOutput.get(i);
			alToOutput.add(new Int(op.val));
		}

	}
}
