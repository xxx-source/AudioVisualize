package com.musicshow.audiofx;

public class LocalDataUtil
{
	public static void ClearData(android.content.Context Context, String DataName)
	{
		Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).edit().clear().commit();
	}

	public static int getIntegerData(android.content.Context Context, String DataName, String Key)
	{
		return Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).getInt(Key, -1);
	}

	public static String getStringData(android.content.Context Context, String DataName, String Key)
	{
		return Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).getString(Key, "-1");
	}

	public static boolean getBooleanData(android.content.Context Context, String DataName, String Key)
	{
		return Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).getBoolean(Key, false);
	}

	public static void putIntegerData(final android.content.Context Context, final String DataName, final String Key, final int Value)
	{
		new java.lang.Thread(new java.lang.Runnable()
		{
			@Override
			public void run()
			{
				Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).edit().putInt(Key, Value).commit();
			}
		}).start();
	}

	public static void putStringData(final android.content.Context Context, final String DataName, final String Key, final String Value)
	{
		new java.lang.Thread(new java.lang.Runnable()
		{
			@Override
			public void run()
			{
				Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).edit().putString(Key, Value).commit();
			}
		}).start();
	}

	public static void putBooleanData(final android.content.Context Context, final String DataName, final String Key, final boolean Value)
	{
		new java.lang.Thread(new java.lang.Runnable()
		{
			@Override
			public void run()
			{
				Context.getSharedPreferences(DataName, Context.MODE_PRIVATE).edit().putBoolean(Key, Value).commit();
			}
		}).start();
	}

	public static void putData(final android.content.Context Context, final String DataName, final String Key, final Object Value)
	{
		new java.lang.Thread(new java.lang.Runnable()
		{
			@Override
			public void run()
			{
				android.content.SharedPreferences SharedPreferences = Context.getSharedPreferences(DataName, Context.MODE_PRIVATE);
				android.content.SharedPreferences.Editor Editor = SharedPreferences.edit();
				if(Value instanceof java.lang.String)
					Editor.putString(Key, (java.lang.String) Value).commit();
				else if(Value instanceof java.lang.Boolean)
					Editor.putBoolean(Key, (java.lang.Boolean) Value).commit();
				else if(Value instanceof java.lang.Integer)
					Editor.putInt(Key, (java.lang.Integer) Value).commit();
				else if(Value instanceof java.lang.Long)
					Editor.putLong(Key, (java.lang.Long) Value).commit();
				else if(Value instanceof java.lang.Float)
					Editor.putFloat(Key, (java.lang.Float) Value).commit();
				else
					Editor.putString(Key, Value.toString()).commit();
			}
		}).start();
	}
}
