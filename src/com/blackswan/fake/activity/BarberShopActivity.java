package com.blackswan.fake.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Element;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackswan.fake.R;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.MyRegion;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.LBSCloudSearch;
import com.blackswan.fake.util.PopCityUtils;
import com.blackswan.fake.view.FakeRefreshListView;
import com.blackswan.fake.view.FakeRefreshListView.OnCancelListener;
import com.blackswan.fake.view.FakeRefreshListView.OnRefreshListener;

public class BarberShopActivity extends ListActivity implements OnItemClickListener, OnRefreshListener, OnCancelListener,OnScrollListener
{
	
	private Context context;
	public static final int MSG_NET_TIMEOUT = 100;
	public static final int MSG_NET_STATUS_ERROR = 200;
	public static final int MSG_NET_SUCC = 1;
	public static final int MSG_DISTRICT = 3;

	private boolean initSearchFlag = false;
	private RelativeLayout progress;
	
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private ArrayList<MyRegion> regions;
	private PopCityUtils cityUtils;
	int last, current;
	
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	public FakeRefreshListView refreshListView;
	public View loadMoreView;
	public ProgressBar progressBar;
	private int visibleLastIndex = 0;   //最后的可视项索引 
	public int totalItem = -1; 
	private BarbershopListAdapter barbershopListAdapter;
	private List<NearBarberShop> barberShops = new ArrayList<NearBarberShop>();
	
	private ArrayList<HashMap<String,Object>> itemList;
	
	private LinearLayout linLayout;
	
	/*
	 * 处理网络请求
	 */
	private final Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
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
					parser(json);
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
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barbershop);
		initViews();
		initEvents();
		search();

		BaseApplication.getmInstance().setHandler(mHandler);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setOnScrollListener(this);
		
		BaseApplication app = (BaseApplication) getApplication();
		barbershopListAdapter = new BarbershopListAdapter(app,BarberShopActivity.this, barberShops);
		setListAdapter(barbershopListAdapter);
		app.setBarbershops(barberShops);
		app.setBarbershopListAdapter(barbershopListAdapter);
		app.setBarberShopActivity(this);
		
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
		BaseApplication application= (BaseApplication) getApplication();
		String cname=application.preferences.getString("city3", null);
		if (cname == null) {
			cname = application.mCurrentcity;
		}
		Log.i("初始化城市表单",""+cname);
		cityUtils = new PopCityUtils(this, handler);
		cityUtils.initDistricts(cname);
		loadMoreView = getLayoutInflater().inflate(R.layout.list_item_footer, null);
		progressBar = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		refreshListView = (FakeRefreshListView) findViewById(R.id.nearbarbershop_reflist);
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
	
	protected void showStarPopupWindow(int width, int height) {
		String title[] = {"服务评价排序","卫生评价排序","设施评价排序","价格评价排序"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 1);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopActivity.this, itemList);
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
								if (initSearchFlag) {
									search();
								}
								layout.setVisibility(View.GONE);
							}
		});
	}
	protected void showDistancePopupWindow(int width, int height) {
		String title[] = {"1千米内","2千米内","5千米内"};
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopActivity.this).inflate(R.layout.popup_distance, null);
		rootList = (ListView) layout.findViewById(R.id.distancecategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name",title[i]);
			items.put("count", 100);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopActivity.this, itemList);
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
								if (initSearchFlag) {
									search();
								}
								layout.setVisibility(View.GONE);
							}
		});
	}
	@SuppressLint("InflateParams")
	private void showCityPopupWindow(int width, int height) {
		
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<regions.size();i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", regions.get(i).getName());
			items.put("count", i*5+4);
			itemList.add(items);
		}
		
		CategoryListAdapter cla = new CategoryListAdapter(BarberShopActivity.this, itemList);
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
									search();
								}
								layout.setVisibility(View.GONE);
							}
					});
			}
		});
	}
	
	/*
	 * 云检索发起
	 */
	private void search() {
		search(LBSCloudSearch.SEARCH_TYPE_LOCAL);
	}
	/*
	 * 获取云检索参数
	 */
	private HashMap<String, String> getRequestParams() {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			map.put("region", URLEncoder.encode("北京", "utf-8"));
			String filter = "";
			String radius;
			if (text3.getText().equals("1千米内")) {
				radius = "1000";
			}
			if (text3.getText().equals("2千米内")) {
				radius = "2000";
			}else {
				radius = "5000";
			}
			map.put("radius", radius);
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
			//本地搜索过滤条件
			map.put("filter", filter);
		} catch (Exception e) {
			// TODO: handle exception
		}
		BaseApplication.getmInstance().setFilterParams(map);
		return map;
	}
	/*
	 * 根据搜索类型发起检索
	 */
	private void search(int searchType){
		progress.setVisibility(View.VISIBLE);
		BaseApplication app = BaseApplication.getmInstance();
		app.getBarbershops().clear(); // 搜索前清空列表
		app.getBarberShopActivity().loadMoreView.setVisibility(View.INVISIBLE);
		if (app.getBarberShopActivity().getListView().getFooterViewsCount() == 0) {
			// 点击查看更多按钮添加
			app.getBarberShopActivity().getListView()
					.addFooterView(app.getBarberShopActivity().loadMoreView);
		}

		app.getBarberShopActivity().getListView().setAdapter(app.getBarbershopListAdapter());

		// 云检索发起
		LBSCloudSearch.request(searchType, getRequestParams(), mHandler,
				BaseApplication.networkType);
	}
	
	/*
	 * 解析返回数据
	 */
	private void parser(JSONObject json) {
		BaseApplication app = (BaseApplication) getApplication();
		List<NearBarberShop> list = app.getBarbershops();

		try {
			app.getBarberShopActivity().totalItem = json.getInt("total");

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
					content.setSDistance((int) results[0]/1000 + "km");

					content.setSDis(jsonObject2.getString("discotent"));
					content.setImageurl(jsonObject2.getString("imageurl"));
					//设置为访问发客数据操作链接
					content.setWebUrl(jsonObject2.getString("roomurl"));
					content.setOrderAddup(jsonObject2.getInt("orderaddup"));
					content.setPriceStar(jsonObject2.getDouble("pricestar"));
					content.setServiceStar(jsonObject2.getDouble("servicestar"));
					list.add(content);
				}

			}
			if (list.size() < 15) {
				app.getBarberShopActivity().getListView()
						.removeFooterView(app.getBarberShopActivity().loadMoreView);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BarbershopListAdapter adapter = ((BaseApplication) getApplication())
				.getBarbershopListAdapter();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			app.getBarberShopActivity().loadMoreView.setVisibility(View.VISIBLE);
			app.getBarberShopActivity().progressBar.setVisibility(View.INVISIBLE);
		}
		if (app.getBaiduMapActivity() != null) {
			app.getBaiduMapActivity().removeAllMarker();
			app.getBaiduMapActivity().addAllMarker();
		}
	}
	
	/**
	 * 加载更多数据
	 */
	private void loadMoreData() {
		HashMap<String, String> filterParams = BaseApplication.getmInstance().getFilterParams();
		filterParams.put("page_index", (barberShops.size()/10 + 1) + "");
		// search type 为 -1，将保持当前的搜索类型
		LBSCloudSearch.request(-1,filterParams, BaseApplication.getmInstance().getHandler(), BaseApplication.networkType);
	}
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = barbershopListAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			loadMoreData();
			progressBar.setVisibility(View.VISIBLE);
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
	private long exitTime = 0;
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

}
