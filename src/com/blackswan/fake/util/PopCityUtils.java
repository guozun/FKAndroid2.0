package com.blackswan.fake.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blackswan.fake.bean.MyRegion;

public class PopCityUtils {
	private Context context;
	private Handler handler;
	public PopCityUtils(final Context context,final Handler handler){
		this.context = context;
		this.handler = handler;
	}
	

	/**
	 * 初始化区
	 * @param cname 城市名
	 */
	public  void initDistricts(final String cname) {
		new Thread() {
			@Override
			public void run() {
				DBManager dbm = new DBManager(
						context);
				dbm.openDateBase();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
		                + DBManager.DB_NAME, null);
				String pcode=null;
				ArrayList<MyRegion> list = new ArrayList<MyRegion>();
				try {
					String sqlString = "SELECT id FROM REGION WHERE name='"+cname+"'";
					Cursor c = db.rawQuery(sqlString, null);
					c.moveToFirst();
					pcode = c.getString(0);
					Log.i("selectcity","选中的城市为："+cname+"城市编号为："+pcode);
					String sql = "SELECT id,name FROM REGION WHERE parent_id='"
							+ pcode + "'"+" UNION SELECT id,name FROM REGION WHERE ID='"+pcode+"'";
					Cursor cursor = db.rawQuery(sql, null);
					// cursor.moveToFirst();
					while (cursor.moveToNext()) {
						String id=cursor.getString(0);
						MyRegion myListItem = new MyRegion();
						myListItem.setId(id);
						if(id.equalsIgnoreCase(pcode)){
							myListItem.setName("全市");
						}else{
							myListItem.setName(cursor.getString(1));
						}
						myListItem.setParent_id(pcode);
						list.add(myListItem);
					}
					dbm.closeDatabase();
					db.close();
					Message msg = new Message();
					msg.what = 3;
					msg.obj = list;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
	

}
