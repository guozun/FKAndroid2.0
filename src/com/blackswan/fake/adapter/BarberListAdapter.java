package com.blackswan.fake.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.bean.NearBarber;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BarberListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<NearBarber> items;
	private ViewHolder holder;
	private DisplayImageOptions options; 
	public BarberListAdapter(Context context, List<NearBarber> list) {
		mInflater = LayoutInflater.from(context);
		items = list;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_barber, null);
			holder = new ViewHolder();
			holder.barbername = (TextView) convertView.findViewById(R.id.tv_barbername);
			holder.barberdis = (TextView) convertView.findViewById(R.id.tv_barberdis);
			holder.appraisestarcount = (TextView) convertView.findViewById(R.id.tv_barberappraise);
			holder.addupcount = (TextView) convertView.findViewById(R.id.tv_barberaddup);
			holder.barberdistance= (TextView) convertView.findViewById(R.id.tv_barberdistancecount);
			holder.barberavatar = (ImageView) convertView.findViewById(R.id.iv_barberavatar);
			holder.appraisestar = (ImageView) convertView.findViewById(R.id.iv_barberappraisestar);
			holder.barbersex = (ImageView) convertView.findViewById(R.id.iv_barbersex);
			holder.barberage = (TextView) convertView.findViewById(R.id.tv_barberage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NearBarber barber = (NearBarber) getItem(position);
		// 向视图填充数据
		holder.barbername.setText((String)barber.getBName() + "");
		holder.barberdis.setText((String) barber.getBDis() + "");
		holder.appraisestarcount.setText((float) barber.getAppraiseStar() + "分");
		holder.addupcount.setText((int) barber.getOrderAddup() + "人");
		holder.barberdistance.setText((String) barber.getBDistance() + "Km");
		ImageLoader.getInstance().displayImage((String) barber.getImageurl(),holder.barberavatar, options);
		displayStar((float)barber.getAppraiseStar());
		displaySex((String) barber.getBSex());
		holder.barberage.setText((int)barber.getBAge()+"");
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		ImageView barberavatar;
		TextView barbername;
		TextView barberdis;
		ImageView appraisestar;
		TextView appraisestarcount;
		TextView addupcount;
		TextView barberdistance;
		ImageView barbersex;
		TextView barberage;
	}
	
	//根据性别显示图片
	public void displaySex(String sex) {
		if (sex =="男") {
			holder.barbersex.setImageResource(R.drawable.ic_user_male);
		}
		if (sex == "女") {
			holder.barbersex.setImageResource(R.drawable.ic_user_famale);
		}
	}
	//根据评分选择星级图片
	public void displayStar(float count) {
		if (count>0&&count<1.0) {
			holder.appraisestar.setImageResource(R.drawable.star05);
		}
		if (count==1.0) {
			holder.appraisestar.setImageResource(R.drawable.star10);
		}
		if (count>1.0&&count<2.0) {
			holder.appraisestar.setImageResource(R.drawable.star15);
		}
		if (count==2.0) {
			holder.appraisestar.setImageResource(R.drawable.star20);
		}
		if (count>2.0&&count<3.0) {
			holder.appraisestar.setImageResource(R.drawable.star25);
		}
		if (count==3.0) {
			holder.appraisestar.setImageResource(R.drawable.star30);
		}
		if (count>3.0&&count<4.0) {
			holder.appraisestar.setImageResource(R.drawable.star35);
		}
		if (count==4.0) {
			holder.appraisestar.setImageResource(R.drawable.star40);
		}
		if (count>4.0&&count<5.0) {
			holder.appraisestar.setImageResource(R.drawable.star45);
		}
		if (count==5.0) {
			holder.appraisestar.setImageResource(R.drawable.star50);
		}
	}
	
}
