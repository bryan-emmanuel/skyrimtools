package com.piusvelte.skyrimtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {
	private Button mBtn_characterbuilder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mBtn_characterbuilder = (Button) findViewById(R.id.btn_characterbuilder);
		mBtn_characterbuilder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mBtn_characterbuilder) {
			startActivity(new Intent(this, CharacterBuilder.class));
		}
	}
	
}
