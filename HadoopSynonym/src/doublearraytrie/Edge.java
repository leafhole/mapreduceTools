// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Edge.java

package doublearraytrie;


public class Edge
{

	public int type;
	public int weight;

	public Edge(int w, int t)
	{
		weight = w;
		type = t;
	}
}
