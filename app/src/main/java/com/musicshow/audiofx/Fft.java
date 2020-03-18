package com.musicshow.audiofx;


public class Fft
{
	private android.app.Activity Activity;
	private android.content.Context Context;
	private String Fft = null;
	private boolean Flag;
	private android.graphics.Bitmap BackBuffer;
	private android.graphics.Bitmap BackBufferT;
	private android.graphics.Bitmap Buffer;
	private android.graphics.Bitmap Logo;
	private android.graphics.Paint Paint;
	private android.graphics.Paint ForePaint;
	private android.graphics.Paint cPan;
	private android.graphics.Paint pC;
	private android.graphics.Paint pan;
	private android.graphics.Paint panBg;
	private android.graphics.Paint PathPaint;
	private android.graphics.Paint DrawTextPaint;
	private android.graphics.Paint CirclePaint;
	private android.graphics.Path Path = null;
	private android.graphics.Canvas Canvas;
	private android.graphics.Canvas RaCanvas;
	private android.graphics.Canvas BackBufferCanvas;
	private android.graphics.PaintFlagsDrawFilter PaintFlagsDrawFilter = new android.graphics.PaintFlagsDrawFilter(0, android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.FILTER_BITMAP_FLAG);
	private android.media.AudioManager AudioManager;
	private android.media.MediaPlayer MediaPlayer;
	private android.media.audiofx.Visualizer Visualizer;
	private android.media.AudioTrack AudioTrack;
	private AudioTrackPlayMusic AudioTrackPlayMusic;
	private boolean AudioFxPlaying;
	private boolean IsPlaying;
	//控制采样率
	private int Frequence = 41100;
	private int PlayChannelConfig = android.media.AudioFormat.CHANNEL_IN_STEREO;
	private int AudioEncoding = android.media.AudioFormat.ENCODING_PCM_16BIT;

	private int ByteAllReadCount = 464;
	private int ByteAllReadCountForWave = 464;
	
	private long deltaTime = -1;
	private long drawTime = -1;
	private float DataKey = 0;
	private float LastDataKey = 0;
	private float DataLastKey = 0;
	private float MainUseLength = 0;
	
	private int[] Wave = null;
	private int[] AudioWave = null;
	private float[] AudioFxBytes = null;
	private float[] LastAudioData = null;
	private float[] Points = null;
	private float[] ReadByte;
	private float[] BufferSize;
	private float RateAdd;
	private int BackGroundColor;
	private String AudioName;
	private String MusicName;
	private float StreamMaxVolume;
	private float StreamVolume;
	private java.lang.Thread UpDrawDataThread = null;
	private long UpDrawDataTime = -1;
	private float CircleR;
	private final float DeltaAngle = (float) (6.283185307179586d / 464);
	
	private long UpDrawDataFrameRate = 1000 / 120;
	private int UpDrawDataRate = 6;
	private int ScreenWidth;
	private int ScreenHeight;
	private int Width;
	private int Height;
	private int BufferWidth, BufferHeight;

	private float LastDrawRng = 0;
	private float LastDrawR = 0;
	private float DrawRng = 0;
	private float DrawRngRate = 0;
	private float DrawR = 0;
	private float DrawRRate = 0;
	private float[] DrawRFBytes = null;
	private float[] DrawRFBytesRate = null;
	private float[] AudioWaveRate = null;

	private FftView FftView;
	private FftSurfaceView FftSurfaceView;
	private HardwareCanvas HardwareCanvas = null;
	private long FPS = 0;
	private int MaxFps = 60;
	private boolean IsFps = false;
	private boolean IsCycleColor = false;
	private boolean IsCycleColorForWave = false;
	private boolean IsInformation = false;
	private int FftDrawWaveMode = 1;

	private float getScreenDensityForNewSize(float ParamSize)
	{
		return ((float) ScreenWidth) * (ParamSize / 1080);
	}

	public void RemoveAllView()
	{
		if(FftView != null)
			FftView.onDetachedFromWindow();
		FftView = null;
		if(FftSurfaceView != null)
			FftSurfaceView.onDetachedFromWindow();
		FftSurfaceView = null;
	}

	public android.view.View getView()
	{
		if(FftView != null)
			return FftView;
		return null;
	}

	public android.view.SurfaceView getSurfaceView()
	{
		if(FftSurfaceView != null)
			return FftSurfaceView;
		return null;
	}

	public android.view.View getView(android.app.Activity $Activity)
	{
		if(Fft == null)
			init($Activity);
		return FftView = new FftView($Activity, this);
	}

	public android.view.SurfaceView getSurfaceView(android.app.Activity $Activity)
	{
		if(Fft == null)
			init($Activity);
		return FftSurfaceView = new FftSurfaceView($Activity, this);
	}

	public android.view.View getView(android.content.Context $Context)
	{
		if(Fft == null)
			init($Context);
		return FftView = new FftView($Context, this);
	}

	public android.view.SurfaceView getSurfaceView(android.content.Context $Context)
	{
		if(Fft == null)
			init($Context);
		return FftSurfaceView = new FftSurfaceView($Context, this);
	}

	public void Toast(boolean ErrorMode, Object Text)
	{
		String Message = (String) Text;
		if(ErrorMode)
			Message = "发生了一个错误：" + (String) Text;
		if(Context != null)
			android.widget.Toast.makeText(Context, Message, 1).show();
	}

	public void init(android.app.Activity $Activity)
	{
		Activity = $Activity;
		Context = $Activity.getApplicationContext();
		AudioManager = (android.media.AudioManager) Activity.getSystemService("audio");
		StreamMaxVolume = AudioManager.getStreamMaxVolume(3);
		StreamVolume = AudioManager.getStreamVolume(3);
		//AudioManager.setStreamVolume(3, StreamMaxVolume / 2, android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		/*AudioManager.adjustStreamVolume(3, 1, android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		AudioManager.getStreamVolume(3);
		*/
		init();
	}

	public void init(android.content.Context $Context)
	{
		Context = $Context;
		init();
	}

