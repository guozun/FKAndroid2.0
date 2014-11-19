package com.blackswan.fake.adapter;

import java.util.ArrayList;
import java.util.List;

import com.blackswan.fake.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class HairdoHairFilterAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	public HairdoHairFilterAdapter(FragmentManager fm) {
		super(fm);
		mFragments.add(new HairType());
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	// 第一个筛选Fragment
	public class HairType extends Fragment {
		private CheckBox cbLong;
		private CheckBox cbShort;
		private CheckBox cbMiddle;
		private CheckBox cbMan;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.hairdo_filter_type,
					container, false);
			cbLong = (CheckBox) root
					.findViewById(R.id.cb_hairdo_filter_dialog_Long);
			cbMiddle = (CheckBox) root
					.findViewById(R.id.cb_hairdo_filter_dialog_middle);
			cbShort = (CheckBox) root
					.findViewById(R.id.cb_hairdo_filter_dialog_short);
			cbMan = (CheckBox) root
					.findViewById(R.id.cb_hairdo_filter_dialog_man);
			return root;
		}

		public Boolean[] getAllCheckValue() {
			Boolean[] value = new Boolean[4];
			value[0] = cbLong.isChecked();
			value[1] = cbMiddle.isChecked();
			value[2] = cbShort.isChecked();
			value[3] = cbMan.isChecked();
			return value;
		}

	}

}
