package com.chiemy.jellyviewpager;

import com.chiemy.jellyviewpager.util.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TestFragment extends Fragment {
	boolean visible = true;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_layout, container, false);
		initUI(rootView);
		return rootView;
	}
	
	private void initUI(View root){
		final View tv1 = root.findViewById(R.id.textView1);
		final View tv2 = root.findViewById(R.id.textView2);
		ImageView iv = (ImageView) root.findViewById(R.id.imageView1);
		Bundle bundle = getArguments();
		int res = bundle.getInt(Constant.KEY, R.drawable.a);
		iv.setImageResource(res);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!visible){
					visible = true;
					tv1.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.VISIBLE);
				}else{
					visible = false;
					tv1.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
}
