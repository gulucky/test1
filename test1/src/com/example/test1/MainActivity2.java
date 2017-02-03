package com.example.test1;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity2 extends BaseActivity {
	
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		button = (Button) findViewById(R.id.btn);
		button.setText("这是三级页面");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		sendRequest("三级页面");
	}

}
