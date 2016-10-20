package com.caoyang.tapon.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:
 * Author：洪培林
 * Created Time:2016/9/1 11:31
 * Email：rainyeveningstreet@gmail.com
 */
public class LoopViewPager extends ViewPager {
    public static final String TAG = "LoopViewPager";

    private LoopPagerAdapter mAdapter;
    private AutoScrollHandler mHandler = new AutoScrollHandler();
    private static final int SCROLL = 1;
    private OnLoopPagerChangeListener onLoopPagerChangeListener;
    private int mOnPageSelectedPosition = -1;

    public LoopViewPager(Context context) {
        super(context);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnLoopPagerChangeListener(OnLoopPagerChangeListener onLoopPagerChangeListener) {
        this.onLoopPagerChangeListener = onLoopPagerChangeListener;
    }

    /**
     * start loop
     *
     * @see AutoScrollHandler#startLoop()
     */
    public void startLoop() {
        if (mAdapter.getDataSize() > 1) {
            mHandler.startLoop();
        }

    }

    /**
     * stop loop
     *
     * @see AutoScrollHandler#stopLoop()
     */
    public void stopLoop() {
        mHandler.stopLoop();
    }

    /**
     * 你的adapter必须实现LoopPagerAdapter，否则会抛出非法状态异常
     *
     * @param adapter LoopPagerAdapter的子类
     */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter instanceof LoopPagerAdapter) {
            mAdapter = (LoopPagerAdapter) adapter;
            super.setAdapter(adapter);
            addOnPageChangeListener(new OnPageChangeListenerImpl());
            //置于准确的位置
            setCurrentItem(1);
        } else {
            throw new IllegalStateException("please use loopPager");
        }

    }

    @Override
    public void setCurrentItem(int item) {
        mOnPageSelectedPosition = item;
        super.setCurrentItem(item);
    }

    /**
     * 这个方法无法准确的得到Item应该的位置 ，请使用setOnLoopPagerChangeListener来完成你的需求
     *
     * @param listener ViewPager监听器
     * @see #setOnLoopPagerChangeListener(OnLoopPagerChangeListener onLoopPagerChangeListener)
     */
    @Override
    @Deprecated
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mHandler.cancel();
                break;
            case MotionEvent.ACTION_UP:
            default:
                mHandler.reset();
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mHandler.cancel();
                break;
            case MotionEvent.ACTION_UP:
            default:
                mHandler.reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.stopLoop();
    }

    /**
     * OnPageChangeListener实现类，将Item调整到准确的位置
     */
    private class OnPageChangeListenerImpl implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //0.00--->0.99
            if (position == mAdapter.getDataSize() && positionOffset > 0.99) {
                /**
                 * 在position4左滑且左滑positionOffset百分比接近1时，将替换为position1（原本会滑到position5）
                 * 触发onPageSelected
                 * 将position固定住，防止回滚导致位置错乱
                 */
                position = 1;
                setCurrentItem(1, false);
                //0.99--->0.00
            } else if (position == 0 && positionOffset < 0.01) {
                /**
                 * 在position1右滑且右滑百分比接近0时，替换为position4（原本会滑到position0）
                 * 触发onPageSelected
                 */
                position = mAdapter.getCount() - 2;
                setCurrentItem(mAdapter.getCount() - 2, false);
            }
            //重置
            if (position == 0) {
                position = 4;
            }
            if (onLoopPagerChangeListener != null) {
                onLoopPagerChangeListener.onPageScrolled(position - 1, positionOffset, positionOffsetPixels);
            }
        }

        /**
         * 这个方法先于onPageScrolled触发，当你调用setCurrentItem时，会触发这个事件
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            //防止回滚
            if (position == mOnPageSelectedPosition) {
                return;
            }
            mOnPageSelectedPosition = position;

            //将item置于准确的位置
            if (mOnPageSelectedPosition == mAdapter.getCount() - 1) {
                mOnPageSelectedPosition = 1;
            } else if (mOnPageSelectedPosition == 0) {
                mOnPageSelectedPosition = mAdapter.getCount() - 2;
            }

            if (onLoopPagerChangeListener != null) {
                onLoopPagerChangeListener.onPageSelected(mOnPageSelectedPosition - 1);
            }
        }

        /**
         * @param state viewpager滚动状态
         * @see OnPageChangeListener#onPageScrollStateChanged(int state)
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (onLoopPagerChangeListener != null) {
                onLoopPagerChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    /**
     * LoopViewPager的适配器。
     */
    public static abstract class LoopPagerAdapter extends PagerAdapter {

        public LoopPagerAdapter() {
        }

        /**
         * 你的数据大小
         *
         * @return 数据长度
         */
        public abstract int getDataSize();

        /**
         * 重写该方法，实现你的逻辑
         *
         * @param container    视图容器
         * @param position     Item的位置
         * @param dataPosition 数据的位置
         * @return
         */
        public abstract Object instantiateLoopItem(ViewGroup container, int position, int dataPosition);

        @Override
        public final int getCount() {
            if (getDataSize() > 1) {
                return getDataSize() + 2;
            }
            return getDataSize();
        }

        @Override
        public final boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public final void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView((View) object);
        }

        @Override
        public final Object instantiateItem(ViewGroup container, int position) {
            int dataSize = getDataSize();
            int dataPosition;
            if (dataSize > 0) {
                dataPosition = (position - 1 + dataSize) % dataSize;
            } else {
                dataPosition = position;
            }
            return instantiateLoopItem(container, position, dataPosition);
        }
    }

    /**
     * 轮播的Handler
     */
    private class AutoScrollHandler extends Handler {
        private boolean stop = true;
        private boolean pause = true;
        int delay = 2000;

        /**
         * 轮询
         */
        @Override
        public void handleMessage(Message msg) {
            if (!stop && !pause) {
                int currentIndex = (getCurrentItem() + 1) % (getAdapter().getCount());
                Log.d(TAG, "handleMessage: " + currentIndex);
                setCurrentItem(currentIndex, true);
            }
            sendEmptyMessageDelayed(msg.what, delay);
        }

        /**
         * 开始轮播
         */
        void startLoop() {
            stop = false;
            pause = false;
            removeCallbacksAndMessages(null);
            sendEmptyMessageDelayed(SCROLL, delay);
        }

        /**
         * 停止轮播
         */
        void stopLoop() {
            stop = true;
            pause = true;
            removeCallbacksAndMessages(null);
            sendEmptyMessageDelayed(SCROLL, delay);
        }

        /**
         * 重置轮播
         * 当轮播本身没有开启（stop） 调用此方法无效
         */
        void reset() {
            pause = false;
            removeCallbacksAndMessages(null);
            sendEmptyMessageDelayed(SCROLL, delay);
        }

        /**
         * 取消轮播
         */
        void cancel() {
            pause = true;
            removeCallbacksAndMessages(null);
            sendEmptyMessageDelayed(SCROLL, delay);

        }
    }

    /**
     * 当前viewpager的滚动的监听。
     */
    public interface OnLoopPagerChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
