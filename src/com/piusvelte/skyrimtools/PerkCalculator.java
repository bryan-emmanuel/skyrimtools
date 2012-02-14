package com.piusvelte.skyrimtools;

import com.piusvelte.skyrimtools.SkyrimToolsProvider.Categories;

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

public class PerkCalculator extends ListActivity {
	private static String EXTRA_CATEGORY = "category";
	private long mCategory = 0;
	private static final int RESET_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perkcalculator);
		Intent i = getIntent();
		if ((i != null) && i.hasExtra(EXTRA_CATEGORY)) {
			mCategory = i.getLongExtra(EXTRA_CATEGORY, 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, RESET_ID, 0, R.string.lbl_reset).setIcon(android.R.drawable.ic_menu_more);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case RESET_ID:
			ContentValues values = new ContentValues();
			values.put(Categories.value, 0);
			getContentResolver().update(Categories.CONTENT_URI, values, null, null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor c;
		if (mCategory > 0) {
			c = managedQuery(Categories.CONTENT_URI, new String[]{Categories._ID, Categories.name, Categories.max, Categories.value, "case when (select category_relationships.child from category_relationships where category_relationships.parent=categories._id) is null then 0 else 1 end as " + Categories.child, Categories.desc}, "_id in (select category_relationships.child from category_relationships where category_relationships.parent=?)", new String[]{Long.toString(mCategory)}, null);
		} else {
			c = managedQuery(Categories.CONTENT_URI, new String[]{Categories._ID, Categories.name, Categories.max, Categories.value, "case when (select category_relationships.child from category_relationships where category_relationships.parent=categories._id) is null then 0 else 1 end as " + Categories.child, Categories.desc}, "(select category_relationships._id from category_relationships where category_relationships.child=categories._id) is null", null, null);
		}
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.perkrow, c, new String[] {Categories.name}, new int[] {R.id.perkname});
		sca.setViewBinder(mViewBinder);
		setListAdapter(sca);
	}
	

	private final SimpleCursorAdapter.ViewBinder mViewBinder = new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (columnIndex == SkyrimToolsProvider.sCategoriesColumns.name.ordinal()) {
				Button perkname = (Button) view;
				final long id = cursor.getLong(SkyrimToolsProvider.sCategoriesColumns._id.ordinal());
				int max = cursor.getInt(SkyrimToolsProvider.sCategoriesColumns.max.ordinal());
				final int value = cursor.getInt(SkyrimToolsProvider.sCategoriesColumns.value.ordinal());
				final String desc = cursor.getString(SkyrimToolsProvider.sCategoriesColumns.desc.ordinal());
				if (max > 0) {
					perkname.setText(String.format(getString(R.string.lbl_perkname), cursor.getString(SkyrimToolsProvider.sCategoriesColumns.name.ordinal()), cursor.getInt(SkyrimToolsProvider.sCategoriesColumns.value.ordinal())));
				} else {
					perkname.setText(cursor.getString(SkyrimToolsProvider.sCategoriesColumns.name.ordinal()));
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
				if (((max == 0) || (value > 0)) && (cursor.getInt(SkyrimToolsProvider.sCategoriesColumns.child.ordinal()) > 0)) {
					// if there are sub-items, they'll load through onClick
					perkchild.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// load PerkCalculator with this category
							startActivity(new Intent(PerkCalculator.this, PerkCalculator.class).putExtra(EXTRA_CATEGORY, id));
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
							values.put(Categories.value, value + 1);
							getContentResolver().update(Categories.CONTENT_URI, values, Categories._ID + "=?", new String[]{Long.toString(id)});
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
