package com.musicshow.audiofx;

public class HardwareCanvas
{
	private android.content.Context Context = null;
	private Object HardwareCanvas = null;
	private Object RenderNode = null;
	private android.view.View View = null;
	private java.lang.reflect.Method SetBitmap = null; 
	private java.lang.reflect.Method DrawBitmap = null;
	private java.lang.reflect.Method DrawCircle = null;
	private java.lang.reflect.Method DrawPath = null;
	private java.lang.reflect.Method DrawRect = null;
	private java.lang.reflect.Method DrawARGB = null;
	private java.lang.reflect.Method DrawLine = null;
	private java.lang.reflect.Method DrawLines = null;
	private java.lang.reflect.Method DrawPoint = null;

	public HardwareCanvas(android.content.Context $Content, int Width, int Height)
	{
		if($Content == null)
			return;
		Context = $Content;
		init(Width, Height);
	}

	private void init(int Width, int Height)
	{
		try
		{
			View = new android.view.View(Context);
			Class<?> HardwareCanvasClass = Class.forName("android.view.GLES20RecordingCanvas");
			Class<?> RenderNodeClass = Class.forName("android.view.RenderNode");
			java.lang.reflect.Method Create = RenderNodeClass.getDeclaredMethod("create", String.class, android.view.View.class);
			Create.setAccessible(true);
			RenderNode = Create.invoke(getClass().getName(), View);
			java.lang.reflect.Method Start = RenderNode.getClass().getDeclaredMethod("start", Integer.class, Integer.class);
			Start.setAccessible(true);
			HardwareCanvas = Start.invoke(RenderNode, Width, Height);
			SetBitmap = GetMethod(HardwareCanvasClass, "setBitmap", android.graphics.Bitmap.class);
			DrawBitmap = GetMethod(HardwareCanvasClass, "drawBitmap", android.graphics.Bitmap.class, Float.class, Float.class, android.graphics.Paint.class);
			DrawCircle = GetMethod(HardwareCanvasClass, "drawCircle", Float.class, Float.class, Float.class, android.graphics.Paint.class);
			DrawPath = GetMethod(HardwareCanvasClass, "drawPath", android.graphics.Path.class, android.graphics.Paint.class);
			DrawRect = GetMethod(HardwareCanvasClass, "drawRect", Float.class, Float.class, Float.class, Float.class, android.graphics.Paint.class);
			DrawARGB = GetMethod(HardwareCanvasClass, "drawARGB", Integer.class, Integer.class, Integer.class, Integer.class);
			DrawLine = GetMethod(HardwareCanvasClass, "drawLine", Float.class, Float.class, Float.class, Float.class, android.graphics.Paint.class);
			DrawLines = GetMethod(HardwareCanvasClass, "drawLines", Float[].class, android.graphics.Paint.class);
			DrawPoint = GetMethod(HardwareCanvasClass, "drawPoint", Float.class, Float.class, android.graphics.Paint.class);
		} catch(Exception E) {}
	}

	private java.lang.reflect.Method GetMethod(Class<?> Class, String Name)
	{
		try
		{
			java.lang.reflect.Method Method = Class.getDeclaredMethod(Name);
			Method.setAccessible(true);
			return Method;
		} catch(Exception E) {}
		return null;
	}

	private java.lang.reflect.Method GetMethod(Class<?> Class, String Name, Class<?>... Classes)
	{
		try
		{
			java.lang.reflect.Method Method = Class.getDeclaredMethod(Name, Classes);
			Method.setAccessible(true);
			return Method;
		} catch(Exception E) {}
		return null;
	}

	public void setBitmap(android.graphics.Bitmap Bitmap)
	{
		if(SetBitmap != null)
		{
			try
			{
				SetBitmap.invoke(HardwareCanvas, Bitmap);
			} catch(Exception E) {}
		}
	}

	public void drawBitmap(android.graphics.Bitmap Bitmap, float X, float Y, android.graphics.Paint Paint)
	{
		if(DrawBitmap != null)
		{
			try
			{
				DrawBitmap.invoke(HardwareCanvas, Bitmap, X, Y, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawCircle(float X, float Y, float Radius, android.graphics.Paint Paint)
	{
		if(DrawCircle != null)
		{
			try
			{
				DrawCircle.invoke(HardwareCanvas, X, Y, Radius, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawPath(android.graphics.Path Path, android.graphics.Paint Paint)
	{
		if(DrawPath != null)
		{
			try
			{
				DrawPath.invoke(HardwareCanvas, Path, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawRect(float Left, float Top, float Right, float Bottom, android.graphics.Paint Paint)
	{
		if(DrawRect != null)
		{
			try
			{
				DrawRect.invoke(HardwareCanvas, Left, Top, Right, Bottom, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawARGB(int A, int R, int G, int B)
	{
		if(DrawARGB != null)
		{
			try
			{
				DrawARGB.invoke(HardwareCanvas, A, R, G, B);
			} catch(Exception E) {}
		}
	}

	public void drawLine(float StartX, float StartY, float StopX, float StopY, android.graphics.Paint Paint)
	{
		if(DrawLine != null)
		{
			try
			{
				DrawLine.invoke(HardwareCanvas, StartX, StartY, StopX, StopY, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawLines(float[] Lines, android.graphics.Paint Paint)
	{
		if(DrawLines != null)
		{
			try
			{
				DrawLines.invoke(HardwareCanvas, Lines, Paint);
			} catch(Exception E) {}
		}
	}

	public void drawPoint(float X, float Y, android.graphics.Paint Paint)
	{
		if(DrawPoint != null)
		{
			try
			{
				DrawPoint.invoke(HardwareCanvas, X, Y, Paint);
			} catch(Exception E) {}
		}
	}
}
