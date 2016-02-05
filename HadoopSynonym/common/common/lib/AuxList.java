package common.lib;

import java.util.ArrayList;

//循环得到活动的下标编号
public class  AuxList{
	private boolean[] alive;
	private int knum;
	private ArrayList ar; //活动的下标列表
	private static long cnt;
	static{
		cnt = 0;
	}
	public boolean isActive(int i)
	{
		return alive[i];
	}
	public AuxList(int knum)
	{
		this.knum = knum;
		 ar = new ArrayList();
		 alive = new boolean[knum];
		 for(int i = 0;i<knum;i++)
			 active(i);
	}
	public static long getID()
	{
		return cnt++;
	}
	public void activeAll()
	{
		for(int i=0;i<knum;i++)active(i);
	}
	public void eraseAll()
	{
		for(int i=0;i<knum;i++)erase(i);
	}
	//把第i个至为活动状态
	public void active(int i)
	{		
		try{
			synchronized(alive)
			{
				alive[i] = true;
			};
			synchronized(ar)
			{
				if(!ar.contains(i))
				{
					ar.add(i);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//把第i个至为不活动状态
	public void erase(int i)
	{		
		try{
				synchronized(alive)
				{
					alive[i] = false;
				};
				synchronized(ar)
				{
					ar.remove(new Integer(i));
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	/*
	 * 获得活动的下标
	 * */
	public int getAlive(long rand)
	{
		try{
			if(ar.size() == 0) {
				return 0;
			}
			int idxidx = (int)rand%ar.size();
			int idx = ((Integer)ar.get(idxidx)).intValue();			
			if(alive[idx])//直接命中
			{			
				return idx;
			};
		}catch(Exception e)
		{
			Log.logger.warn(e.toString());
		}
		return 0;
	}
	public int getAlive()
	{
		return getAlive(getID());
	}
}