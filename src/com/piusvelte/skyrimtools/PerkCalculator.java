package com.piusvelte.skyrimtools;

import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_ID;
import static com.piusvelte.skyrimtools.CharacterBuilder.EXTRA_CHARACTER_NAME;

import com.piusvelte.skyrimtools.SkyrimToolsProvider.Character_perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Vcharacter_perks;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PerkCalculator extends ListActivity {
	private static String EXTRA_PERK_ID = "perk";
	private long mCharacter_id = 0;
	private String mCharacter_name;
	private long mPerk = 0;
	private static final int RESET_ID = 0;
	private static final int SAVE_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perkcalculator);
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
		switch (item.getItemId()) {
		case RESET_ID:
			ContentValues values = new ContentValues();
			values.put(Character_perks.value, 0);
			getContentResolver().update(Character_perks.CONTENT_URI, values, Character_perks._ID + "=?", new String[]{Long.toString(mCharacter_id)});
			return true;
		case SAVE_ID:
			//SAVE
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor c;
		//Vcharacter_perks._id, Vcharacter_perks.character_id, Vcharacter_perks.character_name, Vcharacter_perks.value, Vcharacter_perks.perk, Vcharacter_perks.perk_name, Vcharacter_perks.max, Vcharacter_perks.child, Vcharacter_perks.desc
		if (mPerk > 0) {
			c = managedQuery(Vcharacter_perks.CONTENT_URI, new String[]{Vcharacter_perks._ID, Vcharacter_perks.character_id, Vcharacter_perks.character_name, Vcharacter_perks.value, Vcharacter_perks.perk, Vcharacter_perks.perk_name, Vcharacter_perks.max, Vcharacter_perks.child, Vcharacter_perks.desc}, "_id=? and _id in (select perk_relationships.child from perk_relationships where perk_relationships._id=?)", new String[]{Long.toString(mCharacter_id), Long.toString(mPerk)}, null);
		} else {
			c = managedQuery(Vcharacter_perks.CONTENT_URI, new String[]{Vcharacter_perks._ID, Vcharacter_perks.character_id, Vcharacter_perks.character_name, Vcharacter_perks.value, Vcharacter_perks.perk, Vcharacter_perks.perk_name, Vcharacter_perks.max, Vcharacter_perks.child, Vcharacter_perks.desc}, "_id=? and (select perk_relationships._id from perk_relationships where perk_relationships.child=perks._id) is null", new String[]{Long.toString(mCharacter_id)}, null);
		}
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.perkrow, c, new String[] {Perks.name}, new int[] {R.id.perkname});
		sca.setViewBinder(mViewBinder);
		setListAdapter(sca);
	}

	private final SimpleCursorAdapter.ViewBinder mViewBinder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (columnIndex == SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()) {
				Button perkname = (Button) view;
				final long id = cursor.getLong(SkyrimToolsProvider.Vcharacter_perksColumns._id.ordinal());
				int max = cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.max.ordinal());
				final int value = cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.value.ordinal());
				final String desc = cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.desc.ordinal());
				if (max > 0) {
					perkname.setText(String.format(getString(R.string.lbl_perkname), cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()), cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.value.ordinal())));
				} else {
					perkname.setText(cursor.getString(SkyrimToolsProvider.Vcharacter_perksColumns.perk_name.ordinal()));
				}
				perkname.setOnClickListener(new OnClickListener() {

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
				RelativeLayout viewParent = (RelativeLayout) view.getParent();
				Button perkchild = (Button) viewParent.findViewById(R.id.perkchild);
				if (((max == 0) || (value > 0)) && (cursor.getInt(SkyrimToolsProvider.Vcharacter_perksColumns.child.ordinal()) > 0)) {
					// if there are sub-items, they'll load through onClick
					perkchild.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// load PerkCalculator with this category
							startActivity(new Intent(PerkCalculator.this, PerkCalculator.class).putExtra(EXTRA_CHARACTER_ID, mCharacter_id).putExtra(EXTRA_CHARACTER_NAME, mCharacter_name).putExtra(EXTRA_PERK_ID, id));
						}

					});
				} else {
					perkname.setEnabled(false);
				}
				Button perkadd = (Button) (viewParent).findViewById(R.id.perkadd);
				if (value < max) {
					// if there are sub-items, they'll load through onClick
					perkadd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// load PerkCalculator with this category
							ContentValues values = new ContentValues();
							values.put(Character_perks.value, value + 1);
							getContentResolver().update(Character_perks.CONTENT_URI, values, Character_perks._ID + "=?", new String[]{Long.toString(id)});
						}

					});
				} else {
					perkadd.setEnabled(false);
				}
				// set the add button
				return true;
			} else {
				return false;
			}
		}
	};
	
}
