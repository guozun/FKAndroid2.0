package com.blackswan.fake.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.blackswan.fake.activity.useractivity.PersonalInfoActivity;

/**
 * 获取登录注册结果
 * 
 * @author Administrator
 */
public class UserUtils {
	/**
	 * 获取登录结果
	 * 
	 * @param path
	 * @param data
	 *            :要传的参数
	 * @return
	 */
	public static String loginResult(String path, Map<String, String> data) {
		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();
			// 设置跳转方式
			HttpPost post = new HttpPost(path);
			// 提交的参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : data.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			// 要提交的实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
			post.setEntity(entity);// 提交请求

			// 执行post请求，获取服务器响应
			HttpResponse response = client.execute(post);
			// 判断状态
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获取服务器响应内容
				// 将响应的实体，转换为字符串
				result = EntityUtils.toString(response.getEntity());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static String registerResult(String path, Map<String, String> data) {
		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();
			// 设置跳转方式
			HttpPost post = new HttpPost(path);
			// 提交的参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : data.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			// 要提交的实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
			post.setEntity(entity);// 提交请求

			// 执行post请求，获取服务器响应
			HttpResponse response = client.execute(post);
			// 判断状态
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.d("geek", "响应结果");
				// 获取服务器响应内容
				// 将响应的实体，转换为字符串
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}

	public static Bitmap getUserHeadFromSD(Context context) {
		String path = FileUtils.getDiskCacheDir(context,
				PersonalInfoActivity.HEAD_PATH)
				+ File.separator
				+ PersonalInfoActivity.HEAD_NAME;
		File mfile = new File(path);
		if (mfile.exists()) {// 若该文件存在
			Bitmap bm = BitmapFactory.decodeFile(path);
			return bm;
		}

		return null;
	}

	public static void saveHeadPicToSD(Context context, Bitmap mBitmap) {
		FileOutputStream b = null;
		File path = FileUtils.getDiskCacheDir(context,
				PersonalInfoActivity.HEAD_PATH);// ,
												// File.separator+PersonalInfoActivity.HEAD_NAME);
		path.mkdirs();
		path = new File(path, PersonalInfoActivity.HEAD_NAME);
		if (!path.exists()) {
			try {
				path.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
				return;
			}
		}
		try {
			b = new FileOutputStream(path);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
