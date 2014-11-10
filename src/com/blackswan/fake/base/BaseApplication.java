package com.blackswan.fake.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.blackswan.fake.R;
import com.blackswan.fake.activity.BaiduMapActivity;
import com.blackswan.fake.activity.BarberActivity;
import com.blackswan.fake.activity.BarberShopActivity;
import com.blackswan.fake.adapter.BarberListAdapter;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.bean.NearBarber;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.FileUtils;
import com.blackswan.fake.util.LBSLocation;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApplication extends Application {
	private Bitmap mDefaultAvatar;
	public String mCurrentcity;
	public SharedPreferences preferences;
	SharedPreferences.Editor editor;  
    Context context;
    private static BaseApplication mInstance = null;
    public static final String strKey = "O65sXI7ksbsVeaMXI7aF94gf";

	// 定位结果
	public BDLocation currlocation = null;

	// 周边理发店检索结果
	private List<NearBarberShop> barbershops = new ArrayList<NearBarberShop>();
	
	// 周边美发师检索结果
	private List<NearBarber> barbers = new ArrayList<NearBarber>();

	// 用于更新检索结果后，刷新列表
	private BarbershopListAdapter  barbershopListAdapter;
	private BarberListAdapter  barberadapter;
	
	public static String networkType;

	private Handler handler;
	
	//云检索参数
	private HashMap<String, String> filterParams;
	
	private BarberShopActivity barberShopActivity;
	private BarberActivity barberActivity;
	private BaiduMapActivity baiduMapActivity;
	
	public static List<String> mEmoticons = new ArrayList<String>();
	public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
	public static List<String> mEmoticons_Zem = new ArrayList<String>();
	public static List<String> mEmoticons_Zemoji = new ArrayList<String>();

	public List<NearBarberShop> getBarbershops() {
		return barbershops;
	}

	public void setBarbershops(List<NearBarberShop> barbershops) {
		this.barbershops = barbershops;
	}

	public List<NearBarber> getBarbers() {
		return barbers;
	}

	public void setBarbers(List<NearBarber> barbers) {
		this.barbers = barbers;
	}

	public BarbershopListAdapter getBarbershopListAdapter() {
		return barbershopListAdapter;
	}

	public void setBarbershopListAdapter(BarbershopListAdapter barbershopListAdapter) {
		this.barbershopListAdapter = barbershopListAdapter;
	}

	public BarberListAdapter getBarberadapter() {
		return barberadapter;
	}

	public void setBarberadapter(BarberListAdapter barberadapter) {
		this.barberadapter = barberadapter;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public HashMap<String, String> getFilterParams() {
		return filterParams;
	}

	public void setFilterParams(HashMap<String, String> filterParams) {
		this.filterParams = filterParams;
	}

	public BarberShopActivity getBarberShopActivity() {
		return barberShopActivity;
	}

	public void setBarberShopActivity(BarberShopActivity barberShopActivity) {
		this.barberShopActivity = barberShopActivity;
	}

	public BarberActivity getBarberActivity() {
		return barberActivity;
	}

	public void setBarberActivity(BarberActivity barberActivity) {
		this.barberActivity = barberActivity;
	}

	public BaiduMapActivity getBaiduMapActivity() {
		return baiduMapActivity;
	}

	public void setBaiduMapActivity(BaiduMapActivity baiduMapActivity) {
		this.baiduMapActivity = baiduMapActivity;
	}

	public static BaseApplication getmInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		mDefaultAvatar = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_common_def_header);
		for (int i = 1; i < 64; i++) {
			String emoticonsName = "[zem" + i + "]";
			int emoticonsId = getResources().getIdentifier("zem" + i,
					"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zem.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		for (int i = 1; i < 59; i++) {
			String emoticonsName = "[zemoji" + i + "]";
			int emoticonsId = getResources().getIdentifier("zemoji_e" + i,
					"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zemoji.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		
		//创建系统用SharedPrefernces文件
		preferences = getSharedPreferences("fake", MODE_PRIVATE);
		//初始化 图片引擎
		initImageLoader(getApplicationContext());
		networkType = setNetworkType();
		// 启动定位
		LBSLocation.getInstance(this).startLocation();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.e("BaseApplication", "onLowMemory");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.e("BaseApplication", "onTerminate");
	}

	/**
	 * 设置手机网络类型，wifi，cmwap，ctwap，用于联网参数选择
	 * @return
	 */
	static String setNetworkType() {
		String networkType = "wifi";
		ConnectivityManager manager = (ConnectivityManager) mInstance
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			// 当前网络不可用
			return "";
		}

		String info = netWrokInfo.getExtraInfo();
		if ((info != null)
				&& ((info.trim().toLowerCase().equals("cmwap"))
						|| (info.trim().toLowerCase().equals("uniwap"))
						|| (info.trim().toLowerCase().equals("3gwap")) || (info
						.trim().toLowerCase().equals("ctwap")))) {
			// 上网方式为wap
			if (info.trim().toLowerCase().equals("ctwap")) {
				// 电信
				networkType = "ctwap";
			} else {
				networkType = "cmwap";
			}

		}
		return networkType;
	}
	public void callStatistics(){
		StatisticsTask task = new StatisticsTask(); 
		task.execute("http://api.map.baidu.com/images/blank.gif?t=92248538&platform=android&logname=lbsyunduanzu");
	}

	/*
	 * 百度统计
	 */
	class StatisticsTask extends AsyncTask<String, Integer, String> { 
		 
        // 可变长的输入参数，与AsyncTask.exucute()对应 
        @Override 
        protected String doInBackground(String... params) { 
            try { 
                HttpClient client = new DefaultHttpClient(); 
                // params[0] 代表连接的url 
                HttpGet get = new HttpGet(params[0]); 
                HttpResponse response = client.execute(get); 
                
				int status = response.getStatusLine().getStatusCode();
                if(status == HttpStatus.SC_OK){
                }
                // 返回结果 
                return null; 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            return null; 
        } 
        @Override 
        protected void onCancelled() { 
            super.onCancelled(); 
        } 
        @Override 
        protected void onPostExecute(String result) { 
        } 
        @Override 
        protected void onPreExecute() { 
        } 
        @Override 
        protected void onProgressUpdate(Integer... values) { 
        } 
    }
	
	//向sharedPreferences文件添加字符串数据
	public void putString(String key, String value){  
        editor = preferences.edit();  
        editor.putString(key, value);  
        editor.commit();  
    }
	
	//向sharedPreferences文件添加整型数据
	public void putInt(String key,int value) {
		editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	//向sharedPreferences文件添长整型数据
	public void putLong(String key,Long value) {
		editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	//向sharedPreferences文件添加布尔数据
	public void putBoolean(String key,Boolean value) {
		editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	//向sharedPreferences文件添加浮点数数据
	public void putFloat(String key,Float value) {
		editor = preferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	//初始化图片加载引擎
	private static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
//				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
//				.build();
//		
		int memoryCacheSize = 2 * 1024 * 1024;
		int diskcacheSize = 50 * 1024 * 1024;	// 50 Mb
		LruDiscCache diskcache = null;
		try {
			diskcache = new LruDiscCache(FileUtils.getDiskCacheDir(context,
					"bitmap"), new HashCodeFileNameGenerator(), diskcacheSize);

		} catch (IOException e) {
			e.printStackTrace();
			diskcacheSize = 0;
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				//.memoryCacheExtraOptions(480, 180)
				// default = device screen
				// dimensions
				//.diskCacheExtraOptions(480, 180, null)
				// .taskExecutor()
				// .taskExecutorForCachedImages(...)
				//.threadPoolSize(2)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// default
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(memoryCacheSize))
				.memoryCacheSize(memoryCacheSize)
				// .memoryCacheSizePercentage(1)
				// default
				.diskCache(diskcache)
				// default
				.diskCacheSize(diskcacheSize)
				//.diskCacheFileCount(IMAGE_COUNT)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				//.imageDownloader(new BaseImageDownloader(context)) // default
				//.imageDecoder(new BaseImageDecoder(false)) // default
				//.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}

