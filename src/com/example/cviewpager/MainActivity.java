package com.example.cviewpager;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	JellyViewPager pager;
	int currentItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pager = (JellyViewPager) findViewById(R.id.myViewPager1);
		//pager.setAdapter(new TestPagerAdapter(this));
		pager.setAdapter(new TestFragPagerAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(new OnJellyPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
			}
		});
	}
	
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.preBtn:
			pager.showPre();
			break;
		case R.id.nextBtn:
			pager.showNext();
			break;
		}
	}
	
	
}