	private void init()
	{
		Width = 1080;
		Height = 1920;
		android.content.res.Resources Resources = Context.getResources();
		android.util.DisplayMetrics DisplayMetrics = Resources.getDisplayMetrics();
		ScreenWidth = DisplayMetrics.widthPixels;
		ScreenHeight = DisplayMetrics.heightPixels;
		Width = ScreenWidth;
		Height = ScreenHeight;
		UpDrawDataThread = new java.lang.Thread(new UpDrawDataThreadRunnable());
		Flag = true;
		AudioFxPlaying = true;
		AudioName = "Null";
		MusicName = "Null";
		setVisualizer();
		UpDrawDataThread.start();
		BufferWidth = ScreenWidth - (int) (ScreenWidth * 0.35f);
		//BufferHeight = (ScreenHeight / 2) - (int) (ScreenHeight * 0.1f);
		BufferHeight = BufferWidth;
		BackBuffer = android.graphics.Bitmap.createBitmap(BufferWidth, BufferHeight, android.graphics.Bitmap.Config.ARGB_4444);
		BackBufferT = android.graphics.Bitmap.createBitmap(BufferWidth, BufferHeight, android.graphics.Bitmap.Config.ARGB_4444);
		Buffer = android.graphics.Bitmap.createBitmap(BufferWidth, BufferHeight, android.graphics.Bitmap.Config.ARGB_4444);
		Logo = SuperTools.LocalBitmap(Context, "Logo.png");
		BackGroundColor = android.graphics.Color.parseColor("#2E2E2E");
		AudioFxBytes = null;
		Path = new android.graphics.Path();
		Paint = new android.graphics.Paint();
		Paint.setAntiAlias(true);
		Paint.setARGB(255, 255, 220, 175);
		Paint.setTextSize(getScreenDensityForNewSize(38f));
		Paint.setAlpha(255);
		PathPaint = new android.graphics.Paint();
		PathPaint.setAntiAlias(true);
		PathPaint.setStyle(android.graphics.Paint.Style.STROKE);
		PathPaint.setStrokeWidth(getScreenDensityForNewSize(3.2f));
		PathPaint.setARGB(255, 255, 220, 175);
		ForePaint = new android.graphics.Paint();
		ForePaint.setStrokeWidth(getScreenDensityForNewSize(1.2f));
		ForePaint.setAntiAlias(true);
		ForePaint.setARGB(255, 255, 220, 175);
		ForePaint.setAlpha(255);
		ForePaint.setTextSize(getScreenDensityForNewSize(38f));
		cPan = new android.graphics.Paint();
		cPan.setAntiAlias(true);
		cPan.setAlpha(210);
		cPan.setARGB(230, 255, 255, 255);
		cPan.setStyle(android.graphics.Paint.Style.STROKE);
		pC = new android.graphics.Paint();
		pC.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		panBg = new android.graphics.Paint();
		panBg.setStyle(android.graphics.Paint.Style.FILL);
		pan = new android.graphics.Paint();
		pan.setAntiAlias(true);
		DrawTextPaint = new android.graphics.Paint();
		DrawTextPaint.setAlpha(255);
		DrawTextPaint.setARGB(192,255,255,255);
		DrawTextPaint.setStrokeWidth(getScreenDensityForNewSize(18f));
		DrawTextPaint.setTextSize(getScreenDensityForNewSize(25f));
		DrawTextPaint.setAntiAlias(true);
		CirclePaint = new android.graphics.Paint();
		CirclePaint.setAlpha(255);
		CirclePaint.setARGB(255,255,255,255);
		CirclePaint.setStrokeWidth(getScreenDensityForNewSize(21f));
		CirclePaint.setAntiAlias(true);
		CirclePaint.setStyle(android.graphics.Paint.Style.STROKE);
		//HardwareCanvas = new HardwareCanvas(Context, BufferWidth, BufferHeight);
		RaCanvas = new android.graphics.Canvas();
		//RaCanvas.setDrawFilter(PaintFlagsDrawFilter);
		RaCanvas.setBitmap(android.graphics.Bitmap.createBitmap(BufferWidth, BufferHeight, android.graphics.Bitmap.Config.ARGB_4444));
		BackBufferCanvas = new android.graphics.Canvas();
		//BackBufferCanvas.setDrawFilter(PaintFlagsDrawFilter);
		BackBufferCanvas.setBitmap(android.graphics.Bitmap.createBitmap(BufferWidth, BufferHeight, android.graphics.Bitmap.Config.ARGB_4444));
		BufferSize = new float[4];
		BufferSize[0] = getScreenDensityForNewSize(10);//ScreenWidth * 0.009259259f;
		BufferSize[1] = getScreenDensityForNewSize(40);//ScreenWidth * 0.041666667f;
		BufferSize[2] = getScreenDensityForNewSize(122);//ScreenWidth * 0.117592593f;
		BufferSize[3] = getScreenDensityForNewSize(160);//ScreenWidth * 0.152777778f;
		MainUseLength = 0.0f;
		DataKey = 0.0f;
		Fft = "Fft";
	}

	public boolean IsInit()
	{
		if(Fft != null)
			return true;
		return false;
	}

	private void LocalMusiciIni()
	{
		try
		{
			java.io.InputStream InputStream = Context.getAssets().open("init.ini");
			java.io.File File = new java.io.File("/mnt/sdcard/init.ini");
			java.io.FileOutputStream FileOutputStream = new java.io.FileOutputStream(File);
			int len = -1 ;
			byte[] bytes = new byte[1024];
			while( (len=InputStream.read(bytes)) != -1)
			{
				FileOutputStream.write(bytes,0,len);
			}
			FileOutputStream.close();
			InputStream.close();
		}
		catch (Exception E)
		{
			Toast(true, E);
		}
	}

	private android.media.MediaPlayer LocalAssetsAudio(String Path)
	{
		android.media.MediaPlayer MediaPlayer = null;
		try
		{
			AudioName = Path.substring(0,Path.lastIndexOf("."));
			MusicName = AudioName.substring(AudioName.indexOf("-") + 1 ,AudioName.length()).trim();
			MediaPlayer = new android.media.MediaPlayer();
			android.content.res.AssetManager AssetsManager = Context.getAssets();
			android.content.res.AssetFileDescriptor AssetsFileDescriptor = AssetsManager.openFd(Path);
			MediaPlayer.setDataSource(AssetsFileDescriptor.getFileDescriptor(),AssetsFileDescriptor.getStartOffset(),AssetsFileDescriptor.getLength());
		}
		catch (Exception E)
		{
			Toast(true, E);
		}
		return MediaPlayer;
	}

	private void LocalAssetsAudio(String Path,android.media.MediaPlayer $MediaPlayer)
	{
		try
		{
			AudioName = Path.substring(0,Path.lastIndexOf("."));
			MusicName = AudioName.substring(AudioName.indexOf("-") + 1 ,AudioName.length()).trim();
			android.content.res.AssetManager AssetsManager = Context.getAssets();
			android.content.res.AssetFileDescriptor AssetsFileDescriptor = AssetsManager.openFd(Path);
			$MediaPlayer.setDataSource(AssetsFileDescriptor.getFileDescriptor(),AssetsFileDescriptor.getStartOffset(),AssetsFileDescriptor.getLength());
		}
		catch (Exception E)
		{
			Toast(true, E);
		}
		return;
	}

	private java.io.FileInputStream LocalAssetsAudioFileInput(String Path)
	{
		java.io.FileInputStream FileInputStream = null;
		try
		{
			android.content.res.AssetManager AssetsManager = Context.getAssets();
			android.content.res.AssetFileDescriptor AssetsFileDescriptor = AssetsManager.openFd(Path);
			FileInputStream = AssetsFileDescriptor.createInputStream();
		}
		catch (Exception E)
		{
			Toast(true, E);
		}
		return FileInputStream;
	}

