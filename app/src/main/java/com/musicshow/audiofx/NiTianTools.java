package com.musicshow.audiofx;

public class NiTianTools
{
	public static void Toast(android.content.Context Context,String Message,int Duration)
	{
		android.widget.Toast.makeText(Context,Message,250).show();
	}

	public static String ReadText(String Path)
	{
		String ReadText = null;
		try
		{
			java.io.FileInputStream FileInputStream = new java.io.FileInputStream(Path);
			byte[] Byte = new byte[FileInputStream.available()];
			FileInputStream.read(Byte);
			ReadText = new String(Byte);
			FileInputStream.close();
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
		return ReadText;
	}

	public static boolean WriteText(String Text,String Path)
	{
		java.io.File File = new java.io.File(Path);
		if(!File.getParentFile().exists())
			File.getParentFile().mkdir();
		try
		{
			java.io.FileOutputStream FileOutputStream = new java.io.FileOutputStream(File);
			FileOutputStream.write(Text.getBytes());
			FileOutputStream.close();
		}
		catch(Exception e)
		{
			return e.equals(e);
		}
		return true;
	}

	/**
	* 将utf-8的汉字转换成unicode格式汉字码
	* @param string
	* @return
	*/
	public static String StringToUnicode(String String)
	{
		if(String.contains("\\u"))
			return String;
		StringBuffer Unicode = new StringBuffer();
		for (int i = 0; i < String.length(); i++)
		{
			char c = String.charAt(i);
			Unicode.append("\\u" + Integer.toHexString(c));
		}
		String str = Unicode.toString();

		return str;//.replaceAll("\\\\", "0x");
	}

	/**
	* 将unicode的汉字码转换成utf-8格式的汉字
	* @param unicode
	* @return
	*/
	public static String UnicodeToString(String Unicode)
	{
		if(!Unicode.contains("\\u"))
			return Unicode;
		String String = Unicode;//.replace("0x", "\\");

		StringBuffer StringBuffer = new StringBuffer();
		String[] hex = String.split("\\\\u");
		for (int i = 1; i < hex.length; i++)
		{
			int data = Integer.parseInt(hex[i], 16);
			StringBuffer.append((char) data);
		}
		return StringBuffer.toString();
	}

	public static String UnicodeToStringT(String Unicode)throws Exception
	{
		String[] asciis = Unicode.split ("\\\\u");
		String nativeValue = asciis[0];
		//try
		//{
			for (int i = 1; i < asciis.length; i++)
			{
				String code = asciis[i];
				nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
				if (code.length() > 4)
				{
					nativeValue += code.substring(4, code.length());
				}
			}
		//}
		//catch (NumberFormatException e)
		//{
			//return Unicode;
		//}
		return nativeValue;
	}

	public static boolean ListFiles(String Path,boolean ChangeFile,boolean ChangeFolder)
	{
		if(!new java.io.File(Path).isDirectory())
			return false;
		java.io.File[] Files = new java.io.File(Path).listFiles();
		if(Files==null)
			return false;
		for (java.io.File File : Files)
		{
			if(File.isFile())
			{
				String FileName;
				String FilePath;
				String Text;
				try
				{
					//尝试第二种方法
					FileName = ChangeFile ? UnicodeToStringT(File.getName()) : StringToUnicode(File.getName());
					FilePath = File.getPath().substring(0, File.getPath().lastIndexOf("/") + 1) + FileName;
					File.renameTo(new java.io.File(FilePath));
					Text = ReadText(FilePath);
					Text = ChangeFile ? UnicodeToStringT(Text) : StringToUnicode(Text);
					if(!WriteText(Text,FilePath))
					{
						new Exception("文件写入失败：" + FilePath);
					}
				}
				catch(Exception e)
				{
					//尝试第一种方法
					FileName = ChangeFile ? UnicodeToString(File.getName()) : StringToUnicode(File.getName());
					FilePath = File.getPath().substring(0, File.getPath().lastIndexOf("/") + 1) + FileName;
					File.renameTo(new java.io.File(FilePath));
					Text = ReadText(FilePath);
					Text = ChangeFile ? UnicodeToString(Text) : StringToUnicode(Text);
					if(!WriteText(Text,FilePath))
					{
						new Exception("文件写入失败：" + FilePath);
					}
				}
			}
			else if(File.isDirectory())
			{
				String FileName = null;
				try
				{
					FileName = ChangeFolder ? UnicodeToStringT(File.getName()) : StringToUnicode(File.getName());
				}
				catch(Exception e)
				{
					new Exception("无法处理文件，访问数组越界");
				}
				String FilePath = File.getPath().substring(0, File.getPath().lastIndexOf("/") + 1) + FileName;
				if(!File.renameTo(new java.io.File(FilePath)))
					new Exception("文件写入失败：" + FilePath);
				ListFiles(FilePath,ChangeFile,ChangeFolder);
			}
		}
		return true;
	}

	public static String getStrFromUniCode(String unicode)
	{
		String str = "";
		for(int i = 0;i<unicode.length();i += 4)
		{
			String s = "";
			for(int j = i; j<i+4 ;j++)
			{
				s += String.valueOf(unicode.charAt(j));
			}
			str += String.valueOf((char)Integer.valueOf(s, 16).intValue());
		}
		return str;
	}

	//通过文件名判断是什么类型的文件
	public static boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings)
	{
		for(String aEnd : fileEndings)
		{
			if(checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	public static String[] getListFiles(String Path)
	{
		FilePath = Path;
		java.io.File File = new java.io.File(Path);
		java.io.File[] Files = null;
		if(new java.io.File(Path).isFile())
			FilePath = File.getPath().substring(0,File.getPath().lastIndexOf("/") + 1);
		Files = new java.io.File(FilePath).listFiles();
		java.util.ArrayList<String> ArrayList = new java.util.ArrayList<String>();
		if(File==null)
			return null;
		for (java.io.File FileItem : Files)
		{
			if(FileItem.isDirectory())
				ArrayList.add(FileItem.getName());
		}
		for (java.io.File FileItem : Files)
		{
			if(FileItem.isFile())
				ArrayList.add(FileItem.getName());
		}
		String Item = "/...";
		String[] Items;
		for (int Index = 0; Index<ArrayList.size(); Index++)
		{
			Item += "\r\n" + ArrayList.get(Index);
		}
		Items = Item.split("\r\n");
		return Items;
	}

	private static String FilePath;
	public static String AlertDialogListFiles(final android.app.Activity Activity,String Path,final android.widget.TextView View)
	{
		FilePath = Path;
		java.io.File File = new java.io.File(Path);
		if(File==null)
			return FilePath;
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle("请选择一个文件路径");
		final String[] Items = getListFiles(FilePath);
		AlertDialog.setItems(Items,new android.content.DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(android.content.DialogInterface DialogInterface,int i)
			{
				if(Items[i].equals("/..."))
				{
					java.io.File File = new java.io.File(FilePath);
					AlertDialogListFiles(Activity,File.getPath().substring(0,File.getPath().lastIndexOf("/") + 1),View);
				}
				else
				{
					java.io.File File = new java.io.File(FilePath + "/" + Items[i]);
					if(File.isFile())
					{
						FilePath = File.getAbsolutePath();
						View.setText(FilePath);
					}
					else if(File.isDirectory())
					{
						File = new java.io.File(FilePath + "/" + Items[i]);
						AlertDialogListFiles(Activity,File.getPath(),View);
					}
				}
			}
		});
		AlertDialog.setPositiveButton("选择路径",new android.content.DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(android.content.DialogInterface DialogInterface,int i)
			{
				Toast(Activity.getApplicationContext(),"已加载该路径",0);
				View.setText(FilePath);
			}
		});
		AlertDialog.setNegativeButton("取消",null);
		AlertDialog.setCancelable(true);
		AlertDialog.create();
		AlertDialog.show();
		return FilePath;
	}
}
