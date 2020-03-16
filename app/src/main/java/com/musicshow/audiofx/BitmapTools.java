package com.musicshow.audiofx;

public class BitmapTools
{
	public static android.graphics.Bitmap LocalBitmap(android.content.Context Context, String Path)
	{
		try
		{
			java.io.InputStream InputStream = Context.getAssets().open(Path);
			InputStream.close();
			return android.graphics.BitmapFactory.decodeStream(InputStream);
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
}
