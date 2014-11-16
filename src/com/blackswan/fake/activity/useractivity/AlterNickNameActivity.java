package com.blackswan.fake.activity.useractivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;

public class AlterNickNameActivity extends BaseActivity {
	private EditText name;
	private TextView msg;
	private ImageView dele;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alternickname);

		this.initViews();
	}

	@Override
	protected void initViews() {
		name = (EditText) findViewById(R.id.et_altername);
		msg = (TextView) findViewById(R.id.tv_altername_msg);
		dele = (ImageView) findViewById(R.id.iv_altername_dele);
		findViewById(R.id.iv_altername_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
		
		Intent i = getIntent();
		name.setText(i.getStringExtra("name"));

		dele.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				name.setText("");
			}
		});
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (name.length() == 0) {
					dele.setVisibility(View.GONE);
				} else {
					dele.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (name.length() > 7) {
					msg.setTextColor(Color.RED);
					msg.setText("昵称最大长度为7!     " + name.length() + "/7");
				} else {
					msg.setTextColor(getResources().getColor(R.color.black3));
					msg.setText(name.length() + "/7");
				}
			}
		});
		findViewById(R.id.id_altername_commit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (name.length() > 7) {
							showLongToast("昵称长度最长为7~");
						} else {
							Intent it = new Intent();
							it.putExtra("newName", name.getText().toString());
							setResult(Activity.RESULT_OK, it);
							defaultFinish();
						}
					}
				});
	}

	@Override
	protected void initEvents() {

	}

}
