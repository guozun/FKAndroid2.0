package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.blackswan.fake.R;
//import com.blackswan.fake.activity.BaiduMapActivity;

import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.MyRegion;
import com.blackswan.fake.util.PopCityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class HomePageActivity extends ActivityGroup implements OnClickListener
{
	public static final int MSG_DISTRICT = 3;
	private TabHost tabHost;
	private long exitTime = 0;
	View  cityView;
	TextView city;
	ImageView researchImageView;
	ImageView mapImageView;
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
		setContentView(R.layout.activity_homepage);
		tabHost = (TabHost) findViewById(R.id.homepagetabHost);
		tabHost.setup(this.getLocalActivityManager());;
		TabWidget tabWidget = tabHost.getTabWidget();
		TabSpec tab1 = tabHost.newTabSpec("barber");
        tab1.setIndicator(createContent("发型师",R.drawable.toplabelleft));
        tab1.setContent(new Intent(this,BarberActivity.class));
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("barbershop");
        tab2.setIndicator(createContent("理发店",R.drawable.toplabelright));
        tab2.setContent(new Intent(this,BarberShopActivity.class));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
        initViews();
        initEvent();
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
	}
	
	//为控件添加事件处理
	protected void initEvent() {
		cityView.setOnClickListener(this);
		BaseApplication application = ((BaseApplication) getApplication());
		String historycity=application.preferences.getString("city3", null);
        if (historycity==null) {
        	application.putString("city3", application.mCurrentcity);
			city.setText(application.mCurrentcity);
		}else {
			city.setText(historycity);
		}
        String cname=application.preferences.getString("city3", null);
		if (cname == null) {
			cname = application.mCurrentcity;
		}
        Log.i("初始化城市表单",""+cname);
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
			}
		}
		
		@Override
		protected void onResume() {
			// 重新显示Activity时重置城市
			BaseApplication application = ((BaseApplication) getApplication());
			String historycity=application.preferences.getString("city3", null);
	        if (historycity==null) {
	        	application.putString("city3", application.mCurrentcity);
				city.setText(application.mCurrentcity);
			}else {
				city.setText(historycity);
			}
			super.onResume();
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				exit();
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}
		
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
				showCityPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				break;
			case R.id.barber_text3:
				showDistancePopupWindow(linLayout.getWidth(),linLayout.getHeight());
				break;
			case R.id.barbershop_text1:
				showStarPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				break;
			case R.id.barbershop_text2:
				showCityPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				break;
			case R.id.barbershop_text3:
				showDistancePopupWindow(linLayout.getWidth(),linLayout.getHeight());
				break;
			default:
				break;
			}
		}
		@SuppressLint("InflateParams") 
		protected void showBarberStarPopupWindow(int width, int height) {
			String title = "按评价排序";
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
			mPopWin.showAtLocation(layout, Gravity.LEFT, 0, -160);
			mPopWin.showAsDropDown(text1, 4, 1);
			mPopWin.update();
			
			rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
									text1.setText((String)itemList.get(position).get("name"));
									layout.setVisibility(View.GONE);
								}
			});
		}
		
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
			mPopWin.showAtLocation(layout, Gravity.LEFT, 0, -120);
			mPopWin.showAsDropDown(text1, 4, 1);
			mPopWin.update();
			
			rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
									text4.setText((String)itemList.get(position).get("name"));
									layout.setVisibility(View.GONE);
								}
			});
		}
		
		@SuppressLint("InflateParams") 
		protected void showDistancePopupWindow(int width, int height) {
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
			mPopWin.showAtLocation(layout, Gravity.RIGHT, 0, -138);
			mPopWin.showAsDropDown(text1, 3, 1);
			mPopWin.update();
			
			rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
									text3.setText((String)itemList.get(position).get("name"));
									layout.setVisibility(View.GONE);
								}
			});
		}
		@SuppressLint("InflateParams")
		private void showCityPopupWindow(int width, int height) {
			
			itemList = new ArrayList<HashMap<String,Object>>();
			layout = (LinearLayout) LayoutInflater.from(HomePageActivity.this).inflate(R.layout.popup_category, null);
			rootList = (ListView) layout.findViewById(R.id.rootcategory);
			for(int i=0;i<regions.size();i++){
				HashMap<String,Object> items = new HashMap<String,Object>();
				items.put("name", regions.get(i).getName());
				items.put("count", i*5+4);
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
									layout.setVisibility(View.GONE);
								}
						});
				}
			});
		}
	
}