	private void LocalAudio(String Path)
	{
		if(MediaPlayer != null && Path.substring(Path.lastIndexOf("/") + 1, Path.lastIndexOf(".")).equals(AudioName))
		{
			MediaPlayer.start();
			return;
		}
		try
		{
			if(MediaPlayer == null)
			{
				MediaPlayer = new android.media.MediaPlayer();
			}
			MediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
			MediaPlayer.reset();
			if(new java.io.File(Path).isFile())
			{
				AudioName = Path.substring(Path.lastIndexOf("/") + 1,Path.lastIndexOf("."));
				MusicName = AudioName.substring(AudioName.indexOf("-") + 1 ,AudioName.length()).trim();
				MediaPlayer.setDataSource(Path);
			}
			else
			{
				LocalAssetsAudio(Path, MediaPlayer);
			}
			MediaPlayer.prepare();
			MediaPlayer.setVolume(1.5f,1.5f);
			//缓冲完成的监听
			if(MediaPlayer != null)
				MediaPlayer.start();
			else
			MediaPlayer.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener()
			{
				@Override
				public void onPrepared(android.media.MediaPlayer MediaPlayer)
				{
					//播放音乐
					MediaPlayer.start();
				}
			});
		}
		catch(Exception E)
		{
			Toast(true, E);
		}
	}

	private void setVisualizer()
	{
		if(Visualizer != null)
		{
			if(!Visualizer.getEnabled())
				Visualizer.setEnabled(true);
			return;
		}
		int AudioSessionId = -1;
		if(AudioTrack != null)
			AudioSessionId = AudioTrack.getAudioSessionId();
		else if(MediaPlayer != null)
			AudioSessionId = MediaPlayer.getAudioSessionId();
		if(AudioSessionId != -1)
			Visualizer = new android.media.audiofx.Visualizer(AudioSessionId);
		else
			Visualizer = new android.media.audiofx.Visualizer(0);
		if(Visualizer != null)
			setVisualizer(1024, Visualizer.getMaxCaptureRate(), true, true);
	}

	private void setVisualizer(int CaptureSize, int MaxCaptureRate, boolean UserWave, boolean UserAudioFft)
	{
		Visualizer.setEnabled(false);
		Visualizer.setCaptureSize(CaptureSize);
		Visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
        Visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
		Visualizer.setDataCaptureListener(new android.media.audiofx.Visualizer.OnDataCaptureListener()
		{
			// 更新时域波形数据
			public void onWaveFormDataCapture(android.media.audiofx.Visualizer Visualizer, byte[] Bytes, int S)
			{
				if(FftDrawWaveMode == 1)
					try
					{
						byte[] Model = new byte[Bytes.length];
						for (int Index = 0; Index < Bytes.length; Index++)
						{
							Bytes[Index] = (byte) (((int) Bytes[Index] & 0xFF) - 128);
							Model[Index] = (byte) (Math.hypot(Bytes[Index] << 1, Bytes[(Index + 1) % Bytes.length] << 1));
						}
						UpdateVisualizerWave(Model, ByteAllReadCount);
					}
					catch(Exception E)
					{
						Toast(true, E);
					}
			}
			// 更新频域波形数据
			public void onFftDataCapture(android.media.audiofx.Visualizer Visualizer, byte[] Bytes, int S)
			{
				try
				{
					UpVisualizerStartTime = System.currentTimeMillis();
					byte[] Model = new byte[Bytes.length];
					for (int Index = 0; Index < Bytes.length; Index++)
					{
						Model[Index] = (byte) (Math.hypot(Bytes[Index] * 1.2f, Bytes[(Index + 1) % Bytes.length] * 1.2f));
					}
					UpdateVisualizerFft(Model, ByteAllReadCount);
					UpVisualizerFftData();
					UpVisualizerEndTime = System.currentTimeMillis() - UpVisualizerStartTime;
				}
				catch(Exception E)
				{
					Toast(true, E);
				}
			}
		}, MaxCaptureRate, UserWave, UserAudioFft);
		Visualizer.setEnabled(true);
	}

	private long UpVisualizerStartTime = 0;
	private long UpVisualizerEndTime = 0;

	private void UpVisualizerFftData()
	{
		if(AudioFxBytes == null)
			return;
		if(LastAudioData == null)
			LastAudioData = new float[AudioFxBytes.length];
		if (drawTime != -1)
			deltaTime = System.currentTimeMillis() - drawTime;
		drawTime = System.currentTimeMillis();
		DataKey = 0.0f;
		//AudioFxBytes = calculateVolume(AudioFxBytes);
		for(int Index = 0; Index < AudioFxBytes.length; Index++)
		{
			if(AudioFxBytes[Index] < 0)
				AudioFxBytes[Index] = 0;
			if(AudioFxBytes[Index] > MainUseLength)
				MainUseLength = AudioFxBytes[Index];
			if(AudioFxBytes[Index] > 1)
				DataKey += ((float) (Index * 2.5d) * AudioFxBytes[Index]);
		}
		DataKey /= (float) AudioFxBytes.length;
		DataKey = (float) Math.sqrt(DataKey) / 7f;
		DataKey = (float) Math.sqrt(DataKey);
		LastDataKey = DataKey;
		MainUseLength /= (float) 128;
		if (ReadByte == null || ReadByte.length != ByteAllReadCount)
			ReadByte = new float[ByteAllReadCount];
		//
		//
		for (int Index = 0; Index < ReadByte.length; Index++)
		{
			if(ReadByte[Index] > 1.11f)
				ReadByte[Index] = AudioFxBytes[Index] * (2f + (0.11f * (Index * 0.7f))) / 2f;
			else if(ReadByte[Index] != 0)
				ReadByte[Index] = AudioFxBytes[Index] * 1.51f;
			else
				ReadByte[Index] = AudioFxBytes[Index];
			ReadByte[Index] = ((float) BufferWidth) * (ReadByte[Index] / 1080);
		}//*/ReadByte = AudioFxBytes;

		//RateAdd = 0;
		float Pan = 210f / 255;
		if (DataKey > (DataLastKey))
			RateAdd = Math.min((float) Math.pow(DataKey * 2.564d - DataLastKey * 2.564d, 0.3) * 10, 25) * 1.2f;
		if (DataKey > (DataLastKey * 1.14d + 0.04d))
		{
			RateAdd = Math.min((float) Math.pow(DataKey - DataLastKey, 0.3) * 10, 35) * 3.2f;
			Pan = 150f / 255;
			cPan.setAlpha((int) Pan);
		}
		DataLastKey = DataKey;
	}

	private float[] calculateVolume(float[] f)
	{
		float[] r = new float[f.length];
		int range = 2;
		int start = 0;
		int end = 0;
		int count = 0;
		float rv;
		for (int i = 0; i < r.length; i++)
		{
			rv = 0;
			start = (i - range < 0) ? 0 : (i - range);
			end = (i + range >= r.length) ? (r.length - 1) : (i + range);
			count = end - start + 1;
			for (int j = start; j <= end; j++)
			{
				rv += f[j];
			}
			rv = rv / (count * 255);
			rv = (float) Math.pow(rv, 0.15);
			r[i] = rv * f[i] * 2;
		}
		return r;
	}
	

	public void PlayTask(String Path)
	{
		IsPlaying = true;
		try
		{
			if(AudioTrackPlayMusic != null && AudioTrackPlayMusic.HasMusic && (Path.substring(Path.lastIndexOf("/") + 1, Path.lastIndexOf("."))).equals(AudioName))
			{
				AudioTrackPlayMusic.Playing = true;
				return;
			}
			else
			{
				if(AudioTrackPlayMusic != null)
				{
					AudioTrackPlayMusic.Release();
					AudioTrackPlayMusic = null;
				}
				if (AudioTrack != null)
				{
					if (AudioTrack.getState() == android.media.AudioRecord.STATE_INITIALIZED)
					{
						AudioTrack.stop();
					}
					AudioTrack.release();
					AudioTrack = null;
				}
				System.gc();
				AudioName = Path.substring(Path.lastIndexOf("/") + 1, Path.lastIndexOf("."));
				MusicName = AudioName.substring(AudioName.indexOf("-") + 1, AudioName.length()).trim();
				// 开始播放
				if(AudioTrackPlayMusic == null)
					AudioTrackPlayMusic = new AudioTrackPlayMusic();
				AudioTrackPlayMusic.decodeMusicFile(Path, Path, Context);
			}
		}
		catch (Exception E)
		{
			Toast(true, E);
		}
		return;
	}

	public void Draw(android.graphics.Canvas Canvas, int Width, int Height)
	{
		try
		{
			long Time = System.currentTimeMillis();
			synchronized(Canvas)
			{
				if(Canvas == null)
					return;
				Canvas.drawColor(android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
				UpDrawBitmap(Canvas);
				Canvas.drawBitmap(Buffer, (Width >> 1) - (Buffer.getWidth() >> 1), (Height >> 1) - (Buffer.getHeight() >> 1), pan);
				if(IsInformation)
				{
					int Y = 100;
					Canvas.drawText("UpVisualizerEndTime：" + UpVisualizerEndTime, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("DataKey：" + DataKey, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("DrawRng：" + DrawRng, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("DrawR：" + DrawR, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("RateAdd：" + RateAdd, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("DeltaTime：" + (System.currentTimeMillis() - Time), 20, Y, Paint);
					Y += 40;
					Canvas.drawText("IsHardwareAccelerated：" + Canvas.isHardwareAccelerated(), 20, Y, Paint);
					Y += 40;
					Canvas.drawText("ByteAllReadCount：" + ByteAllReadCount, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("ByteAllReadCountForWave：" + ByteAllReadCountForWave, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("StreamMaxVolume：" + StreamMaxVolume, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("StreamVolume：" + StreamVolume, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("ScreenWidth：" + ScreenWidth, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("ScreenHeight：" + ScreenHeight, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("BufferWidth：" + BufferWidth, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("BufferHeight：" + BufferHeight, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("MusicName：" + MusicName, 20, Y, Paint);
					Y += 40;
					Canvas.drawText("Canvas：" + (Canvas.getClass().toString()), 20, Y, Paint);
					Y += 40;
					Canvas.drawText("DrawCanvas：" + (RaCanvas.getClass().toString()), 20, Y, Paint);
					Y += 40;
				}
				if(IsFps)
				{
					FPS = 1000 / (System.currentTimeMillis() - Time);
					if(FPS > MaxFps)
						FPS = MaxFps;
					Canvas.drawText("" + FPS, Width - BufferWidth - (ForePaint.getTextSize() / 2), BufferHeight, Paint);
				}
			}
		}
		catch(Exception E)
		{
			Toast(true, E);
		}
	}

	public boolean IsInformation()
	{
		return IsInformation;
	}

	public void SetFPS(int $FrameRate)
	{
		MaxFps = $FrameRate;
		if(MaxFps > 120)
			MaxFps = 120;
		else if(MaxFps < 60)
			MaxFps = 60;
		if(FftView != null)
			FftView.SetFPS(this, MaxFps);
		else if(FftSurfaceView != null)
			FftSurfaceView.SetFPS(this, MaxFps);
	}

	public void ShowFPS(boolean Check)
	{
		IsFps = Check;
	}

	public void ShowInformation(boolean Check)
	{
		IsInformation = Check;
	}

	public void FftDrawWaveMode(int Mode)
	{
		FftDrawWaveMode = Mode;
		if(FftDrawWaveMode < 0)
			FftDrawWaveMode = 0;
		else if(Mode > 3)
			FftDrawWaveMode = 3;
		if(IsInit())
			switch(FftDrawWaveMode)
			{
				case 0:
					ByteAllReadCountForWave = 0;
				break;
				case 1:
					ByteAllReadCountForWave = 86 << 1;
					CirclePaint.setStrokeWidth(getScreenDensityForNewSize(21f));
				break;
				case 2:
					ByteAllReadCountForWave = 86;
					CirclePaint.setStrokeWidth(getScreenDensityForNewSize(21f));
				break;
				case 3:
					ByteAllReadCountForWave = 260;
					CirclePaint.setStrokeWidth(getScreenDensityForNewSize(17f));
				break;
			}
	}

	private void UpDrawBitmap(android.graphics.Canvas Canvas)
	{
		RaCanvas.setBitmap(Buffer);
		RaCanvas.drawRect(0, 0, BufferWidth, BufferHeight, pC);
		//RaCanvas.drawARGB(255, 0, 0, 0);
		panBg.setAlpha((int) (140 * Math.cos(DataKey)));
		pan.setAlpha(180);
		RaCanvas.drawBitmap(BackBufferT, 0, 0, pan);
		BackBufferCanvas.setBitmap(BackBufferT);
		BackBufferCanvas.drawRect(0, 0, BufferWidth, BufferHeight, pC);

		//======================
		float CenterX = BufferWidth >> 1;
		float CenterY = BufferHeight >> 1;
		//======================

		ForePaint.setAlpha(255);

		//======绘制条形频段======
		//波形三不需要绘制条形
		if(FftDrawWaveMode != 3)
			if(IsCycleColor)
			{
				ColorCounter = 58.3f;
				//SetColorCounter();
				if(DrawR > 0)
					for (int Bar = 0; Bar < ByteAllReadCount; Bar++)
					{
						float BarLine = DrawRFBytes[Bar];
						BarLine = ((float) ScreenWidth) * (BarLine / 1080);
						float BarAngle = DeltaAngle * Bar;
						CycleColor(ForePaint, 0.024f);
						RaCanvas.drawLine((float)(CenterX + Math.cos(BarAngle) * DrawR), (float)(CenterY - Math.sin(BarAngle) * DrawR), (float)(CenterX + Math.cos(BarAngle) * (BarLine * 1.5f + DrawR)), (float)(CenterY - Math.sin(BarAngle) * (BarLine * 1.5f + DrawR)), ForePaint);
					}
			}
			else
			{
				if(Points != null)
				{
					ForePaint.setARGB(255, 255, 220, 175);
					RaCanvas.drawLines(Points, ForePaint);
				}
			}

		//======绘制波形======
		if(FftDrawWaveMode != 0 && DrawR > 0)
		{
			ColorCounter = 58.3f;
			//SetColorCounter();
			Path.reset();
			float LastX = CenterX;
			float LastY = CenterY;
			float FirstX = 0;
			float FirstY = 0;
			//======波形一======
			if(FftDrawWaveMode == 1 && AudioWave != null)
			{
				float DeltaAngleForWave = (float) (6.283185307179586d / ByteAllReadCountForWave);
				float BarWidth = (float) (6.283185307179586d / (ByteAllReadCountForWave >> 1));
				for (int Bar = 0; Bar < ByteAllReadCountForWave; Bar++)
				{
					float BarLine = AudioWave[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					float BarAngle = (DeltaAngleForWave * Bar) + BarWidth;
					float R = Math.abs(BarLine + CircleR);
					float SLineX = (float)(CenterX + Math.cos(BarAngle) * R);
					float SLineY = (float)(CenterY - Math.sin(BarAngle) * R);
					if(Bar == 0)
					{
						FirstX = SLineX;
						FirstY = SLineY;
					}
					else
					{
						Path.moveTo(LastX, LastY);
						Path.lineTo(SLineX, SLineY);
					}
					LastX = SLineX;
					LastY = SLineY;
					if(Bar == ByteAllReadCountForWave - 1)
					{
						Path.lineTo(FirstX, FirstY);
						if(!IsCycleColorForWave)
						{
							RaCanvas.drawPath(Path, PathPaint);
							Path.reset();
						}
					}
					if(IsCycleColorForWave)
					{
						CycleColor(PathPaint, 0.046f);
						RaCanvas.drawPath(Path, PathPaint);
						Path.reset();
					}
				}
			}

			//======波形二======
			if(FftDrawWaveMode == 2)
			{
				float Time = 1.2f;
				float DeltaAngleForWave = (float) (6.283185307179586d / ByteAllReadCountForWave);
				float BarWidth = (float) (6.283185307179586d / ByteAllReadCountForWave / 2);
				for (int Bar = 0; Bar < ByteAllReadCountForWave; Bar++)
				{
					if(IsCycleColorForWave)
						Path.reset();
					float BarLine = DrawRFBytes[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					float BarAngle = DeltaAngleForWave * Bar;
					float R = CircleR;
					float SmoothLineR = CircleR;
					float Rate = Math.abs((BarLine - DrawRFBytes[Bar + 1]) * 3);
					SmoothLineR += Rate;
					R += BarLine * Time - Rate;
					float BarLineX = (float)(CenterX + Math.cos(BarAngle + BarWidth) * R);
					float BarLineY = (float)(CenterY - Math.sin(BarAngle + BarWidth) * R);
					float ELineX = (float)(CenterX + Math.cos(BarWidth * 2 + BarAngle) * SmoothLineR);
					float ELineY = (float)(CenterY - Math.sin(BarWidth * 2 + BarAngle) * SmoothLineR);
					if(Bar == 0)
					{
						//上行
						float SLineX = (float)(CenterX + Math.cos(BarAngle) * CircleR);
						float SLineY = (float)(CenterY - Math.sin(BarAngle) * CircleR);
						FirstX = SLineX;
						FirstY = SLineY;
						Path.moveTo(SLineX, SLineY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(ELineX, ELineY);
						//下行
						R = Math.abs(BarLine * Time - CircleR - Rate);
						BarLineX = (float)(CenterX + Math.cos(BarAngle + BarWidth) * R);
						BarLineY = (float)(CenterY - Math.sin(BarAngle + BarWidth) * R);
						Path.moveTo(SLineX, SLineY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(ELineX, ELineY);
					}
					else if(Bar != ByteAllReadCountForWave)
					{
						//上行
						Path.moveTo(LastX, LastY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(ELineX, ELineY);
						//下行
						R = Math.abs(BarLine * Time - CircleR - Rate);
						BarLineX = (float)(CenterX + Math.cos(BarAngle + BarWidth) * R);
						BarLineY = (float)(CenterY - Math.sin(BarAngle + BarWidth) * R);
						Path.moveTo(LastX, LastY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(ELineX, ELineY);
					}
					if(Bar == ByteAllReadCountForWave)
					{
						//上行
						Path.moveTo(LastX, LastY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(FirstX, FirstY);
						//下行
						R = Math.abs(BarLine * Time - CircleR - Rate);
						BarLineX = (float)(CenterX + Math.cos(BarAngle + BarWidth) * R);
						BarLineY = (float)(CenterY - Math.sin(BarAngle + BarWidth) * R);
						Path.moveTo(LastX, LastY);
						Path.lineTo(BarLineX, BarLineY);
						Path.lineTo(FirstX, FirstY);
					}
					//点
					R = Math.abs(BarLine * Time - (CircleR * 0.7f) - Rate);
					float PointX = (float)(CenterX + Math.cos(BarAngle + BarWidth) * R);
					float PointY = (float)(CenterY - Math.sin(BarAngle + BarWidth) * R);
					LastX = ELineX;
					LastY = ELineY;
					//极致色彩
					if(IsCycleColorForWave)
					{
						CycleColor(PathPaint, 0.08f);
						RaCanvas.drawPath(Path, PathPaint);
						RaCanvas.drawPoint(PointX, PointY, PathPaint);
					}
					else
						RaCanvas.drawPoint(PointX, PointY, PathPaint);
				}
			}

			if(!IsCycleColorForWave)
			{
				PathPaint.setARGB(255, 255, 220, 175);
				RaCanvas.drawPath(Path, PathPaint);
				Path.reset();
			}

			//======波形三======
			if(FftDrawWaveMode == 3)
			{
				int MaxHeight = (int) DrawR >> 1;
				float Time = 1.6f;
				float BarWidth = (float) (6.283185307179586d / (ByteAllReadCountForWave >> 1));
				float DeltaAngleForWave = (float) (6.283185307179586d / (ByteAllReadCountForWave + 1));
				float DrawRForWave = DrawR * 0.7f;
				//======点======
				for (int Bar = 0; Bar < ByteAllReadCountForWave + 1; Bar++)
				{
					float BarLine = DrawRFBytes[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					if(BarLine > MaxHeight)
						BarLine /= 2;
					float BarAngle = (DeltaAngleForWave * Bar) + BarWidth;
					float R = Math.abs(BarLine * Time - DrawRForWave);
					float PointX = (float)(CenterX + Math.cos(BarAngle) * R);
					float PointY = (float)(CenterY - Math.sin(BarAngle) * R);
					//极致色彩
					if(IsCycleColorForWave)
					{
						CycleColor(PathPaint, 0.034f);
						RaCanvas.drawPoint(PointX, PointY, PathPaint);
					}
					else
					{
						PathPaint.setARGB(255, 255, 255, 220);
						RaCanvas.drawPoint(PointX, PointY, PathPaint);
					}
					Path.reset();
				}

				PathPaint.setARGB(255, 255, 255, 255);
				PathPaint.setStrokeWidth(getScreenDensityForNewSize(1.1f));
				//======线条======
				for (int Bar = 0; Bar < ByteAllReadCountForWave + 1; Bar++)
				{
					float BarLine = DrawRFBytes[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					if(BarLine > MaxHeight)
						BarLine /= 2;
					float BarAngle = (DeltaAngleForWave * Bar) + BarWidth;
					float R = Math.abs(BarLine * Time + DrawR);
					float SLineX = (float)(CenterX + Math.cos(BarAngle) * R);
					float SLineY = (float)(CenterY - Math.sin(BarAngle) * R);
					R = Math.abs(BarLine * Time - DrawR);
					float ELineX = (float)(CenterX + Math.cos(BarAngle) * R);
					float ELineY = (float)(CenterY - Math.sin(BarAngle) * R);
					Path.moveTo(SLineX, SLineY);
					Path.lineTo(ELineX, ELineY);
					RaCanvas.drawPath(Path, PathPaint);
					Path.reset();
				}
				PathPaint.setStrokeWidth(getScreenDensityForNewSize(3.2f));

				//======上行======
				for (int Bar = 0; Bar < ByteAllReadCountForWave + 1; Bar++)
				{
					float BarLine = DrawRFBytes[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					if(BarLine > MaxHeight)
						BarLine /= 2;
					float BarAngle = (DeltaAngleForWave * Bar) + BarWidth;
					float R = Math.abs(BarLine * Time + DrawR);
					float SLineX = (float)(CenterX + Math.cos(BarAngle) * R);
					float SLineY = (float)(CenterY - Math.sin(BarAngle) * R);
					if(Bar == 0)
					{
						FirstX = SLineX;
						FirstY = SLineY;
						Path.moveTo(SLineX, SLineY);
					}
					else
						Path.lineTo(SLineX, SLineY);
					if(Bar == ByteAllReadCountForWave)
					{
						Path.lineTo(FirstX, FirstY);
						RaCanvas.drawPath(Path, PathPaint);
						Path.reset();
					}
				}
				//======下行======
				for (int Bar = 0; Bar < ByteAllReadCountForWave + 1; Bar++)
				{
					float BarLine = DrawRFBytes[Bar];
					BarLine = ScreenWidth * (BarLine / 1080);
					if(BarLine > MaxHeight)
						BarLine /= 2;
					float BarAngle = (DeltaAngleForWave * Bar) + BarWidth;
					float R = Math.abs(BarLine * Time - DrawR);
					float SLineX = (float)(CenterX + Math.cos(BarAngle) * R);
					float SLineY = (float)(CenterY - Math.sin(BarAngle) * R);
					if(Bar == 0)
					{
						FirstX = SLineX;
						FirstY = SLineY;
						Path.moveTo(SLineX, SLineY);
					}
					else
						Path.lineTo(SLineX, SLineY);
					if(Bar == ByteAllReadCountForWave)
					{
						Path.lineTo(FirstX, FirstY);
						RaCanvas.drawPath(Path, PathPaint);
						Path.reset();
					}
				}
			}
		}
		if(FftDrawWaveMode != 3)
			RaCanvas.drawCircle(CenterX, CenterY, CircleR, CirclePaint); // _47
		else
			RaCanvas.drawCircle(CenterX, CenterY, DrawR, CirclePaint);
		pan.setAlpha(255);
		BackBufferCanvas.drawBitmap(Buffer, 0, 0, Paint);
	}

	private void UpDrawData()
	{
		//RoateColor();
		if(AudioFxBytes == null)
			return;
		float[] RFBytes = ReadByte;
		// 60 * 137
		// 10 45 127 165
		float Rng = 0;
		float R = 0;
		if(DataKey == 0 || DataKey < 0)
		{
			Rng = BufferSize[1] * 0.7f + BufferSize[2];
			R = (int) (BufferSize[3] * 0.9f+ BufferSize[0] * 0.6f);
		}
		else
		{
			Rng = (DataKey * 1.7f + MainUseLength) * (BufferSize[1] * 0.7f) + BufferSize[2];
			Rng += RateAdd;
			//======================
			R = (BufferSize[3] * 0.9f + ((DataKey * 4.7f + MainUseLength) * (BufferSize[0] * 0.6f))); // * 25
			R += RateAdd;
		}
		Rng = getScreenDensityForNewSize(Rng);
		R = getScreenDensityForNewSize(R);
		//=====================
		if(DrawRng == 0)
		{
			DrawRng = Rng / 2;
			LastDrawRng = DrawRng;
		}
		else if(Rng > DrawRng)
		{
			DrawRngRate = (Rng - DrawRng) / 4;
			DrawRng += DrawRngRate;
		}
		else if(Rng < DrawRng)
		{
			DrawRngRate = (DrawRng - Rng) / 4;
			if(DrawRngRate < 0)
				DrawRngRate = UpDrawDataRate;
			DrawRng -= DrawRngRate;
		}
		if(DrawRng > 260)
			DrawRng = LastDrawRng;
		if(DrawRng > 260)
			DrawRng = 260 + (int) (DataKey) << 3;
		LastDrawRng = DrawRng;
		//=====================
		if(DrawR == 0)
		{
			DrawR = R / 2;
			LastDrawR = DrawR;
		}
		else if(R > DrawR)
		{
			DrawRRate = (R - DrawR) / 4;
			DrawR += DrawRRate;
		}
		else if(R < DrawR)
		{
			DrawRRate = (DrawR - R) / 4;
			if(DrawRRate < 0)
				DrawRRate = UpDrawDataRate;
			DrawR -= DrawRRate;
		}
		if(DrawR > 260)
			DrawR = LastDrawR;
		if(DrawR > 260)
			DrawR = 200 + (int) (DataKey) << 3;
		LastDrawR = DrawR;
		//=====================
		if(DrawRFBytes == null)
		{
			DrawRFBytes = new float[RFBytes.length];
			DrawRFBytesRate = new float[RFBytes.length];
		}
		else
		{
			for(int Index = 0; Index < ByteAllReadCount; Index++)
			{
				if(RFBytes[Index] > DrawRFBytes[Index])
				{
					DrawRFBytesRate[Index] = (RFBytes[Index] - DrawRFBytes[Index]) / UpDrawDataRate;
					DrawRFBytes[Index] += DrawRFBytesRate[Index];
				}
				else if(RFBytes[Index] < DrawRFBytes[Index])
				{
					DrawRFBytesRate[Index] = (DrawRFBytes[Index] - RFBytes[Index]) / UpDrawDataRate;
						DrawRngRate = UpDrawDataRate;
					DrawRFBytes[Index] -= DrawRFBytesRate[Index];
				}
			}
		}
		if(Wave != null)
		if(AudioWave == null)
		{
			AudioWave = new int[Wave.length];
			AudioWaveRate = new float[Wave.length];
		}
		else
		{
			for(int Index = 0; Index < ByteAllReadCountForWave; Index++)
			{
				if(Wave[Index] > AudioWave[Index])
				{
					AudioWaveRate[Index] = (Wave[Index] - AudioWave[Index]) / UpDrawDataRate;
					AudioWave[Index] += AudioWaveRate[Index];
				}
				else if(Wave[Index] < AudioWave[Index])
				{
					AudioWaveRate[Index] = (AudioWave[Index] - Wave[Index]) / UpDrawDataRate;
					DrawRngRate = UpDrawDataRate;
					AudioWave[Index] -= AudioWaveRate[Index];
				}
			}
		}
		if (Points == null || Points.length < ByteAllReadCount << 2)
		{
			Points = new float[ByteAllReadCount << 2];
		}
		if(!IsCycleColor && FftDrawWaveMode != 3)
		{
			float CenterX = BufferWidth >> 1;
			float CenterY = BufferHeight >> 1;
			if(DrawR > 0)
				for (int Bar = 0; Bar < ByteAllReadCount; Bar++)
				{
					float BarLine = DrawRFBytes[Bar];
					BarLine = ((float) ScreenWidth) * (BarLine / 1080);
					float BarAngle = DeltaAngle * Bar;
					this.Points[Bar << 2] = (float)(CenterX + Math.cos(BarAngle) * DrawR);
					this.Points[1 + (Bar << 2)] = (float)(CenterY - Math.sin(BarAngle) * DrawR);
					this.Points[2 + (Bar << 2)] = (float)(CenterX + Math.cos(BarAngle) * (BarLine * 1.5f + DrawR));
					this.Points[3 + (Bar << 2)] = (float)(CenterY - Math.sin(BarAngle) * (BarLine * 1.5f + DrawR));
			}
		}
		CircleR = DrawRng - getScreenDensityForNewSize(57);
		MainUseLength = 0;
	}

	private class UpDrawDataThreadRunnable implements java.lang.Runnable
	{
		@Override
		public void run()
		{
			while (Flag)
			{
				if(System.currentTimeMillis() >= UpDrawDataTime)
				{
					UpDrawDataTime = System.currentTimeMillis() + UpDrawDataFrameRate;
					UpDrawData();
				}
				try
				{
					Thread.sleep(1);
				} catch(Exception E) {}
			}
			return;
		}
	}

	public void OpenCycleColor(boolean Check)
	{
		IsCycleColor = Check;
	}

	public void OpenCycleColorForWave(boolean Check)
	{
		IsCycleColorForWave = Check;
	}

	private float ColorCounter = 58.3f;
	private float RotateColor = 58.3f;
	private boolean ColorFlag = true;
	private void CycleColor(android.graphics.Paint $Paint, float Index)
	{
		int R = (int)Math.floor(128 * (Math.sin(ColorCounter) + 1));
		int G = (int)Math.floor(128 * (Math.sin(ColorCounter + 2) + 1));
		int B = (int)Math.floor(128 * (Math.sin(ColorCounter + 4) + 1));
		$Paint.setColor(android.graphics.Color.rgb(R, G, B));
		ColorCounter += 0.01;
		if(!ColorFlag)
		{
			ColorCounter += Index;
		}
		else if(ColorFlag)
		{
			ColorCounter -= Index;
		}
		if(ColorCounter <= 0)
			ColorFlag = false;
		else if(ColorCounter >= 128)
			ColorFlag = true;
	}

	private void SetColorCounter()
	{
		ColorCounter = RotateColor;
	}

	private void RoateColor()
	{
		RotateColor += 0.02f;
	}

	private void UpdateVisualizerFft(byte[] Datas, int Length)
	{
		float[] HandleDatas = new float[1 + Length];
		for(int Index = 0; Index < HandleDatas.length; Index++)
		{
			Datas[Index] = (byte) (Datas[Index] & 0xFF);
			HandleDatas[Index] = (float)(Math.sqrt(Datas[Index] * Datas[Index] + Datas[Index + 1] * Datas[Index + 1]) / 2);
		}
		AudioFxBytes = HandleDatas;
	}

	private void UpdateVisualizer(float[] Datas) 
	{
		float[] HandleDatas = new float[Datas.length >> 1 + 1];
		HandleDatas[0] = (float)(Math.sqrt((Datas[0] * Datas[0]) * 127 * 127 / 2));
		for(int Index = 1, IndexT = 2; Index < HandleDatas.length && (IndexT + 1) < Datas.length;)
		{
			HandleDatas[Index] = (float)(Math.sqrt(((Datas[IndexT] * Datas[IndexT] + Datas[(IndexT + 1) % Datas.length] * Datas[IndexT + 1]) * 127 * 127) / 2));
			Index ++;
			IndexT += 2;
		}
		AudioFxBytes = HandleDatas;
	}

	private void UpdateVisualizerWave(byte[] Datas, int Length)
	{
		int[] HandleDatas = new int[1 + Length];
		for(int Index = 0; Index < HandleDatas.length; Index++)
		{
			HandleDatas[Index] = (int)(Math.sqrt(((Datas[Index] * Datas[Index])) >> 1));
		}
		Wave = HandleDatas;
	}

	public void Stop()
	{
		if(Fft != null)
		{
			Flag = false;
			boolean WorkIsNotFinish = true;
			while(WorkIsNotFinish)
			{
				try
				{
					UpDrawDataThread.join();
				}
				catch (Exception E)
				{
					Toast(true, E);
				}
				Context = null;
				Canvas = null;
				WorkIsNotFinish = false;
			}
		}
	}

	public void Pause(String Play)
	{
		switch(Play)
		{
			case "MediaPlayer":
				if(MediaPlayer != null)
					MediaPlayer.pause();
			break;
			case "AudioTrack":
				if(AudioTrackPlayMusic != null)
					if(AudioTrackPlayMusic.HasMusic == true)
						AudioTrackPlayMusic.Playing = false;
			break;
		}
		AudioFxPlaying = false;
	}

	public void Pause()
	{
		if(MediaPlayer != null)
			MediaPlayer.pause();
		if(AudioTrackPlayMusic != null)
			if(AudioTrackPlayMusic.HasMusic == true)
				AudioTrackPlayMusic.Playing = false;
		AudioFxPlaying = false;
	}

	public void Start(String Play)
	{
		switch(Play)
		{
			case "MediaPlayer":
				if(MediaPlayer != null)
					MediaPlayer.start();
				if(Visualizer != null && !Visualizer.getEnabled())
					Visualizer.setEnabled(true);
			break;
			case "AudioTrack":
				if(AudioTrackPlayMusic != null)
					if(AudioTrackPlayMusic.HasMusic == true)
						AudioTrackPlayMusic.Playing = true;
			break;
		}
		AudioFxPlaying = true;
	}

	public void Start()
	{
		if(MediaPlayer != null)
			MediaPlayer.start();
		if(Visualizer != null && !Visualizer.getEnabled())
			Visualizer.setEnabled(true);
		if(AudioTrackPlayMusic != null)
			if(AudioTrackPlayMusic.HasMusic == true)
				AudioTrackPlayMusic.Playing = true;
		AudioFxPlaying = true;
	}

	public void LoaderMusic(String Play, String Path)
	{
		switch(Play)
		{
			case "MediaPlayer":
				if(MediaPlayer != null && MediaPlayer.isPlaying() != false && AudioName.equals(Path.substring(Path.lastIndexOf("/") + 1, Path.lastIndexOf("."))))
				{
					MediaPlayer.start();
					if(Visualizer != null && !Visualizer.getEnabled())
						Visualizer.setEnabled(true);
					else
						setVisualizer();
					AudioFxPlaying = true;
				}
				if(!Path.substring(Path.lastIndexOf("/") + 1, Path.lastIndexOf(".")).equals(AudioName))
				{
					if(MediaPlayer != null)
						MediaPlayer.stop();
					LocalAudio(Path);
					setVisualizer();
					AudioFxPlaying = true;
				}
			break;
			case "AudioTrack":
				if(AudioTrackPlayMusic != null && AudioTrackPlayMusic.HasMusic)
				{
					AudioTrackPlayMusic.Playing = true;
					AudioFxPlaying = true;
				}
				else
				{
					PlayTask(Path);
					AudioFxPlaying = true;
				}
			break;
		}
	}

	public boolean onKeyDown(int KeyCode, android.view.KeyEvent KeyEvent)
	{
		if(KeyEvent.getAction() == android.view.KeyEvent.ACTION_DOWN)
		switch(KeyCode)
		{
			case 4:
				Stop();
				System.exit(0);
			break;
			case 24:
				if(AudioManager != null)
				{
					AudioManager.adjustStreamVolume(3, 1, android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
					StreamVolume = AudioManager.getStreamVolume(3);
				}
			break;
			case 25:
				if(AudioManager != null)
				{
					AudioManager.adjustStreamVolume(3, -1, android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
					StreamVolume = AudioManager.getStreamVolume(3);
				}
			break;
		}
		return true;
	}

	public boolean onTouchEvent(android.view.MotionEvent MotionEvent)
	{
		float X = MotionEvent.getX();
		float Y = MotionEvent.getY();
		switch(MotionEvent.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(Math.sqrt(Math.pow((((Width / 2) - (0)) - (int) X), 2) + Math.pow((((Height / 2) - (0)) - (int) Y), 2)) <= 137)
				{
					if(MediaPlayer != null)
					{
						if(MediaPlayer.isPlaying())
							MediaPlayer.pause();
						else
							MediaPlayer.start();
					}
				}
				if(Math.sqrt(Math.pow(((0) - (int) X), 2) + Math.pow(((0) - (int) Y), 2)) <= 137)
				{
					Stop();
					System.exit(0);
				}
			break;
		}
		return false;
	}

	class AudioTrackPlayMusic
	{
		boolean Playing = false;
		boolean HasMusic = false;
		long StartMicroseconds;
		long EndMicroseconds;
		//采样率，声道数，时长，音频文件类型
		int SampleRate;
		int ChannelCount;
		long Duration;
		String Mime = null;
		//MediaExtractor, MediaFormat, MediaCodec
		android.media.MediaExtractor MediaExtractor;
		android.media.MediaFormat MediaFormat;
		android.media.MediaCodec MediaCodec;

		boolean DecodeInputEnd = false;
		boolean DecodeOutputEnd = false;
		//当前读取采样数据的大小
		int SampleDataSize;
		//当前输入数据的ByteBuffer序号，当前输出数据的ByteBuffer序号
		int InputBufferIndex;
		int OutputBufferIndex;
		//音频文件的采样位数字节数，= 采样位数/8
		int ByteNumber;
		//当前采样的音频时间，比如在当前音频的第40秒的时候
		long PresentationTimeUs;
		//定义编解码的超时时间
		long TimeOutUs;
		//存储输入数据的ByteBuffer数组，输出数据的ByteBuffer数组
		java.nio.ByteBuffer[] InputBuffers;
		java.nio.ByteBuffer[] OutputBuffers;
		//当前编解码器操作的 输入数据ByteBuffer 和 输出数据ByteBuffer，可以从targetBuffer中获取解码后的PCM数据
		java.nio.ByteBuffer SourceBuffer;
		java.nio.ByteBuffer TargetBuffer;
		//获取输出音频的媒体格式信息
		android.media.MediaFormat OutputFormat;
		android.media.MediaCodec.BufferInfo BufferInfo;

		public void Release()
		{
			DecodeOutputEnd = true;
			Playing = false;
			HasMusic = false;
		}

		public boolean decodeMusicFile(String MusicFileUrl, String DecodeFileUrl, android.content.Context Context)
		{
			DecodeInputEnd = false;
			DecodeOutputEnd = false;
			return decodeMusicFile(MusicFileUrl, DecodeFileUrl, 0, -1, Context);
		}
		/**
		 * 将音乐文件解码
		 * * @param musicFileUrl 源文件路径
		 * @param decodeFileUrl 解码文件路径
		 * @param startMicroseconds 开始时间 微秒
		 * @param endMicroseconds 结束时间 微秒
		 * @param decodeOperateInterface 解码过程回调 */
		private boolean decodeMusicFile(String MusicFileUrl, String DecodeFileUrl, long $StartMicroseconds, long $EndMicroseconds,android.content.Context Context)//, DecodeOperateInterface decodeOperateInterface)
		{
			StartMicroseconds = 0;
			EndMicroseconds = 0;
			//采样率，声道数，时长，音频文件类型
			SampleRate = 0;
			ChannelCount = 0;
			Duration = 0;
			Mime = null;
			//MediaExtractor, MediaFormat, MediaCodec
			MediaExtractor = new android.media.MediaExtractor();
			MediaFormat = null;
			MediaCodec = null;
			//给媒体信息提取器设置源音频文件路径
			try
			{
				if(new java.io.File(MusicFileUrl).isFile())
					MediaExtractor.setDataSource(MusicFileUrl);
				else
				{
					android.content.res.AssetManager AssetsManager = Context.getAssets();
					android.content.res.AssetFileDescriptor AssetsFileDescriptor;
					try
					{
						AssetsFileDescriptor = AssetsManager.openFd(MusicFileUrl);
						MediaExtractor.setDataSource(AssetsFileDescriptor.getFileDescriptor(), AssetsFileDescriptor.getStartOffset(), AssetsFileDescriptor.getLength());
					}
					catch (Exception E)
					{
						Toast(true, E);
						return false;
					}
				}
			}
			catch (Exception E)
			{
				Toast(true, E);
			}
			//获取音频格式轨信息
			MediaFormat = MediaExtractor.getTrackFormat(0);
			//从音频格式轨信息中读取 采样率，声道数，时长，音频文件类型
			SampleRate = MediaFormat.containsKey(android.media.MediaFormat.KEY_SAMPLE_RATE) ? MediaFormat.getInteger(android.media.MediaFormat.KEY_SAMPLE_RATE) : 44100;
			ChannelCount = MediaFormat.containsKey(android.media.MediaFormat.KEY_CHANNEL_COUNT) ? MediaFormat.getInteger(android.media.MediaFormat.KEY_CHANNEL_COUNT) : 1;
			Duration = MediaFormat.containsKey(android.media.MediaFormat.KEY_DURATION) ? MediaFormat.getLong(android.media.MediaFormat.KEY_DURATION) : 0;
			Mime = MediaFormat.containsKey(android.media.MediaFormat.KEY_MIME) ? MediaFormat.getString(android.media.MediaFormat.KEY_MIME) : "";
			android.widget.Toast.makeText(Context, "歌曲信息: \n音频类型：" + Mime + "\n采样率：" + SampleRate + "\n声道数：" + ChannelCount + "\n时长：" + Duration, 1).show();
			if (android.text.TextUtils.isEmpty(Mime) || !Mime.startsWith("audio/"))
			{
				Toast(false, "解码文件不是音频文件：" + Mime);
				return false;
			}
			Toast(false, "Mime：" + Mime);
			MediaFormat.setString(android.media.MediaFormat.KEY_MIME, Mime);
			if (Duration <= 0)
			{
				Toast(false, "音频文件 Duration 为：" + Duration);
				return false;
			}
			//解码的开始时间和结束时间
			EndMicroseconds = Duration;
			if (StartMicroseconds >= EndMicroseconds)
			{
				Toast(false, "Error Microseconds");
				return false;
			}
			//======创建 ======
			int Frequences = 0;
			if(Frequence <= 0)
				Frequences = SampleRate;
			else
				Frequences = Frequence;
			int BufferSize = android.media.AudioTrack.getMinBufferSize(Frequences, PlayChannelConfig, AudioEncoding);
			if(AudioTrack == null)
				AudioTrack = 
				//new android.media.AudioTrack(AudioManager.STREAM_MUSIC, Frequence, PlayChannelConfig, AudioEncoding, BufferSize, android.media.AudioTrack.MODE_STREAM);
				new android.media.AudioTrack(android.media.AudioManager.STREAM_MUSIC, Frequences, android.media.AudioFormat.CHANNEL_IN_STEREO, android.media.AudioFormat.ENCODING_PCM_16BIT, BufferSize, android.media.AudioTrack.MODE_STREAM);
			//======End======
			//创建一个解码器
			try
			{
				MediaCodec = android.media.MediaCodec.createDecoderByType(Mime);
				MediaCodec.configure(MediaFormat, null, null, 0);
			}
			catch (Exception E)
			{
				Toast(false, "解码器 Configure 出错");
				return false;
			}
			//得到输出PCM文件的路径
			//decodeFileUrl = decodeFileUrl.substring(0, decodeFileUrl.lastIndexOf("."));
			String PcmFilePath = DecodeFileUrl.substring(0, DecodeFileUrl.lastIndexOf(".")) + ".pcm";
			//android.widget.Toast.makeText(Context,pcmFilePath,0).show();

			//后续解码操作
			Toast(false, "以 " + Frequences + " 采样率开始调试解码");
			//初始化解码状态，未解析完成
			DecodeInputEnd = false;
			DecodeOutputEnd = false;
			//当前采样的音频时间，比如在当前音频的第40秒的时候
			PresentationTimeUs = 0;
			//定义编解码的超时时间
			TimeOutUs = 100;
			OutputFormat = MediaCodec.getOutputFormat();
			ByteNumber = (OutputFormat.containsKey("bit-width") ? OutputFormat.getInteger("bit-width") : 0) / 8;
			//开始解码操作
			MediaCodec.start();
			if(AudioTrack != null)
				AudioTrack.play();
			//获取存储输入数据的ByteBuffer数组，输出数据的ByteBuffer数组
			//android.widget.Toast.makeText(Context,":::",0).show();
			InputBuffers = MediaCodec.getInputBuffers();
			OutputBuffers = MediaCodec.getOutputBuffers();
			MediaExtractor.selectTrack(0);
			//当前解码的缓存信息，里面的有效数据在offset和offset+size之间
			BufferInfo = new android.media.MediaCodec.BufferInfo();
			//获取解码后文件的输出流
			//java.io.BufferedOutputStream BufferedOutputStream = null;
			try
			{
				//BufferedOutputStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(PcmFilePath/*DecodeFileUrl*/)));
			}
			catch (Exception E)
			{
				Toast(true, E);
				return false;
			}
			//FileFunction.getBufferedOutputStreamFromFile(decodeFileUrl);

			Playing = true;
			HasMusic = true;
			new java.lang.Thread(new java.lang.Runnable()
			{
				@Override
				public void run()
				{
					//开始进入循环解码操作，判断读入源音频数据是否完成，输出解码音频数据是否完成
					while (!DecodeOutputEnd)
					{
						if (!Playing)
							while(Playing == false)
								if(!HasMusic || DecodeOutputEnd)
									break;
						if (DecodeInputEnd)
							break;
						try
						{
							//操作解码输入数据
							//从队列中获取当前解码器处理输入数据的ByteBuffer序号
							InputBufferIndex = MediaCodec.dequeueInputBuffer(TimeOutUs);
							if (InputBufferIndex >= 0)
							{
								//取得当前解码器处理输入数据的ByteBuffer
								SourceBuffer = InputBuffers[InputBufferIndex];
								//获取当前ByteBuffer，编解码器读取了多少采样数据
								SampleDataSize = MediaExtractor.readSampleData(SourceBuffer, 0);
								//如果当前读取的采样数据<0，说明已经完成了读取操作
								if (SampleDataSize < 0)
								{
									DecodeInputEnd = true;
									SampleDataSize = 0;
								}
								else
								{
									PresentationTimeUs = MediaExtractor.getSampleTime();
								}
								//然后将当前ByteBuffer重新加入到队列中交给编解码器做下一步读取操作
								MediaCodec.queueInputBuffer(InputBufferIndex, 0, SampleDataSize, PresentationTimeUs, DecodeInputEnd ? android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
								//前进到下一段采样数据
								if (!DecodeInputEnd)
								{
									MediaExtractor.advance();
								}
							}
							else
							{
								//Toast(false, "inputBufferIndex" + inputBufferIndex);
							}
							//操作解码输出数据
							//从队列中获取当前解码器处理输出数据的ByteBuffer序号
							OutputBufferIndex = MediaCodec.dequeueOutputBuffer(BufferInfo, TimeOutUs);
							if (OutputBufferIndex < 0)
							{
								//输出ByteBuffer序号<0，可能是输出缓存变化了，输出格式信息变化了
								switch (OutputBufferIndex)
								{
									case android.media.MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED: OutputBuffers = MediaCodec.getOutputBuffers();
										//android.util.Log.e("", "MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED [AudioDecoder]output buffers have changed.");
									break;
									case android.media.MediaCodec.INFO_OUTPUT_FORMAT_CHANGED: OutputFormat = MediaCodec.getOutputFormat();
										SampleRate = OutputFormat.containsKey(android.media.MediaFormat.KEY_SAMPLE_RATE) ? OutputFormat.getInteger(android.media.MediaFormat.KEY_SAMPLE_RATE) : SampleRate;
										ChannelCount = OutputFormat.containsKey(android.media.MediaFormat.KEY_CHANNEL_COUNT) ? OutputFormat.getInteger(android.media.MediaFormat.KEY_CHANNEL_COUNT) : ChannelCount;
										ByteNumber = (OutputFormat.containsKey("bit-width") ? OutputFormat.getInteger("bit-width") : 0) / 8;
										//Toast(false, "MediaCodec.INFO_OUTPUT_FORMAT_CHANGED [AudioDecoder]output format has changed to " + mediaCodec.getOutputFormat());
									break;
									default:
										//Toast(false, "error [AudioDecoder] dequeueOutputBuffer returned " + outputBufferIndex);
									break;
								}
								continue;
							}
							//取得当前解码器处理输出数据的ByteBuffer
							TargetBuffer = OutputBuffers[OutputBufferIndex];
							byte[] SourceByteArray = new byte[BufferInfo.size];
							//将解码后的TargetBuffer中的数据复制到SourceByteArray中
							TargetBuffer.get(SourceByteArray);
							TargetBuffer.clear();
							//释放当前的输出缓存
							MediaCodec.releaseOutputBuffer(OutputBufferIndex, false);
							//判断当前是否解码数据全部结束了
							if ((BufferInfo.flags & android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)
								DecodeOutputEnd = true;
							//sourceByteArray就是最终解码后的采样数据
							//接下来可以对这些数据进行采样位数，声道的转换，但这是可选的，默认是和源音频一样的声道和采样位数
							if (SourceByteArray.length > 0 && /*BufferedOutputStream*/AudioTrack != null)
							{
								if (PresentationTimeUs < StartMicroseconds)
									continue;
								//采样位数转换，按自己需要是否实现
								//byte[] convertByteNumberByteArray = convertByteNumber(byteNumber, Constant.ExportByteNumber, sourceByteArray);
								//声道转换，按自己需要是否实现
								//byte[] resultByteArray = convertChannelNumber(channelCount, Constant.ExportChannelNumber, Constant.ExportByteNumber, convertByteNumberByteArray);
								//将解码后的PCM数据写入到PCM文件
								try
								{
									AudioTrack.write(SourceByteArray, 0, SourceByteArray.length);
									if(Visualizer == null)
										setVisualizer();
									else if(!Visualizer.getEnabled())
										Visualizer.setEnabled(true);
									//BufferedOutputStream.write(SourceByteArray);
									//bufferedOutputStream.write(resultByteArray);
								}
								catch (Exception E)
								{
									Toast(false, "输出解压音频数据异常：" + E);
									return ;
								}
							}
							if (PresentationTimeUs > EndMicroseconds)
							{
								break;
							}
						}
						catch (Exception E)
						{
							Toast(false, "getDecodeData 异常：" + E);
							return ;
						}
					}

					if (/*BufferedOutputStream*/AudioTrack != null && Playing)
					{
						try
						{
							AudioTrack.stop();
							AudioTrack.release();
							HasMusic = false;
							Playing = false;
							//BufferedOutputStream.close();
						}
						catch (Exception E)
						{
							Toast(false, "释放内存异常：" + E);
							return;
						}
					}
					//重置采样率，按自己需要是否实现
					/*if (sampleRate != Constant.ExportSampleRate)
					{
						Resample(sampleRate, decodeFileUrl);
					}
					notifyProgress(decodeOperateInterface, 100);*/
					//释放mediaCodec 和 mediaExtractor
					if (MediaCodec != null)
					{
						MediaCodec.stop();
						MediaCodec.release();
					}
					if (MediaExtractor != null)
					{
						MediaExtractor.release();
					}
				}
			}).start();
			return true;
		}
	}
}
