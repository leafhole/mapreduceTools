package common.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VarArray {
	
	@Nullable
    public Object[] elements;
	private int size;
	//Object aa
	public VarArray(@NotNull Object[] elements)
	{
		this.elements = elements;
		size = elements.length;
	}
	public int size()
	{
		return size;
	}
	@Nullable
    public Object get(int pos)
	{
		if(pos<size)return elements[pos];
		return null;
	}
	public VarArray(int capacity)
	{
		elements = new Object[capacity];
		size=0;
	}
	private void realloc()
	{
		Object[] re = new Object[elements.length+100];
		System.arraycopy(elements,0,re,0,elements.length);
		elements = null;
		elements = re;
		//System.out.println("realloc:"+re.length);
	}
	public void add(Object element)
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
		VarArray ia = new VarArray(1000);
		for(int i =0;i<100;i++)
			ia.add(new Integer(i));
		Object[] var = ia.elements;
		for(int i=0;i<ia.size;i++)
		{
			System.out.println(i+":"+var[i]);
		}
	}


}
