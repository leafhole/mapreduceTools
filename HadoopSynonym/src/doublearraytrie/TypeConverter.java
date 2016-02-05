// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeConverter.java

package doublearraytrie;


public class TypeConverter
{

	public TypeConverter()
	{
	}

	private static int intValue(byte bytes[])
	{
		if (bytes.length != 4)
		{
			return 0;
		} else
		{
			int b0 = bytes[0] & 0xff;
			int b1 = bytes[1] & 0xff;
			int b2 = bytes[2] & 0xff;
			int b3 = bytes[3] & 0xff;
			return (b0 << 24) + (b1 << 16) + (b2 << 8) + b3;
		}
	}

	public static int byteToInt(byte bytes[], int offset, int len)
	{
		byte b[] = subByte(bytes, offset, len);
		return intValue(b);
	}

	public static char byteToChar(byte high, byte low)
	{
		return (char)(((high & 0xff) << 8) + (low & 0xff));
	}

	public static String byteToString(byte bytes[])
	{
		int strLen = 0;
		if (bytes.length % 2 == 0)
			strLen = bytes.length / 2;
		else
			strLen = (bytes.length + 1) / 2;
		char chars[] = new char[strLen];
		for (int i = 0; i < strLen; i++)
			chars[i] = byteToChar(bytes[i * 2], bytes[i * 2 + 1]);

		return String.valueOf(chars);
	}

	public static byte[] subByte(byte bytes[], int offset, int len)
	{
		byte b[] = new byte[len];
		for (int i = 0; i < len; i++)
		{
			if (offset + i >= bytes.length)
				break;
			b[i] = bytes[offset + i];
		}

		return b;
	}
}
