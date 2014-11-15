package com.blackswan.fake.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.barberactivity.BarberListActivity;
//import com.blackswan.fake.activity.BaiduMapActivity;
import com.blackswan.fake.activity.barbershopactivity.BarberShopListActivity;
import com.blackswan.fake.adapter.BarberListAdapter;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.MyRegion;
import com.blackswan.fake.bean.NearBarber;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.LBSCloudSearch;
import com.blackswan.fake.util.PopCityUtils;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class HomePageActivity extends ActivityGroup implements OnClickListener
{
	//处理主页中几个activity之间的切换
	private TabHost tabHost;
	private long exitTime = 0;
	View  cityView;
	TextView city;
	ImageView researchImageView;
	ImageView mapImageView;
	
	//处理筛选条件选择
	public static final int MSG_DISTRICT = 3;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;
	private TextView text5;
	private TextView text6;
	private RelativeLayout barbershopfilter;
	private RelativeLayout barberfilter;
	private ArrayList<MyRegion> regions;
	private PopCityUtils cityUtils;
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	private ArrayList<HashMap<String,Object>> itemList;
	private LinearLayout linLayout;
	
	//处理网络请求
	private Context context;
	public static final int MSG_NET_TIMEOUT = 100;
	public static final int MSG_NET_STATUS_ERROR = 200;
	public static final int MSG_NET_SUCC = 1;
	public static final int RESEAR_BARBER = 88;
	public static final int RESEAR_BARBERSHOP = 99;
	private boolean initSearchFlag = false;
	private RelativeLayout progress;
	
	//获取云端美发师数据
	private final Handler mBHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progress.setVisibility(View.INVISIBLE);
			switch (msg.what) {
			case MSG_NET_TIMEOUT:
				break;
			case MSG_NET_STATUS_ERROR:
				break;
			case MSG_NET_SUCC:
				initSearchFlag = true;
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					Log.i("返回美发师", "内容："+json.toString());
					parserBarber(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			}
		}
	};
	
	//获取云端理发店数据
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progress.setVisibility(View.INVISIBLE);
			switch (msg.what) {
			case MSG_NET_TIMEOUT:
				break;
			case MSG_NET_STATUS_ERROR:
				break;
			case MSG_NET_SUCC:
				initSearchFlag = true;
				String shopresult = msg.obj.toString();
				try {
					JSONObject shopjson = new JSONObject(shopresult);
					Log.i("返回理发店", "内容："+shopjson.toString());
					parserBarberShop(shopjson);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			}
		}
	};

	/*
	 * 初始化城市列表
	 */
	private final Handler handler = new Handler()
	{
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DISTRICT:
				regions = (ArrayList<MyRegion>) msg.obj;
				break;
			}
		}
		
	};
	
	
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		context = this;
		setContentView(R.layout.activity_homepage);
		tabHost = (TabHost) findViewById(R.id.homepagetabHost);
		tabHost.setup(this.getLocalActivityManager());;
		TabWidget tabWidget = tabHost.getTabWidget();
		TabSpec tab1 = tabHost.newTabSpec("barber");
        tab1.setIndicator(createContent("发型师",R.drawable.toplabelleft));
        tab1.setContent(new Intent(this,BarberListActivity.class));
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("barbershop");
        tab2.setIndicator(createContent("理发店",R.drawable.toplabelright));
        tab2.setContent(new Intent(this,BarberShopListActivity.class));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
        initViews();
        initEvent();
        
        //请求网络搜索
        BaseApplication.setNetworkType();
        searchBarber(LBSCloudSearch.SEARCH_TYPE_LOCAL);
		BaseApplication.getmInstance().setHandler(mBHandler);
	}
	//注入界面控件
	protected void initViews() {
		cityView = (View)findViewById(R.id.ll_city);
		city = (TextView) findViewById(R.id.tv_city);
		researchImageView = (ImageView)findViewById(R.id.homepageresearch);
		mapImageView = (ImageView) findViewById(R.id.homepagemap);
		barberfilter = (RelativeLayout) findViewById(R.id.barberfilter);
		barbershopfilter = (RelativeLayout) findViewById(R.id.barbershopfilter);
		linLayout = (LinearLayout) findViewById(R.id.ll_homepage);
		text1 = (TextView) findViewById(R.id.barber_text1);
		text2 = (TextView) findViewById(R.id.barber_text2);
		text3 = (TextView) findViewById(R.id.barber_text3);
		text4 = (TextView) findViewById(R.id.barbershop_text1);
		text5 = (TextView) findViewById(R.id.barbershop_text2);
		text6 = (TextView) findViewById(R.id.barbershop_text3);
		text4.setVisibility(View.INVISIBLE);
		text5.setVisibility(View.INVISIBLE);
		text6.setVisibility(View.INVISIBLE);
		barbershopfilter.setVisibility(View.INVISIBLE);
		progress = (RelativeLayout) findViewById(R.id.progress);
	}
	
	//为控件添加事件处理
	protected void initEvent() {
		cityView.setOnClickListener(this);
		BaseApplication application = ((BaseApplication) getApplication());
		//设置首页城市
		String historycity=application.preferences.getString("city3", null);
        if (historycity==null) {
        	if (application.mCurrentcity==null) {
				city.setText("城市");
			}else {
				application.putString("city3", application.mCurrentcity);
				city.setText(application.mCurrentcity);
			}
		}else {
			city.setText(historycity);
		}
        //初始化PopWindows城市列表
        String cname=application.preferences.getString("city3", null);
		if (cname == null) {
			cname = application.mCurrentcity;
		}
        Log.i("初始化城市列表","选中的城市是"+cname);
		cityUtils = new PopCityUtils(this, handler);
		cityUtils.initDistricts(cname);
		researchImageView.setOnClickListener(this);
		mapImageView.setOnClickListener(this);
		text1.setOnClickListener(this);
		text2.setOnClickListener(this);
		text3.setOnClickListener(this);
		text4.setOnClickListener(this);
		text5.setOnClickListener(this);
		text6.setOnClickListener(this);
		// 设置tabHost切换时动态更改图标
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
					tabChanged(tabId);
				}

			});		
	}
	
	//设置tab按钮的文字及背景
	private View createContent(String text, int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.toptabwidget, null,
				false);
		TextView tv_name = (TextView) view.findViewById(R.id.toptabwidgettext);
		tv_name.setText(text);
		tv_name.setBackgroundResource(resid);
		return view;
	}
	
	// 捕获tab变化事件
	private void tabChanged(String tabId) {
		// 当前选中项
		if (tabId.equals("barbershop")) {
			tabHost.setCurrentTabByTag("理发店");
			text1.setVisibility(View.INVISIBLE);
			text2.setVisibility(View.INVISIBLE);
			text3.setVisibility(View.INVISIBLE);
			barberfilter.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.VISIBLE);
			text5.setVisibility(View.VISIBLE);
			text6.setVisibility(View.VISIBLE);
			barbershopfilter.setVisibility(View.VISIBLE);
			//发起对理发店的检索
			searchBarberShop(LBSCloudSearch.SEARCH_TYPE_LOCAL);
			BaseApplication.getmInstance().setHandler(mHandler);
		} else if (tabId.equals("barber")) {
			tabHost.setCurrentTabByTag("发型师");
			text1.setVisibility(View.VISIBLE);
			text2.setVisibility(View.VISIBLE);
			text3.setVisibility(View.VISIBLE);
			barberfilter.setVisibility(View.VISIBLE);
			text4.setVisibility(View.INVISIBLE);
			text5.setVisibility(View.INVISIBLE);
			text6.setVisibility(View.INVISIBLE);
			barbershopfilter.setVisibility(View.INVISIBLE);
			searchBarber(LBSCloudSearch.SEARCH_TYPE_LOCAL);
			BaseApplication.getmInstance().setHandler(mBHandler);
		}
	}
		
	@Override
	protected void onResume() {
		// 重新显示主页时重置城市
		BaseApplication application = ((BaseApplication) getApplication());
		String historycity=application.preferences.getString("city3", null);
	       if (historycity==null) {
	    	   if (application.mCurrentcity==null) {
					city.setText("城市");
				}else {
					application.putString("city3", application.mCurrentcity);
					city.setText(application.mCurrentcity);
				}
			}else {
				city.setText(historycity);
			}
	        //重置PopWindows城市列表
	        String cname=application.preferences.getString("city3", null);
			if (cname == null) {
				cname = application.mCurrentcity;
			}
	        Log.i("重置城市列表","新选中的城市是"+cname);
			cityUtils = new PopCityUtils(this, handler);
			cityUtils.initDistricts(cname);
			super.onResume();
		}
	
	//处理返回键消息
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//双击返回键退出系统
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出剃头",
				Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
		
		}
		
	//响应点击事件	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homepageresearch:
			Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.homepagemap:
			Intent intent2 = new Intent(HomePageActivity.this,BaiduMapActivity.class);
			startActivity(intent2);
			break;
		case R.id.ll_city:
			Intent intent3 = new Intent(HomePageActivity.this,SelectCityActivity.class);
			startActivity(intent3);
			break;
		case R.id.barber_text1:
			showBarberStarPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		case R.id.barber_text2:
			showBCityPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		case R.id.barber_text3:
			showBDistancePopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		case R.id.barbershop_text1:
			showStarPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		case R.id.barbershop_text2:
			showBSCityPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		case R.id.barbershop_text3:
			showBSDistancePopupWindow(linLayout.getWidth(),linLayout.getHeight());
			break;
		default:
			break;
			}
		}
	
	//弹出美发师评价排序条件
	@SuppressLint("InflateParams") 
	protected void showBarberStarPopupWindow(int width, int height) {
		String title = "按评分排序";
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		HashMap<String,Object> items = new HashMap<String,Object>();
		items.put("name",title);
		items.put("count", 1);
		itemList.add(items);
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		mPopWin = new PopupWindow(layout, width * 2/5, height *1/7, true);
		mPopWin.showAtLocation(layout, Gravity.LEFT,0,-198);
		mPopWin.showAsDropDown(text1, 4, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								text1.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarber(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
		});
	}	
	
	//弹出美发师城市区域条件选项
	@SuppressLint("InflateParams")
	private void showBCityPopupWindow(int width, int height) {
		
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<regions.size();i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", regions.get(i).getName());
			items.put("count", i);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		flChild = (FrameLayout) layout.findViewById(R.id.child_lay);
		childList = (ListView) layout.findViewById(R.id.childcategory);
		childList.setAdapter(cla);
		flChild.setVisibility(View.INVISIBLE);
		
		mPopWin = new PopupWindow(layout, width * 9 / 10, height / 2, true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(text1, 5, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flChild.setVisibility(View.VISIBLE);
				childList
						.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								text2.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarber(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
					});
			}
		});
	}
	
	//弹出美发师距离条件选项
	@SuppressLint("InflateParams") 
	protected void showBDistancePopupWindow(int width, int height) {
		String title[] = {"1千米内","2千米内","5千米内"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 100);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		mPopWin = new PopupWindow(layout, width * 1/3, height*1/ 3, true);
		mPopWin.showAtLocation(layout, Gravity.RIGHT, 0, -126);
		mPopWin.showAsDropDown(text1, 3, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								text3.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarber(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
		});
	}			
	//弹出理发店评价排序条件
	@SuppressLint("InflateParams") 
	protected void showStarPopupWindow(int width, int height) {
		String title[] = {"服务评价排序","卫生评价排序","设施评价排序","价格评价排序"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 1);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		mPopWin = new PopupWindow(layout, width * 2/5, height *2/5, true);
		mPopWin.showAtLocation(layout, Gravity.LEFT, 0, -100);
		mPopWin.showAsDropDown(text1, 4, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								text4.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarberShop(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
		});
	}
	
	
	
	//弹出理发店距离条件选项
	@SuppressLint("InflateParams") 
	protected void showBSDistancePopupWindow(int width, int height) {
		String title[] = {"1千米内","2千米内","5千米内"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 100);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		mPopWin = new PopupWindow(layout, width * 1/3, height*1/ 3, true);
		mPopWin.showAtLocation(layout, Gravity.RIGHT, 0, -126);
		mPopWin.showAsDropDown(text1, 3, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								text6.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarberShop(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
		});
	}
	
	
	
	//弹出理发店城市区域选项
	private void showBSCityPopupWindow(int width, int height) {
		
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<regions.size();i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", regions.get(i).getName());
			items.put("count", i);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(HomePageActivity.this, itemList);
		rootList.setAdapter(cla);
		
		flChild = (FrameLayout) layout.findViewById(R.id.child_lay);
		childList = (ListView) layout.findViewById(R.id.childcategory);
		childList.setAdapter(cla);
		flChild.setVisibility(View.INVISIBLE);
		
		mPopWin = new PopupWindow(layout, width * 9 / 10, height / 2, true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(text1, 5, 1);
		mPopWin.update();
		
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flChild.setVisibility(View.VISIBLE);
				childList
						.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								text5.setText((String)itemList.get(position).get("name"));
								if (initSearchFlag) {
									searchBarberShop(LBSCloudSearch.SEARCH_TYPE_LOCAL);
								}
								layout.setVisibility(View.GONE);
							}
					});
			}
		});
	}
	
	/*
	 * 获取美发师云检索参数
	 */
	private HashMap<String, String> getBRequestParams() {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			//设置搜索地区参数
			String region = null;
			if (text2.getText().toString().equals("全市")) {
				BaseApplication application = (BaseApplication) getApplication();
				if (application.preferences.getString("city3", null)==null) {
					if (application.mCurrentcity==null) {
						Toast.makeText(context, "网络异常，无法获取当前城市信息！", Toast.LENGTH_SHORT).show();
					}else {
						region = application.mCurrentcity;
					}
					
				}else {
					region = application.preferences.getString("city3", null);
				}
			}else {
				region = text2.getText().toString();
			}
			map.put("region", region);
			//设置搜索半径参数
			String radius="5000";
			if (text3.getText().toString().equals("1千米内")) {
				radius = "1000";
			}
			if (text3.getText().toString().equals("2千米内")) {
				radius = "2000";
			}
			if (text3.getText().toString().equals("5千米内")) {
				radius = "5000";
			}
			map.put("radius", radius);
			//设置中心点参数
			BaseApplication app = BaseApplication.getmInstance();
			if (app.currlocation != null) {
				map.put("location", app.currlocation.getLongitude() + ","
						+ app.currlocation.getLatitude());
			} else {
				// 无定位数据默认北京中心
				double cLat = 39.909230;
				double cLon = 116.397428;
				map.put("location", cLat + "," + cLon);
			}
			//设置排序条件参数
			map.put("sortby","distance:1|appraisestar:-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseApplication.getmInstance().setFilterParams(map);
		return map;
	}
	
	/*
	 * 获取理发店云检索参数
	 */
	private HashMap<String, String> getBSRequestParams() {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			//设置搜索地区参数
			String region = null;
			if (text5.getText().toString().equals("全市")) {
				BaseApplication application = (BaseApplication) getApplication();
				if (application.preferences.getString("city3", null)==null) {
					if (application.mCurrentcity==null) {
						Toast.makeText(context, "定位异常，无法获取当前城市信息！", Toast.LENGTH_SHORT).show();
					}else {
						region = application.mCurrentcity;
					}
				}else {
					region = application.preferences.getString("city3", null);
				}
			}else {
				region = text5.getText().toString();
			}
			map.put("region", region);
			//设置搜索半径参数
			String radius="5000";
			if (text6.getText().toString().equals("1千米内")) {
				radius = "1000";
			}
			if (text6.getText().toString().equals("2千米内")) {
				radius = "2000";
			}
			if (text6.getText().toString().equals("5千米内")) {
				radius = "5000";
			}
			map.put("radius", radius);
			//设置中心点参数
			BaseApplication app = BaseApplication.getmInstance();
			if (app.currlocation != null) {
				map.put("location", app.currlocation.getLongitude() + ","
						+ app.currlocation.getLatitude());
			} else {
				// 无定位数据默认北京中心
				double cLat = 39.909230;
				double cLon = 116.397428;
				map.put("location", cLat + "," + cLon);
			}
			//设置排序条件参数
			String sort="servicestar:-1";
			if (text4.getText().equals("服务评价排序")) {
				sort = "servicestar:-1";
			}
			if (text4.getText().equals("卫生评价排序")) {
				sort = "healthstar:-1";
			}
			if (text4.getText().equals("价格评价排序")) {
				sort = "pricestar:-1";
			}
			if (text4.getText().equals("设施评价排序")) {
				sort = "facilitystar:-1";
			}
			map.put("sortby", "distance:1|"+sort);
			map.put("tags", "理发店");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseApplication.getmInstance().setFilterParams(map);
		return map;
	}
	
	
	
	/*
	 * 根据搜索类型发起对理发店的检索
	 */
	private void searchBarberShop(int searchType){
		progress.setVisibility(View.VISIBLE);
		BaseApplication app = BaseApplication.getmInstance();
		app.getBarbershops().clear(); // 搜索前清空列表
		app.getBarberShopListActivity().loadMoreView.setVisibility(View.INVISIBLE);
		if (app.getBarberShopListActivity().getListView().getFooterViewsCount() == 0) {
			// 点击查看更多按钮添加
			app.getBarberShopListActivity().getListView()
					.addFooterView(app.getBarberShopListActivity().loadMoreView);
		}

		app.getBarberShopListActivity().getListView().setAdapter(app.getBarbershopListAdapter());

		// 云检索发起
		LBSCloudSearch.request(searchType, getBSRequestParams(), mHandler,
				BaseApplication.networkType,"78111");
	}
	
	/*
	 * 根据搜索类型发起对美发师的检索
	 */
	private void searchBarber(int searchType){
		progress.setVisibility(View.VISIBLE);
		BaseApplication app = BaseApplication.getmInstance();
		app.getBarbers().clear(); // 搜索前清空列表
		app.getBarberListActivity().loadMoreView.setVisibility(View.INVISIBLE);
		if (app.getBarberListActivity().getListView().getFooterViewsCount() == 0) {
			// 点击查看更多按钮添加
			app.getBarberListActivity().getListView()
					.addFooterView(app.getBarberListActivity().loadMoreView);
		}

		app.getBarberListActivity().getListView().setAdapter(app.getBarberadapter());

		// 云检索发起
		LBSCloudSearch.request(searchType, getBRequestParams(), mBHandler,
				BaseApplication.networkType,"85084");
	}
	
	/*
	 * 解析返回美发师数据
	 */
	private void parserBarber(JSONObject json) {
		BaseApplication app = (BaseApplication)getApplication();
		List<NearBarber> list = app.getBarbers();

		try {
			Log.i("返回对象总数",""+json.getInt("total"));
			app.getBarberListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");
			if (jsonArray != null && jsonArray.length() <= 0) {
				Toast.makeText(context, "没有符合要求的数据", Toast.LENGTH_SHORT).show();
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					NearBarber content = new NearBarber();
					content.setBName(jsonObject2.getString("title"));
					content.setBAddress(jsonObject2.getString("address"));

					JSONArray locArray = jsonObject2.getJSONArray("location");
					double latitude = locArray.getDouble(1);
					double longitude = locArray.getDouble(0);
					content.setLatitude(latitude);
					content.setLongitude(longitude);

					float results[] = new float[1];

					if (app.currlocation != null) {
						Location.distanceBetween(
								app.currlocation.getLatitude(),
								app.currlocation.getLongitude(), latitude,
								longitude, results);
					}
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String dist = decimalFormat.format(results[0]/1000);
					content.setBDistance(dist);
					Log.i("距离", ""+content.getBDistance());
					content.setBDis(jsonObject2.getString("discontent"));
					content.setImageurl(jsonObject2.getJSONObject("imageurl").getString("big"));
					content.setBSex(jsonObject2.getString("sex"));
					content.setBAge(jsonObject2.getInt("age"));
					//设置为访问发客数据操作链接
		
					content.setOrderAddup(jsonObject2.getInt("orderaddup"));
					content.setAppraiseStar(jsonObject2.getDouble("appraisestar"));
					list.add(content);
					if (!text3.getText().toString().equals("按距离排序")&&text1.getText().toString().equals("按评价排序")) {
						Collections.sort(list, new CompareBarberUnit());
					}
				}

			}
			if (list.size() <15) {
				app.getBarberListActivity().getListView()
						.removeFooterView(app.getBarberListActivity().loadMoreView);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		BarberListAdapter adapter = ((BaseApplication) getApplication())
				.getBarberadapter();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			app.getBarberListActivity().loadMoreView.setVisibility(View.VISIBLE);
			app.getBarberListActivity().progressBar.setVisibility(View.INVISIBLE);
		}
//		if (app.getBaiduMapActivity() != null) {
//			app.getBaiduMapActivity().isBarber = true;
//			app.getBaiduMapActivity().removeAllMarker();
//			app.getBaiduMapActivity().addAllMarker();
//		}
	}
	
	/*
	 * 解析返回理发店数据
	 */
	private void parserBarberShop(JSONObject json) {
		BaseApplication app = (BaseApplication)getApplication();
		List<NearBarberShop> list = app.getBarbershops();

		try {
			Log.i("返回对象总数",""+json.getInt("total"));
			app.getBarberShopListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");
			if (jsonArray != null && jsonArray.length() <= 0) {
				Toast.makeText(context, "没有符合要求的数据", Toast.LENGTH_SHORT).show();
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					NearBarberShop content = new NearBarberShop();
					content.setSName(jsonObject2.getString("title"));
					content.setSAddress(jsonObject2.getString("address"));

					JSONArray locArray = jsonObject2.getJSONArray("location");
					double latitude = locArray.getDouble(1);
					double longitude = locArray.getDouble(0);
					content.setLatitude(latitude);
					content.setLongitude(longitude);

					float results[] = new float[1];

					if (app.currlocation != null) {
						Location.distanceBetween(
								app.currlocation.getLatitude(),
								app.currlocation.getLongitude(), latitude,
								longitude, results);
					}
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String dist = decimalFormat.format(results[0]/1000);
					content.setSDistance(dist);
					Log.i("距离", ""+content.getSDistance());
					content.setSDis(jsonObject2.optString("discontent"));
					content.setImageurl(jsonObject2.getJSONObject("imageurl").getString("big"));
					//设置为访问发客数据操作链接
		
					content.setOrderAddup(jsonObject2.getInt("orderaddup"));
					content.setPriceStar(jsonObject2.getDouble("pricestar"));
					content.setServiceStar(jsonObject2.getDouble("servicestar"));
					list.add(content);
					if (!text6.getText().toString().equals("按距离排序")&&text4.getText().toString().equals("按评价排序")) {
						Collections.sort(list, new CompareBarberShopUnit());
					}
				}

			}
			if (list.size() <15) {
				app.getBarberShopListActivity().getListView()
						.removeFooterView(app.getBarberShopListActivity().loadMoreView);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		BarbershopListAdapter adapter = ((BaseApplication) getApplication())
				.getBarbershopListAdapter();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			app.getBarberShopListActivity().loadMoreView.setVisibility(View.VISIBLE);
			app.getBarberShopListActivity().progressBar.setVisibility(View.INVISIBLE);
		}
		if (app.getBaiduMapActivity() != null) {
//			app.getBaiduMapActivity().isBarber = false;
			app.getBaiduMapActivity().removeAllMarker();
			app.getBaiduMapActivity().addAllMarker();
		}
	}
	//对美发师进行按距离排序
	class CompareBarberUnit implements Comparator<NearBarber>{
	    @Override
	    public int compare(NearBarber t1, NearBarber t2) {
	        if(Float.valueOf(t1.getBDistance())<Float.valueOf(t2.getBDistance())){
	            return -1;
	        }
	        if(Float.valueOf(t1.getBDistance())>Float.valueOf(t2.getBDistance())){
	            return 1;
	        }
	        return 0;
	    }
	}
	
	//对理发店进行按距离排序
	class CompareBarberShopUnit implements Comparator<NearBarberShop>{
	    @Override
	    public int compare(NearBarberShop t1, NearBarberShop t2) {
	        if(Float.valueOf(t1.getSDistance())<Float.valueOf(t2.getSDistance())){
	            return -1;
	        }
	        if(Float.valueOf(t1.getSDistance())>Float.valueOf(t2.getSDistance())){
	            return 1;
	        }
	        return 0;
	    }
	}
}
