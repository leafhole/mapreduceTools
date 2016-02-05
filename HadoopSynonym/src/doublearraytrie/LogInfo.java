// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LogInfo.java

package doublearraytrie;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class LogInfo
{

	public LogInfo()
	{
	}

	public static String getSystemDate()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("ddºÅ hh:mm:ss:");
		return f.format(c.getTime());
	}

	public static void logOut(String strLog, PrintStream ps)
	{
		ps.println(getSystemDate() + strLog);
	}

	public static String newLogFile()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("ddºÅhhmmss");
		String sFileName = "LOG_" + f.format(c.getTime());
		StringBuffer sFilePath = new StringBuffer(System.getProperty("user.dir"));
		String sFullName = sFilePath + "\\document\\" + sFileName + ".txt";
		return sFullName;
	}
}
