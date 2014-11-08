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
import com.blackswan.fake.bean.NearBarber;
import com.blackswan.fake.view.HandyTextView;

public class BarberListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<NearBarber> items;

	private ViewHolder holder;

	public void ContentAdapter(Context context, List<NearBarber> list) {
		mInflater = LayoutInflater.from(context);

		items = list;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

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

		// 向视图填充数据
		holder.barbername.setText((String) items.get(position).getBName() + "");
		holder.barberdis.setText((String) items.get(position).getBDis() + "");
		holder.appraisestarcount.setText((float) items.get(position).getAppraiseStar() + "");
		holder.addupcount.setText((float) items.get(position).getOrderAddup() + "人");
		holder.barberdistance.setText((float) items.get(position).getBDistance() + "Km");
		holder.barberavatar.setImageBitmap(getBitmapFromUrl((String) items.get(position).getImageurl()));
		holder.appraisestar.setImageBitmap(displayStar((float) items.get(position).getAppraiseStar()));
		holder.barbersex.setImageBitmap(displaySex((String) items.get(position).getBSex()));
		holder.barberage.setText((String) items.get(position).getBAge());
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
