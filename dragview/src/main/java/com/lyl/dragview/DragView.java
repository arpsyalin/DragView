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
    //显示最小大小
    private float mMinShowSize;
    //中间的大小 用于判断松手后是回弹最小 还是回弹最大显示
    private float mCenterShowSize;
    private int mNowShowSize;
    int mNowHideDis = -1;
    //初始的位置记录left top
    private Point mInitLocationPos = new Point();
    private View mChildView;
    //拖拽助手
    private ViewDragHelper mViewDragHelper;
    //默认是从底部往上滑
    private int mDragModel = ViewDragHelper.EDGE_BOTTOM;

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
        mMinShowSize = ta.getDimension(R.styleable.DragView_minShowSize, 0);
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
                    return mInitLocationPos.x;
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
                    return mInitLocationPos.y;
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
                            mNowHideDis = (mCenterShowSize > mNowHideDis) ? 0 : mInitLocationPos.y;
                            mViewDragHelper.settleCapturedViewAt(mInitLocationPos.x, mNowHideDis);
                            break;
                        case ViewDragHelper.EDGE_TOP:
                            mNowHideDis = (-mNowHideDis < mCenterShowSize) ? 0 : mInitLocationPos.y;
                            mViewDragHelper.settleCapturedViewAt(mInitLocationPos.x, mNowHideDis);
                            break;
                        case ViewDragHelper.EDGE_LEFT:
                            mNowHideDis = (-mNowHideDis < mCenterShowSize) ? 0 : mInitLocationPos.x;
                            mViewDragHelper.settleCapturedViewAt(mNowHideDis, mInitLocationPos.y);
                            break;
                        case ViewDragHelper.EDGE_RIGHT:
                            mNowHideDis = (mNowHideDis > mCenterShowSize) ? mInitLocationPos.x : 0;
                            mViewDragHelper.settleCapturedViewAt(mNowHideDis, mInitLocationPos.y);
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
            case ViewDragHelper.EDGE_BOTTOM:
                mCenterShowSize = (mChildView.getMeasuredHeight() - mMinShowSize) / 2;
                mNowShowSize = (int) (mChildView.getMeasuredHeight() - mMinShowSize);
                break;
            case ViewDragHelper.EDGE_TOP:
                mCenterShowSize = (mChildView.getMeasuredHeight() - mMinShowSize) / 2;
                mNowShowSize = (int) mMinShowSize;
                break;

            case ViewDragHelper.EDGE_RIGHT:
                mCenterShowSize = (mChildView.getMeasuredWidth() - mMinShowSize) / 2;
                mNowShowSize = (int) (mChildView.getMeasuredWidth() - mMinShowSize);
                break;
            case ViewDragHelper.EDGE_LEFT:
                mCenterShowSize = (mChildView.getMeasuredWidth() - mMinShowSize) / 2;
                mNowShowSize = (int) mMinShowSize;
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        switch (mDragModel) {
            case ViewDragHelper.EDGE_BOTTOM:
                mChildView.layout(0, mNowHideDis == -1 ? mNowShowSize : mNowHideDis, mChildView.getMeasuredWidth(), mChildView.getMeasuredHeight() + (mNowHideDis == -1 ? mNowShowSize : mNowHideDis));
                break;
            case ViewDragHelper.EDGE_TOP:
                mChildView.layout(0, (mNowHideDis == -1 ? mNowShowSize : mNowHideDis) - mChildView.getMeasuredHeight(), mChildView.getMeasuredWidth(), mNowHideDis == -1 ? mNowShowSize : mNowHideDis);
                break;
            case ViewDragHelper.EDGE_RIGHT:
                mChildView.layout(mNowHideDis == -1 ? mNowShowSize : mNowHideDis, mChildView.getMeasuredHeight(), mChildView.getMeasuredWidth() + (mNowHideDis == -1 ? mNowShowSize : mNowHideDis), 0);
                break;
            case ViewDragHelper.EDGE_LEFT:
                mChildView.layout((mNowHideDis == -1 ? mNowShowSize : mNowHideDis) - mChildView.getMeasuredWidth(), 0, mNowHideDis == -1 ? mNowShowSize : mNowHideDis, mChildView.getMeasuredHeight());
                break;
        }
        mInitLocationPos.x = mChildView.getLeft();
        mInitLocationPos.y = mChildView.getTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}