package com.example.cviewpager;

import android.view.View;

public interface JellyPagerAdapter{

	public int getCount();

	public Object getItem(int position);

	public View getView(int position,View convertView);
	
}
