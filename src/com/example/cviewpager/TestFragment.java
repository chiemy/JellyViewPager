package com.example.cviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_layout, container, false);
		TextView tv = (TextView) rootView.findViewById(R.id.tv);
		tv.setText("frag");
		return rootView;
	}
	
}
