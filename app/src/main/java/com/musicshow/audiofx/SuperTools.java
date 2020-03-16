package com.musicshow.audiofx;

public class SuperTools
{
	public static android.graphics.Bitmap LocalBitmap(android.content.Context Context, String Path)
	{
		android.graphics.Bitmap Bitmap;
		try
		{
			java.io.InputStream InputStream = Context.getAssets().open(Path);
			Bitmap = android.graphics.BitmapFactory.decodeStream(InputStream);
			InputStream.close();
			return Bitmap;
		}
		catch (Exception e)
		{
			android.widget.Toast.makeText(Context, e.getMessage(), 0).show();
			return null;
		}
	}

	public static android.graphics.Bitmap LocalblurBitmap(android.content.Context Context, String BitmapPath, float r16f)
	{
		android.graphics.Bitmap Bitmap;
		try
		{
			java.io.InputStream InputStream = Context.getAssets().open(BitmapPath);
			Bitmap = android.graphics.BitmapFactory.decodeStream(InputStream);
			InputStream.close();
			android.graphics.Bitmap r4_Bitmap = Bitmap.createScaledBitmap(Bitmap, Math.round(Bitmap.getWidth()), Math.round(Bitmap.getHeight()), false);
			android.renderscript.RenderScript RenderScript = android.renderscript.RenderScript.create(Context);
			android.renderscript.ScriptIntrinsicBlur ScriptIntrinsicBlur = android.renderscript.ScriptIntrinsicBlur.create(RenderScript, android.renderscript.Element.U8_4(RenderScript));
			android.renderscript.Allocation Allocation = android.renderscript.Allocation.createFromBitmap(RenderScript, r4_Bitmap);
			ScriptIntrinsicBlur.setRadius(r16f);
			ScriptIntrinsicBlur.setInput(Allocation.createFromBitmap(RenderScript, Bitmap));
			ScriptIntrinsicBlur.forEach(Allocation);
			Allocation.copyTo(r4_Bitmap);
			return r4_Bitmap;
		}
		catch (Exception e)
		{
			android.widget.Toast.makeText(Context, e.getMessage(), 0).show();
			return null;
		}
	}

	public static android.graphics.Bitmap LocalblurBitmap(android.content.Context Context, android.graphics.Bitmap Bitmap, float r16f)
	{
		android.graphics.Bitmap r4_Bitmap = Bitmap.createScaledBitmap(Bitmap, Math.round(Bitmap.getWidth()), Math.round(Bitmap.getHeight()), false);
		android.renderscript.RenderScript RenderScript = android.renderscript.RenderScript.create(Context);
		android.renderscript.ScriptIntrinsicBlur ScriptIntrinsicBlur = android.renderscript.ScriptIntrinsicBlur.create(RenderScript, android.renderscript.Element.U8_4(RenderScript));
		android.renderscript.Allocation Allocation = android.renderscript.Allocation.createFromBitmap(RenderScript, r4_Bitmap);
		ScriptIntrinsicBlur.setRadius(r16f);
		ScriptIntrinsicBlur.setInput(Allocation.createFromBitmap(RenderScript, Bitmap));
		ScriptIntrinsicBlur.forEach(Allocation);
		Allocation.copyTo(r4_Bitmap);
		return r4_Bitmap;
	}

	public static android.graphics.Bitmap ResetSizeBitmap(android.graphics.Bitmap Bitmap,float Width,float Height)
	{
		android.graphics.Matrix Matrix = new android.graphics.Matrix();
		float BitmapWidth = Width;
		float BitmapHeight = Height;
		if(BitmapHeight > Bitmap.getHeight())
		{
			BitmapWidth = Bitmap.getWidth();
			BitmapHeight = Height;
		}
		if(Bitmap.getWidth() < Bitmap.getHeight() && Width > Height)
		{
			BitmapWidth = Bitmap.getWidth();
			BitmapHeight = Bitmap.getHeight();
		}
		float WidthScale = ((float)BitmapWidth) / Bitmap.getWidth();
		float HeightScale = ((float)BitmapHeight) / Bitmap.getHeight() + 0.08f;
		Matrix.postScale(WidthScale,HeightScale);
		return android.graphics.Bitmap.createBitmap(Bitmap,0,0,Bitmap.getWidth(),Bitmap.getHeight(),Matrix,true);
	}

	public static int Save()
	{
		return 0;
	}

	public static void TalkText(android.content.Context Context, String Message, int Duration)
	{
		android.widget.Toast.makeText(Context, Message, Duration).show();
	}

	public static int getColorToInteger(String Color)
	{
		if (Color.startsWith("#"))
		{
			return android.graphics.Color.parseColor(Color);
		}
		else
		{
			return Integer.parseInt(Color);
		}
	}

	public static float getDisplayMetrics(android.content.Context Context, float axy)
	{
		return 0.5f + (axy * Context.getResources().getDisplayMetrics().density);
	}

	public static void setActivityBackGround(android.app.Activity Activity)
	{
		Activity.getWindow().setBackgroundDrawable(android.app.WallpaperManager.getInstance(Activity).getDrawable());
	}

	public static void setGradient(android.view.View View, int n, int n2, String ColorO, String ColorT)
	{
		int IntegerColorO = SuperTools.getColorToInteger(ColorO);
		int IntegerColorT = SuperTools.getColorToInteger(ColorT);
		android.graphics.drawable.GradientDrawable GradientDrawable = new android.graphics.drawable.GradientDrawable();
		GradientDrawable.setColor(IntegerColorO);
		if (n2 > 0)
		{
			GradientDrawable.setCornerRadius(n2);
		}
		if (n > 0)
		{
			GradientDrawable.setStroke(n, IntegerColorT);
		}
		View.setBackground(GradientDrawable);
	}

	public static void setSystemUi(android.app.Activity Activity)
	{
		Activity.requestWindowFeature(1);
		Activity.getWindow().addFlags(128);
		if (android.os.Build.VERSION.SDK_INT >= 21)
		{
			android.view.Window Window = Activity.getWindow();
			Window.clearFlags(201326592);
			Window.getDecorView().setSystemUiVisibility(1792);
			Window.addFlags(Integer.MIN_VALUE);
			Window.setStatusBarColor(0);
			Window.setNavigationBarColor(0);
		}
	}

	public static void setWindowFeature(android.app.Activity Activity)
	{
		Activity.getWindow().setFlags(1024, 1024);
		Activity.getWindow().addFlags(128);
	}
}
