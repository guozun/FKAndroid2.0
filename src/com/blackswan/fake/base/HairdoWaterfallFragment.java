package com.blackswan.fake.base;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.HairDoDetailActivity;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HairdoWaterfallFragment extends Fragment {

	// 图片加载配置
	private DisplayImageOptions options;
	private List<String> imageUrls ;
	private MultiColumnPullToRefreshListView waterfallView;

	public HairdoWaterfallFragment(List<String> imageurl) {
		imageUrls = imageurl;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				 .showImageOnLoading(R.drawable.homepage_location)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewroot = inflater.inflate(R.layout.fragment_hairdo_waterfall,
				container, false);
		waterfallView = (MultiColumnPullToRefreshListView) viewroot
				.findViewById(R.id.id_fragment_hairdo_waterfall);
		waterfallView.setAdapter(new WaterfallAdapter());
		waterfallView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO //下拉刷新要做的事
				
				//刷新完成后记得调用这个
				waterfallView.onRefreshComplete();
			}
		});
		return viewroot;
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public class WaterfallAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		public WaterfallAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}
		@Override
		public int getCount() {
			return (imageUrls==null)?0:imageUrls.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup group) {
			final Holder holder;
			// 得到View
			if (view == null) {
				holder = new Holder();
				view = inflater.inflate(R.layout.item_hairdo_waterfall, group, false);
				holder.ivPic = (ImageView) view.findViewById(R.id.iv_hairdo_item_picture);
				holder.tvUp = (TextView) view.findViewById(R.id.tv_hairdo_item_good);
				holder.tvDown = (TextView) view.findViewById(R.id.tv_hairdo_item_bad);
				view.setTag(holder);
			} 
			else {
				holder = (Holder) view.getTag();
			}
			
			String url = imageUrls.get(position);
			ImageLoader.getInstance().displayImage(url, holder.ivPic,options);
			holder.tvUp.setText("10");
			holder.tvDown.setText("100");
			
			holder.ivPic.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), HairDoDetailActivity.class);
//					Bundle
//						intent.putExtras(bundle);
					
					startActivity(intent);
					
				}
			});
			
			return view;
		}



	}

	class Holder {
		public ImageView ivPic;
		public ImageView ivUp;
		public ImageView ivDown;
		public TextView tvUp;
		public TextView tvDown;
	}

}
