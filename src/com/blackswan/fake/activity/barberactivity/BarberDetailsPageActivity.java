package com.blackswan.fake.activity.barberactivity;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BarberDetailsPageActivity extends BaseActivity
{
	ImageView reback;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barberdetails);
		reback = (ImageView) findViewById(R.id.barberdetailsreback);
		reback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}

}
