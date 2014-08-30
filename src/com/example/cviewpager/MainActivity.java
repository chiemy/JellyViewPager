package com.example.cviewpager;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private LayoutInflater inflater;
	JellyViewPager pager;
	int currentItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = LayoutInflater.from(this);
		pager = (JellyViewPager) findViewById(R.id.myViewPager1);
		pager.setAdapter(new MyAdapter());
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				Toast.makeText(MainActivity.this, "" + arg0, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
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
	
	public class MyAdapter implements JellyPagerAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Integer getItem(int position) {
			return R.drawable.ic_launcher;
		}

		@Override
		public View getView(int position,View convertView) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.frag_layout, null);
			}
			TextView tv = ViewHolder.get(convertView,R.id.tv);
			tv.setText(position + "");
			return convertView;
		}
		
	}
}
