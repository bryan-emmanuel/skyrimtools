package com.piusvelte.skyrimtools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StatCalculator extends Activity implements OnClickListener {
	private EditText mFld_level;
	private EditText mFld_magicka;
	private EditText mFld_health;
	private EditText mFld_stamina;
	private Button mBtn_levelup;
	private Button mBtn_undo;
	private Button mBtn_reset;
	private int last_changed_idx = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statcalculator);
		mFld_level = (EditText) findViewById(R.id.fld_level);
		mFld_magicka = (EditText) findViewById(R.id.fld_magicka);
		mFld_health = (EditText) findViewById(R.id.fld_health);
		mFld_stamina = (EditText) findViewById(R.id.fld_stamina);
		mBtn_levelup = (Button) findViewById(R.id.btn_levelup);
		mBtn_undo = (Button) findViewById(R.id.btn_undo);
		mBtn_reset = (Button) findViewById(R.id.btn_reset);
		SharedPreferences sp = getSharedPreferences(getString(R.string.key_preferences), MODE_PRIVATE);
		mFld_level.setText(sp.getString(getString(R.string.key_level), "1"));
		mFld_magicka.setText(sp.getString(getString(R.string.key_magicka), "100"));
		mFld_health.setText(sp.getString(getString(R.string.key_health), "100"));
		mFld_stamina.setText(sp.getString(getString(R.string.key_stamina), "100"));
		mBtn_levelup.setOnClickListener(this);
		mBtn_undo.setOnClickListener(this);
		mBtn_reset.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		SharedPreferences sp = getSharedPreferences(getString(R.string.key_preferences), MODE_PRIVATE);
		SharedPreferences.Editor spe = sp.edit();
		spe.putString(getString(R.string.key_level), mFld_level.getEditableText().toString());
		spe.putString(getString(R.string.key_magicka), mFld_magicka.getEditableText().toString());
		spe.putString(getString(R.string.key_health), mFld_health.getEditableText().toString());
		spe.putString(getString(R.string.key_stamina), mFld_stamina.getEditableText().toString());
		spe.commit();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v == mBtn_levelup) {
			final int level = Integer.parseInt(mFld_level.getEditableText().toString());
			if (level < 81) {
				(new AlertDialog.Builder(this))
				.setItems(R.array.categories, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mFld_level.setText(Integer.toString(level + 1));
						switch (which) {
						case 0:
							mFld_magicka.setText(Integer.toString(Integer.parseInt(mFld_magicka.getEditableText().toString()) + 10));
							break;
						case 1:
							mFld_health.setText(Integer.toString(Integer.parseInt(mFld_health.getEditableText().toString()) + 10));
							break;
						case 2:
							mFld_stamina.setText(Integer.toString(Integer.parseInt(mFld_stamina.getEditableText().toString()) + 10));
							break;
						}
						last_changed_idx = which;
						mBtn_undo.setEnabled(true);
					}
				})
				.show();
			}
		} else if (v == mBtn_undo) {
			mBtn_undo.setEnabled(false);
			mFld_level.setText(Integer.toString(Integer.parseInt(mFld_level.getEditableText().toString()) - 1));
			switch (last_changed_idx) {
			case 0:
				mFld_magicka.setText(Integer.toString(Integer.parseInt(mFld_magicka.getEditableText().toString()) - 10));
				break;
			case 1:
				mFld_health.setText(Integer.toString(Integer.parseInt(mFld_health.getEditableText().toString()) - 10));
				break;
			case 2:
				mFld_stamina.setText(Integer.toString(Integer.parseInt(mFld_stamina.getEditableText().toString()) - 10));
				break;
			}
		} else if (v == mBtn_reset) {
			mFld_level.setText("1");
			mFld_magicka.setText("100");
			mFld_health.setText("100");
			mFld_stamina.setText("100");
		}
	}
}