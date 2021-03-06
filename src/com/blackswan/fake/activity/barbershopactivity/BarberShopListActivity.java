package com.blackswan.fake.activity.barbershopactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blackswan.fake.R;
import com.blackswan.fake.adapter.BarbershopListAdapter;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.util.LBSCloudSearch;

public class BarberShopListActivity extends ListActivity implements OnScrollListener{
	
	private  BarbershopListAdapter adapter;
	private List<NearBarberShop> list = new ArrayList<NearBarberShop>();
	public View loadMoreView;
	public ProgressBar progressBar;
	private int visibleLastIndex = 0;   //最后的可视项索引 
	public int totalItem = -1;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		loadMoreView = getLayoutInflater().inflate(R.layout.list_item_footer, null);
		progressBar = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreData();
				progressBar.setVisibility(View.VISIBLE);
			}
		});
		
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setOnScrollListener(this);
		
		adapter = new BarbershopListAdapter(BarberShopListActivity.this, list);
		setListAdapter(adapter);
		BaseApplication.setNetworkType();
		BaseApplication application=(BaseApplication) getApplication();
		application.setBarbershops(list);
		application.setBarbershopListAdapter(adapter);
		application.setBarberShopListActivity(this);
		
		
	}
	
	
	/**
	 * 列表item点击回调
	 */
	@SuppressLint("InflateParams") 
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
//		View popview = LayoutInflater.from(this).inflate(R.layout.marker_pop, null);
//		popview.setDrawingCacheEnabled(true);
//		//启用绘图缓存   
//		popview.setDrawingCacheEnabled(true);        
//        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null   
//		popview.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),  
//                MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));  
//        //这个方法也非常重要，设置布局的尺寸和位置   
//		popview.layout(0, 0, popview.getMeasuredWidth(), popview.getMeasuredHeight());  
//        //获得绘图缓存中的Bitmap   
//		popview.buildDrawingCache();   
//		
//		super.onListItemClick(l, v, position, id);
		
//		String webUrl = list.get(position).getWebUrl();
		
//		Intent intent= new Intent();       
//	    intent.setAction("android.intent.action.VIEW");   
//	    Uri content_url = Uri.parse(webUrl);  
//	    intent.setData(content_url); 
//	    startActivity(intent);
	    
	    //调用百度统计接口
//	    BaseApplication.getmInstance().callStatistics();
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
