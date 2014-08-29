package com.example.cviewpager;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private LayoutInflater inflater;
	MyViewPager pager;
	int currentItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = LayoutInflater.from(this);
		pager = (MyViewPager) findViewById(R.id.myViewPager1);
		pager.setAdapter(new MyAdapter());
	}
	
	public void onClick(View view) {
		pager.setCurrentItem(pager.getCurrentItem() + 1);
	}
	
	public class MyAdapter implements SampleAdapter{

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Integer getItem(int position) {
			return R.drawable.ic_launcher;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position,View convertView,ViewGroup container) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.frag_layout, null);
				convertView.setBackgroundColor(Color.BLUE);
			}
			ImageView iv = ViewHolder.get(convertView, R.id.imageView1);
			TextView tv = ViewHolder.get(convertView,R.id.tv);
			tv.setText(position + "");
			iv.setImageResource(getItem(position));
			return convertView;
		}
		
	}
}
