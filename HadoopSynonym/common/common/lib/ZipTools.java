package common.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.zip.GZIPInputStream;
/*Object 必须被序列化*/
public class ZipTools {
	public ZipTools(){}
	
	@Nullable
    public static byte[] writeCompressObject(Object object_)
	{
		byte[] data_=null; 
		long btime,etime;
		try
		{ 
			btime = System.currentTimeMillis();
			//建立字节数组输出流 
			ByteArrayOutputStream o = new ByteArrayOutputStream(); 
			//建立gzip压缩输出流 
			//GZIPOutputStream gzout=new GZIPOutputStream(o); 
			//建立对象序列化输出流
			//ObjectOutputStream out = new ObjectOutputStream(gzout); 
			ObjectOutputStream out = new ObjectOutputStream(o);
			out.writeObject(object_); 
			out.flush(); 
			out.close(); 
			//gzout.close(); 
			//返回压缩字节流 
			data_=o.toByteArray(); 			
			o.close(); 
			etime = System.currentTimeMillis();
			Log.logger.info("zipTool->writecompress len:"+data_.length+"take:"+(etime-btime)+"ms");
		}catch(IOException e) 
		{ 
			System.out.println(e); 
		} 
		return(data_);  
	}
	@Nullable
    public static Object readCompressObject(@NotNull byte[] data_)
	{
			Object object_=null; 
			long btime,etime;
			try 
			{ 
				btime = System.currentTimeMillis();
				//建立字节数组输入流 
				ByteArrayInputStream i = new ByteArrayInputStream(data_); 
				//建立gzip解压输入流 
				GZIPInputStream gzin=new GZIPInputStream(i); 
				//建立对象序列化输入流 
				ObjectInputStream in = new ObjectInputStream(gzin); 
				//按制定类型还原对象 
				object_=in.readObject(); 
				i.close(); 
				gzin.close(); 
				in.close(); 
				etime = System.currentTimeMillis();
				Log.logger.info("zipTool->readcompress len:"+data_.length+"take:"+(etime-btime)+"ms");
			}catch(ClassNotFoundException e) 
			{ 
				System.out.println(e); 
			}catch(IOException e) 
			{ 
				System.out.println(e); 
			} 
			return(object_); 
	}  

}
