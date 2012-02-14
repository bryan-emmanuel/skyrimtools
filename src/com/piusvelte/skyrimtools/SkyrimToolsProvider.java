package com.piusvelte.skyrimtools;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class SkyrimToolsProvider extends ContentProvider {
	public static final String AUTHORITY = "com.piusvelte.skyrimtools.SkyrimToolsProvider";

	private static final UriMatcher sUriMatcher;

	private static final String DATABASE_NAME = "skyrimtools.db";
	private static final int DATABASE_VERSION = 1;
	private DatabaseHelper mDatabaseHelper;
	
	private static final int CATEGORIES = 0;
	protected static final String TABLE_CATEGORIES = "categories";
	private static HashMap<String, String> categories_projectionMap;
	
	public static final class Categories implements BaseColumns {
		
		private Categories() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CATEGORIES);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_CATEGORIES;
		
		public static final String name = "name";
		public static final String max = "max";
		public static final String value = "value";
		public static final String child = "child";
	}

	protected static enum sCategoriesColumns {
		_id, name, max, value, child
	}
	
	private static final int CATEGORY_RELATIONSHIPS = 1;
	protected static final String TABLE_CATEGORY_RELATIONSHIPS = "category_relationships";
	private static HashMap<String, String> category_relationships_projectionMap;
	
	public static final class Category_relationships implements BaseColumns {
		
		private Category_relationships() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CATEGORY_RELATIONSHIPS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_CATEGORY_RELATIONSHIPS;
		
		public static final String parent = "parent";
		public static final String child = "child";
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, TABLE_CATEGORIES, CATEGORIES);
		categories_projectionMap = new HashMap<String, String>();
		categories_projectionMap.put(Categories._ID, Categories._ID);
		categories_projectionMap.put(Categories.name, Categories.name);
		categories_projectionMap.put(Categories.max, Categories.max);
		categories_projectionMap.put(Categories.value, Categories.value);
		categories_projectionMap.put(Categories.child, Categories.child);
		sUriMatcher.addURI(AUTHORITY, TABLE_CATEGORY_RELATIONSHIPS, CATEGORY_RELATIONSHIPS);
		category_relationships_projectionMap = new HashMap<String, String>();
		category_relationships_projectionMap.put(Category_relationships._ID, Category_relationships._ID);
		category_relationships_projectionMap.put(Category_relationships.parent, Category_relationships.parent);
		category_relationships_projectionMap.put(Category_relationships.child, Category_relationships.child);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case CATEGORIES:
			count = db.delete(TABLE_CATEGORIES, selection, selectionArgs);
			break;
		case CATEGORY_RELATIONSHIPS:
			count = db.delete(TABLE_CATEGORY_RELATIONSHIPS, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case CATEGORIES:
			return Categories.CONTENT_TYPE;
		case CATEGORY_RELATIONSHIPS:
			return Category_relationships.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long rowId;
		Uri returnUri = null;
		switch (sUriMatcher.match(uri)) {
		case CATEGORIES:
			rowId = db.insert(TABLE_CATEGORIES, Categories._ID, values);
			returnUri = ContentUris.withAppendedId(Categories.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		case CATEGORY_RELATIONSHIPS:
			rowId = db.insert(TABLE_CATEGORY_RELATIONSHIPS, Category_relationships._ID, values);
			returnUri = ContentUris.withAppendedId(Category_relationships.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return returnUri;
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		Cursor c;
		switch (sUriMatcher.match(uri)) {
		case CATEGORIES:
			qb.setTables(TABLE_CATEGORIES);
			qb.setProjectionMap(categories_projectionMap);
			break;
		case CATEGORY_RELATIONSHIPS:
			qb.setTables(TABLE_CATEGORY_RELATIONSHIPS);
			qb.setProjectionMap(category_relationships_projectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int count;
		switch (sUriMatcher.match(uri)) {
		case CATEGORIES:
			count = db.update(TABLE_CATEGORIES, values, selection, selectionArgs);
			break;
		case CATEGORY_RELATIONSHIPS:
			count = db.update(TABLE_CATEGORY_RELATIONSHIPS, values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// setup the categories
			db.execSQL("create table if not exists " + TABLE_CATEGORIES
					+ " (" + Categories._ID + " integer primary key autoincrement, "
					+ Categories.name + " text,"
					+ Categories.max + " integer,"
					+ Categories.value + " integer);");
			db.execSQL("create table if not exists " + TABLE_CATEGORY_RELATIONSHIPS
					+ " (" + Category_relationships._ID + " integer primary key autoincrement, "
					+ Category_relationships.parent + " integer,"
					+ Category_relationships.child + " integer);");
			// initial data
			//THE WARRIOR
			long tlid = initCategory(db, "The Warrior", 0);
			//Archery
			long id = initCategory(db, "Overdraw", 5, initCategory(db, "Archery", 0, tlid));
			long id2 = initCategory(db, "Eagle Eye", 1, id);
			initCategory(db, "Steady Hand", 2, id2);
			initRelationship(db,
					initCategory(db, "Quick Shot", 1, initCategory(db, "Power Shot", 1, id2)), 
					initCategory(db, "Bullseye", 1, initCategory(db, "Ranger", 1, initCategory(db, "Hunter's Discipline", 1, initCategory(db, "Critical Shot", 3, id)))));
			//Block
			id = initCategory(db, "Shield Wall", 5, initCategory(db, "Block", 0, tlid));
			initCategory(db, "Quick Reflexes", 1, id);
			initRelationship(db,
					initCategory(db, "Disarming Bash", 1, initCategory(db, "Deadly Bash", 1, initCategory(db, "Power Bash", 1, id))),
					initCategory(db, "Shield Charge", 1, initCategory(db, "Block Runner", 1, initCategory(db, "Elemental Protection", 1, initCategory(db, "Deflect Arrows", 1, id)))));
			//Heavy Armor
			id = initCategory(db, "Juggernaut", 5, initCategory(db, "Heavy Armor", 0, tlid));
			initCategory(db, "Conditioning", 1, initCategory(db, "Cushioned", 1, initCategory(db, "Fists of Steel", 1, id)));
			initCategory(db, "Reflect Blows", 1, initCategory(db, "Matching Set", 1, initCategory(db, "Tower of Strength", 1, initCategory(db, "Well Fitted", 1, id))));
			//One Handed
			id = initCategory(db, "Armsman", 5, initCategory(db, "One Handed", 0, tlid));
			initCategory(db, "Hack and Slash", 3, id);
			id2 = initCategory(db, "Fighting Stance", 1, id);
			initRelationship(db,
					initCategory(db, "Critical Charge", 1, id2),
					initCategory(db, "Paralyzing Strike", 1, initCategory(db, "Savage Strike", 1, id2)));
			initCategory(db, "Bone Breaker", 3, id);
			initCategory(db, "Bladesman", 3, id);
			initCategory(db, "Dual Savagery", 1, initCategory(db, "Dual Flurry", 2, id));
			//Two Handed
			id = initCategory(db, "Barbarian", 5, initCategory(db, "Two Handed", 0, tlid));
			initCategory(db, "Limbsplitter", 3, id);
			id2 = initCategory(db, "Champion's Stance", 1, id);
			initCategory(db, "Warmaster", 1,
					initRelationship(db,
							initCategory(db, "Great Critical Charge", 1, id2),
							initCategory(db, "Sweep", 1, initCategory(db, "Devastating Blow", 1, id2))));
			initCategory(db, "Deep Wounds", 3, id);
			initCategory(db, "Skull Crusher", 3, id);
			//Smithing
			id = initCategory(db, "Steel Smithing", 1, initCategory(db, "Smithing", 0, tlid));
			initCategory(db, "Arcane Blacksmith", 1, id);
			initRelationship(db,
					initCategory(db, "Glass Smith", 1, initCategory(db, "Advanced Armors", 1, initCategory(db, "Elven Smithing", 1, id))),
					initCategory(db, "Dragon Smithing", 1, initCategory(db, "Daedric Smithing", 1, initCategory(db, "Ebony Smithing", 1, initCategory(db, "Orcish Smithing", 1, initCategory(db, "Dwarven Smithing", 1, id))))));
			//THE MAGE
			tlid = initCategory(db, "The Mage", 0);
			//Alteration
			id = initCategory(db, "Novice Alteration", 1, initCategory(db, "Alteration", 0, tlid));
			initCategory(db, "Alteration Dual Casting", 1, id);
			id2 = initCategory(db, "Apprentice Alteration", 1, id);
			initCategory(db, "Mage Armor", 3, id2);
			initCategory(db, "Magic Resistance", 3, id2);
			id2 = initCategory(db, "Adept Alteration", 1, id2);
			initCategory(db, "Stability", 1, id2);
			id2 = initCategory(db, "Expert Alteration", 1, id2);
			initCategory(db, "Atronach", 1, id2);
			initCategory(db, "Master Alteration", 1, id2);
			//Conjuration
			id = initCategory(db, "Novice Conjuration", 1, initCategory(db, "Conjuration", 0, tlid));
			initRelationship(db,
					initCategory(db, "Elemental Potency", 1, initCategory(db, "Astromancy", 1, initCategory(db, "Summoner", 1, id))),
					initCategory(db, "Twin Souls", 1, initCategory(db, "Dark Souls", 1, initCategory(db, "Necromancy", 1, id))));
			initCategory(db, "Conjuration Dual Casting", 1, id);
			initCategory(db, "Oblivion", 1, initCategory(db, "Soul Stealer", 1, initCategory(db, "Mystic Binding", 1, id)));
			initCategory(db, "Master Conjuration", 1, initCategory(db, "Expert Conjuration", 1, initCategory(db, "Adept Conjuration", 1, initCategory(db, "Apprentice Conjuration", 1, id))));
			//Destruction
			id = initCategory(db, "Novice Destruction", 1, initCategory(db, "Destruction", 0, tlid));
			initCategory(db, "Intense Flames", 1, initCategory(db, "Augmented Flames", 1, id));
			initCategory(db, "Deep Freeze", 1, initCategory(db, "Augmented Frost", 1, id));
			initCategory(db, "Disintigrate", 1, initCategory(db, "Augmented Shock", 1, id));
			id2 = initCategory(db, "Apprentice Destruction", 1, id);
			initCategory(db, "Master Destruction", 1, initCategory(db, "Expert Destruction", 1, initCategory(db, "Adept Destruction", 1, id2)));
			initCategory(db, "Rune Master", 1, id2);
			initCategory(db, "Impact", 1, initCategory(db, "Destruction Dual Casting", 1, id));
			//Illusion
			id = initCategory(db, "Novice Illusion", 1, initCategory(db, "Illusion", 0, tlid));
			initCategory(db, "Illusion Dual Casting", 1, id);
			initCategory(db, "Master Illusion", 1, initCategory(db, "Expert Illusion", 1, initCategory(db, "Adept Illusion", 1, initCategory(db, "Apprentice Illusion", 1, id))));
			initRelationship(db,
					initCategory(db, "Rage", 1, initCategory(db, "Aspect of Terror", 1, initCategory(db, "Hypnotic Gaze", 1, id))),
					initCategory(db, "Master of the Mind", 1, initCategory(db, "Quiet Casting", 1, initCategory(db, "Kindred Mage", 1, initCategory(db, "Animage", 1, id)))));
			//Restoration
			id = initCategory(db, "Novice Restoration", 1, initCategory(db, "Restoration", 0, tlid));
			initCategory(db, "Respite", 1, id);
			initCategory(db, "Necromage", 1, initCategory(db, "Regeneration", 1, id));
			initCategory(db, "Ward Absorb", 1, id);
			initCategory(db, "Master Restoration", 1, initCategory(db, "Expert Restoration", 1, initCategory(db, "Adept Restoration", 1, initCategory(db, "Apprentice Restoration", 1, id))));
			initCategory(db, "Avoid Death", 1, initCategory(db, "Recovery", 1, id));
			initCategory(db, "Restoration Dual Casting", 1, id);
			//Enchanting
			id = initCategory(db, "Enchanter", 5, initCategory(db, "Enchanting", 0, tlid));
			initRelationship(db,
					initCategory(db, "Storm Enchanter", 1, initCategory(db, "Frost Enchanter", 1, initCategory(db, "Fire Enchanter", 1, id))),
					initCategory(db, "Extra Effect", 1, initCategory(db, "Corpus Enchanter", 1, initCategory(db, "Insightful Enchanter", 1, id))));
			initCategory(db, "Soul Siphon", 1, initCategory(db, "Soul Squeezer", 1, id));
			//THE THEIF
			tlid = initCategory(db, "The Theif", 0);
			//Alchemy
			id = initCategory(db, "Physician", 1, initCategory(db, "Alchemist", 5, initCategory(db, "Alchemy", 0, tlid)));
			id2 = initCategory(db, "Concetrated Poison", 1, initCategory(db, "Poisoner", 1, id));
			initCategory(db, "Green Thumb", 1, id2);
			initCategory(db, "Purity", 1,
					initRelationship(db,
							id2,
							initCategory(db, "Snakeblood", 1, initCategory(db, "Experimenter", 3, initCategory(db, "Benefactor", 1, id)))));
			//Light Armor
			id = initCategory(db, "Custom Fit", 1, initCategory(db, "Agile Defender", 5, initCategory(db, "Light Armor", 0, tlid)));
			initRelationship(db,
					initCategory(db, "Wind Walker", 1, initCategory(db, "Unhindered", 1, id)),
					initCategory(db, "Deft Movement", 1, initCategory(db, "Matching Set", 1, id)));
			//Lockpicking
			id = initCategory(db, "Apprentice Locks", 1, initCategory(db, "Novice Locks", 1, initCategory(db, "Lockpicking", 0, tlid)));
			initCategory(db, "Wax Key", 1, initCategory(db, "Quick Hands", 1, id));
			id = initCategory(db, "Adept Locks", 1, id);
			initCategory(db, "Treasure Hunter", 1, initCategory(db, "Golden Touch", 1, id));
			id = initCategory(db, "Expert Locks", 1, id);
			initCategory(db, "Unbreakable", 1, initCategory(db, "Locksmith", 1, id));
			initCategory(db, "Master Locks", 1, id);
			//Pickpocket
			id = initCategory(db, "Night Theif", 1, initCategory(db, "Light Fingers", 5, initCategory(db, "Pickpocket", 0, tlid)));
			initCategory(db, "Poisoned", 1, id);
			initCategory(db, "Extra Pockets", 1, id);
			id = initCategory(db, "Cutpurse", 1, id);
			initCategory(db, "Keymaster", 1, id);
			initCategory(db, "Perfect Touch", 1, initCategory(db, "Misdirection", 1, id));
			//Sneak
			id = initCategory(db, "Stealth", 5, initCategory(db, "Sneak", 0, tlid));
			initCategory(db, "Shadow Warrior", 1, initCategory(db, "Silence", 1, initCategory(db, "Silent Roll", 1, initCategory(db, "Light Foot", 1, initCategory(db, "Muffled Movement", 1, id)))));
			initCategory(db, "Assassin's Blade", 1, initCategory(db, "Deadly Aim", 1, initCategory(db, "Backstab", 1, id)));
			//Speech
			id = initCategory(db, "Haggling", 5, initCategory(db, "Speech", 0, tlid));
			initCategory(db, "Master Trader", 1, initCategory(db, "Fence", 1, initCategory(db, "Investor", 1, initCategory(db, "Merchant", 1, initCategory(db, "Allure", 1, id)))));
			initCategory(db, "Intimidation", 1, initCategory(db, "Persuasion", 1, initCategory(db, "Bribery", 1, id)));
		}
		
		private long initCategory(SQLiteDatabase db, String name, int max) {
			ContentValues values = new ContentValues();
			values.put(Categories.name, name);
			values.put(Categories.max, max);
			values.put(Categories.value, 0);
			return db.insert(TABLE_CATEGORIES, Categories._ID, values);
		}
		
		private long initRelationship(SQLiteDatabase db, long parent, long child) {
			ContentValues values = new ContentValues();
			values.put(Category_relationships.parent, parent);
			values.put(Category_relationships.child, child);
			db.insert(TABLE_CATEGORY_RELATIONSHIPS, Category_relationships._ID, values);
			return child;
		}
		
		private long initCategory(SQLiteDatabase db, String name, int max, long parent) {
			long child = initCategory(db, name, max);
			initRelationship(db, parent, child);
			return child;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
	}
	
}
