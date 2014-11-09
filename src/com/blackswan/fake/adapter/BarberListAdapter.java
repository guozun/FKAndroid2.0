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
import com.blackswan.fake.bean.NearBarber;
import com.blackswan.fake.view.HandyTextView;

public class BarberListAdapter extends BaseObjectListAdapter
{
	public BarberListAdapter(BaseApplication application, Context context,
			List<? extends Entity> datas) {
		super(application, context, datas);
	}

	private ViewHolder holder;
	
	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_barber, null);
			holder = new ViewHolder();
			holder.barbername = (HandyTextView) convertView.findViewById(R.id.tv_barbername);
			holder.barberdis = (HandyTextView) convertView.findViewById(R.id.tv_barberdis);
			holder.appraisestarcount = (HandyTextView) convertView.findViewById(R.id.tv_appraisecount);
			holder.addupcount = (HandyTextView) convertView.findViewById(R.id.tv_barberaddup);
			holder.barberdistance= (HandyTextView) convertView.findViewById(R.id.tv_barberdistancecount);
			holder.barberavatar = (ImageView) convertView.findViewById(R.id.iv_barberavatar);
			holder.appraisestar = (ImageView) convertView.findViewById(R.id.iv_barberappraisestar);
			holder.barbersex = (ImageView) convertView.findViewById(R.id.iv_barbersex);
			holder.barberage = (HandyTextView) convertView.findViewById(R.id.tv_barberage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NearBarber barber = (NearBarber) getItem(position);
		// 向视图填充数据
		holder.barbername.setText((String)barber.getBName() + "");
		holder.barberdis.setText((String) barber.getBDis() + "");
		holder.appraisestarcount.setText((float) barber.getAppraiseStar() + "");
		holder.addupcount.setText((float) barber.getOrderAddup() + "人");
		holder.barberdistance.setText((float) barber.getBDistance() + "Km");
		holder.barberavatar.setImageBitmap(getBitmapFromUrl((String) barber.getImageurl()));
		holder.appraisestar.setImageBitmap(displayStar((float) barber.getAppraiseStar()));
		holder.barbersex.setImageBitmap(displaySex((String) barber.getBSex()));
		holder.barberage.setText((String)barber.getBAge());
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		ImageView barberavatar;
		HandyTextView barbername;
		HandyTextView barberdis;
		ImageView appraisestar;
		HandyTextView appraisestarcount;
		HandyTextView addupcount;
		HandyTextView barberdistance;
		ImageView barbersex;
		HandyTextView barberage;
	}
	
	//根据性别显示图片
	public Bitmap displaySex(String sex) {
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
