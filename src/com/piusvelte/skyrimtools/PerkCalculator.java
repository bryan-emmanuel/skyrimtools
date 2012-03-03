package com.piusvelte.skyrimtools;

import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_NAME;
import static com.piusvelte.skyrimtools.CharacterBuilder.RESET_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.SAVE_ID;
import static com.piusvelte.skyrimtools.Main.GOOGLE_AD_ID;

import com.google.ads.*;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Character_perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Character_perks_temp;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Init_character_perks_temp;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Perk_relationships;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Save_character_perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Vcharacter_perks;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PerkCalculator extends ListActivity {
	private static String EXTRA_PERK_ID = "perk";
	private long mCharacter_id = 0;
	private String mCharacter_name;
	private long mPerk = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perkcalculator);
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
			if (i.hasExtra(EXTRA_PERK_ID)) {
				mPerk = i.getLongExtra(EXTRA_PERK_ID, 0);
			}
		}
		registerForContextMenu(getListView());
		// load the temp table, for the top level
		if (mPerk == 0) {
			final ProgressDialog loadingDialog = new ProgressDialog(this);
			final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					getContentResolver().update(Init_character_perks_temp.CONTENT_URI, null, null, null);
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
		}
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
					values.put(Character_perks.value, 0);
					getContentResolver().update(Character_perks.CONTENT_URI, values, Character_perks._ID + "=?", new String[]{Long.toString(mCharacter_id)});
					getContentResolver().update(Init_character_perks_temp.CONTENT_URI, null, null, null);
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
					getContentResolver().update(Save_character_perks.CONTENT_URI, null, null, null);
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor c;
		if (mPerk > 0) {
			c = managedQuery(Vcharacter_perks.CONTENT_URI, new String[]{Vcharacter_perks._ID, Vcharacter_perks.character, Vcharacter_perks.character_name, Vcharacter_perks.value, Vcharacter_perks.perk, Vcharacter_perks.perk_name, Vcharacter_perks.max, Vcharacter_perks.hasChildren, Vcharacter_perks.desc, Vcharacter_perks.parentHasValue}, Vcharacter_perks.character + "=? and " + Vcharacter_perks.perk + " in (select " + SkyrimToolsProvider.TABLE_PERK_RELATIONSHIPS + "." + Perk_relationships.child + " from " + SkyrimToolsProvider.TABLE_PERK_RELATIONSHIPS + " where " + SkyrimToolsProvider.TABLE_PERK_RELATIONSHIPS + "." + Perk_relationships.parent + "=?)", new String[]{Long.toString(mCharacter_id), Long.toString(mPerk)}, null);
		} else {
			c = managedQuery(Vcharacter_perks.CONTENT_URI, new String[]{Vcharacter_perks._ID, Vcharacter_perks.character, Vcharacter_perks.character_name, Vcharacter_perks.value, Vcharacter_perks.perk, Vcharacter_perks.perk_name, Vcharacter_perks.max, Vcharacter_perks.hasChildren, Vcharacter_perks.desc, Vcharacter_perks.parentHasValue}, Vcharacter_perks.character + "=? and max=0", new String[]{Long.toString(mCharacter_id)}, null);
		}
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.perkrow, c, new String[] {Vcharacter_perks.perk_name}, new int[] {R.id.perk});
		sca.setViewBinder(mViewBinder);
		setListAdapter(sca);
	}

	private final SimpleCursorAdapter.ViewBinder mViewBinder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (columnIndex == SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()) {
				Context context = PerkCalculator.this.getApplicationContext();
				LinearLayout perkLayout = (LinearLayout) view;
				perkLayout.removeAllViews();
				final long id = cursor.getLong(SkyrimToolsProvider.Vcharacter_perksColumns._id.ordinal());
				int max = cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.max.ordinal());
				final int value = cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.value.ordinal());
				final String desc = cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.desc.ordinal());
				if ((desc != null) && (desc.length() > 0)) {
					Button child = new Button(context);
					if (max > 0) {
						child.setText(String.format(getString(R.string.lbl_perkname), cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()), cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.value.ordinal())));
					} else {
						child.setText(cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()));
					}
					child.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							(new AlertDialog.Builder(PerkCalculator.this))
							.setTitle(R.string.lbl_description)
							.setMessage(desc)
							.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							})
							.show();
						}

					});
					perkLayout.addView(child, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				} else {
					TextView child = new TextView(context);
					if (max > 0) {
						child.setText(String.format(getString(R.string.lbl_perkname), cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()), cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.value.ordinal())));
					} else {
						child.setText(cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()));
					}
					perkLayout.addView(child, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				if (max > 0) {
					Button child = new Button(context);
					child.setText(R.string.lbl_perkadd);
					if ((value < max) && (cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.parentHasValue.ordinal()) > 0)) {
						child.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// load PerkCalculator with this category
								ContentValues values = new ContentValues();
								values.put(Character_perks.value, value + 1);
								getContentResolver().update(Character_perks_temp.CONTENT_URI, values, Character_perks._ID + "=?", new String[]{Long.toString(id)});
							}

						});
					} else {
						child.setEnabled(false);
					}
					perkLayout.addView(child, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				if (cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.hasChildren.ordinal()) > 0) {
					// if there are sub-items, they'll load through onClick
					Button child = new Button(context);
					child.setText(R.string.lbl_perkchild);
					child.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// load PerkCalculator with this category
							startActivity(new Intent(PerkCalculator.this, PerkCalculator.class).putExtra(EXTRA_CHARACTER_ID, mCharacter_id).putExtra(EXTRA_CHARACTER_NAME, mCharacter_name).putExtra(EXTRA_PERK_ID, id));
						}

					});
					perkLayout.addView(child, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				return true;
			} else {
				return false;
			}
		}
	};

}
