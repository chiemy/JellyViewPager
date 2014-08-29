package com.example.cviewpager;

import android.view.View;
import android.view.ViewGroup;

public interface SampleAdapter{

	public int getCount();

	public Object getItem(int position);

	public long getItemId(int position);

	public View getView(int position,View convertView,ViewGroup container);
	
}
