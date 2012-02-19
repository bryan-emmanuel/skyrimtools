package com.piusvelte.skyrimtools;

import com.google.ads.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends Activity implements OnClickListener {
	protected static final String GOOGLE_AD_ID = "";
	private Button mBtn_characterbuilder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (getPackageName().toLowerCase().contains("free")) {
			AdView adView = new AdView(this, AdSize.BANNER, GOOGLE_AD_ID);
			((LinearLayout) findViewById(R.id.ads)).addView(adView);
			adView.loadAd(new AdRequest());
		}
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
