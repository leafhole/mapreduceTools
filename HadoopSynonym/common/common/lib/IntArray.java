package common.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntArray {
	@Nullable
    public int[] elements;
	private int size;
	//Object aa
	public IntArray(@NotNull int[] elements)
	{
		this.elements = elements;
		size = elements.length;
	}
	public int size()
	{
		return size;
	}
	public int get(int pos)
	{
		if(pos<size)return elements[pos];
		return -1;
	}
	public IntArray()
	{
		elements = new int[10];
		size=0;
	}
	private void realloc()
	{
		int[] re = new int[elements.length+20];
		System.arraycopy(elements,0,re,0,elements.length);
		elements = null;
		elements = re;
		//System.out.println("realloc:"+re.length);
	}
	public void add(int element)
	{
		if(size<elements.length)
			elements[size++] = element;
		else
		{
			realloc();
			add(element);
		}
	}
	
	public static void main(String[] args)
	{		
		IntArray ia = new IntArray();
		for(int i =0;i<100;i++)
			ia.add(i);
		int[] var = ia.elements;
		for(int i=0;i<ia.size;i++)
		{
			System.out.println(i+":"+var[i]);
		}
	}
}
