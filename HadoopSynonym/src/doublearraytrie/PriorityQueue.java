// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PriorityQueue.java

package doublearraytrie;


public abstract class PriorityQueue
{

	private Object heap[];
	private int size;
	private int maxSize;

	public PriorityQueue()
	{
	}

	protected abstract boolean lessThan(Object obj, Object obj1);

	protected final void initialize(int maxSize)
	{
		size = 0;
		int heapSize = maxSize + 1;
		heap = new Object[heapSize];
		this.maxSize = maxSize;
	}

	public final void put(Object element)
	{
		size++;
		heap[size] = element;
		upHeap();
	}

	public boolean insert(Object element)
	{
		if (size < maxSize)
		{
			put(element);
			return true;
		}
		if (size > 0 && !lessThan(element, top()))
		{
			heap[1] = element;
			adjustTop();
			return true;
		} else
		{
			return false;
		}
	}

	public final Object top()
	{
		if (size > 0)
			return heap[1];
		else
			return null;
	}

	public final Object pop()
	{
		if (size > 0)
		{
			Object result = heap[1];
			heap[1] = heap[size];
			heap[size] = null;
			size--;
			downHeap();
			return result;
		} else
		{
			return null;
		}
	}

	public final void adjustTop()
	{
		downHeap();
	}

	public final int size()
	{
		return size;
	}

	public final void clear()
	{
		for (int i = 0; i <= size; i++)
			heap[i] = null;

		size = 0;
	}

	private final void upHeap()
	{
		int i = size;
		Object node = heap[i];
		for (int j = i >>> 1; j > 0 && lessThan(node, heap[j]); j >>>= 1)
		{
			heap[i] = heap[j];
			i = j;
		}

		heap[i] = node;
	}

	private final void downHeap()
	{
		int i = 1;
		Object node = heap[i];
		int j = i << 1;
		int k = j + 1;
		if (k <= size && lessThan(heap[k], heap[j]))
			j = k;
		while (j <= size && lessThan(heap[j], node)) 
		{
			heap[i] = heap[j];
			i = j;
			j = i << 1;
			k = j + 1;
			if (k <= size && lessThan(heap[k], heap[j]))
				j = k;
		}
		heap[i] = node;
	}
}
