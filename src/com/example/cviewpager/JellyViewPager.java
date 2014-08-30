package com.example.cviewpager;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

public class JellyViewPager extends ViewPager{
	/**
	 * View最小缩放比例
	 */
	private static final float MIN_SCALE = 0.2f;
	
	/**
	 * 最大缩放比例
	 */
	private static final float MAX_SCALE = 0.9f;
	/**
	 * 滑动的最低速度
	 */
	private static final float FLING_VELOCITY = 500; //100毫秒速度
	private static final int UNIT = 100; //计算速率的单位（毫秒）
	
	/**
	 * 手指滑动的距离，大于此距离时，移出屏幕
	 */
	private static float OUT_DISTANCE_BOUDARY;
	
	private static float MAX_DEGREE = 15;
	
	private VelocityTracker vTracker;
	
	private JellyPagerAdapter mAdapter;
	private MyTouchListener mListener;
	private Spring mScaleSpring;
	private Spring tranSpring;
	private Spring rotateSpring;
	
	private final BaseSpringSystem mSpringSystem = SpringSystem.create();
	private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
	
	private View currentView;
	private int currentItem = -1;
	
	/**
	 * 屏幕高度
	 */
	private int screenHeight,screenWidth;
	
	private OnPageChangeListener pageChangeListener;
	
	private SparseArray<View> viewHolder = new SparseArray<View>();

	public JellyViewPager(Context context) {
		super(context);
		init(context);
	}

	public JellyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		super.setOnPageChangeListener(new MyPageChageListener());
		mListener = new MyTouchListener();
		
		mScaleSpring = mSpringSystem.createSpring();
		tranSpring = mSpringSystem.createSpring();
		rotateSpring = mSpringSystem.createSpring();
		mScaleSpring.addListener(mSpringListener);
		tranSpring.addListener(mSpringListener);
		rotateSpring.addListener(mSpringListener);
		
		DisplayMetrics merics = context.getResources().getDisplayMetrics();
		screenHeight = merics.heightPixels;
		screenWidth = merics.widthPixels;
		
