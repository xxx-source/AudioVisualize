package com.musicshow.audiofx;

public class DataInputOut
{
	public static int getIntegerData(android.content.Context Context, String Data, String Key)
	{
		return Context.getSharedPreferences(Data, 0).getInt(Key, -1);
	}

	public static String getStringData(android.content.Context Context, String Data, String Key)
	{
		return Context.getSharedPreferences(Data, 0).getString(Key, "-1");
	}

	public static void putIntegerData(android.content.Context Context, String Data, String Key, int Value)
	{
		Context.getSharedPreferences(Data,0).edit().putInt(Key,Value).commit();
	}

	public static void putStringData(android.content.Context Context, String Data, String Key, String Value)
	{
		Context.getSharedPreferences(Data,0).edit().putString(Key,Value).commit();
	}
}
