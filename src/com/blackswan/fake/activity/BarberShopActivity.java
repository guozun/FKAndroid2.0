package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blackswan.fake.R;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.adapter.CategoryListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.LBSCloudSearch;
import com.blackswan.fake.view.FakeRefreshListView;
import com.blackswan.fake.view.FakeRefreshListView.OnCancelListener;
import com.blackswan.fake.view.FakeRefreshListView.OnRefreshListener;

public class BarberShopActivity extends ListActivity implements OnItemClickListener, OnRefreshListener, OnCancelListener,OnScrollListener
{
	private TextView text1;
	private TextView text2;
	private TextView text3;
	
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
	private String title[]= {"环翠区","高新技术开发区","经济开发区","文登区"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barbershop);
		initViews();
		initEvents();
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
//		listView.addFooterView(loadMoreView);
		listView.setOnScrollListener(this);
		
		barbershopListAdapter = new BarbershopListAdapter((BaseApplication)getApplication(),BarberShopActivity.this, barberShops);
		setListAdapter(barbershopListAdapter);

		BaseApplication app = (BaseApplication) getApplication();
		app.setBarbershops(barberShops);
		app.setBarbershopListAdapter(barbershopListAdapter);
		app.setBarberShopActivity(this);
		
	}
	
	protected void initViews() {
		text1 = (TextView) findViewById(R.id.barbershop_text1);
		text2 = (TextView) findViewById(R.id.barbershop_text2);
		text3 = (TextView) findViewById(R.id.barbershop_text3);
		linLayout = (LinearLayout) findViewById(R.id.barbershop);
		loadMoreView = getLayoutInflater().inflate(R.layout.list_item_footer, null);
		progressBar = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		refreshListView = (FakeRefreshListView) findViewById(R.id.nearbarbershop_reflist);
	}
	
	protected void initEvents() {
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
			}
		});
		
		text2.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//将理发店铺列表按评价从高到低排列
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
				}
			});
		
		text3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将理发店铺列表按距离从近到远排序
				showPopupWindow(linLayout.getWidth(),linLayout.getHeight());
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
	private void showPopupWindow(int width, int height) {
		
		itemList = new ArrayList<HashMap<String,Object>>();
		layout = (LinearLayout) LayoutInflater.from(BarberShopActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for(int i=0;i<title.length;i++){
			HashMap<String,Object> items = new HashMap<String,Object>();
			items.put("name", title[i]);
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
								// TODO Auto-generated method stub
								layout.setVisibility(View.GONE);
							}
					});
			}
		});
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
