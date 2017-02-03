package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity1 extends BaseActivity {
	
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		button = (Button) findViewById(R.id.btn);
		button.setText("点击进入三级页面");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity1.this, MainActivity2.class);
				startActivity(intent);
			}
		});
		sendRequest("二级页面");
	}

}
