package com.piusvelte.skyrimtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {
	private Button mBtn_statscalculator;
	private Button mBtn_perkscalculator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mBtn_statscalculator = (Button) findViewById(R.id.btn_statscalculator);
		mBtn_perkscalculator = (Button) findViewById(R.id.btn_perkscalculator);
		mBtn_statscalculator.setOnClickListener(this);
		mBtn_perkscalculator.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mBtn_statscalculator) {
			startActivity(new Intent(this, StatCalculator.class));
		} else if (v == mBtn_perkscalculator) {
			startActivity(new Intent(this, PerkCalculator.class));
		}
	}
	
}
