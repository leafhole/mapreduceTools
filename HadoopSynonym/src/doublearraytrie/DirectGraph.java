// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DirectGraph.java

package doublearraytrie;

import java.io.PrintStream;

// Referenced classes of package com.go2map.lsp.local.engine.mapservice.queryAnalyzer.util:
//			Edge

public class DirectGraph
{

	Edge mat[][];
	int n;

	public DirectGraph(int vertex)
	{
		mat = new Edge[vertex][vertex];
		n = vertex;
	}

	public void addEdge(int start, int end, int weight)
	{
		mat[start][end] = new Edge(weight, -1);
	}

	public void addEdge(int start, int end, int weight, int type)
	{
		mat[start][end] = new Edge(weight, type);
	}

	public Edge getEdge(int start, int end)
	{
		return mat[start][end];
	}

	public void removeEdge(int start, int end)
	{
		mat[start][end] = null;
	}

	public int[] getShorstPath(int start, int end)
	{
		int path[] = new int[n];
		path[0] = -1;
		int flag[] = new int[n];
		int value[] = new int[n];
		for (int i = 0; i < n; i++)
			value[i] = 0x7fffffff;

		value[start] = 0;
		while (!isEmpty(flag)) 
		{
			int min = extractMin(value, flag);
			flag[min] = -1;
			for (int i = 0; i < n; i++)
				if (mat[min][i] != null)
				{
					int t = value[min] + mat[min][i].weight;
					if (t < value[i])
					{
						value[i] = t;
						path[i] = min;
					}
				}

		}
		return path;
	}

	private int extractMin(int value[], int flag[])
	{
		int min = 0x7fffffff;
		int p = 0;
		for (int i = 1; i < n; i++)
			if (flag[i] == 0 && value[i] < min)
			{
				min = value[i];
				p = i;
			}

		return p;
	}

	private boolean isEmpty(int flag[])
	{
		for (int i = 0; i < n; i++)
			if (flag[i] == 0)
				return false;

		return true;
	}

	public static void main(String args[])
	{
		int n = 6;
		DirectGraph dg = new DirectGraph(n);
		for (int i = 0; i < n - 1; i++)
			dg.addEdge(i, i + 1, 1);

		dg.addEdge(0, 2, 1);
		dg.addEdge(1, 3, 1);
		dg.addEdge(3, 5, 1);
		int p[] = dg.getShorstPath(0, n - 1);
		for (int t = n - 1; t >= 0; t = p[t])
			System.out.print(t + "  ");

	}
}
