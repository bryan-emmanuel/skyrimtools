package com.piusvelte.skyrimtools;

import com.piusvelte.skyrimtools.SkyrimToolsProvider.Characters;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

public class CharacterBuilder extends ListActivity {
	protected static String EXTRA_CHARACTER_ID = "character_id";
	protected static String EXTRA_CHARACTER_NAME = "character_name";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.characterbuilder);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//_id, name, level, magicka, health, stamina
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
					public void onClick(View arg0) {
						startActivity(new Intent(CharacterBuilder.this, PerkCalculator.class).putExtra(EXTRA_CHARACTER_ID, id).putExtra(EXTRA_CHARACTER_NAME, name));
					}
					
				});
				return true;
			} else {
				return false;
			}
		}
	};

}
