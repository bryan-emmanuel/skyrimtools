package com.piusvelte.skyrimtools;

import com.piusvelte.skyrimtools.SkyrimToolsProvider.Character_perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Characters;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.Perks;
import com.piusvelte.skyrimtools.SkyrimToolsProvider.PerksColumns;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class CharacterBuilder extends ListActivity implements OnClickListener {
	protected static String EXTRA_CHARACTER_ID = "character_id";
	protected static String EXTRA_CHARACTER_NAME = "character_name";
	protected static final int RESET_ID = 0;
	protected static final int SAVE_ID = 1;
	protected static final int UNDO_ID = 2;
	private Button mBtn_buildcharacter;
	protected static final int DEFAULT_LEVEL = 1;
	protected static final int DEFAULT_MAGICKA = 100;
	protected static final int DEFAULT_HEALTH = 100;
	protected static final int DEFAULT_STAMINA = 100;
	protected static final int DEFAULT_VALUE = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.characterbuilder);
		mBtn_buildcharacter = (Button) findViewById(R.id.btn_buildcharacter);
		mBtn_buildcharacter.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor c = managedQuery(Characters.CONTENT_URI, new String[]{Characters._ID, Characters.name}, null, null, null);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.characterrow, c, new String[] {Characters.name}, new int[] {R.id.name});
		sca.setViewBinder(mViewBinder);
		setListAdapter(sca);
	}

	private final SimpleCursorAdapter.ViewBinder mViewBinder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (columnIndex == SkyrimToolsProvider.CharactersColumns.name.ordinal()) {
				Button btn_name = (Button) view;
				final long id = cursor.getLong(SkyrimToolsProvider.CharactersColumns._id.ordinal());
				final String name = cursor.getString(SkyrimToolsProvider.CharactersColumns.name.ordinal());
				btn_name.setText(name);
				btn_name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(CharacterBuilder.this)
						.setItems(new String[]{getString(R.string.lbl_statcalculator), getString(R.string.lbl_perkcalculator)}, 
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										switch (which) {
										case 0:
											startActivity(new Intent(CharacterBuilder.this, StatCalculator.class).putExtra(EXTRA_CHARACTER_ID, id).putExtra(EXTRA_CHARACTER_NAME, name));
											break;
										case 1:
											startActivity(new Intent(CharacterBuilder.this, PerkCalculator.class).putExtra(EXTRA_CHARACTER_ID, id).putExtra(EXTRA_CHARACTER_NAME, name));
											break;
										}		
									}})
						.show();
					}
					
				});
				return true;
			} else {
				return false;
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v == mBtn_buildcharacter) {
			final EditText mFld_name = new EditText(this);
			new AlertDialog.Builder(this)
			.setTitle(R.string.lbl_name)
			.setView(mFld_name)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String name = mFld_name.getText().toString();
					if ((name != null) && (name.length() > 0)) {
						// init character
						ContentValues values = new ContentValues();
						values.put(Characters.name, name);
						long id = Long.parseLong(getContentResolver().insert(Characters.CONTENT_URI, values).getLastPathSegment());
						Cursor c  = getContentResolver().query(Perks.CONTENT_URI, new String[]{Perks._ID}, null, null, null);
						if (c.moveToFirst()) {
							while (!c.isAfterLast()) {
								values.clear();
								values.put(Character_perks.character, id);
								values.put(Character_perks.perk, c.getLong(PerksColumns._id.ordinal()));
								getContentResolver().insert(Character_perks.CONTENT_URI, values);
								c.moveToNext();
							}
						}
					}
				}
				
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			})
			.show();
		}
	}

}
