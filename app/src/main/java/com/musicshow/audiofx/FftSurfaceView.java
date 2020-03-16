package com.musicshow.audiofx;


public class FftSurfaceView extends android.view.SurfaceView implements android.view.SurfaceHolder.Callback, java.lang.Runnable
{
	private String FftSurfaceView;
	private boolean Flag;
	private android.content.Context Context;
	private android.graphics.Canvas Canvas;
	private android.graphics.Paint Paint;
	private java.lang.Thread MainDrawThread = null;
	private long UpDrawTime = -1;
	private long UpDrawFrameRate = 1000 / 60;
	private int ScreenWidth;
	private int ScreenHeight;
	private android.view.SurfaceHolder SurfaceHolder;
	private int BufferWidth, BufferHeight;
	private boolean IsHardWareCanvas = false;
	private java.lang.reflect.Method LockHardwareCanvas = null;

	private Fft Fft;

	private float getScreenDensityForNewSize(float ParamSize)
	{
		return ((float) ScreenWidth) * (ParamSize / 1080);
	}

	public FftSurfaceView(android.content.Context $Context, Fft $Fft)
	{
		super($Context);
		Context = $Context;
		Fft = $Fft;
		setZOrderOnTop(true); 
		setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
		getHolder().setFormat(android.graphics.PixelFormat.TRANSLUCENT);
		getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_HARDWARE | android.view.SurfaceHolder.SURFACE_TYPE_GPU | android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		SurfaceHolder = getHolder();
		SurfaceHolder.addCallback(this);
		setFocusable(true);
		setKeepScreenOn(true);
		if(android.os.Build.VERSION.SDK_INT >= 23 && LockHardwareCanvas == null)
		{
			try
			{
				LockHardwareCanvas = SurfaceHolder.getSurface().getClass().getDeclaredMethod("lockHardwareCanvas");
				LockHardwareCanvas.setAccessible(true);
			}
			catch(Exception E){}
			IsHardWareCanvas = true;
		}
	}

	private void Toast(boolean ErrorMode, Object Text)
	{
		String Message = (String) Text;
		if(ErrorMode)
			Message = "发生了一个错误：" + (String) Text;
		if(Context != null)
			android.widget.Toast.makeText(Context, Message, 1).show();
	}

	private void init()
	{
		/*if(getWidth() > getHeight())
		{
			ScreenWidth = getHeight();
			ScreenHeight = getWidth();
		}
		else
		{
			ScreenWidth = getWidth();
			ScreenHeight = getHeight();
		}*/
		BufferWidth = ScreenWidth - (int) (ScreenWidth * 0.35f);
		BufferHeight = BufferWidth;
		Paint = new android.graphics.Paint();
		Paint.setAntiAlias(true);
		Paint.setARGB(255, 250, 220, 190);
		Paint.setTextSize(getScreenDensityForNewSize(38f));
		FftSurfaceView = "FftSurfaceView";
	}

	
	private void Draw()
	{
		try
		{
			synchronized(SurfaceHolder.getSurface())
			{
				if(IsHardWareCanvas && LockHardwareCanvas != null)
				{
					try
					{
						Canvas = (android.graphics.Canvas) LockHardwareCanvas.invoke(SurfaceHolder.getSurface());
						if(Canvas == null)
							IsHardWareCanvas = false;
					}
					catch(Exception E){}
				}
				else
					if(Fft != null && Fft.IsInformation())
						Canvas = SurfaceHolder.getSurface().lockCanvas(null);
					else
						Canvas = SurfaceHolder.getSurface().lockCanvas(new android.graphics.Rect((getWidth() >> 1) - (BufferWidth >> 1), (getHeight() >> 1) - (BufferHeight >> 1), ((getWidth() >> 1) - (BufferWidth >> 1)) + BufferWidth, ((getHeight() >> 1) - (BufferHeight >> 1)) + BufferHeight));
				if(Canvas == null)
					return;
				//Canvas.setDrawFilter(PaintFlagsDrawFilter);
				Canvas.drawColor(android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
				if(Fft != null)
					Fft.Draw(Canvas, getWidth(), getHeight());
			}
		}
		catch(Exception E)
		{
			//Toast(true, E);
		}
		finally
		{
			if(Canvas != null && SurfaceHolder != null)
				SurfaceHolder.getSurface().unlockCanvasAndPost(Canvas);
			Canvas = null;
		}
	}

	public void Stop()
	{
		Flag = false;
		boolean WorkIsNotFinish = true;
		while(WorkIsNotFinish)
		{
			try
			{
				MainDrawThread.join();
			}
			catch (Exception E)
			{
				Toast(true, E);
			}
			finally
			{
				SurfaceHolder.getSurface().release();
				Context = null;
				Canvas = null;
				WorkIsNotFinish = false;
			}
		}
		Fft = null;
	}

	@Override
	public boolean onKeyDown(int KeyCode, android.view.KeyEvent KeyEvent)
	{
		Fft.onKeyDown(KeyCode, KeyEvent);
		return super.onKeyDown(KeyCode, KeyEvent);
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent MotionEvent)
	{
		return Fft.onTouchEvent(MotionEvent);
	}

	public void SetFPS(Fft $Fft, long $FrameRate)
	{
		if(Fft == $Fft)
		{
			long FrameRate = $FrameRate;
			if(FrameRate > 120)
				FrameRate = 120;
			else if(FrameRate < 60)
				FrameRate = 60;
			UpDrawFrameRate = 1000 / FrameRate;
		}
	}

	@Override
	public void run()
	{
		while (Flag)
		{
			if(System.currentTimeMillis() >= UpDrawTime)
			{
				UpDrawTime = System.currentTimeMillis() + UpDrawFrameRate;
				Draw();
			}
			try
			{
				Thread.sleep(1);
			}
			catch(Exception E)
			{}
		}
		return;
	}

	@Override
	protected void onDetachedFromWindow()
	{
		Stop();
		super.onDetachedFromWindow();
	}

	@Override
	public void surfaceChanged(android.view.SurfaceHolder SurfaceHolder, int Format, int Width, int Height)
	{
	}

	@Override
	public void surfaceCreated(android.view.SurfaceHolder SurfaceHolder)
	{
		ScreenWidth = getWidth();
		ScreenHeight = getHeight();
		android.content.res.Resources Resources = Context.getResources();
		android.util.DisplayMetrics DisplayMetrics = Resources.getDisplayMetrics();
		ScreenWidth = DisplayMetrics.widthPixels;
		ScreenHeight = DisplayMetrics.heightPixels;
		if(FftSurfaceView == null)
		{
			init();
			MainDrawThread = new java.lang.Thread(this);
			Flag = true;
			MainDrawThread.start();
		}
		else
			Flag = true;
	}

	public android.view.SurfaceHolder getSurfaceHolder()
	{
		return SurfaceHolder != null ? SurfaceHolder : null;
	}

	@Override
	public void surfaceDestroyed(android.view.SurfaceHolder SurfaceHolder)
	{
		if(MainDrawThread != null)
		{
			Stop();
			//Fft.Stop();
		}
		Flag = false;
	}
}
