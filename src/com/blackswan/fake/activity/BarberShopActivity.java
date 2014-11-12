package com.blackswan.fake.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class BarberShopActivity extends Activity
{
	private Context context;
	public static final int MSG_NET_TIMEOUT = 100;
	public static final int MSG_NET_STATUS_ERROR = 200;
	public static final int MSG_NET_SUCC = 1;
	public static final int MSG_DISTRICT = 3;

	private boolean initSearchFlag = false;
	private RelativeLayout progress;
	
	private TextView text3;
	
	
	
	/*
	 * 处理网络请求
	 */
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
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					parser(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barbershop);
		
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
			e.printStackTrace();
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
		app.getBarberShopListActivity().loadMoreView.setVisibility(View.INVISIBLE);
		if (app.getBarberShopListActivity().getListView().getFooterViewsCount() == 0) {
			// 点击查看更多按钮添加
			app.getBarberShopListActivity().getListView()
					.addFooterView(app.getBarberShopListActivity().loadMoreView);
		}

		app.getBarberShopListActivity().getListView().setAdapter(app.getBarbershopListAdapter());

		// 云检索发起
		LBSCloudSearch.request(searchType, getRequestParams(), mHandler,
				BaseApplication.networkType,"78111");
	}
	
	/*
	 * 解析返回数据
	 */
	private void parser(JSONObject json) {
		BaseApplication app = (BaseApplication) getApplication();
		List<NearBarberShop> list = app.getBarbershops();

		try {
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
			app.getBaiduMapActivity().removeAllMarker();
			app.getBaiduMapActivity().addAllMarker();
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
