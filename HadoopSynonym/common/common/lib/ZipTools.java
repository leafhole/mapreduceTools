package common.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.zip.GZIPInputStream;
/*Object ���뱻���л�*/
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
			//�����ֽ���������� 
			ByteArrayOutputStream o = new ByteArrayOutputStream(); 
			//����gzipѹ������� 
			//GZIPOutputStream gzout=new GZIPOutputStream(o); 
			//�����������л������
			//ObjectOutputStream out = new ObjectOutputStream(gzout); 
			ObjectOutputStream out = new ObjectOutputStream(o);
			out.writeObject(object_); 
			out.flush(); 
			out.close(); 
			//gzout.close(); 
			//����ѹ���ֽ��� 
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
				//�����ֽ����������� 
				ByteArrayInputStream i = new ByteArrayInputStream(data_); 
				//����gzip��ѹ������ 
				GZIPInputStream gzin=new GZIPInputStream(i); 
				//�����������л������� 
				ObjectInputStream in = new ObjectInputStream(gzin); 
				//���ƶ����ͻ�ԭ���� 
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
