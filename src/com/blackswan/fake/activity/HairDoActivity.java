package com.blackswan.fake.activity;

import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListViewFooter;
import me.maxwin.view.XListViewHeader;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.huewu.pla.lib.MultiColumnListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HairDoActivity extends BaseActivity   {

	LinkedList<String> imageList = new LinkedList<String>();
	private DisplayImageOptions options;
	private LayoutInflater inflater;

	MultiColumnListView mmListView;

	private Scroller mScroller; // used for scroll back
	private float mLastY = -1; // save event y

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad = true;	//允许 上拉 使能
	private boolean mPullLoading  ;	
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;
	private final static int SCROLL_DURATION = 400; // scroll back duration

	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px

	// at bottom, trigger
	// load more.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hairdo);
		inflater = getLayoutInflater();
		options = new DisplayImageOptions.Builder()

		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
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
		initWithContext(this);
		mmListView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (mLastY == -1) {
					mLastY = ev.getRawY();
				}

				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastY = ev.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					final float deltaY = ev.getRawY() - mLastY;
					mLastY = ev.getRawY();
					if (mmListView.getFirstVisiblePosition() == 0
							&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
						// the first item is showing, header has shown or pull
						// down.
						updateHeaderHeight(deltaY / OFFSET_RADIO);
						// invokeOnScrolling();
					} else if (mmListView.getLastVisiblePosition() == mTotalItemCount - 1
							&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
						// last item, already pulled up or want to pull up.
						updateFooterHeight(-deltaY / OFFSET_RADIO);
					}
					break;
				case MotionEvent.ACTION_UP:
					
					
					break;
				default:
					mLastY = -1; // reset
					if (mmListView.getFirstVisiblePosition() == 0) {
						// invoke refresh
						if (mEnablePullRefresh
								&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
							mPullRefreshing = true;
							mHeaderView
									.setState(XListViewHeader.STATE_REFRESHING);
							
							
							ContentTask task = new ContentTask();
							task.execute("asd");
							

						}
						resetHeaderHeight();
					} else if (mmListView.getLastVisiblePosition() == mTotalItemCount - 1) {
						// invoke load more.
						if (mEnablePullLoad
								&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
							showLongToast("在xiala ?？");

						}
						resetFooterHeight();
					}
					break;
				}
				return onTouchEvent(ev);
			}
		});
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			mmListView.addFooterView(mFooterView);
		}
		
		mmListView.setAdapter(new BaseAdapter() {
			class Holder {
				public ImageView ivPic;
				public ImageView ivUp;
				public ImageView ivDown;
				public TextView tvUp;
				public TextView tvDown;
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
					view.setTag(holder);
				} else {
					holder = (Holder) view.getTag();
				}

				String url = imageList.get(position);
				ImageLoader.getInstance().displayImage(url, holder.ivPic,
						options);
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

	private void updateHeaderHeight(float delta) {
		//Log.d("HEAD", "位置"+delta);
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		mmListView.setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		mHeaderView.setVisiableHeight(mScroller.getCurrY());
		mmListView.invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			mmListView.invalidate();
		}
	}
	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
//		mmListView.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		mmListView.addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						mHeaderView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	 private class ContentTask extends AsyncTask<String, Integer, String> {

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
			stopRefresh();
			super.onPostExecute(result);
		}

	
		 
	 }

}
