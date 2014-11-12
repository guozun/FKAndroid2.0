package com.blackswan.fake.activity.barbershopactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.BarberShopActivity;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.MyRegion;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.LBSCloudSearch;
import com.blackswan.fake.util.PopCityUtils;
import com.blackswan.fake.view.FakeRefreshListView;

public class BarberShopListActivity extends ListActivity implements OnScrollListener{
	
	public static final int MSG_DISTRICT = 3;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private ArrayList<MyRegion> regions;
	private PopCityUtils cityUtils;
	
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	private ArrayList<HashMap<String,Object>> itemList;
	
	private LinearLayout linLayout;
	
	private  BarbershopListAdapter adapter;
	private List<NearBarberShop> list = new ArrayList<NearBarberShop>();
	public View loadMoreView;
	public ProgressBar progressBar;
	private int visibleLastIndex = 0;   //最后的可视项索引 
	public int totalItem = -1;
	public FakeRefreshListView refreshListView;
	
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
	
	@SuppressLint("InflateParams") 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initViews();
		initEvents();
		
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setOnScrollListener(this);
		BaseApplication application=(BaseApplication) getApplication();
		adapter = new BarbershopListAdapter(application,BarberShopListActivity.this, list);
		setListAdapter(adapter);

		application.setBarbershops(list);
		application.setBarbershopListAdapter(adapter);
		application.setBarberShopListActivity(this);
		
		
	}
	@Override
	protected void onResume() {
		BaseApplication application= (BaseApplication) getApplication();
		String cname=application.preferences.getString("city3", null);
		if (cname == null) {
			cname = application.mCurrentcity;
		}
		cityUtils.initDistricts(cname);
		super.onResume();
	}
	
	@SuppressLint("InflateParams") 
	protected void initViews() {
		text1 = (TextView) findViewById(R.id.barbershop_text1);
		text2 = (TextView) findViewById(R.id.barbershop_text2);
		text3 = (TextView) findViewById(R.id.barbershop_text3);
		linLayout = (LinearLayout) findViewById(R.id.barbershop);
		loadMoreView = getLayoutInflater().inflate(R.layout.list_item_footer, null);
		progressBar = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		refreshListView = (FakeRefreshListView) findViewById(R.id.nearbarbershop_reflist);
		BaseApplication application= (BaseApplication) getApplication();
		String cname=application.preferences.getString("city3", null);
		if (cname == null) {
			cname = application.mCurrentcity;
		}
		Log.i("初始化城市表单",""+cname);
		cityUtils = new PopCityUtils(this, handler);
		cityUtils.initDistricts(cname);
	}
	
	protected void initEvents() {
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将理发店的星级评价
				showStarPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			}
		});
		
		text2.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//将理发店铺列表按评价从高到低排列
				showCityPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				}
			});
		
		text3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将理发店铺列表按距离从近到远排序
				showDistancePopupWindow(linLayout.getWidth(),linLayout.getHeight());
			}
		});
		loadMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreData();
				progressBar.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	@SuppressLint("InflateParams") 
	protected void showStarPopupWindow(int width, int height) {
		String title[] = {"服务评价排序","卫生评价排序","设施评价排序","价格评价排序"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopListActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 1);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopListActivity.this, itemList);
		rootList.setAdapter(cla);
		
		mPopWin = new PopupWindow(layout, width * 2/5, height *2/5, true);
		mPopWin.showAtLocation(layout, Gravity.LEFT, 0, -120);
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
	protected void showDistancePopupWindow(int width, int height) {
		String title[] = {"1千米内","2千米内","5千米内"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopListActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 100);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopListActivity.this, itemList);
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
		layout = (LinearLayout) LayoutInflater.from(BarberShopListActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<regions.size();i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", regions.get(i).getName());
			items.put("count", i*5+4);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopListActivity.this, itemList);
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
	/**
	 * 列表item点击回调
	 */
	@SuppressLint("InflateParams") 
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		View popview = LayoutInflater.from(this).inflate(R.layout.marker_pop, null);
		popview.setDrawingCacheEnabled(true);
		//启用绘图缓存   
		popview.setDrawingCacheEnabled(true);        
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null   
		popview.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),  
                MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));  
        //这个方法也非常重要，设置布局的尺寸和位置   
		popview.layout(0, 0, popview.getMeasuredWidth(), popview.getMeasuredHeight());  
        //获得绘图缓存中的Bitmap   
		popview.buildDrawingCache();   
		
		super.onListItemClick(l, v, position, id);
		
//		String webUrl = list.get(position).getWebUrl();
		
//		Intent intent= new Intent();       
//	    intent.setAction("android.intent.action.VIEW");   
//	    Uri content_url = Uri.parse(webUrl);  
//	    intent.setData(content_url); 
//	    startActivity(intent);
	    
	    //调用百度统计接口
	    BaseApplication.getmInstance().callStatistics();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		if (totalItemCount == totalItem) {
			getListView().removeFooterView(loadMoreView);
		}
	}
	
	/**
	 * 加载更多数据
	 */
	private void loadMoreData() {
		HashMap<String, String> filterParams = BaseApplication.getmInstance().getFilterParams();
		filterParams.put("page_index", (list.size()/10 + 1) + "");
		// search type 为 -1，将保持当前的搜索类型
		LBSCloudSearch.request(-1,filterParams, BaseApplication.getmInstance().getHandler(), BaseApplication.networkType,"78111");
	}

	
}
