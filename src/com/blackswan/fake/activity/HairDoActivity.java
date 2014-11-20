package com.blackswan.fake.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.view.HairdoSlideShowView;
import com.blackswan.fake.view.HandyTextView;
import com.blackswan.fake.view.ProgressWheel;
import com.huewu.pla.lib.MultiColumnListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class HairDoActivity extends BaseActivity {

	LinkedList<String> imageList = new LinkedList<String>();
	private DisplayImageOptions options;
	private LayoutInflater inflater;
	MultiColumnListView mmListView;
	HairdoSlideShowView mHairdoSlideShowView;
	// HeadView
	private final static int RELEASE_TO_REFRESH = 0;
	private final static int PULL_TO_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	private final static int RATIO = 3;

	private LinearLayout mHeader;

	private HandyTextView mHtvTitle;
	private HandyTextView mHtvTime;
	private ImageView mIvArrow;
	private ImageView mIvLoading;
	private ImageView mIvCancel;

	private android.view.animation.RotateAnimation mPullAnimation;
	private android.view.animation.RotateAnimation mReverseAnimation;
	private Animation mLoadingAnimation;

	private boolean mIsRecored;

	private int mHeaderHeight;

	private int mStartY;

	private int mState;

	private boolean mIsBack;
	private boolean mIsRefreshable;
	private boolean mIsCancelable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hairdo);
		inflater = getLayoutInflater();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		initViews();

	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void initViews() {

		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043531502.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043532264.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533581.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533571.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534672.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534854.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535929.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535784.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536626.jpg");
		imageList
				.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536244.jpg");
		mmListView = (MultiColumnListView) findViewById(R.id.hairdo_mm_list_view);
		findViewById(R.id.tv_hairdo_select).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(HairdoFilterActivity.class);
					}
				});
		initWithContext(this);
		mmListView.setOnTouchListener(new View.OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent ev) {

				int action = ev.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (mIsRefreshable) {
						if (mmListView.getFirstVisiblePosition() == 0
								&& !mIsRecored) {
							mIsRecored = true;
							mStartY = (int) ev.getY();
						}
					}

					break;

				case MotionEvent.ACTION_MOVE:
					int moveY = (int) ev.getY();
					if (mIsRefreshable) {
						if (!mIsRecored
								&& mmListView.getFirstVisiblePosition() == 0) {
							mIsRecored = true;
							mStartY = moveY;
						}
						if (mState != REFRESHING && mIsRecored
								&& mState != LOADING) {
							if (mState == RELEASE_TO_REFRESH) {
								mmListView.setSelection(0);
								if (((moveY - mStartY) / RATIO < mHeaderHeight)
										&& (moveY - mStartY) > 0) {
									mState = PULL_TO_REFRESH;
									changeHeaderViewByState();
								} else if (moveY - mStartY <= 0) {
									mState = DONE;
									changeHeaderViewByState();
								}
							}
							if (mState == PULL_TO_REFRESH) {
								mmListView.setSelection(0);
								if ((moveY - mStartY) / RATIO >= mHeaderHeight) {
									mState = RELEASE_TO_REFRESH;
									mIsBack = true;
									changeHeaderViewByState();
								} else if (moveY - mStartY <= 0) {
									mState = DONE;
									changeHeaderViewByState();
								}
							}
							if (mState == DONE) {
								if (moveY - mStartY > 0) {
									mState = PULL_TO_REFRESH;
									changeHeaderViewByState();
								}
							}
							if (mState == PULL_TO_REFRESH) {
								mHeader.setPadding(0, -1 * mHeaderHeight
										+ (moveY - mStartY) / RATIO, 0, 0);
							}
							if (mState == RELEASE_TO_REFRESH) {
								mHeader.setPadding(0, (moveY - mStartY) / RATIO
										- mHeaderHeight, 0, 0);
							}

						}

					}
					break;

				case MotionEvent.ACTION_UP:
					if (mState != REFRESHING && mState != LOADING) {
						if (mState == PULL_TO_REFRESH) {
							mState = DONE;
							changeHeaderViewByState();
						}
						if (mState == RELEASE_TO_REFRESH) {
							mState = REFRESHING;
							changeHeaderViewByState();

							ContentTask task = new ContentTask();
							task.execute("asd");

						}
					}
					mIsRecored = false;
					mIsBack = false;
					break;
				}
				return false;

			}
		});

		mmListView.setAdapter(new BaseAdapter() {
			class Holder {
				public ImageView ivPic;
				public ImageView ivUp;
				public ImageView ivDown;
				public TextView tvUp;
				public TextView tvDown;
				public ProgressWheel mProgressWheel;
			}

			@Override
			public int getCount() {
				return imageList.size();
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public View getView(final int position, View view, ViewGroup group) {
				final Holder holder;
				// 得到View
				if (view == null) {
					holder = new Holder();
					view = inflater.inflate(R.layout.item_hairdo_waterfall,
							group, false);
					holder.ivPic = (ImageView) view
							.findViewById(R.id.iv_hairdo_item_picture);
					holder.tvUp = (TextView) view
							.findViewById(R.id.tv_hairdo_item_good);
					holder.tvDown = (TextView) view
							.findViewById(R.id.tv_hairdo_item_bad);
					holder.mProgressWheel = (ProgressWheel) view
							.findViewById(R.id.id_hairdo_waterfall_progress);
					view.setTag(holder);
				} else {
					holder = (Holder) view.getTag();
				}

				String url = imageList.get(position);
				ImageLoader.getInstance().displayImage(url, holder.ivPic,
						options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// 这儿初先初始化出来image所占的位置的大小，先把瀑布流固定住，这样瀑布流就不会因为图片加载出来后大小变化了
								RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.ivPic
										.getLayoutParams();
								if (position == 0) {
									lp.height = mScreenHeight / 4;
									lp.width = mScreenWidth/2;
								} else {
									lp.height = mScreenHeight / 3;
									lp.width = mScreenWidth/2;
								}
								holder.ivPic.setLayoutParams(lp);
								holder.mProgressWheel
										.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								showLongToast("网络异常");
								holder.mProgressWheel.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								//holder.mProgressWheel.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								holder.mProgressWheel.setVisibility(View.GONE);

							}
						}, new ImageLoadingProgressListener() {

							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								// if (current > 0 && current < 180) {
								//
								// }
								holder.mProgressWheel.setProgress(current * 360
										/ total);

							}
						});
				holder.tvUp.setText("10");
				holder.tvDown.setText("100");

				holder.ivPic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

				return view;
			}
		});

	}

	// protected View target;
	protected float dispatchX;
	protected float dispatchY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		// Log.d("", "dispatchTouchEvent x="+event.getX()+"   y="+event.getY());

		int position = mmListView.getFirstVisiblePosition();
		if (position == 0) {
			View view = mmListView.getChildAt(position);
			Rect rect = new Rect();
			// 获取view 相对父容器坐标系统中的位置
			view.getHitRect(rect);
			// 因为本身listView与DectorView也有边距
			rect.top += mmListView.getTop();
			rect.bottom += mmListView.getTop();

			boolean contains = rect.contains((int) event.getX(),
					(int) event.getY());
			if (contains) {
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					dispatchX = event.getX();
					dispatchY = event.getY();
					mHairdoSlideShowView.dispatchTouchEvent(event);
					break;
				case MotionEvent.ACTION_MOVE:
					if (//
					Math.abs(dispatchX - event.getX())//
					> //
					Math.abs(dispatchY - event.getY())//
					) //
					{
						mHairdoSlideShowView.dispatchTouchEvent(event);
					}

					break;

				default:
					mHairdoSlideShowView.dispatchTouchEvent(event);
					break;
				}

				// if(mHairdoSlideShowView.getPeriod() !=
				// mHairdoSlideShowView.PERIOD_INIT){
				// return true;
				// }
			}
		}

		return super.dispatchTouchEvent(event);
		/*
		 * int action = event.getAction(); if (action ==
		 * MotionEvent.ACTION_DOWN) { int position =
		 * mmListView.getFirstVisiblePosition(); if (position == 0) { View view
		 * = mmListView.getChildAt(position); Rect rect = new Rect(); // 获取view
		 * 相对父容器坐标系统中的位置 view.getHitRect(rect); // 因为本身listView与DectorView也有边距
		 * rect.top += mmListView.getTop(); rect.bottom += mmListView.getTop();
		 * 
		 * boolean contains = rect.contains((int) event.getX(), (int)
		 * event.getY()); if (contains) { target = mHairdoSlideShowView; } else
		 * { target = null; } } } if (target != null) {
		 * target.dispatchTouchEvent(event); if (action == MotionEvent.ACTION_UP
		 * || action == MotionEvent.ACTION_CANCEL) { target = null; } } return
		 * super.dispatchTouchEvent(event);
		 */
	}

	private void initWithContext(Context context) {

		mHeader = (LinearLayout) inflater.inflate(
				R.layout.hairdo_pull_to_refreshing_header, null);
		mHtvTitle = (HandyTextView) mHeader
				.findViewById(R.id.refreshing_header_htv_title);
		mHtvTime = (HandyTextView) mHeader
				.findViewById(R.id.refreshing_header_htv_time);
		mIvArrow = (ImageView) mHeader
				.findViewById(R.id.refreshing_header_iv_arrow);
		mIvLoading = (ImageView) mHeader
				.findViewById(R.id.refreshing_header_iv_loading);
		mIvCancel = (ImageView) mHeader
				.findViewById(R.id.refreshing_header_iv_cancel);
		mHairdoSlideShowView = new HairdoSlideShowView(context);
		int hh = mScreenWidth * 9 / 16;// 按照 16:9分配该广告
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mScreenWidth, hh);
		mHeader.addView(mHairdoSlideShowView, params);
		mIvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onRefreshComplete();
			}
		});

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		mHeader.measure(w, h);
		mHeaderHeight = mHeader.getMeasuredHeight() - hh;

		mHeader.setPadding(0, -1 * mHeaderHeight, 0, 0);
		mHeader.invalidate();
		mmListView.addHeaderView(mHeader);
		mHtvTitle.setText("下拉刷新");
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		mHtvTime.setText("最后刷新: " + date);

		mPullAnimation = new android.view.animation.RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mPullAnimation.setInterpolator(new LinearInterpolator());
		mPullAnimation.setDuration(250);
		mPullAnimation.setFillAfter(true);

		mReverseAnimation = new android.view.animation.RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mLoadingAnimation = AnimationUtils.loadAnimation(context,
				R.anim.loading);

		mState = DONE;
		mIsRefreshable = true;
		mIsCancelable = true;
		changeHeaderViewByState();
	}

	@SuppressLint("SimpleDateFormat")
	public void onRefreshComplete() {
		mState = DONE;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		mHtvTime.setText("最后刷新: " + date);
		changeHeaderViewByState();
	}

	private void changeHeaderViewByState() {
		switch (mState) {
		case RELEASE_TO_REFRESH:
			mIvArrow.setVisibility(View.VISIBLE);
			mIvLoading.setVisibility(View.GONE);
			mHtvTitle.setVisibility(View.VISIBLE);
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			mIvArrow.clearAnimation();
			mIvArrow.startAnimation(mPullAnimation);
			mIvLoading.clearAnimation();
			mHtvTitle.setText("松开刷新");
			break;
		case PULL_TO_REFRESH:
			mIvArrow.setVisibility(View.VISIBLE);
			mIvLoading.setVisibility(View.GONE);
			mHtvTitle.setVisibility(View.VISIBLE);
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
			mIvArrow.clearAnimation();
			if (mIsBack) {
				mIsBack = false;
				mIvArrow.clearAnimation();
				mIvArrow.startAnimation(mReverseAnimation);
				mHtvTitle.setText("下拉刷新");
			} else {
				mHtvTitle.setText("下拉刷新");
			}
			break;

		case REFRESHING:
			mHeader.setPadding(0, 0, 0, 0);
			mIvLoading.setVisibility(View.VISIBLE);
			mIvArrow.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
			mIvLoading.startAnimation(mLoadingAnimation);
			mIvArrow.clearAnimation();
			mHtvTitle.setText("正在刷新...");
			mHtvTime.setVisibility(View.VISIBLE);
			if (mIsCancelable) {
				mIvCancel.setVisibility(View.VISIBLE);
			} else {
				mIvCancel.setVisibility(View.GONE);
			}

			break;
		case DONE:
			mHeader.setPadding(0, -1 * mHeaderHeight, 0, 0);

			mIvLoading.setVisibility(View.GONE);
			mIvArrow.clearAnimation();
			mIvLoading.clearAnimation();
			mIvArrow.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			mHtvTitle.setText("下拉刷新");
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			break;
		}
	}

	private class ContentTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "http://img0.bdstatic.com/img/image/qiuyi1111.jpg";
		}

		@Override
		protected void onPostExecute(String result) {
			showLongToast("执行完毕啦");
			onRefreshComplete();
			super.onPostExecute(result);
		}

	}

}
