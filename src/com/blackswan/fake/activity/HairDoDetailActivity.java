package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.base.FaKeConstants;
import com.blackswan.fake.util.Utility;
import com.blackswan.fake.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class HairDoDetailActivity extends BaseActivity implements
		OnTouchListener {
	protected final int PIC_NUM = 5;
	private TextView barber;
	private ImageView barberpic;
	// 完成图片消失的动画的图片
	private ImageView imageGone;
	private ViewPager mViewPager;
	private DisplayImageOptions options;
	private Button order;
	private ImageView ivBack;
	// 赞数 顶数
	private TextView upNum;
	private TextView downNum;
	// 赞 顶 按钮
	private ImageView upImg;
	private ImageView downImg;
	// 进度条
	private ProgressWheel mProgressWheel;
	// ImageSwitch
	private List<String> imgUrl = new ArrayList<String>();
	private int currentPosition = 0;
	private ImageView[] imgPage = new ImageView[PIC_NUM];
	// 小点点
	private ImageView[] drops = new ImageView[PIC_NUM];
	// 存放 上下两个相册的图片
	private ImageView[] cacheImage = new ImageView[2];
	/**
	 * 按下点的X坐标
	 */
	private float downY;

	public final int SHARE = 1;

	private UMSocialService mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hair_do_detail);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 填入测试数据
		barber = (TextView) findViewById(R.id.tv_hairdo_detail_barber);
		barberpic = (ImageView) findViewById(R.id.iv_rotat_hairdo_detail);
		mViewPager = (ViewPager) findViewById(R.id.id_hairdo_detail_pic);
		order = (Button) findViewById(R.id.bt_hairdo_detail_order);
		mProgressWheel = (ProgressWheel) findViewById(R.id.id_hairdo_detail_progress);
		imageGone = (ImageView) findViewById(R.id.id_hairdo_detail_pic_cache);
		ivBack = (ImageView) findViewById(R.id.iv_hairdo_detail_back);
		upNum = (TextView) findViewById(R.id.tv_hairdo_item_good);
		downNum = (TextView) findViewById(R.id.tv_hairdo_item_bad);
		upImg = (ImageView) findViewById(R.id.iv_hairdo_detail_good);
		downImg = (ImageView) findViewById(R.id.iv_hairdo_detail_bad);

		findViewById(R.id.iv_hairdo_detail_share).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// DialogFragment df = new DialogFragment();
						// df.set
						// ShareDialog sd = new
						// ShareDialog(HairDoDetailActivity.this,R.style.style_dialog_share);
						// sd.show();
						// Intent intent = new Intent(HairDoDetailActivity.this,
						// ShareActicity.class);
						// startActivityForResult(intent, SHARE);
						initShareContext();
						mController.openShare(HairDoDetailActivity.this, false);
					}
				});
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
		upImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		downImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mProgressWheel.setVisibility(View.GONE);
		imageGone.setImageLevel(1);
		mViewPager.setOnTouchListener(this);
		initDrops();

		cacheImage[0] = new ImageView(this);
		cacheImage[1] = new ImageView(this);
		this.initViews();
		refreshDrops();

	}

	// private void ShareToWX(int scene) {
	// // 初始化一个WXTextObject对象
	// WXTextObject textObj = new WXTextObject();
	// textObj.text = "text";
	//
	// // 用WXTextObject对象初始化一个WXMediaMessage对象
	// WXMediaMessage msg = new WXMediaMessage();
	// msg.mediaObject = textObj;
	// // 发送文本类型的消息时，title字段不起作用
	// // msg.title = "Will be ignored";
	// msg.description = "description";
	//
	// // 构造一个Req
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.scene = scene;
	// req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
	// req.message = msg;
	// // req.scene = isTimelineCb.isChecked() ?
	// // SendMessageToWX.Req.WXSceneTimeline :
	// // SendMessageToWX.Req.WXSceneSession;
	//
	// // 调用api接口发送数据到微信
	// IWXAPI api = WXAPIFactory.createWXAPI(this, WXUtils.APP_ID);
	// api.sendReq(req);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		// switch (requestCode) {
		// case SHARE:
		// if (resultCode == RESULT_OK) {
		// if (ShareActicity.SHARE_WX_FRIEND_CRICLE == data.getIntExtra(
		// ShareActicity.SHARE_TYPE, 0)) {
		//
		// // 对话
		// // ShareToWX(SendMessageToWX.Req.WXSceneSession);
		//
		// }
		//
		// }
		// break;
		//
		// default:
		// break;
		// }
	}

	private void initShareContext() {

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 要分享的文字内容
		String mShareContent = "小巫CSDN博客客户端，CSDN移动开发专家——IT_xiao小巫的专属客户端，你值得拥有。";
		mController.setShareContent(mShareContent);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_64x64);

		UMImage mUMImgBitmap = new UMImage(this, bitmap);
		mController.setShareImage(mUMImgBitmap);
		mController.setAppWebSite(""); // 设置应用地址

		// 添加新浪和qq空间的SSO授权支持
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO支持
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, FaKeConstants.WX_APP_ID);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,
				FaKeConstants.WX_APP_ID);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(mShareContent);
		// 设置title
		weixinContent.setTitle("小巫CSDN博客客户端");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl("http://blog.csdn.net/wwj_748");
		// 设置分享图片
		weixinContent.setShareImage(mUMImgBitmap);
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(mShareContent);
		// 设置朋友圈title
		circleMedia.setTitle("小巫CSDN博客客户端");
		circleMedia.setShareImage(mUMImgBitmap);
		circleMedia.setTargetUrl("你的http://blog.csdn.net/wwj_748链接");
		mController.setShareMedia(circleMedia);

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1102369913",
				"62ru775qbkentOUp");
		qqSsoHandler.addToSocialSDK();

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1102369913", "62ru775qbkentOUp");
		qZoneSsoHandler.addToSocialSDK();

		// 添加人人网SSO授权功能
		// APPID:201874
		// API Key:28401c0964f04a72a14c812d6132fcef
		// Secret:3bf66e42db1e4fa9829b955cc300b737
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(this,
				"271529", "682c45dbdeba4b608922fef124223efb",
				"2c7c3b63f58b4bfcad3665b49e65d47f");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();

		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(mShareContent);
		qqShareContent.setTitle("小巫CSDN博客");
		qqShareContent.setShareImage(mUMImgBitmap);
		qqShareContent.setTargetUrl("http://blog.csdn.net/wwj_748");
		mController.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(mShareContent);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl("http://blog.csdn.net/wwj_748");
		// 设置分享内容的标题
		qzone.setTitle("小巫CSDN博客");
		// 设置分享图片
		qzone.setShareImage(mUMImgBitmap);
		mController.setShareMedia(qzone);

	}

	@Override
	protected void initViews() {
		imgUrl.add("http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c478e556ff8f2b21193138a26.jpg");
		imgUrl.add("http://g.hiphotos.baidu.com/image/pic/item/f31fbe096b63f624b55c6a738444ebf81a4ca3b9.jpg");
		imgUrl.add("http://e.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9eae1d75710d739b6003af3b319.jpg");
		imgUrl.add("http://c.hiphotos.baidu.com/image/w%3D230/sign=beeba198a0cc7cd9fa2d33da09012104/0823dd54564e9258c34d3c9b9f82d158ccbf4e4d.jpg");

		for (int i = 0; i < imgUrl.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setImageLevel(0);
			imgPage[i] = iv;
		}
		barber.setText("11111");
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 进行预约操作
			}
		});
		// 进行上下图片缓存
		ImageLoader
				.getInstance()
				.displayImage(
						"http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f0820287c2ab3c48fbfbedab641b64.jpg",
						cacheImage[0], options);

		ImageLoader
				.getInstance()
				.displayImage(
						"http://img0.bdstatic.com/img/image/shouye/mnnll-14204092064.jpg",
						barberpic, options);
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return imgUrl.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(imgPage[position]);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				ImageLoader.getInstance().displayImage(imgUrl.get(position),
						imgPage[position], options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								mProgressWheel.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}

						}, new ImageLoadingProgressListener() {

							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								if (current > 45 && current < 180) {
									mProgressWheel.setVisibility(View.VISIBLE);
								}
								mProgressWheel.setProgress(current * 360
										/ total);

							}
						});

				container.addView(imgPage[position]);
				return imgPage[position];

			}
		});
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						changeDrops(currentPosition, arg0);
						currentPosition = arg0;
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	private void showNext() {
		imgUrl.clear();
		imgUrl.add("http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f0820287c2ab3c48fbfbedab641b64.jpg");
		imgUrl.add("http://d.hiphotos.baidu.com/image/pic/item/dc54564e9258d109207093b1d258ccbf6c814d25.jpg");
		imgUrl.add("http://d.hiphotos.baidu.com/image/pic/item/4afbfbedab64034f9b88f805acc379310a551d6f.jpg");
		imgUrl.add("http://a.hiphotos.baidu.com/image/pic/item/aa18972bd40735fa369774ba9d510fb30f240849.jpg");

		for (int i = 0; i < imgUrl.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setImageLevel(0);
			imgPage[i] = iv;
		}
		mViewPager.getAdapter().notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		mViewPager.setBackgroundColor(Color.BLACK);
		imageGone.setVisibility(View.GONE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downY = event.getY();
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastY = event.getY();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastY > downY) {

			}

			else if ((downY - lastY) > mScreenHeight / 10) {
				// Log.d("", "上华中");
				AnimationSet mAnimationSet = new AnimationSet(false);
				TranslateAnimation translate = new TranslateAnimation(0, 0, 0,
						-mScreenHeight);
				AlphaAnimation alpha = new AlphaAnimation((float) 1.0,
						(float) 0.3);
				ScaleAnimation scale = new ScaleAnimation((float) 1.0,
						(float) 1.0, (float) 1.0, (float) 0.5);
				mAnimationSet.addAnimation(translate);
				mAnimationSet.addAnimation(alpha);
				mAnimationSet.addAnimation(scale);
				mAnimationSet.setDuration(500);

				imageGone.setImageDrawable(imgPage[mViewPager.getCurrentItem()]
						.getDrawable());
				imageGone.setVisibility(View.VISIBLE);
				imageGone.startAnimation(mAnimationSet);

				mViewPager.setBackgroundDrawable(cacheImage[0].getDrawable());
				mViewPager.removeAllViews();
				showNext();
			}
		}
		}
		return false;
	}

	private void initDrops() {
		// 控制位置
		int marginSum = 30;
		RelativeLayout relat = (RelativeLayout) findViewById(R.id.id_hairdo_detail_bar);
		int i;
		for (i = 0; i < PIC_NUM; i++) {
			drops[i] = new ImageView(this);
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
					Utility.Dp2Px(this, 15), Utility.Dp2Px(this, 15));
			if (i == 0) {
				parms.leftMargin = Utility.Dp2Px(this, marginSum);
				parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);

			} else {
				parms.leftMargin = Utility.Dp2Px(this, marginSum);
			}
			parms.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			relat.addView(drops[i], parms);
			marginSum += 24;
		}

	}

	private void refreshDrops() {
		int i;
		for (i = 0; i < imgUrl.size(); i++) {
			if (i == 0) {
				drops[i].setImageResource(R.drawable.bg_drop_no_lack);
			} else {
				drops[i].setImageResource(R.drawable.bg_drop_lack);
			}
			drops[i].setVisibility(View.VISIBLE);
		}
		for (; i < PIC_NUM; i++) {
			drops[i].setVisibility(View.GONE);
		}
	}

	private void changeDrops(int old, int now) {
		drops[now].setImageResource(R.drawable.bg_drop_no_lack);
		drops[old].setImageResource(R.drawable.bg_drop_lack);
	}
}
