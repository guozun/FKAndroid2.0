package com.blackswan.fake.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.blackswan.fake.R;
import com.blackswan.fake.bean.Advertisement;
import com.blackswan.fake.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("ClickableViewAccessibility")
public class HairdoSlideShowView extends RelativeLayout implements
		OnTouchListener, OnClickListener, OnGestureListener {
	private ImageView imgA;
	private ImageView imgB;
	private List<Advertisement> advers;
	private ImageView[] drops;// 小点点
	private Bitmap[] imgCache;// 缓存图片
	private GestureDetector detector;
	private DisplayImageOptions options;
	ImageLoader loader = ImageLoader.getInstance();
	private Scroller mScrollerA;
	private Scroller mScrollerB;
	private Boolean current = true;// true->ImgA ,false->ImgB
	private Boolean currentDirection;// true->右侧图片计入
	private int currentItme = 0; // 当前页
	private int nextItme = 0; // 期望的下一页页
	private int imgPositionA; // 记录imgA的坐标
	private int imgPositionB; // 记录imgB的坐标
	private int moveDistance; // 此次移动累计距离

	private int imgWidth;
	private final int CHANGE_TIME = 1000;
	private final int SCROLL_RADIO = 1;

	public final int PERIOD_INIT = 0;
	public final int PERIOD_DOWN = 1;
	public final int PERIOD_SCROLL = 2;
	private int Period = PERIOD_INIT; //

	private Handler handler;
	private Runnable runnable;

	public int getPeriod() {
		return Period;
	}

	public HairdoSlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public HairdoSlideShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public HairdoSlideShowView(Context context) {
		super(context);
		initView(context);
	}

	public void setContent() {
		advers = new ArrayList<Advertisement>();
		advers.add(new Advertisement(
				"http://f.hiphotos.baidu.com/image/h%3D360/sign=b8c3c6ecd2c8a786a12a4c085708c9c7/5bafa40f4bfbfbed36b850697bf0f736aec31fc6.jpg"));
		advers.add(new Advertisement(
				"http://a.hiphotos.baidu.com/image/h%3D360/sign=1a48d428daf9d72a0864161be42a282a/4ec2d5628535e5dde71515c875c6a7efce1b62ef.jpg"));
		advers.add(new Advertisement(
				"http://b.hiphotos.baidu.com/image/h%3D360/sign=f4a414a5b1b7d0a264c9029bfbee760d/b2de9c82d158ccbf3511cd2d1ad8bc3eb1354173.jpg"));
		advers.add(new Advertisement(
				"http://e.hiphotos.baidu.com/image/pic/item/5243fbf2b21193130ccf6ca766380cd791238d1d.jpg"));
		advers.add(new Advertisement(
				"http://e.hiphotos.baidu.com/image/pic/item/0b7b02087bf40ad11edc57f5542c11dfa9eccee8.jpg"));
	}

	protected void initView(Context context) {
		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.hairdo_silde_show, this, true);
		mScrollerA = new Scroller(context);
		mScrollerB = new Scroller(context);
		setContent();
		detector = new GestureDetector(this);
		imgA = (ImageView) findViewById(R.id.hairdo_silde_imageA);
		imgB = (ImageView) findViewById(R.id.hairdo_silde_imageB);

		initDrops(context);
		refreshDrops();
		setOnTouchListener(this);
		setOnClickListener(this);
		initImg(context);

		ViewTreeObserver vto2 = imgA.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				imgA.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				imgWidth = imgA.getWidth();
			}
		});
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				initImgPosition(true);
				finishImgScroll(true);
				handler.postDelayed(this, 5000);

			}
		};
		startAutoPlay();
	}
	private void stopAutoPlay(){
		handler.removeCallbacks(runnable); 
	}
	private void startAutoPlay(){
		handler.postDelayed(runnable, 5000);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public void computeScroll() {
		// 先判断mScroller滚动是否完成
		if (mScrollerA.computeScrollOffset()) {
			// 这里调用View的scrollTo()完成实际的滚动
			imgA.scrollTo(mScrollerA.getCurrX(), mScrollerA.getCurrY());
			// Log.d("mScrollerA getCurrX", mScrollerA.getCurrX() + "");
		}
		if (mScrollerB.computeScrollOffset()) {
			// 这里调用View的scrollTo()完成实际的滚动
			imgB.scrollTo(mScrollerB.getCurrX(), mScrollerB.getCurrY());
			// Log.d("B", mScrollerB.getCurrX() + "+" + mScrollerB.getCurrY());
			// Log.i("mScrollerB getCurrX", mScrollerB.getCurrX() + "");
		}
		// 必须调用该方法，否则不一定能看到滚动效果
		postInvalidate();
		super.computeScroll();

	}

	/**
	 * 根据 得到下一个显示图片的索引
	 * 
	 * @param true->右
	 */
	private int getNextPosition(Boolean lr) {
		if (lr) {
			if (currentItme >= advers.size() - 1) {
				return 0;
			} else {
				return currentItme + 1;
			}
		} else {

			if (currentItme <= 0) {
				return advers.size() - 1;
			} else {
				return currentItme - 1;
			}
		}

	}

	private void scrollTraceFiger(int x) {
		int test;// 测试是否 跟随手指 到达快要到达指定位置
		if (current) {
			// B要进来,, A在上面,把A换到下面去
			imgPositionA += x / 2;
			imgPositionB += x;
			// mScrollerA.setFinalX();
			// mScrollerB.setFinalX(x);
			// mScrollerA.setFinalX(mScrollerA.getCurrX()+x / 2);
			// mScrollerB.setFinalX(mScrollerB.getCurrX()+x);
			// imgA.scrollBy(x / 2, 0);
			// imgB.scrollBy(x, 0);
			// if(imgPositionB)
			test = imgPositionB;
		} else {
			// A要进来,,, B在上面,把B换到下面去
			imgPositionA += x;
			imgPositionB += x / 2;
			// mScrollerB.setFinalX(mScrollerB.getCurrX()+x / 2);
			// mScrollerA.setFinalX(mScrollerA.getCurrX()+x);
			// imgB.scrollBy(x / 2, 0);
			// imgA.scrollBy(x, 0);
			test = imgPositionA;
		}

		imgA.scrollTo(imgPositionA, 0);
		imgB.scrollTo(imgPositionB, 0);
		if (Math.abs(test) < 50) {
			Period = PERIOD_INIT;
			finishImgScroll(currentDirection);
		}
		// Log.d("A设置", imgPositionA + "");
		// Log.d("B设置", imgPositionB + "");
		// invalidate();
	}

	/**
	 * 为图像位置动画 进行该次位置初始化
	 * 
	 * @param true->右边图片进入
	 */
	private void initImgPosition(Boolean dir) {
		nextItme = getNextPosition(dir);
		if (current) {
			// A在上面,把A换到下面去
			imgB.bringToFront();
			imgB.setImageBitmap(imgCache[nextItme]);
			if (dir) {
				imgPositionA = 0;
				imgPositionB = -imgWidth;
				// mScrollerB.setFinalX(-imgWidth);
				// imgB.scrollTo(-imgWidth, 0);
			} else {
				imgPositionA = 0;
				imgPositionB = imgWidth;
				// mScrollerB.setFinalX(imgWidth);
				// imgB.scrollTo(imgWidth, 0);
			}
			imgB.scrollTo(imgPositionB, 0);
		} else {
			// B在上面,把B换到下面去
			imgA.setImageBitmap(imgCache[nextItme]);
			imgA.bringToFront();
			if (dir) {
				imgPositionA = -imgWidth;
				imgPositionB = 0;
				// mScrollerA.setFinalX(-imgWidth);
				// imgA.scrollTo(-imgWidth, 0);
			} else {
				imgPositionA = imgWidth;
				imgPositionB = 0;
				// mScrollerA.setFinalX(imgWidth);
				// imgA.scrollTo(imgWidth, 0);
			}
			imgA.scrollTo(imgPositionA, 0);
		}
		// invalidate();
		// Log.d("A初始化imgPositionA", imgPositionA + "");
		// Log.d("B初始化imgPositionB", imgPositionB + "");
	}

	/**
	 * 手指松了之后 完成图像的移动
	 * 
	 * @param true->右边图片进入
	 */
	private void finishImgScroll(Boolean dir) {
		int diffAx; // A的移动距离
		int diffBx;
		if (dir) {
			if (current) {
				// A为当前 右边B图片进入
				// finalAx = imgWidth / 2;
				// finalBx = 0;
				diffAx = imgWidth / 2 - imgPositionA;
				diffBx = 0 - imgPositionB;
			} else {
				// B为当前 右边A图片进入
				// finalBx = imgWidth / 2;
				// finalAx = 0;
				diffBx = imgWidth / 2 - imgPositionB;
				diffAx = 0 - imgPositionA;
			}
		} else {
			if (current) {
				// A为当前 左边边B图片进入
				// finalAx = -imgWidth / 2;
				// finalBx = 0;
				diffAx = -imgWidth / 2 - imgPositionA;
				diffBx = 0 - imgPositionB;
			} else {
				// B为当前 左边边A图片进入
				// finalBx = -imgWidth / 2;
				// finalAx = 0;
				diffBx = -imgWidth / 2 - imgPositionB;
				diffAx = 0 - imgPositionA;
			}
		}
		int time = Math.min(Math.abs(diffAx), Math.abs(diffBx));
		time = CHANGE_TIME * time / imgWidth;

		// mScrollerA.startScroll(imgPositionA, 0, diffAx, 0,
		// Math.abs(CHANGE_TIME * diffAx / imgWidth));
		// mScrollerB.startScroll(imgPositionB, 0, diffBx, 0,
		// Math.abs(CHANGE_TIME * diffBx / imgWidth));
		mScrollerA.startScroll(imgPositionA, 0, diffAx, 0, time);
		mScrollerB.startScroll(imgPositionB, 0, diffBx, 0, time);

		changeDrops(currentItme, nextItme);
		currentItme = nextItme;
		current = !current;
		invalidate();
		// Log.e("重点A", mScrollerA.getFinalX() + "");
		// Log.e("重点B", mScrollerB.getFinalX() + "");
		// Log.e("A时间", CHANGE_TIME * diffAx / imgWidth + "");
		// Log.e("B时间", CHANGE_TIME * diffBx / imgWidth + "");

	}

	/**
	 * 手指松了之后 完成图像的回滚，用于 滑动很小距离的时候
	 * 
	 * @param true->右边图片进入
	 */
	private void imgScrollBack(Boolean dir) {
		int diffAx; // A的移动距离
		int diffBx;
		if (dir) {
			if (current) {
				// A为当前 右边B图片进入
				diffAx = 0 - imgPositionA;
				diffBx = -imgWidth - imgPositionB;
			} else {
				// B为当前 右边A图片进入
				diffBx = 0 - imgPositionB;
				diffAx = -imgWidth - imgPositionA;
			}
		} else {
			if (current) {
				// A为当前 左边边B图片进入
				diffAx = 0 - imgPositionA;
				diffBx = imgWidth - imgPositionB;
			} else {
				// B为当前 左边边A图片进入
				diffBx = 0 - imgPositionB;
				diffAx = imgWidth - imgPositionA;
			}
		}
		int time = Math.min(Math.abs(diffAx), Math.abs(diffBx));
		time = CHANGE_TIME * time / imgWidth;

		mScrollerA.startScroll(imgPositionA, 0, diffAx, 0, time);
		mScrollerB.startScroll(imgPositionB, 0, diffBx, 0, time);

		// changeDrops(currentItme, nextItme);
		// currentItme = nextItme;
		// current = !current;
		invalidate();
	}

	// 图像资源 一次性 全部加载
	private void initImg(Context context) {
		imgCache = new Bitmap[advers.size()];
		imgLoadTask task = new imgLoadTask();
		task.execute("");
	}

	private class imgLoadTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values[0] == 50 && advers.size() > 0) {

				// 第一张图片 display
				loader.displayImage(advers.get(0).getImgUrl(), imgA, options,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								imgCache[0] = loadedImage;
								imgA.invalidate();

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}
						});

			}
		}

		@Override
		protected Bitmap doInBackground(String... params) {

			// TODO 先去访问服务器 取得JSON

			// 然后UI更新第一个图片
			publishProgress(50);

			// 再去异步加载图像

			int i;
			for (i = 1; i < advers.size(); i++) {
				imgCache[i] = loader.loadImageSync(advers.get(i).getImgUrl(),
						options);

			}
			return null;
		}

	}

	// 小点点的初始化
	protected void initDrops(Context context) {
		drops = new ImageView[advers.size()];
		// 控制位置
		int marginSum = 30;
		int i;
		for (i = advers.size() - 1; i >= 0; i--) {
			drops[i] = new ImageView(context);
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
					Utility.Dp2Px(context, 10), Utility.Dp2Px(context, 10));
			parms.rightMargin = Utility.Dp2Px(context, marginSum);
			parms.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			parms.bottomMargin = Utility.Dp2Px(context, 5);
			addView(drops[i], parms);
			marginSum += 15;
		}

	}

	// 刷新小点点
	private void refreshDrops() {
		int i;
		for (i = 0; i < advers.size(); i++) {
			if (i == 0) {
				drops[i].setImageResource(R.drawable.bg_drop_no_lack);
			} else {
				drops[i].setImageResource(R.drawable.bg_drop_lack);
			}
			drops[i].setVisibility(View.VISIBLE);
		}
	}

	private void changeDrops(int old, int now) {
		drops[now].setImageResource(R.drawable.bg_drop_no_lack);
		drops[old].setImageResource(R.drawable.bg_drop_lack);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.detector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// Log.i("", "Up");
			if (PERIOD_SCROLL == Period) {
				// 如果周期开始，该周期随着 手指抬起结束
				Period = PERIOD_INIT;
				// if (Math.abs(moveDistance) < 300) {
				// imgScrollBack(currentDirection);
				// } else {
				startAutoPlay();
				finishImgScroll(currentDirection);
				// }

			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// Log.i("", "onDown");
		if (PERIOD_INIT == Period && mScrollerA.isFinished()
				&& mScrollerB.isFinished()) {
			Period = PERIOD_DOWN;
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// Log.i("", "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 加上点击动作
		Log.i("", "onSingleTapUp" + currentItme);
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (PERIOD_DOWN == Period) {
			// 根据方向给图像设定初始化
			if (distanceX == 0) {
				return false;
			} else if (distanceX < 0) {
				currentDirection = false;
			} else {
				currentDirection = true;
			}
			stopAutoPlay();
			moveDistance = 0;
			initImgPosition(currentDirection);
			Period = PERIOD_SCROLL;
		}
		if (PERIOD_SCROLL == Period) {
			int buff = (int) (distanceX / SCROLL_RADIO);
			scrollTraceFiger(buff);
			moveDistance += distanceX;
		}
		// Log.i("", "onScroll" + distanceX);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// Log.i("", "onLongPress");

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Log.i(Period + "   Period", "onFling");
		// Log.i(velocityX + "   velocityX", velocityX + "onFling");
		// int dis = (int) Math.abs(e2.getX() - e1.getX());
		// dis = imgWidth / dis;
		if (PERIOD_SCROLL == Period) {
			Period = PERIOD_INIT;
			finishImgScroll(currentDirection);
			startAutoPlay();
			return true;
		} else {
			return false;
		}
	}

}
