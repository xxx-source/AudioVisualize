package com.musicshow.audiofx;

public class DialogTools
{
	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Message,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setMessage(Message);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,null);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,String ButtonT,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,null);
		AlertDialog.setNegativeButton(ButtonT,null);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,String ButtonT,String ButtonTH,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,null);
		AlertDialog.setNegativeButton(ButtonT,null);
		AlertDialog.setNeutralButton(ButtonTH,null);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,android.content.DialogInterface.OnClickListener OnClickListener,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,OnClickListener);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,String ButtonT,android.content.DialogInterface.OnClickListener OnClickListenerO,android.content.DialogInterface.OnClickListener OnClickListenerT,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,OnClickListenerO);
		AlertDialog.setNegativeButton(ButtonT,OnClickListenerT);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String Message,String ButtonO,String ButtonT,String ButtonTH,android.content.DialogInterface.OnClickListener OnClickListenerO,android.content.DialogInterface.OnClickListener OnClickListenerT,android.content.DialogInterface.OnClickListener OnClickListenerTH,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setMessage(Message);
		AlertDialog.setPositiveButton(ButtonO,OnClickListenerO);
		AlertDialog.setNegativeButton(ButtonT,OnClickListenerT);
		AlertDialog.setNeutralButton(ButtonTH,OnClickListenerTH);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String[] Items,android.content.DialogInterface.OnClickListener OnClickListenerItems,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setItems(Items,OnClickListenerItems);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	public static android.app.AlertDialog.Builder Dialog(final android.app.Activity Activity,String Title,String[] Items,String ButtonO,String ButtonT,String ButtonTH,android.content.DialogInterface.OnClickListener OnClickListenerItems,android.content.DialogInterface.OnClickListener OnClickListenerO,android.content.DialogInterface.OnClickListener OnClickListenerT,android.content.DialogInterface.OnClickListener OnClickListenerTH,boolean Cancelable)
	{
		android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(Activity);
		AlertDialog.setTitle(Title);
		AlertDialog.setItems(Items,OnClickListenerItems);
		AlertDialog.setPositiveButton(ButtonO,OnClickListenerO);
		AlertDialog.setNegativeButton(ButtonT,OnClickListenerT);
		AlertDialog.setNeutralButton(ButtonTH,OnClickListenerTH);
		AlertDialog.setCancelable(Cancelable);
		AlertDialog.create();
		AlertDialog.show();
		return AlertDialog;
	}

	/*
	AlertDialog.setItems(Items,new android.content.DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(android.content.DialogInterface DialogInterface,int i)
		{
		}
	});
	*/
}
