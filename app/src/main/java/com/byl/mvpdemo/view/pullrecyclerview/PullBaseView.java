package com.byl.mvpdemo.view.pullrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.byl.mvpdemo.R;


/**
 * 基类：重写LinearLayout，在LinearLayout中依次添加headerview-RecyclerView-footerview,
 * 并对LinearLayout进行手势监听
 * http://blog.csdn.net/baiyuliang2013
 */
public abstract class PullBaseView<T extends RecyclerView> extends LinearLayout {

    protected T mRecyclerView;
    private boolean isCanScrollAtRereshing = false;//刷新时是否可滑动
    private boolean isCanPullDown = true;//是否可下拉
    private boolean isCanPullUp = true;//是否可上拉

    // pull state
    private static final int PULL_DOWN_STATE = 0;//下拉
    private static final int PULL_UP_STATE = 1;//上拉
    // refresh states
    private static final int PULL_TO_REFRESH = 2;//未达到刷新条件
    private static final int RELEASE_TO_REFRESH = 3;//已达到刷新条件
    private static final int REFRESHING = 4;//正在刷新

    private int mLastMotionY;//last y
    private View mHeaderView;//HeaderView
    private View mFooterView;//FooterView
    private int mHeaderViewHeight;//HeaderView的高度
    private int mFooterViewHeight;//FooterView的高度
    private TextView mFooterTextView;//FooterView提示语
    private ProgressBar mFooterProgressBar;//FooterView ProgressBar

    private int mHeaderState;//HeaderView当前刷新状态
    private int mFooterState;//FooterView当前刷新状态
    private int mPullState;//刷新状态：下拉或上拉

    private OnHeaderRefreshListener mOnHeaderRefreshListener;//HeaderView刷新监听
    private OnFooterRefreshListener mOnFooterRefreshListener;//FooterView刷新监听
    private OnPullDownScrollListener onPullDownScrollListener;

    private float startY;//手指落点
    private float offsetY;//手指滑动的距离

    //headview中的动画实现
    private RefreshAnimView refreshAnimView;
    private RefreshLoadingView refreshLoadingView;
    private AnimationDrawable loadAnimation;

    private LayoutInflater mInflater;

    public PullBaseView(Context context) {
        super(context);
    }

    public PullBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * init
     */
    private void init(Context context, AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        addHeaderView();//首先在Linear中添加headview
        mRecyclerView = createRecyclerView(context, attrs);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        addView(mRecyclerView);//添加RecyclerView
    }

