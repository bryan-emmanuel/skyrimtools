package com.piusvelte.skyrimtools;

import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_LEVEL;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_MAGICKA;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_HEALTH;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_STAMINA;
import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_NAME;
import static com.piusvelte.skyrimtools.CharacterBuilder.RESET_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.SAVE_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.UNDO_ID;
import static com.piusvelte.skyrimtools.Main.GOOGLE_AD_ID;

import com.google.ads.*;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Characters;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.CharactersColumns;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Vcharacters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatCalculator extends Activity implements OnClickListener {
	private EditText mFld_level;
	private EditText mFld_magicka;
	private EditText mFld_health;
	private EditText mFld_stamina;
	private Button mBtn_levelup;
	private Button mBtn_reset;
	private int last_changed_idx = 0;
	private long mCharacter_id = 0;
	private String mCharacter_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statcalculator);
		if (getPackageName().toLowerCase().contains("free")) {
			AdView adView = new AdView(this, AdSize.BANNER, GOOGLE_AD_ID);
			((LinearLayout) findViewById(R.id.ads)).addView(adView);
			adView.loadAd(new AdRequest());
		}
		Intent i = getIntent();
		if (i != null) {
			if (i.hasExtra(EXTRA_CHARACTER_ID)) {
				mCharacter_id = i.getLongExtra(EXTRA_CHARACTER_ID, 0);
			}
			if (i.hasExtra(EXTRA_CHARACTER_NAME)) {
				mCharacter_name = i.getStringExtra(EXTRA_CHARACTER_NAME);
				((TextView) findViewById(R.id.fld_name)).setText(mCharacter_name);
			}
		}
		mFld_level = (EditText) findViewById(R.id.fld_level);
		mFld_magicka = (EditText) findViewById(R.id.fld_magicka);
		mFld_health = (EditText) findViewById(R.id.fld_health);
		mFld_stamina = (EditText) findViewById(R.id.fld_stamina);
		mBtn_levelup = (Button) findViewById(R.id.btn_levelup);
		mBtn_levelup.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, RESET_ID, 0, R.string.lbl_reset).setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(0, SAVE_ID, 0, R.string.lbl_save).setIcon(android.R.drawable.ic_menu_save);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final ProgressDialog loadingDialog;
		final AsyncTask<Void, Void, Void> asyncTask;
		switch (item.getItemId()) {
		case RESET_ID:
			loadingDialog = new ProgressDialog(this);
			asyncTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					ContentValues values = new ContentValues();
					values.put(Characters.level, DEFAULT_LEVEL);
					values.put(Characters.magicka, DEFAULT_MAGICKA);
					values.put(Characters.health, DEFAULT_HEALTH);
					values.put(Characters.stamina, DEFAULT_STAMINA);
					getContentResolver().update(Characters.CONTENT_URI, values, Characters._ID + "=?", new String[]{Long.toString(mCharacter_id)});
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}
			};
			loadingDialog.setMessage(getString(R.string.msg_loading));
			loadingDialog.setCancelable(true);
			loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {				
				@Override
				public void onCancel(DialogInterface dialog) {
					if (!asyncTask.isCancelled()) asyncTask.cancel(true);
				}
			});
			loadingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			loadingDialog.show();
			asyncTask.execute();
			return true;
		case SAVE_ID:
			loadingDialog = new ProgressDialog(this);
			asyncTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					ContentValues values = new ContentValues();
					values.put(Characters.level, Integer.parseInt(mFld_level.getEditableText().toString()));
					values.put(Characters.magicka, Integer.parseInt(mFld_magicka.getEditableText().toString()));
					values.put(Characters.health, Integer.parseInt(mFld_health.getEditableText().toString()));
					values.put(Characters.stamina, Integer.parseInt(mFld_stamina.getEditableText().toString()));
					getContentResolver().update(Characters.CONTENT_URI, values, Characters._ID + "=?", new String[]{Long.toString(mCharacter_id)});
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}
			};
			loadingDialog.setMessage(getString(R.string.msg_loading));
			loadingDialog.setCancelable(true);
			loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {				
				@Override
				public void onCancel(DialogInterface dialog) {
					if (!asyncTask.isCancelled()) asyncTask.cancel(true);
				}
			});
			loadingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			loadingDialog.show();
			asyncTask.execute();
			return true;
		case UNDO_ID:
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadStats();
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
					}
				})
				.show();
			}
		} else if (v == mBtn_reset) {
			mFld_level.setText("1");
			mFld_magicka.setText("100");
			mFld_health.setText("100");
			mFld_stamina.setText("100");
			loadStats();
		}
	}
	
	private void loadStats() {
		Cursor c = managedQuery(Vcharacters.CONTENT_URI, new String[]{Characters._ID, Characters.name, Characters.level, Characters.magicka, Characters.health, Characters.stamina}, Characters._ID + "=?", new String[]{Long.toString(mCharacter_id)}, null);
		if (c.moveToFirst()) {
			mFld_level.setText(Integer.toString(c.getInt(CharactersColumns.level.ordinal())));
			mFld_magicka.setText(Integer.toString(c.getInt(CharactersColumns.magicka.ordinal())));
			mFld_health.setText(Integer.toString(c.getInt(CharactersColumns.health.ordinal())));
			mFld_stamina.setText(Integer.toString(c.getInt(CharactersColumns.stamina.ordinal())));
		}
	}
}