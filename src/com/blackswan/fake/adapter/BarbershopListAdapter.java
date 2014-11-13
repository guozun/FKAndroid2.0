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
import com.blackswan.fake.bean.NearBarberShop;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BarbershopListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<NearBarberShop> items;
	private DisplayImageOptions options; 
	public BarbershopListAdapter(Context context,
			List<NearBarberShop> list) {
		mInflater = LayoutInflater.from(context);
		items = list;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	private ViewHolder holder;

	@SuppressLint("InflateParams") @Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_barbershop, null);
			holder = new ViewHolder();
			
			holder.barbershopname = (TextView) convertView.findViewById(R.id.tv_barbershopname);
			holder.barbershop_discontent = (TextView) convertView.findViewById(R.id.tv_barbershop_discontent);
			holder.servicestarcount = (TextView) convertView.findViewById(R.id.tv_servicestarcount);
			holder.pricestarcount = (TextView) convertView.findViewById(R.id.tv_pricestarconut);
			holder.addupcount = (TextView) convertView.findViewById(R.id.tv_addupcount);
			holder.distancecount= (TextView) convertView.findViewById(R.id.tv_distancecount);
			holder.servicestar = (ImageView) convertView.findViewById(R.id.iv_servicestar);
			holder.pricestar = (ImageView) convertView.findViewById(R.id.iv_pricestar);
			holder.barbershopavatar = (ImageView) convertView.findViewById(R.id.iv_barbershopavatar);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NearBarberShop barberShop = (NearBarberShop) getItem(position);
		// 向视图填充数据
		holder.barbershopname.setText((String)barberShop.getSName() + "");
		holder.barbershop_discontent.setText((String)barberShop.getSDis() + "");
		holder.servicestarcount.setText((float)barberShop.getServiceStar() + "");
		holder.pricestarcount.setText((float)barberShop.getPriceStar() + "");
		holder.addupcount.setText((int)barberShop.getOrderAddup() + "人");
		holder.distancecount.setText((String)barberShop.getSDistance()+"Km");
		ImageLoader.getInstance().displayImage((String)barberShop.getImageurl(), holder.barbershopavatar,options);
		displayStar((float)barberShop.getServiceStar(), holder.servicestar);
		displayStar((float)barberShop.getPriceStar(), holder.pricestar);
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		ImageView barbershopavatar;
		TextView barbershopname;
		TextView barbershop_discontent;
		ImageView servicestar;
		TextView servicestarcount;
		ImageView pricestar;
		TextView pricestarcount;
		TextView addupcount;
		TextView distancecount;
	}
	
	//根据评分选择星级图片
	public void displayStar(float count,ImageView view) {
		if (count>0&&count<1.0) {
			view.setImageResource(R.drawable.star05);
		}
		if (count==1.0) {
			view.setImageResource(R.drawable.star10);
		}
		if (count>1.0&&count<2.0) {
			view.setImageResource(R.drawable.star15);
		}
		if (count==2.0) {
			view.setImageResource(R.drawable.star20);
		}
		if (count>2.0&&count<3.0) {
			view.setImageResource(R.drawable.star25);
		}
		if (count==3.0) {
			view.setImageResource(R.drawable.star30);
		}
		if (count>3.0&&count<4.0) {
			view.setImageResource(R.drawable.star35);
		}
		if (count==4.0) {
			view.setImageResource(R.drawable.star40);
		}
		if (count>4.0&&count<5.0) {
			view.setImageResource(R.drawable.star45);
		}
		if (count==5.0) {
			view.setImageResource(R.drawable.star50);
		}
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

}
