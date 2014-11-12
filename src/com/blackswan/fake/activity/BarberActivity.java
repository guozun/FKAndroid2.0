package com.blackswan.fake.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.view.FakeRefreshListView.OnCancelListener;
import com.blackswan.fake.view.FakeRefreshListView.OnRefreshListener;

public class BarberActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener, OnCancelListener
{
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barber);
	}
	
	@Override
	protected void initViews() {
	}
	
	@Override
	protected void initEvents() {
	}
	
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	

}
