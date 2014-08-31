package com.chiemy.jellyviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chiemy.jellyviewpager.R;

public class TestPagerAdapter extends PagerAdapter{
	
	private LayoutInflater inflater;
	
	public TestPagerAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflater.inflate(R.layout.frag_layout, null);
		container.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
}
