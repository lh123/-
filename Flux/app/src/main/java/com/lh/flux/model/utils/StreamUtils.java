package com.lh.flux.model.utils;
import java.io.*;

public class StreamUtils
{
	public static String readFromStream(InputStream in) throws IOException
	{
		InputStreamReader isr=new InputStreamReader(in);
		BufferedReader br=new BufferedReader(isr);
		StringBuilder sb=new StringBuilder();
		String temp=null;
		try
		{
			while ((temp = br.readLine()) != null)
			{
				sb.append(temp);
			}
			temp=sb.toString();
		}
		catch (IOException e)
		{
			temp=null;
			e.printStackTrace();
		}
		finally
		{
			closeStream(br);
			closeStream(isr);
			closeStream(in);
		}
		return temp;
	}
	
	public static void writeToStream(OutputStream ou,byte[] b)
	{
		try
		{
			ou.write(b);
			ou.flush();
		}
		catch (IOException e)
		{}
		finally
		{
			closeStream(ou);
		}
	}
	
	private static void closeStream(Closeable c)
	{
		if(c!=null)
		{
			try
			{
				c.close();
			}
			catch (IOException e)
			{}
		}
	}
}
