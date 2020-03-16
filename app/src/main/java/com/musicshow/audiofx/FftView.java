package com.musicshow.audiofx;

public class FftView extends android.view.View implements java.lang.Runnable
{
	private android.content.Context Context;
	private java.lang.Thread MainDrawThread = null;
	private long UpDrawTime = System.currentTimeMillis();
	private long UpDrawFrameRate = 1000 / 60;
	private int BufferWidth = 0;
	private int BufferHeight = 0;
	private boolean Flag = false;
	private Fft Fft;

	public FftView(android.content.Context $Context, Fft $Fft)
	{
		super($Context);
		Context = $Context;
		android.content.res.Resources Resources = Context.getResources();
		android.util.DisplayMetrics DisplayMetrics = Resources.getDisplayMetrics();
		int ScreenWidth = DisplayMetrics.widthPixels;
		int ScreenHeight = DisplayMetrics.heightPixels;
		BufferWidth = ScreenWidth - (int) (ScreenWidth * 0.35f);
		BufferHeight = BufferWidth;
		Fft = $Fft;
		setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
		if(MainDrawThread == null)
		{
			MainDrawThread = new java.lang.Thread(this);
			Flag = true;
			MainDrawThread.start();
		}
	}

	@Override
	protected void onDraw(android.graphics.Canvas Canvas)
	{
		if(Fft != null && !Fft.IsInformation())
			Canvas.clipRect(new android.graphics.Rect((getWidth() >> 1) - (BufferWidth >> 1), (getHeight() >> 1) - (BufferHeight >> 1), ((getWidth() >> 1) - (BufferWidth >> 1)) + BufferWidth, ((getHeight() >> 1) - (BufferHeight >> 1)) + BufferHeight));
		super.onDraw(Canvas);
		if(Fft != null)
			Fft.Draw(Canvas, getWidth(), getHeight());
	}

	public void UpDraw()
	{
		if(Fft != null)
		{
			if(Fft.IsInformation())
				postInvalidate();
			else
				postInvalidate((getWidth() >> 1) - (BufferWidth >> 1), (getHeight() >> 1) - (BufferHeight >> 1), ((getWidth() >> 1) - (BufferWidth >> 1)) + BufferWidth, ((getHeight() >> 1) - (BufferHeight >> 1)) + BufferHeight);
		}
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
			{}
			WorkIsNotFinish = false;
		}
		Fft = null;
	}

	@Override
	public void run()
	{
		while (Flag)
		{
			if(System.currentTimeMillis() > UpDrawTime)
			{
				UpDrawTime = System.currentTimeMillis() + UpDrawFrameRate;
				postInvalidate();
				/*if(Fft != null && Fft.IsInformation())
					postInvalidate();
				else
					postInvalidate((getWidth() / 2) - (BufferWidth / 2), (getHeight() / 2) - (BufferHeight / 2), ((getWidth() / 2) - (BufferWidth / 2)) + BufferWidth, ((getHeight() / 2) - (BufferHeight / 2)) + BufferHeight);
				*/
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
}
