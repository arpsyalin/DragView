package com.lyl.dragview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

/**
 * * @Description 底部拖拉控件
 * * @Author 刘亚林
 * * @CreateDate 2021/3/25
 * * @Version 1.0
 * * @Remark TODO
 **/
public class DragView extends FrameLayout {
    //子View
    private View mChildView;
    //显示最小大小
    private int mViewMinShowSize;
    //最大显示
    private int mViewMaxShowSize;
    //回弹阈值，根据拖拽up的位置计算滑动回弹的位置（大于显示最大，小于则显示最小）
    private float mCenterShowSize;
    //拖拽助手
    private ViewDragHelper mViewDragHelper;
    //默认是从底部往上滑
    private int mDragModel = ViewDragHelper.EDGE_BOTTOM;
    //初始显示的模式
    private boolean mShowModel;
    //记录拖动过程中的当前的位置
    private int mNowHideDis;

    public DragView(@NonNull Context context) {
        super(context);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragView);
        mViewMinShowSize = (int) ta.getDimension(R.styleable.DragView_minShowSize, 0);
        mShowModel = ta.getBoolean(R.styleable.DragView_showModel, true);
        mDragModel = ta.getInt(R.styleable.DragView_dragModel, ViewDragHelper.EDGE_BOTTOM);
        ta.recycle();
        initConfig();
    }

    private void initConfig() {
        this.setBackgroundColor(Color.TRANSPARENT);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mChildView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (mDragModel == ViewDragHelper.EDGE_BOTTOM || mDragModel == ViewDragHelper.EDGE_TOP) {
                    return 0;
                } else {
                    if (mDragModel == ViewDragHelper.EDGE_RIGHT) {
                        if (left > 0) {
                            mNowHideDis = left;
                            return left;
                        } else {
                            mNowHideDis = 0;
                            return 0;
                        }
                    } else {
                        if (left < 0) {
                            mNowHideDis = left;
                            return left;
                        } else {
                            mNowHideDis = 0;
                            return 0;
                        }
                    }
                }
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (mDragModel == ViewDragHelper.EDGE_LEFT || mDragModel == ViewDragHelper.EDGE_RIGHT) {
                    return 0;
                } else {
                    if (mDragModel == ViewDragHelper.EDGE_BOTTOM) {
                        if (top > 0) {
                            mNowHideDis = top;
                            return top;
                        } else {
                            mNowHideDis = 0;
                            return 0;
                        }
                    } else {
                        if (top < 0) {
                            mNowHideDis = top;
                            return top;
                        } else {
                            mNowHideDis = 0;
                            return 0;
                        }
                    }
                }
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mChildView) {
                    switch (mDragModel) {
                        case ViewDragHelper.EDGE_BOTTOM:
                            mShowModel = mNowHideDis < mCenterShowSize;
                            mNowHideDis = mShowModel ? 0 : mViewMinShowSize;
                            mViewDragHelper.settleCapturedViewAt(0, mNowHideDis);
                            break;
                        case ViewDragHelper.EDGE_TOP:
                            mShowModel = mNowHideDis > -mCenterShowSize;
                            mNowHideDis = mShowModel ? 0 : mViewMinShowSize - mViewMaxShowSize;
                            mViewDragHelper.settleCapturedViewAt(0, mNowHideDis);
                            break;
                        case ViewDragHelper.EDGE_LEFT:
                            mShowModel = mNowHideDis > -mCenterShowSize;
                            mNowHideDis = mShowModel ? 0 : mViewMinShowSize - mViewMaxShowSize;
                            mViewDragHelper.settleCapturedViewAt(mNowHideDis, 0);
                            break;
                        case ViewDragHelper.EDGE_RIGHT:
                            mShowModel = mNowHideDis < mCenterShowSize;
                            mNowHideDis = mShowModel ? 0 : mViewMinShowSize;
                            mViewDragHelper.settleCapturedViewAt(mNowHideDis, 0);
                            break;
                    }
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mChildView, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });


        switch (mDragModel) {
            case ViewDragHelper.EDGE_BOTTOM:
                mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
                break;
            case ViewDragHelper.EDGE_TOP:
                mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
                break;

            case ViewDragHelper.EDGE_RIGHT:
                mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
                break;
            case ViewDragHelper.EDGE_LEFT:
                mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0 || getChildAt(0) == null) {
            throw new RuntimeException("there have no child-View in the DragView！");
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("there just alow one child-View in the DragView!");
        }
        mChildView = getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (mDragModel) {
            case ViewDragHelper.EDGE_TOP:
            case ViewDragHelper.EDGE_BOTTOM:
                mViewMaxShowSize = mChildView.getMeasuredHeight();
                mViewMaxShowSize = mChildView.getMeasuredHeight();
                break;
            case ViewDragHelper.EDGE_RIGHT:
            case ViewDragHelper.EDGE_LEFT:
                mViewMaxShowSize = mChildView.getMeasuredWidth();
                mViewMaxShowSize = mChildView.getMeasuredWidth();
                break;
        }
        mCenterShowSize = (mViewMaxShowSize - mViewMinShowSize) / 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = l, top = t, right = r, bottom = b;
        switch (mDragModel) {
            case ViewDragHelper.EDGE_BOTTOM:
                left = 0;
                top = mShowModel ? 0 : mViewMinShowSize;
                right = mChildView.getMeasuredWidth();
                bottom = mViewMaxShowSize + top;
                break;
            case ViewDragHelper.EDGE_TOP:
                left = 0;
                top = mShowModel ? 0 : (mViewMinShowSize - mViewMaxShowSize);
                right = mChildView.getMeasuredWidth();
                bottom = mViewMaxShowSize + top;
                break;
            case ViewDragHelper.EDGE_RIGHT:
                left = mShowModel ? 0 : mViewMinShowSize;
                top = 0;
                right = mViewMaxShowSize + left;
                bottom = mChildView.getMeasuredHeight();
                break;
            case ViewDragHelper.EDGE_LEFT:
                left = mShowModel ? 0 : (mViewMinShowSize - mViewMaxShowSize);
                top = 0;
                right = mViewMaxShowSize + left;
                bottom = mChildView.getMeasuredHeight();
                break;
        }
        mChildView.layout(left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (mDragModel) {
                case ViewDragHelper.EDGE_BOTTOM:
                    if (event.getY() < (mShowModel ? 0 : mViewMinShowSize)) {
                        return false;
                    }
                    return true;
                case ViewDragHelper.EDGE_TOP:
                    if (event.getY() > (mShowModel ? mViewMaxShowSize : mViewMinShowSize)) {
                        return false;
                    }
                    return true;
                case ViewDragHelper.EDGE_LEFT:
                    if (event.getX() > (mShowModel ? mViewMaxShowSize : mViewMinShowSize)) {
                        return false;
                    }
                    return true;
                case ViewDragHelper.EDGE_RIGHT:
                    if (event.getX() < (mShowModel ? 0 : mViewMinShowSize)) {
                        return false;
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}