    /**
     * header view
     */
    private void addHeaderView() {
        mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);
        refreshAnimView = (RefreshAnimView) mHeaderView.findViewById(R.id.first_step_view);
        refreshLoadingView = (RefreshLoadingView) mHeaderView.findViewById(R.id.second_step_view);
        refreshLoadingView.setBackgroundResource(R.drawable.anim_refresh);
        loadAnimation = (AnimationDrawable) refreshLoadingView.getBackground();
        measureView(mHeaderView);//测量高度
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        params.topMargin = -(mHeaderViewHeight);// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
        addView(mHeaderView, params);

    }

    /**
     * footer view
     */
    private void addFooterView() {
        mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
        mFooterTextView = (TextView) mFooterView.findViewById(R.id.pull_to_load_text);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pull_to_load_progress);
        measureView(mFooterView);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
        addView(mFooterView, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addFooterView();//最后添加footview
    }

    /**
     * dispatchTouchEvent 在最顶级也就是父布局（Linear）中拦截手势监听
     * 1.处理正在刷新时是否可以滑动
     * 2.处理头部动画
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = ev.getY() - startY;
                if (!isCanScrollAtRereshing) {
                    if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
                        return true;
                    }
                }
                if (isCanPullUp && offsetY > 0) {
                    float headerViewShowHeight = offsetY * 0.3f;//headview露出的高度
                    float currentProgress = headerViewShowHeight / mHeaderViewHeight;//根据此比例，在滑动时改变动图大小
                    if (currentProgress >= 1) {
                        currentProgress = 1;
                    }
                    if (mHeaderState == PULL_TO_REFRESH || mHeaderState == RELEASE_TO_REFRESH) {
                        refreshAnimView.setCurrentProgress(currentProgress);//绘制headview中的动画
                        refreshAnimView.postInvalidate();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;// 首先拦截down事件,记录y坐标
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY; // deltaY > 0 是向下运动,< 0是向上运动
                if (isRefreshViewScroll(deltaY)) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return false)，
     * 则由PullBaseView的子View来处理;否则由下面的方法来处理(即由PullBaseView自己来处理)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY;
                if (isCanPullDown && mPullState == PULL_DOWN_STATE) {
                    if (onPullDownScrollListener != null) {
                        onPullDownScrollListener.onPullDownScrolled();
                    }
                    headerPrepareToRefresh(deltaY);
                } else if (isCanPullUp && mPullState == PULL_UP_STATE) {
                    footerPrepareToRefresh(deltaY);
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                if (isCanPullDown && mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        headerRefreshing(); // 开始刷新
                    } else {
                        if (onPullDownScrollListener != null) {
                            onPullDownScrollListener.onPullDownFinished();
                        }
                        setHeaderTopMargin(-mHeaderViewHeight); // 还没有执行刷新，重新隐藏
                    }
                } else if (isCanPullUp && mPullState == PULL_UP_STATE) {
                    if (Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight) {
                        footerRefreshing();// 开始执行footer 刷新
                    } else {
                        setHeaderTopMargin(-mHeaderViewHeight);// 还没有执行刷新，重新隐藏
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否应该到了父View,即PullBaseView滑动
     *
     * @param deltaY , deltaY > 0 是向下运动,< 0是向上运动
     * @return
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
            return false;
        }
        if (deltaY >= -20 && deltaY <= 20)
            return false;

        if (mRecyclerView != null) {
            // 滑动到最顶端
            if (deltaY > 0) {
                View child = mRecyclerView.getChildAt(0);
                if (child == null) {
                    // mRecyclerView,不拦截
                    return false;
                }
                if (isScrollTop() && child.getTop() == 0) {
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }
                int top = child.getTop();
                int padding = mRecyclerView.getPaddingTop();
                if (isScrollTop() && Math.abs(top - padding) <= 8) {
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }

            } else if (deltaY < 0) {
                View lastChild = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
                if (lastChild == null) {
                    // mRecyclerView,不拦截
                    return false;
                }
                // 最后一个子view的Bottom小于父View的高度说明mRecyclerView的数据没有填满父view,
                // 等于父View的高度说明mRecyclerView已经滑动到最后
                if (lastChild.getBottom() <= getHeight() && isScrollBottom()) {
                    mPullState = PULL_UP_STATE;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断mRecyclerView是否滑动到顶部
     *
     * @return
     */
    boolean isScrollTop() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断mRecyclerView是否滑动到底部
     *
     * @return
     */
    boolean isScrollBottom() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (linearLayoutManager.findLastVisibleItemPosition() == (mRecyclerView.getAdapter().getItemCount() - 1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * header 准备刷新,手指移动过程,还没有释放
     *
     * @param deltaY ,手指滑动的距离
     */
    private void headerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
        if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
            mHeaderState = RELEASE_TO_REFRESH;
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
            mHeaderState = PULL_TO_REFRESH;
        }
    }

    /**
     * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
     * 高度是一样，都是通过修改header view的topmargin的值来达到
     *
     * @param deltaY ,手指滑动的距离
     */
    private void footerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // 如果header view topMargin 的绝对值大于或等于header + footer 的高度
        // 说明footer view 完全显示出来了，修改footer view 的提示状态
        if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight) && mFooterState != RELEASE_TO_REFRESH) {
            mFooterTextView.setText(R.string.pull_to_refresh_footer_release_label);
            mFooterState = RELEASE_TO_REFRESH;
        } else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
            mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
            mFooterState = PULL_TO_REFRESH;
        }
    }

    /**
     * 修改Header view top margin的值
     *
     * @param deltaY
     */
    private int changingHeaderViewTopMargin(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;
        // 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了
        // 表示如果是在上拉后一段距离,然后直接下拉
        if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
            return params.topMargin;
        }
        // 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }

    /**
     * header refreshing
     */
    public void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        refreshAnimView.setVisibility(View.GONE);
        refreshLoadingView.setVisibility(View.VISIBLE);
        loadAnimation.start();
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);
        }
    }

    /**
     * footer refreshing
     */
    private void footerRefreshing() {
        mFooterState = REFRESHING;
        int top = mHeaderViewHeight + mFooterViewHeight;
        setHeaderTopMargin(-top);
        mFooterTextView.setText(R.string.pull_to_refresh_footer_refreshing_label);
        mFooterProgressBar.setVisibility(View.VISIBLE);
        if (mOnFooterRefreshListener != null) {
            mOnFooterRefreshListener.onFooterRefresh(this);
        }
    }

    /**
     * 设置header view 的topMargin的值
     *
     * @param topMargin ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }

    /**
     * header view 完成更新后恢复初始状态
     */
    public void onHeaderRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        refreshAnimView.setVisibility(View.VISIBLE);
        refreshLoadingView.setVisibility(View.GONE);
        loadAnimation.stop();
        mHeaderState = PULL_TO_REFRESH;
        if (onPullDownScrollListener != null) {
            onPullDownScrollListener.onPullDownFinished();
        }
    }


    /**
     * footer view 完成更新后恢复初始状态
     */
    public void onFooterRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
        mFooterProgressBar.setVisibility(View.GONE);
        mFooterState = PULL_TO_REFRESH;
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        }

    }

    /**
     * 获取当前header view 的topMargin
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }


    /**
     * 设置下拉监听接口
     */
    public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;
    }

    /**
     * 设置上拉监听接口
     */
    public void setOnFooterRefreshListener(OnFooterRefreshListener footerRefreshListener) {
        mOnFooterRefreshListener = footerRefreshListener;
    }

    /**
     * 上拉监听
     */
    public interface OnFooterRefreshListener {
        void onFooterRefresh(PullBaseView view);
    }

    /**
     * 下拉监听
     */
    public interface OnHeaderRefreshListener {
        void onHeaderRefresh(PullBaseView view);
    }

    /**
     * 下拉滑动动作监听
     */
    public interface OnPullDownScrollListener {
        void onPullDownScrolled();

        void onPullDownFinished();
    }

    public void setOnPullDownScrollListener(OnPullDownScrollListener onPullDownScrollListener) {
        this.onPullDownScrollListener = onPullDownScrollListener;
    }

    /**
     * 设置是否可以在刷新时滑动
     *
     * @param canScrollAtRereshing
     */
    public void setCanScrollAtRereshing(boolean canScrollAtRereshing) {
        isCanScrollAtRereshing = canScrollAtRereshing;
    }

    /**
     * 设置是否可上拉
     *
     * @param canPullUp
     */
    public void setCanPullUp(boolean canPullUp) {
        isCanPullUp = canPullUp;
    }

    /**
     * 设置是否可下拉
     *
     * @param canPullDown
     */
    public void setCanPullDown(boolean canPullDown) {
        isCanPullDown = canPullDown;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    /**
     * 抽象方法，子类实现，传入View
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createRecyclerView(Context context, AttributeSet attrs);

}
