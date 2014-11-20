package com.blackswan.fake.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackswan.fake.R;

/**
 * @author JZP 上拉加载更多，以及 提前预加载 2014.11.20
 */
public class FakePullDrowListView extends FakeRefreshListView {
	private View mFoot;
	private TextView tv_info;
	private ProgressBar mProgressWheel;
	private PreAddMoreListener mPreAddMoreListener;
	private AddMoreListener mAddMoreListener;
	private int preAddThreshold = 3; // 当未看的数据还有preAddThreshold页时候开始预加载数据
	private Boolean isAddAll;// 是否已经全部加载 True->全部加载，此时不添加Foot
	private Boolean isPreAdding;// 是否正在预加载
	private int lastY;
	private int footState; // 上拉过程 状态机
	private final int STATE_NONE = 3;
	private final int STATE_INIT = 0;
	private final int STATE_DROW_TO_ADD_MORE = 1;
	private final int STATE_LOOSEN = 2;

	private final int STATE_DOING = 4;

	public void setmPreAddMoreListener(PreAddMoreListener mPreAddMoreListener) {
		this.mPreAddMoreListener = mPreAddMoreListener;
	}

	public void setmAddMoreListener(AddMoreListener mAddMoreListener) {
		this.mAddMoreListener = mAddMoreListener;
	}

	public void setPreAddThreshold(int preAddThreshold) {
		this.preAddThreshold = preAddThreshold;
	}

	public void setIsAddAll(Boolean isAddAll) {
		this.isAddAll = isAddAll;
	}

	public FakePullDrowListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initFoot();
	}

	public FakePullDrowListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFoot();
	}

	public FakePullDrowListView(Context context) {
		super(context);
		initFoot();
	}

	private void initFoot() {
		mFoot = mInflater.inflate(R.layout.include_pull_to_refreshing_foot,
				null);
		tv_info = (TextView) mFoot.findViewById(R.id.tv_listview_foot);
		mProgressWheel = (ProgressBar) mFoot
				.findViewById(R.id.id_listview_foot_progress);
		addFooterView(mFoot);

		// tv_info.setText("加载更多");
		// mProgressWheel.setProgress(0);

		footState = STATE_NONE;
		isAddAll = true;
		isPreAdding = false;
		changViewByState();
	}

	/**
	 * @author JZP 加载更多 接口 用户向下滑动 快要到底端时候 预加载
	 */
	public interface PreAddMoreListener {
		public void onPreAddMore();
	}

	/**
	 * @author JZP 加载更多 接口， 用户已经到最下端，此时需要加载数据
	 */
	public interface AddMoreListener {
		public void onAddMore();
	}

	/**
	 * 加载完成后  需要调用之
	 */
	public void onAddMoreComplete() {
		if (footState == STATE_DOING) {
			footState = STATE_NONE;
			changViewByState();
		}
		
	}
	/**
	 * 预加载完成后  需要调用之
	 */
	public void onPreAddMoreComplete() {
		isPreAdding = false;
	}

	private void changViewByState() {
		switch (footState) {
		case STATE_NONE:
			tv_info.setVisibility(View.GONE);
			mProgressWheel.setVisibility(View.GONE);
			break;
		case STATE_INIT:
			mProgressWheel.setVisibility(View.GONE);
			tv_info.setVisibility(View.VISIBLE);
			if (isAddAll) {
				tv_info.setText("无更多数据");
			} else {
				tv_info.setText("上拉加载更多");

			}
			break;
		case STATE_DROW_TO_ADD_MORE:

			break;
		case STATE_LOOSEN:
			tv_info.setText("松开加载更多数据");
			break;
		case STATE_DOING:
			mProgressWheel.setVisibility(View.VISIBLE);
			tv_info.setText("正在加载");
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		// 只有当 屏幕放不了 才加载foot
		if (totalItemCount > visibleItemCount) {
			if (footState == STATE_NONE) {
				if (mIsBottom && !isAddAll) {
					footState = STATE_INIT;
				}
			}
		}
		if (isAddAll == null)
			initFoot();
		if (!isAddAll) {
			int refer = totalItemCount - visibleItemCount * preAddThreshold;
			if (firstVisibleItem > refer) {
				// 预加载
				if (mPreAddMoreListener != null && isPreAdding==false) {
					mPreAddMoreListener.onPreAddMore();
					isPreAdding = true;
				}
			}
//			Log.d("", "refer"+refer+";  totalItemCount"+totalItemCount
//					+"visibleItemCount"+visibleItemCount
//					+"firstVisibleItem"+firstVisibleItem);
		}
		changViewByState();
	}

	@Override
	public void onDown(MotionEvent ev) {
		super.onDown(ev);
		if (footState == STATE_INIT) {
			lastY = mDownPoint.y;
			footState = STATE_DROW_TO_ADD_MORE;
		}
		
	}

	@Override
	public void onUp(MotionEvent ev) {
		super.onUp(ev);

		if (footState == STATE_LOOSEN) {
			footState = STATE_DOING;
			changViewByState();
			if (mAddMoreListener != null) {
				mAddMoreListener.onAddMore();
			}

		}
		
	}

	@Override
	public void onMove(MotionEvent ev) {
		super.onMove(ev);
		if (footState == STATE_DROW_TO_ADD_MORE) {
			int diff = lastY - mMovePoint.y;
			if (diff > 300) {
				footState = STATE_LOOSEN;
				changViewByState();
			}

		}


	}

}