		OUT_DISTANCE_BOUDARY = MAX_SCALE*screenHeight/3;
	}

	public void setAdapter(JellyPagerAdapter adapter) {
		if (adapter == null) {
			new NullPointerException("SampleAdapter cannot be null!");
		}
		mAdapter = adapter;
		viewHolder.clear();
		super.setAdapter(new ViewPagerAdapter());
	}
	
	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		pageChangeListener = listener;
	}
	
	
	/**
	 * 重置spring
	 */
	private void resetSpring() {
		if(tranSpring.isAtRest()){
			tranSpring.removeAllListeners();
			tranSpring.setCurrentValue(0);
			tranSpring.setEndValue(0);
			tranSpring.addListener(mSpringListener);
		}
		if(rotateSpring.isAtRest()){
			rotateSpring.removeAllListeners();
			rotateSpring.setCurrentValue(0);
			rotateSpring.setEndValue(0);
			rotateSpring.addListener(mSpringListener);
		}
	}
	
	/**
	 * 显示下一页
	 */
	public void showNext(){
		resetSpring();
		animOutIfNeeded(screenHeight, 0);
	}
	
	/**
	 * 显示上一页
	 */
	public void showPre(){
		resetSpring();
		animOutIfNeeded(-screenHeight, 0);
	}
	
	private void nextPage() {
		super.setCurrentItem(currentItem + 1,false);
	}
	
	private void prePage(){
		super.setCurrentItem(currentItem - 1,false);
	}
	
	/**
	 * 是否有下一页
	 * @return
	 */
	private boolean hasNext() {
		return getCurrentItem() < mAdapter.getCount() - 1;
	}
	
	
	/**
	 * 是否有上一页
	 * @return
	 */
	private boolean hasPre() {
		return getCurrentItem() > 0;
	}
	
	/**
	 * 获取当前视图
	 * @return
	 */
	private View getCurrentView() {
		return viewHolder.get(getCurrentItem());
	}
	
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mAdapter.getCount();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(viewHolder.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View convertView = viewHolder.get(position);
			View view = mAdapter.getView(position, convertView);
			viewHolder.put(position, view);
			if(position != 0){
				view.setScaleX(MIN_SCALE);
				view.setScaleY(MIN_SCALE);
			}else{
				view.setScaleX(MAX_SCALE);
				view.setScaleY(MAX_SCALE);
			}
			render(view);
			container.addView(view);
			return view;
		}
	}
	
	private void render(View view) {
		view.setOnTouchListener(mListener);
	}
	
	private float downY,downX;
	private float distance;
	private class MyTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			float currentY = event.getRawY();
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(vTracker == null){
					vTracker = VelocityTracker.obtain(); 
				}else{
					vTracker.clear();
				}
				vTracker.addMovement(event);
				distance = 0;
				downY = event.getRawY();
				downX = event.getRawX();
				resetSpring();
				break;
			case MotionEvent.ACTION_MOVE:
				vTracker.addMovement(event);
				distance = currentY - downY;
				tranSpring.setEndValue(distance);
				float degree = MAX_DEGREE*distance/screenHeight;
				if(downX < screenWidth/2){
					degree = -degree;
				}
				rotateSpring.setEndValue(degree);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				vTracker.computeCurrentVelocity(UNIT);   
		        float velocityY = vTracker.getYVelocity();
				animOutIfNeeded(distance,velocityY);
				if(vTracker != null){
					vTracker.recycle();
					vTracker = null;
				}
				break;
			}
			return true;
		}
	}
	
	/**
	 * 移出视图动画
	 * @param scrollDis 滑动距离
	 * @param velocityY 滑动速度
	 */
	private void animOutIfNeeded(float scrollDis,float velocityY) {
		float tranY = 0;
		tranSpring.setOvershootClampingEnabled(true);
		//下移
		if(velocityY > FLING_VELOCITY 
			|| (scrollDis > OUT_DISTANCE_BOUDARY)){
			if(hasNext()){
				tranY = screenHeight;
				//和endvalue不相等，不会rest，下一个view会出现旋转，所以要设置为rest
				rotateSpring.setAtRest();
			}else{
				//角度回正
				rotateSpring.setEndValue(0);
				Toast.makeText(getContext(), "已经是最后一条", Toast.LENGTH_SHORT).show();
			}
		}else if(velocityY < -FLING_VELOCITY
				|| (scrollDis < -OUT_DISTANCE_BOUDARY)){ //上移
			if(hasPre()){
				tranY = -screenHeight;
				rotateSpring.setAtRest();
			}else{
				rotateSpring.setEndValue(0);
				Toast.makeText(getContext(), "已经是第一条", Toast.LENGTH_SHORT).show();
			}
		}else{
			//不移动
			tranSpring.setOvershootClampingEnabled(false);
			rotateSpring.setEndValue(0);
		}
		tranSpring.setEndValue(tranY);
	}
	
	/**
	 * 确保获得正确位置的View
	 */
	private void ensureCorrectView() {
		if(currentItem != getCurrentItem()){
			currentItem = getCurrentItem();
			currentView = getCurrentView();
			System.out.println(">>>rotation:" + currentView.getRotation());
		}
	}

	private class ExampleSpringListener extends SimpleSpringListener {
		@Override
		public void onSpringUpdate(Spring spring) {
			ensureCorrectView();
			float value = (float) spring.getCurrentValue();
			String springId = spring.getId();
			if(springId.equals(tranSpring.getId())){
				currentView.setTranslationY(value);
				if(spring.isAtRest()){
					if(value >= screenHeight){
						nextPage();
					}else if(value <= -screenHeight){
						prePage();
					}
				}
			}else if(springId.equals(mScaleSpring.getId())){
				currentView.setScaleX(value);
				currentView.setScaleY(value);
			}else if(springId.equals(rotateSpring.getId())){
				currentView.setRotation(value);
			}
		}
	}
	
	private class MyPageChageListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if(pageChangeListener != null){
				pageChangeListener.onPageScrollStateChanged(arg0);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if(pageChangeListener != null){
				pageChangeListener.onPageScrolled(arg0, arg1, arg2);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if(pageChangeListener != null){
				pageChangeListener.onPageSelected(position);
			}
			if(currentView != null){
				currentView.setTranslationY(0);
				currentView.setRotation(0);
			}
			mScaleSpring.setCurrentValue(MIN_SCALE);
			mScaleSpring.setEndValue(MAX_SCALE);
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
	
	@Override
	@Deprecated
	public void setAdapter(PagerAdapter arg0) {
		throw new RuntimeException(
				"setAdapter cannot be used.");
	}
	
	@Override
	@Deprecated
	public void setCurrentItem(int item) {
		throw new RuntimeException(
				"setCurrentItem cannot be used.");
	}
	
	@Override
	@Deprecated
	public void setCurrentItem(int item, boolean smoothScroll) {
		throw new RuntimeException(
				"setCurrentItem cannot be used.");
	}
}
