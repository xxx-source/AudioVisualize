package com.musicshow.audiofx;

public class MainActivity extends android.app.Activity implements android.view.View.OnClickListener, android.widget.SeekBar.OnSeekBarChangeListener, android.widget.CompoundButton.OnCheckedChangeListener, android.widget.RadioGroup.OnCheckedChangeListener
{
	private android.widget.EditText Path;
	private android.widget.TextView ShowFrameRate;
	private Fft Fft = null;
	private android.view.WindowManager WindowManager = null;
	private int CreateScreen = -1;

	@Override
	protected void onCreate(android.os.Bundle Bundle)
	{
		super.onCreate(Bundle);
		setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.main_activity);
		try
		{
			if(!LocalDataUtil.getBooleanData(MainActivity.this, "FftSetting", "Fft"))
			{
				LocalDataUtil.ClearData(MainActivity.this, "FftSetting");
				LocalDataUtil.putBooleanData(MainActivity.this, "FftSetting", "Fft", true);
			}
			//======Start 申请权限======
			if(android.os.Build.VERSION.SDK_INT >= 23)
				AskPermission();
			if (android.os.Build.VERSION.SDK_INT >= 29)
				startActivity(new android.content.Intent().setClassName("com.android.systemui", "com.android.systemui.media.MediaProjectionPermissionActivity"));
			//======End======
			//======Start 获取控件以及设置事件======
			Path = findViewById(R.id.Path);
			ShowFrameRate = findViewById(R.id.ShowFrameRate);
			((android.view.View) findViewById(R.id.Get)).setOnClickListener(this);
			((android.view.View) findViewById(R.id.Pause)).setOnClickListener(this);
			((android.view.View) findViewById(R.id.Start)).setOnClickListener(this);
			((android.view.View) findViewById(R.id.Rest)).setOnClickListener(this);
			((android.widget.RadioGroup) findViewById(R.id.RadioGroup)).setOnCheckedChangeListener(this);
			((android.widget.SeekBar) findViewById(R.id.FrameRateControl)).setOnSeekBarChangeListener(this);
			((android.widget.CompoundButton) findViewById(R.id.ShowFPS)).setOnCheckedChangeListener(this);
			((android.widget.CompoundButton) findViewById(R.id.ShowInformation)).setOnCheckedChangeListener(this);
			((android.widget.CompoundButton) findViewById(R.id.CycleColor)).setOnCheckedChangeListener(this);
			((android.widget.CompoundButton) findViewById(R.id.OpenCycleColorForWave)).setOnCheckedChangeListener(this);
			((android.widget.CompoundButton) findViewById(R.id.EnableSurfaceView)).setOnCheckedChangeListener(this);
			//======End======

			//======Start 创建 Fft 视图======
			boolean Check = false;
			if(CreateScreen == -1)
			{
				Fft = new Fft();
				int DrawMode = LocalDataUtil.getIntegerData(MainActivity.this, "FftSetting", "EnableSurfaceView");
				if(DrawMode == 1)
					Check = true;
				else if(DrawMode < 0 && android.os.Build.VERSION.SDK_INT >= 23)
					Check = true;
				((android.widget.CompoundButton) findViewById(R.id.EnableSurfaceView)).setChecked(Check);
				if(WindowManager == null)
					CreateWindow(Check);
				CreateScreen = 1;
			}
			//======End======
			//======Start 设置 Fft 属性======
			Check = false;
			if(LocalDataUtil.getBooleanData(MainActivity.this, "FftSetting", "ShowFPS") == true)
				Check = true;
			((android.widget.CompoundButton) findViewById(R.id.ShowFPS)).setChecked(Check);
			Check = false;
			if(LocalDataUtil.getBooleanData(MainActivity.this, "FftSetting", "ShowInformation") == true)
				Check = true;
			((android.widget.CompoundButton) findViewById(R.id.ShowInformation)).setChecked(Check);
			Check = false;
			if(LocalDataUtil.getBooleanData(MainActivity.this, "FftSetting", "CycleColor") == true)
				Check = true;
			((android.widget.CompoundButton) findViewById(R.id.CycleColor)).setChecked(Check);
			Check = false;
			if(LocalDataUtil.getBooleanData(MainActivity.this, "FftSetting", "CycleColorForWave") == true)
				Check = true;
			((android.widget.CompoundButton) findViewById(R.id.OpenCycleColorForWave)).setChecked(Check);
			int FftDrawWaveMode = LocalDataUtil.getIntegerData(MainActivity.this, "FftSetting", "FftDrawWaveMode");
			int Id = R.id.WaveTH;
			switch(FftDrawWaveMode)
			{
				case 0:
					Id = R.id.WaveZ;
				break;
				case 1:
					Id = R.id.WaveO;
				break;
				case 2:
					Id = R.id.WaveT;
				break;
				case 3:
					Id = R.id.WaveTH;
				break;
				case -1:
					Id = R.id.WaveTH;
				break;
			}
			((android.widget.RadioButton) findViewById(Id)).setChecked(true);
			int Progress = LocalDataUtil.getIntegerData(MainActivity.this, "FftSetting", "FrameRate");
			if(Progress < 60)
				Progress = 60;
			if(Progress > 120)
				Progress = 120;
			((android.widget.SeekBar) findViewById(R.id.FrameRateControl)).setProgress(Progress);
			ShowFrameRate.setText("帧率：" + Progress);
			Fft.FftDrawWaveMode(FftDrawWaveMode);
			Fft.SetFPS(Progress);
			//======End======
		}
		catch(Exception E)
		{
			E.printStackTrace();
			android.widget.Toast.makeText(getApplicationContext(), E.getMessage(), 0).show();
		}
	}

	private android.view.WindowManager CreateWindow(android.view.WindowManager $WindowManager, android.content.Context Context, android.view.View View, int Width, int Height, int X, int Y)
	{
		if($WindowManager != null)
			WindowManager = $WindowManager;
		else
			WindowManager = (android.view.WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
		android.view.WindowManager.LayoutParams WindowManagerParams = new android.view.WindowManager.LayoutParams();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
			WindowManagerParams.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		else
			WindowManagerParams.type = 2006;
		WindowManagerParams.format = android.graphics.PixelFormat.RGBA_8888;
		WindowManagerParams.flags = android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | 824;
		WindowManagerParams.width = Width;
		WindowManagerParams.height = Height;
		WindowManagerParams.x = X;
		WindowManagerParams.y = Y;
		View.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
		WindowManager.addView(View, WindowManagerParams);
		return WindowManager;
	}

	private void RemoveWindowView()
	{
		if(Fft != null && WindowManager != null)
		{
			android.view.View View = Fft.getView();
			android.view.SurfaceView SurfaceView = Fft.getSurfaceView();
			if(View != null)
				WindowManager.removeView(View);
			if(SurfaceView != null)
				WindowManager.removeView(SurfaceView);
			Fft.RemoveAllView();
		}
	}

	private void CreateWindow(boolean IsChecked)
	{
		if(IsChecked)
		{
			LocalDataUtil.putIntegerData(MainActivity.this, "FftSetting", "EnableSurfaceView", 1);
			if(WindowManager == null)
				WindowManager = CreateWindow(null, getApplicationContext(), Fft.getSurfaceView(MainActivity.this), -1, -1, 0, 0);
			else
				CreateWindow(WindowManager, getApplicationContext(), Fft.getSurfaceView(MainActivity.this), -1, -1, 0, 0);
		}
		else
		{
			LocalDataUtil.putIntegerData(MainActivity.this, "FftSetting", "EnableSurfaceView", 0);
			if(WindowManager == null)
				WindowManager = CreateWindow(null, getApplicationContext(), Fft.getView(MainActivity.this), -1, -1, 0, 0);
			else
				CreateWindow(WindowManager, getApplicationContext(), Fft.getView(MainActivity.this), -1, -1, 0, 0);
		}
	}

	@Override
	public boolean onKeyDown(int KeyCode,android.view.KeyEvent KeyEvent)
	{
		Fft.onKeyDown(KeyCode, KeyEvent);
		return true;
	}

	@Override
	public void onCheckedChanged(android.widget.CompoundButton Button, boolean IsChecked)
	{
		if(Fft != null)
			switch(Button.getId())
			{
				case R.id.ShowFPS:
					if(Fft.IsInit())
						Fft.ShowFPS(IsChecked);
					LocalDataUtil.putBooleanData(MainActivity.this, "FftSetting", "ShowFPS", IsChecked);
				break;
				case R.id.ShowInformation:
						Fft.ShowInformation(IsChecked);
					LocalDataUtil.putBooleanData(MainActivity.this, "FftSetting", "ShowInformation", IsChecked);
					break;
				case R.id.CycleColor:
					Fft.OpenCycleColor(IsChecked);
					LocalDataUtil.putBooleanData(MainActivity.this, "FftSetting", "CycleColor", IsChecked);
				break;
				case R.id.OpenCycleColorForWave:
					Fft.OpenCycleColorForWave(IsChecked);
					LocalDataUtil.putBooleanData(MainActivity.this, "FftSetting", "CycleColorForWave", IsChecked);
				break;
				case R.id.EnableSurfaceView:
					if(WindowManager != null)
						RemoveWindowView();
					CreateWindow(IsChecked);
				break;
			}
	}

	@Override
	public void onCheckedChanged(android.widget.RadioGroup Group, int CheckedId)
	{
		int Mode = 0;
		if(Fft != null && Fft.IsInit())
			switch(CheckedId)
			{
				case R.id.WaveZ:
					Mode = 0;
				break;
				case R.id.WaveO:
					Mode = 1;
				break;
				case R.id.WaveT:
					Mode = 2;
				break;
				case R.id.WaveTH:
					Mode = 3;
				break;
			}
		Fft.FftDrawWaveMode(Mode);
		LocalDataUtil.putIntegerData(MainActivity.this, "FftSetting", "FftDrawWaveMode", Mode);
	}

	public void onClick(android.view.View View)
	{
		switch(View.getId())
		{
			case R.id.Get:
				android.app.AlertDialog.Builder AlertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
				AlertDialog.setTitle("关于");
				AlertDialog.setMessage("By：NTX\n\n\n本软件的开发参考自 @啊哈hi 的开源作品：Audio-V \n\nAudio-V 的开源链接：\nhttps://github.com/EdrowsLuo/audio-v \nhttps://github.com/EdrowsLuo/audio-v/releases");
				AlertDialog.setPositiveButton("确定", null);
				AlertDialog.setCancelable(true);
				AlertDialog.create();
				AlertDialog.show();
				displayBriefMemory();
			break;
			case R.id.Pause:
				if(Fft != null)
					Fft.Pause();
			break;
			case R.id.Start:
				if(Fft != null)
					Fft.Start();
			break;
			case R.id.Rest:
				if(Fft != null && !Path.getText().toString().equals(""))
					if(new java.io.File(Path.getText().toString()).isFile())
						//Fft.LoaderMusic("MediaPlayer", Path.getText().toString());
						Fft.PlayTask(Path.getText().toString());
			break;
		}
	}

	//当用户对拖动条的拖动的动作完成时触发
	@Override
	public void onStopTrackingTouch(android.widget.SeekBar SeekBar)
	{
		int FrameRate = SeekBar.getProgress();
		if(FrameRate > 120)
			FrameRate = 120;
		else if(FrameRate < 60)
		{
			FrameRate = 60;
			SeekBar.setProgress(60);
		}
		ShowFrameRate.setText("帧率：" + FrameRate);
		LocalDataUtil.putIntegerData(MainActivity.this, "FftSetting", "FrameRate", FrameRate);
	}
	//当用户对拖动条进行拖动时触发
	@Override
	public void onStartTrackingTouch(android.widget.SeekBar SeekBar) {}
	//当拖动条的值发生改变的时触发
	@Override
	public void onProgressChanged(android.widget.SeekBar SeekBar, int Progress, boolean FromUser)
	{
		switch(SeekBar.getId())
		{
			case R.id.FrameRateControl:
				int FrameRate = Progress;
				if(FrameRate > 120)
					FrameRate = 120;
				else if(FrameRate < 60)
				{
					FrameRate = 60;
					SeekBar.setProgress(60);
				}
				ShowFrameRate.setText("帧率：" + FrameRate);
				if(Fft != null)
					Fft.SetFPS(FrameRate);
			break;
		}
	}

	java.util.List<String> Permissions = new java.util.ArrayList<String>();
	private boolean AskPermission()
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
		{
			String[] Permission = new String[]
			{
				android.Manifest.permission.SYSTEM_ALERT_WINDOW,
				android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
				android.Manifest.permission.PERSISTENT_ACTIVITY,
				android.Manifest.permission.READ_EXTERNAL_STORAGE,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE
			};
			for(String Index : Permission)
			{
				int Granted = checkSelfPermission(Index);
				if(Granted != android.content.pm.PackageManager.PERMISSION_GRANTED)
					Permissions.add(Index);
			}
			if (!Permissions.isEmpty())
				requestPermissions(Permissions.toArray(new String[Permissions.size()]), 666);
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int RequestCode, String[] Permissions, int[] GrantResults)
	{
		if (RequestCode == 666)
		{
			for (int Index = 0; Index < GrantResults.length; Index++)
			{
				if(GrantResults[Index] == android.content.pm.PackageManager.PERMISSION_GRANTED)
				{
					android.widget.Toast.makeText(MainActivity.this, "有以下权限还未授权：" + Permissions[Index], 0).show();
					AskPermission();
				}
			}
		}
		super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);
	}

	private void displayBriefMemory()
	{
		final android.app.ActivityManager ActivityManager = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
		android.app.ActivityManager.MemoryInfo info = new android.app.ActivityManager.MemoryInfo();
		ActivityManager.getMemoryInfo(info);
		StringBuffer StringBuffer = new StringBuffer("");
		StringBuffer.append("系统剩余内存：" + ((info.availMem >> 10) / 1024) + " MB\n");
		StringBuffer.append("系统是否处于低内存运行：" + info.lowMemory + "\n");
		StringBuffer.append("当系统剩余内存低于：" + (info.threshold / 1024 / 1024) + " MB 时就将设备看成处于低内存运行状态");
		android.widget.Toast.makeText(getApplicationContext(), StringBuffer.toString(), 1).show();
		StringBuffer = null;
	}
}
