package com.blackswan.fake.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Entity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseApplication;
import com.blackswan.fake.base.BaseObjectListAdapter;
import com.blackswan.fake.bean.NearBarberShop;
import com.blackswan.fake.view.HandyTextView;

public class BarbershopListAdapter extends BaseObjectListAdapter
{
	public BarbershopListAdapter(BaseApplication application, Context context,
			List<?> datas) {
		super(application, context, datas);
	}

	private ViewHolder holder;

	@SuppressLint("InflateParams") 
	@Override
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
		NearBarberShop barberShop = (NearBarberShop) getItem(position);
		// 向视图填充数据
		holder.barbershopname.setText((String)barberShop.getSName() + "");
		holder.barbershop_discontent.setText((String)barberShop.getSDis() + "");
		holder.servicestarcount.setText((float)barberShop.getServiceStar() + "");
		holder.pricestarcount.setText((float)barberShop.getPriceStar() + "");
		holder.addupcount.setText((int)barberShop.getOrderAddup() + "人");
		holder.distancecount.setText((String)barberShop.getSDistance());
		holder.barbershopavatar.setImageBitmap(getBitmapFromUrl((String)barberShop.getImageurl()));
		holder.pricestar.setImageBitmap(displayStar((float)barberShop.getPriceStar()));
		holder.servicestar.setImageBitmap(displayStar((float)barberShop.getServiceStar()));

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
