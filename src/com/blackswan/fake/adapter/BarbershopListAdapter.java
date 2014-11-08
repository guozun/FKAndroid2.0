package com.blackswan.fake.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blackswan.fake.R;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.view.HandyTextView;

public class BarbershopListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<NearBarberShop> items;

	private ViewHolder holder;

	public void ContentAdapter(Context context, List<NearBarberShop> list) {
		mInflater = LayoutInflater.from(context);

		items = list;
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

	@SuppressLint("InflateParams") @Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_barbershop, null);
			holder = new ViewHolder();
			
			holder.barbershopname = (HandyTextView) convertView.findViewById(R.id.tv_barbershopname);
			holder.barbershop_discontent = (HandyTextView) convertView.findViewById(R.id.tv_barbershop_discontent);
			holder.servicestarcount = (HandyTextView) convertView.findViewById(R.id.tv_servicestarcount);
			holder.pricestarcount = (HandyTextView) convertView.findViewById(R.id.tv_pricestarconut);
			holder.addupcount = (HandyTextView) convertView.findViewById(R.id.tv_addupcount);
			holder.distancecount= (HandyTextView) convertView.findViewById(R.id.tv_distancecount);
			holder.servicestar = (ImageView) convertView.findViewById(R.id.iv_servicestar);
			holder.pricestar = (ImageView) convertView.findViewById(R.id.iv_pricestar);
			holder.barbershopavatar = (ImageView) convertView.findViewById(R.id.iv_barbershopavatar);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 向视图填充数据
		holder.barbershopname.setText((String) items.get(position).getSName() + "");
		holder.barbershop_discontent.setText((String) items.get(position).getSDis() + "");
		holder.servicestarcount.setText((float) items.get(position).getServiceStar() + "");
		holder.pricestarcount.setText((float) items.get(position).getPriceStar() + "");
		holder.addupcount.setText((float) items.get(position).getOrderAddup() + "人");
		holder.distancecount.setText((float) items.get(position).getSDistance() + "Km");
		holder.barbershopavatar.setImageBitmap(getBitmapFromUrl((String) items.get(position).getImageurl()));
		holder.pricestar.setImageBitmap(displayStar((float) items.get(position).getPriceStar()));
		holder.servicestar.setImageBitmap(displayStar((float) items.get(position).getServiceStar()));

		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		ImageView barbershopavatar;
		HandyTextView barbershopname;
		HandyTextView barbershop_discontent;
		ImageView servicestar;
		HandyTextView servicestarcount;
		ImageView pricestar;
		HandyTextView pricestarcount;
		HandyTextView addupcount;
		HandyTextView distancecount;
	}
	
	//根据评分选择星级图片
	public Bitmap displayStar(float count) {
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL("");
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	private Bitmap getBitmapFromUrl(String imgUrl) {
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(imgUrl);
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
