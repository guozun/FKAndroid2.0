package com.blackswan.fake.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.bean.NowOrder;
import com.blackswan.fake.view.FakePullDrowListView;
import com.blackswan.fake.view.FakeRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class NowOrderListFragment extends Fragment {

	// 图片加载配置
	private DisplayImageOptions options;
	NowOrder order;
	private LayoutInflater mInflater;
	FakePullDrowListView mRefreshListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mRefreshListView = new FakePullDrowListView(getActivity());

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.bg_cover_userheader_outerpress)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// test data
		order = new NowOrder();
		order.setBarberHeadUrl("http://img0.bdstatic.com/img/image/shouye/mxlss-13058626297.jpg");
		order.setBarbershopName("高区审美店");
		order.setBarberName("姓名");
		order.setOrderTime("09:00");
		order.setOperationName("烫发");
		order.setOrderNum("2人");
		order.setProgress("进展情况");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewroot = inflater.inflate(R.layout.fragment_pullto_refresh,
				container, false);
		mRefreshListView = (FakePullDrowListView) viewroot
				.findViewById(R.id.fragment_pullto_fefresh);
		mRefreshListView.setAdapter(new Adapter());
		mRefreshListView.setOnRefreshListener(new FakeRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				//刷新
				//mRefreshListView.onRefreshComplete();
			}
		});
		mRefreshListView.setOnCancelListener(new FakeRefreshListView.OnCancelListener() {
			@Override
			public void onCancel() {
				mRefreshListView.onRefreshComplete();
			}
		});
		mInflater = inflater;
		return viewroot;

	}

	private class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (order != null) {
				return 1;
			} else {
				return 0;
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_now_order, null);
				holder.ivBarberHead = (ImageView) convertView
						.findViewById(R.id.iv_now_order_head);
				holder.tvDosome = (TextView) convertView
						.findViewById(R.id.tv_now_order_do);
				holder.detail = (TextView) convertView
						.findViewById(R.id.tv_now_order_detail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvDosome.setText("取消预约");
			ImageLoader.getInstance().displayImage(order.getBarberHeadUrl(),
					holder.ivBarberHead, options);
			int index = 0;
			SpannableStringBuilder ssb = new SpannableStringBuilder();
			// 理发店名字
			ssb.append(order.getBarbershopName());
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#232323")),
					index, ssb.length(),
					SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new RelativeSizeSpan((float) 1.5), index, ssb.length(),
					SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			index += ssb.length();
			// 来理发师名字
			String rn = System.getProperty("line.separator");
			ssb.append("  " + order.getBarberName() + rn);
			ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), index,
					ssb.length(),
					SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			index += ssb.length();
			// 详情

			ssb.append("项目:" + order.getOperationName() + rn);
			ssb.append("预约时间:" + order.getOrderTime() + rn);
			ssb.append("预约人数:" + order.getOrderNum() + rn);
			ssb.append("进展状态:" + order.getProgress());
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#353636")),
					index, ssb.length(),
					SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			// ssb.setSpan(new RelativeSizeSpan((float) 1.2), index,
			// ssb.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

			holder.detail.setText(ssb);
			return convertView;
		}
	}

	private final class ViewHolder {
		public TextView detail;
		public TextView tvDosome;
		public Button button;
		public ImageView ivBarberHead;
	}

}