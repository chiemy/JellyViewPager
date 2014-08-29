package com.example.cviewpager;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;

public class MyViewPager extends ViewPager{
	/**
	 * View缩放
	 */
	private static final float SCALE = 0.2f;
	
	private SampleAdapter mAdapter;
	private MyTouchListener mListener;
	private Spring mScaleSpring;
	private Spring tranSpring;
	private final BaseSpringSystem mSpringSystem = SpringSystem.create();
	private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
	
	private int flingVelocity;
	private View currentView;
	private int currentItem = -1;
	
	private int screenHeight;
	
	private SparseArray<View> viewHolder = new SparseArray<View>();

	public MyViewPager(Context context) {
		super(context);
		init(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mListener = new MyTouchListener();
		mScaleSpring = mSpringSystem.createSpring();
		tranSpring = mSpringSystem.createSpring();
		mScaleSpring.addListener(mSpringListener);
		tranSpring.addListener(mSpringListener);
		this.setOnPageChangeListener(new MyPageChageListener());
		
		flingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
	}

	public void setAdapter(SampleAdapter adapter) {
		if (adapter == null) {
			new NullPointerException("SampleAdapter cannot be null!");
		}
		mAdapter = adapter;
		viewHolder.clear();
		super.setAdapter(new ViewPagerAdapter());
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
	public void setAdapter(PagerAdapter arg0) {
		throw new RuntimeException(
				"setOnItemClickListener cannot be used with a spinner.");
	}
	
	@Override
	public void setCurrentItem(int item) {
		this.setCurrentItem(item,false);
	}
	
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}
	
	private void nextPage() {
		setCurrentItem(currentItem + 1);
	}
	
	private void prePage(){
		setCurrentItem(currentItem - 1);
	}
	
	private boolean hasNext() {
		return getCurrentItem() < mAdapter.getCount() - 1;
	}
	
	private boolean hasPre() {
		return getCurrentItem() > 0;
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
			View view = mAdapter.getView(position, convertView, container);
			viewHolder.put(position, view);
			if(position != 0){
				view.setScaleX(SCALE);
				view.setScaleY(SCALE);
			}
			render(view);
			container.addView(view);
			return view;
		}
	}

	private View getCurrentView() {
		return viewHolder.get(getCurrentItem());
	}
	
	private void render(View view) {
		view.setOnTouchListener(mListener);
	}
	
	private float downY;
	private float distance;
	private class MyTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			float currentY = event.getRawY();
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				downY = event.getRawY();
				tranSpring.setCurrentValue(0);
				tranSpring.setEndValue(0);
				break;
			case MotionEvent.ACTION_MOVE:
				distance = currentY - downY;
				tranSpring.setEndValue(distance);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				float tranY = 0;
				if(currentView != null && Math.abs(distance) > (currentView.getHeight() / 2)){
					boolean next;
					if(distance > 0){
						next = true;
						if(hasNext()){
							tranY = screenHeight;
						}else{
							Toast.makeText(getContext(), "已经是最后一条", Toast.LENGTH_SHORT).show();
						}
					}else{
						next = false;
						if(hasPre()){
							tranY = -screenHeight;
						}else{
							Toast.makeText(getContext(), "已经是第一条", Toast.LENGTH_SHORT).show();
						}
					}
					tranSpring.setOvershootClampingEnabled(true);
				}else{
					tranSpring.setOvershootClampingEnabled(false);
					tranY = 0; 
				}
				tranSpring.setEndValue(tranY);
				break;
			}
			return true;
		}

	}

	private class ExampleSpringListener implements SpringListener {
		@Override
		public void onSpringUpdate(Spring spring) {
			if(currentItem != getCurrentItem()){
				currentItem = getCurrentItem();
				currentView = getCurrentView();
			}
			
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
			}
		}

		@Override
		public void onSpringActivate(Spring arg0) {
		}

		@Override
		public void onSpringAtRest(Spring spring) {
		}

		@Override
		public void onSpringEndStateChange(Spring arg0) {
		}
	}
	
	private class MyPageChageListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			if(currentView != null){
				currentView.setTranslationY(0);
			}
			mScaleSpring.setCurrentValue(SCALE);
			mScaleSpring.setEndValue(1);
		}
	}
}
