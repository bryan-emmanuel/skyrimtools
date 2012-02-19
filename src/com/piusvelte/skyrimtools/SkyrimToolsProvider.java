package com.piusvelte.skyrimtools;

import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_LEVEL;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_MAGICKA;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_HEALTH;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_STAMINA;
import static com.piusvelte.skyrimtools.CharacterBuilder.DEFAULT_VALUE;

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
import android.util.Log;

public class SkyrimToolsProvider extends ContentProvider {
	public static final String AUTHORITY = "com.piusvelte.skyrimtools.SkyrimToolsProvider";

	private static final UriMatcher sUriMatcher;

	private static final String DATABASE_NAME = "skyrimtools.db";
	private static final int DATABASE_VERSION = 1;
	private DatabaseHelper mDatabaseHelper;
	
	private static final int PERKS = 0;
	protected static final String TABLE_PERKS = "perks";
	private static HashMap<String, String> perks_projectionMap;
	
	public static final class Perks implements BaseColumns {
		
		private Perks() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PERKS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_PERKS;
		
		public static final String name = "name";
		public static final String max = "max";
		public static final String child = "child";
		public static final String desc = "desc";
	}

	protected static enum PerksColumns {
		_id, name, max, child, desc
	}
	
	private static final int PERK_RELATIONSHIPS = 1;
	protected static final String TABLE_PERK_RELATIONSHIPS = "perk_relationships";
	private static HashMap<String, String> perk_relationships_projectionMap;
	
	public static final class Perk_relationships implements BaseColumns {
		
		private Perk_relationships() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PERK_RELATIONSHIPS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_PERK_RELATIONSHIPS;
		
		public static final String parent = "parent";
		public static final String child = "child";
	}
	
	private static final int CHARACTERS = 2;
	protected static final String TABLE_CHARACTERS = "characters";
	private static HashMap<String, String> characters_projectionMap;
	
	public static final class Characters implements BaseColumns {
		
		private Characters() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CHARACTERS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_CHARACTERS;
		
		public static final String name = "name";
		public static final String level = "level";
		public static final String magicka = "magicka";
		public static final String health = "health";
		public static final String stamina = "stamina";
	}

	protected static enum CharactersColumns {
		_id, name, level, magicka, health, stamina
	}

	private static final int CHARACTER_PERKS = 3;
	protected static final String TABLE_CHARACTER_PERKS = "character_perks";
	private static HashMap<String, String> character_perks_projectionMap;
	
	public static final class Character_perks implements BaseColumns {
		
		private Character_perks() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CHARACTER_PERKS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_CHARACTER_PERKS;
		
		public static final String character = "character";
		public static final String perk = "perk";
		public static final String value = "value";
	}

	protected static enum Character_perksColumns {
		_id, character, perk, value
	}
	
	private static final int VCHARACTER_PERKS = 5;
	protected static final String VIEW_CHARACTER_PERKS = "vcharacter_perks";
	private static HashMap<String, String> vcharacter_perks_projectionMap;
	
	public static final class Vcharacter_perks implements BaseColumns {
		
		private Vcharacter_perks() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + VIEW_CHARACTER_PERKS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + VIEW_CHARACTER_PERKS;

		public static final String character = "character";
		public static final String character_name = "character_name";
		public static final String value = "value";
		public static final String perk = "perk";
		public static final String perk_name = "perk_name";
		public static final String max = "max";
		public static final String hasChildren = "hasChildren";
		public static final String desc = "desc";
		public static final String parentHasValue = "parentHasValue";
	}

	protected static enum Vcharacter_perksColumns {
		_id, character, character_name, value, perk, perk_name, max, hasChildren, desc, parentHasValue
	}
	
	private static final int VCHARACTERS = 6;
	protected static final String VIEW_CHARACTERS = "vcharacters";
	
	public static final class Vcharacters implements BaseColumns {
		
		private Vcharacters() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + VIEW_CHARACTERS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + VIEW_CHARACTERS;
	}
	
	private static final int CHARACTER_PERKS_TEMP = 7;
	protected static final String TABLE_CHARACTER_PERKS_TEMP = "character_perks_temp";
	
	public static final class Character_perks_temp implements BaseColumns {
		
		private Character_perks_temp() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CHARACTER_PERKS_TEMP);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + TABLE_CHARACTER_PERKS_TEMP;
	}
	
	private static final int INIT_CHARACTER_PERKS_TEMP = 8;
	protected static final String INIT_TABLE_CHARACTER_PERKS_TEMP = "init_character_perks_temp";
	
	public static final class Init_character_perks_temp implements BaseColumns {
		
		private Init_character_perks_temp() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + INIT_TABLE_CHARACTER_PERKS_TEMP);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + INIT_TABLE_CHARACTER_PERKS_TEMP;
	}
	
	private static final int SAVE_CHARACTER_PERKS = 9;
	protected static final String SAVE_TABLE_CHARACTER_PERKS = "save_character_perks";
	
	public static final class Save_character_perks implements BaseColumns {
		
		private Save_character_perks() {
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SAVE_TABLE_CHARACTER_PERKS);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte." + SAVE_TABLE_CHARACTER_PERKS;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, TABLE_PERKS, PERKS);
		perks_projectionMap = new HashMap<String, String>();
		perks_projectionMap.put(Perks._ID, Perks._ID);
		perks_projectionMap.put(Perks.name, Perks.name);
		perks_projectionMap.put(Perks.max, Perks.max);
		perks_projectionMap.put(Perks.child, Perks.child);
		perks_projectionMap.put(Perks.desc, Perks.desc);
		sUriMatcher.addURI(AUTHORITY, TABLE_PERK_RELATIONSHIPS, PERK_RELATIONSHIPS);
		perk_relationships_projectionMap = new HashMap<String, String>();
		perk_relationships_projectionMap.put(Perk_relationships._ID, Perk_relationships._ID);
		perk_relationships_projectionMap.put(Perk_relationships.parent, Perk_relationships.parent);
		perk_relationships_projectionMap.put(Perk_relationships.child, Perk_relationships.child);
		sUriMatcher.addURI(AUTHORITY, TABLE_CHARACTERS, CHARACTERS);
		characters_projectionMap = new HashMap<String, String>();
		characters_projectionMap.put(Characters._ID, Characters._ID);
		characters_projectionMap.put(Characters.name, Characters.name);
		characters_projectionMap.put(Characters.level, Characters.level);
		characters_projectionMap.put(Characters.magicka, Characters.magicka);
		characters_projectionMap.put(Characters.health, Characters.health);
		characters_projectionMap.put(Characters.stamina, Characters.stamina);
		sUriMatcher.addURI(AUTHORITY, TABLE_CHARACTER_PERKS, CHARACTER_PERKS);
		character_perks_projectionMap = new HashMap<String, String>();
		character_perks_projectionMap.put(Character_perks._ID, Character_perks._ID);
		character_perks_projectionMap.put(Character_perks.character, Character_perks.character);
		character_perks_projectionMap.put(Character_perks.perk, Character_perks.perk);
		character_perks_projectionMap.put(Character_perks.value, Character_perks.value);
		sUriMatcher.addURI(AUTHORITY, VIEW_CHARACTER_PERKS, VCHARACTER_PERKS);
		vcharacter_perks_projectionMap = new HashMap<String, String>();
		vcharacter_perks_projectionMap.put(Vcharacter_perks._ID, Vcharacter_perks._ID);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.character, Vcharacter_perks.character);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.character_name, Vcharacter_perks.character_name);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.value, Vcharacter_perks.value);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.perk, Vcharacter_perks.perk);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.perk_name, Vcharacter_perks.perk_name);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.max, Vcharacter_perks.max);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.hasChildren, Vcharacter_perks.hasChildren);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.desc, Vcharacter_perks.desc);
		vcharacter_perks_projectionMap.put(Vcharacter_perks.parentHasValue, Vcharacter_perks.parentHasValue);
		sUriMatcher.addURI(AUTHORITY, VIEW_CHARACTERS, VCHARACTERS);
		sUriMatcher.addURI(AUTHORITY, TABLE_CHARACTER_PERKS_TEMP, CHARACTER_PERKS_TEMP);
		sUriMatcher.addURI(AUTHORITY, INIT_TABLE_CHARACTER_PERKS_TEMP, INIT_CHARACTER_PERKS_TEMP);
		sUriMatcher.addURI(AUTHORITY, SAVE_TABLE_CHARACTER_PERKS, SAVE_CHARACTER_PERKS);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case PERKS:
			count = db.delete(TABLE_PERKS, selection, selectionArgs);
			break;
		case PERK_RELATIONSHIPS:
			count = db.delete(TABLE_PERK_RELATIONSHIPS, selection, selectionArgs);
			break;
		case CHARACTERS:
			count = db.delete(TABLE_CHARACTERS, selection, selectionArgs);
			break;
		case CHARACTER_PERKS:
			count = db.delete(TABLE_CHARACTER_PERKS, selection, selectionArgs);
			break;
		case CHARACTER_PERKS_TEMP:
			count = db.delete(TABLE_CHARACTER_PERKS_TEMP, selection, selectionArgs);
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
		case PERKS:
			return Perks.CONTENT_TYPE;
		case PERK_RELATIONSHIPS:
			return Perk_relationships.CONTENT_TYPE;
		case CHARACTERS:
			return Characters.CONTENT_TYPE;
		case CHARACTER_PERKS:
			return Character_perks.CONTENT_TYPE;
		case VCHARACTER_PERKS:
			return Vcharacter_perks.CONTENT_TYPE;
		case VCHARACTERS:
			return Vcharacters.CONTENT_TYPE;
		case CHARACTER_PERKS_TEMP:
			return Character_perks_temp.CONTENT_TYPE;
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
		case PERKS:
			rowId = db.insert(TABLE_PERKS, Perks._ID, values);
			returnUri = ContentUris.withAppendedId(Perks.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		case PERK_RELATIONSHIPS:
			rowId = db.insert(TABLE_PERK_RELATIONSHIPS, Perk_relationships._ID, values);
			returnUri = ContentUris.withAppendedId(Perk_relationships.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		case CHARACTERS:
			rowId = db.insert(TABLE_CHARACTERS, Characters._ID, values);
			returnUri = ContentUris.withAppendedId(Characters.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		case CHARACTER_PERKS:
			rowId = db.insert(TABLE_CHARACTER_PERKS, Character_perks._ID, values);
			returnUri = ContentUris.withAppendedId(Character_perks.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			break;
		case CHARACTER_PERKS_TEMP:
			rowId = db.insert(TABLE_CHARACTER_PERKS_TEMP, Character_perks_temp._ID, values);
			returnUri = ContentUris.withAppendedId(Character_perks_temp.CONTENT_URI, rowId);
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
		case PERKS:
			qb.setTables(TABLE_PERKS);
			qb.setProjectionMap(perks_projectionMap);
			break;
		case PERK_RELATIONSHIPS:
			qb.setTables(TABLE_PERK_RELATIONSHIPS);
			qb.setProjectionMap(perk_relationships_projectionMap);
			break;
		case CHARACTERS:
			qb.setTables(TABLE_CHARACTERS);
			qb.setProjectionMap(characters_projectionMap);
			break;
		case CHARACTER_PERKS:
			qb.setTables(TABLE_CHARACTER_PERKS);
			qb.setProjectionMap(character_perks_projectionMap);
			break;
		case VCHARACTER_PERKS:
			Log.v("SkyrimToolsProvider", "query:"+VIEW_CHARACTER_PERKS);
			Log.v("SkyrimToolsProvider", "selection:"+selection);
			for (String s : selectionArgs) {
				Log.v("SkyrimToolsProvider", "selectionArg:"+s);
			}
			qb.setTables(VIEW_CHARACTER_PERKS);
			qb.setProjectionMap(vcharacter_perks_projectionMap);
			break;
		case VCHARACTERS:
			qb.setTables(VIEW_CHARACTERS);
			qb.setProjectionMap(characters_projectionMap);
			break;
		case CHARACTER_PERKS_TEMP:
			qb.setTables(TABLE_CHARACTER_PERKS_TEMP);
			qb.setProjectionMap(vcharacter_perks_projectionMap);
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
		case PERKS:
			count = db.update(TABLE_PERKS, values, selection, selectionArgs);
			break;
		case PERK_RELATIONSHIPS:
			count = db.update(TABLE_PERK_RELATIONSHIPS, values, selection, selectionArgs);
			break;
		case CHARACTERS:
			count = db.update(TABLE_CHARACTERS, values, selection, selectionArgs);
			break;
		case CHARACTER_PERKS:
			count = db.update(TABLE_CHARACTER_PERKS, values, selection, selectionArgs);
			break;
		case CHARACTER_PERKS_TEMP:
			count = db.update(TABLE_CHARACTER_PERKS_TEMP, values, selection, selectionArgs);
			break;
		case INIT_CHARACTER_PERKS_TEMP:
			db.execSQL("delete from " + TABLE_CHARACTER_PERKS_TEMP + ";");
			db.execSQL("insert into " + TABLE_CHARACTER_PERKS_TEMP + " select * from " + TABLE_CHARACTER_PERKS + ";");
			count = 0;
			break;
		case SAVE_CHARACTER_PERKS:
			db.execSQL("delete from " + TABLE_CHARACTER_PERKS + ";");
			db.execSQL("insert into " + TABLE_CHARACTER_PERKS + " select * from " + TABLE_CHARACTER_PERKS_TEMP + ";");
			count = 0;
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
			db.execSQL("create table if not exists " + TABLE_PERKS
					+ " (" + Perks._ID + " integer primary key autoincrement,"
					+ Perks.name + " text,"
					+ Perks.max + " integer,"
					+ Perks.desc + " text);");
			db.execSQL("create table if not exists " + TABLE_PERK_RELATIONSHIPS
					+ " (" + Perk_relationships._ID + " integer primary key autoincrement,"
					+ Perk_relationships.parent + " integer,"
					+ Perk_relationships.child + " integer);");
			db.execSQL("create table if not exists " + TABLE_CHARACTERS
					+ " (" + Characters._ID + " integer primary key autoincrement,"
					+ Characters.name + " text,"
					+ Characters.level + " integer,"
					+ Characters.magicka + " integer,"
					+ Characters.health + " integer,"
					+ Characters.stamina + " integer);");
			db.execSQL("create view if not exists " + VIEW_CHARACTERS + " as select "
					+ Characters._ID
					+ "," + Characters.name
					+ ",case when " + Characters.level + " is null then " + DEFAULT_LEVEL + " else " + Characters.level + " end as " + Characters.level
					+ ",case when " + Characters.magicka + " is null then " + DEFAULT_MAGICKA + " else " + Characters.magicka + " end as " + Characters.magicka
					+ ",case when " + Characters.health + " is null then " + DEFAULT_HEALTH + " else " + Characters.health + " end as " + Characters.health
					+ ",case when " + Characters.stamina + " is null then " + DEFAULT_STAMINA + " else " + Characters.stamina + " end as " + Characters.stamina
					+ " from "
					+ TABLE_CHARACTERS + ";");
			db.execSQL("create table if not exists " + TABLE_CHARACTER_PERKS
					+ " (" + Character_perks._ID + " integer primary key autoincrement,"
					+ Character_perks.character + " integer,"
					+ Character_perks.perk + " integer,"
					+ Character_perks.value + " integer);");
			db.execSQL("create table if not exists " + TABLE_CHARACTER_PERKS_TEMP + " as select * from " + TABLE_CHARACTER_PERKS + ";");
			db.execSQL("create view if not exists " + VIEW_CHARACTER_PERKS + " as select a."
					+ Character_perks._ID + " as " + Vcharacter_perks._ID
					+ ",b." + Characters._ID + " as " + Vcharacter_perks.character
					+ ",b." + Characters.name + " as " + Vcharacter_perks.character_name
					+ ",case when a." + Character_perks.value + " is null then " + DEFAULT_VALUE + " else a." + Character_perks.value + " end as " + Vcharacter_perks.value
					+ ",c." + Perks._ID + " as " + Vcharacter_perks.perk
					+ ",c." + Perks.name + " as " + Vcharacter_perks.perk_name
					+ ",c." + Perks.max + " as " + Vcharacter_perks.max
					+ ",case when (select d." + Perks.child + " from " + TABLE_PERK_RELATIONSHIPS + " d where d." + Perk_relationships.parent + "=b." + Perks._ID + ") is null then 0 else 1 end as " + Vcharacter_perks.hasChildren
					+ ",c." + Perks.desc + " as " + Vcharacter_perks.desc
					+ ",case when (select e." + Character_perks.value + " from " + TABLE_CHARACTER_PERKS_TEMP + " e," + TABLE_PERKS + " f where f." + Perks._ID + "=e." + Character_perks.perk + " and (e." + Character_perks.value + ">0 or f." + Perks.max + "=0) and e." + Character_perks.perk + "=b." + Perks._ID + ") is null then 0 else 1 end as " + Vcharacter_perks.parentHasValue
					+ " from "
					+ TABLE_CHARACTER_PERKS_TEMP
					+ " a," + TABLE_CHARACTERS
					+ " b," + TABLE_PERKS
					+ " c where b." + Characters._ID + "=a." + Character_perks.character
					+ " and c." + Perks._ID + "=a." + Character_perks.perk + ";");
			// initial data
			//THE WARRIOR
			//Archery
			long id = initCategory(db, "Overdraw", 5, initCategory(db, "Archery", 0, ""), "Bows do 20% more damage.(+20% per additional rank)");
			long id2 = initCategory(db, "Eagle Eye", 1, id, "Pressing Block while aiming will zoom in your view");
			initCategory(db, "Steady Hand", 2, id2, "Zooming in with a bow slows time by 25% (50% for second rank)");
			initRelationship(db,
					initCategory(db, "Quick Shot", 1, initCategory(db, "Power Shot", 1, id2, "Arrows stagger all but the largest opponents 50% of the time"), "Can draw a bow 30% faster"), 
					initCategory(db, "Bullseye", 1, initCategory(db, "Ranger", 1, initCategory(db, "Hunter's Discipline", 1, initCategory(db, "Critical Shot", 3, id, "10% chance of a critical hit that does extra damage (+5% per additional rank, +10% / +15% / +20% critical damage for the 1st / 2nd / 3rd rank)"), "Recover twice as many arrows from dead bodies"), "Able to move faster with a drawn bow"), "15% chance of paralyzing the target for a few seconds"));
			//Block
			id = initCategory(db, "Shield Wall", 5, initCategory(db, "Block", 0, ""), "Blocking is 20% more effective (+5% per additional rank)");
			initCategory(db, "Quick Reflexes", 1, id, "Time slows down if you are blocking during an enemy's power attack");
			initRelationship(db,
					initCategory(db, "Disarming Bash", 1, initCategory(db, "Deadly Bash", 1, initCategory(db, "Power Bash", 1, id, "Able to do a power bash"), "Bashing does five times more damage"), "Chance to disarm when power bashing"),
					initCategory(db, "Shield Charge", 1, initCategory(db, "Block Runner", 1, initCategory(db, "Elemental Protection", 1, initCategory(db, "Deflect Arrows", 1, id, "Arrows that hit the shield do no damage"), "Blocking with a shield reduces incoming fire, frost and shock damage by 50%"), "Able to move faster with a shield raised"), "Sprinting with a raised shield knocks down most targets"));
			//Heavy Armor
			id = initCategory(db, "Juggernaut", 5, initCategory(db, "Heavy Armor", 0, ""), "Increases armor rating for Heavy Armor by 20% (+20% per additional rank)");
			initCategory(db, "Conditioning", 1, initCategory(db, "Cushioned", 1, initCategory(db, "Fists of Steel", 1, id, "Unarmed attacks with Heavy Armor gaunlets do their armor rating in extra damage"), "Half damage from falling if wearing all Heavy Armor: head, chest, hands, feet"), "Heavy Armor weighs nothing and doesn't slow you down when worn");
			initCategory(db, "Reflect Blows", 1, initCategory(db, "Matching Set", 1, initCategory(db, "Tower of Strength", 1, initCategory(db, "Well Fitted", 1, id, "25% armor bonus if wearing all Heavy Armor: head, chest, hands, feet"), "50% less stagger time when wearing only Heavy Armor"), "Additional 25% armor bonus if wearing a matched set of Heavy Armor"), "10% chance to reflect melee damage back to the enemy while wearing all Heavy Armor: head, chest, hands, feet");
			//One Handed
			id = initCategory(db, "Armsman", 5, initCategory(db, "One Handed", 0, ""), "One-Handed weapons do 20% more damage (+20% per additional rank)");
			initCategory(db, "Hack and Slash", 3, id, "Attacks with war axes cause extra bleeding damage (Additional ranks raise the bleeding damage)");
			id2 = initCategory(db, "Fighting Stance", 1, id, "Power attacks with one-handed weapons cost 20% less stamina");
			initRelationship(db,
					initCategory(db, "Critical Charge", 1, id2, "Can do a one-handed power attack while sprinting that deals double critical damage"),
					initCategory(db, "Paralyzing Strike", 1, initCategory(db, "Savage Strike", 1, id2, "Standing power attacks do 25% bonus damage with a chance to decapitate your enemies"), "Backwards power attacks have a 25% chance to paralyze the target"));
			initCategory(db, "Bone Breaker", 3, id, "Attacks with maces ignore 25% of armor (+25% per additional rank)");
			initCategory(db, "Bladesman", 3, id, "Attacks with swords have a 10% chance of doing critical damage (+5% per additional rank)");
			initCategory(db, "Dual Savagery", 1, initCategory(db, "Dual Flurry", 2, id, "Dual wielding attacks are 20% faster (35% for second rank)"), "Dual wielding power attacks do 50% bonus damage");
			//Two Handed
			id = initCategory(db, "Barbarian", 5, initCategory(db, "Two Handed", 0, ""), "Two-Handed weapons do 20% more damage (+20% per additional rank)");
			initCategory(db, "Limbsplitter", 3, id, "Attacks with battle axes cause extra bleeding damage (Additional ranks increase the bleeding damage)");
			id2 = initCategory(db, "Champion's Stance", 1, id, "Power attacks with two-handed weapons cost 25% less stamina");
			initCategory(db, "Warmaster", 1,
					initRelationship(db,
							initCategory(db, "Great Critical Charge", 1, id2, "Can do a two-handed power attack while sprinting that does double critical damage"),
							initCategory(db, "Sweep", 1, initCategory(db, "Devastating Blow", 1, id2, "Standing power attacks do 25% bonus damage with a chance to decapitate your enemies"), "Sideways power attacks with two-handed weapons hit all targets in front of you")), "Backwards power attack has a 25% chance to paralyze the target");
			initCategory(db, "Deep Wounds", 3, id, "Attacks with greatswords have a 10% chance of doing critical damage (+5% per additional rank)");
			initCategory(db, "Skull Crusher", 3, id, "Attacks with warhammers ignore 25% of armor (+25% per additional rank)");
			//Smithing
			id = initCategory(db, "Steel Smithing", 1, initCategory(db, "Smithing", 0, ""), "Can create Steel armor and weapons at forges, and improve them twice as much");
			initCategory(db, "Arcane Blacksmith", 1, id, "You can improve magical weapons and armor");
			initRelationship(db,
					initCategory(db, "Glass Smith", 1, initCategory(db, "Advanced Armors", 1, initCategory(db, "Elven Smithing", 1, id, "Can create Elven armor and weapons at forges, and improve them twice as much"), "Can create Scaled and Plate armor at forges, and improve them twice as much"), "Can create Glass armor and weapons at forges, and improve them twice as much"),
					initCategory(db, "Dragon Smithing", 1, initCategory(db, "Daedric Smithing", 1, initCategory(db, "Ebony Smithing", 1, initCategory(db, "Orcish Smithing", 1, initCategory(db, "Dwarven Smithing", 1, id, "Can create Dwarven armor and weapons at forges, and improve them twice as much"), "Can create Orcish armor and weapons at forges, and improve them twice as much"), "Can create Ebony armor and weapons at forges, and improve them twice as much"), "Can create Daedric armor and weapons at forges, and improve them twice as much"), "Can create Dragon armor at forges, and improve them twice as much (Light and Heavy Dragon Armor)"));
			//THE MAGE
			//Alteration
			id = initCategory(db, "Novice Alteration", 1, initCategory(db, "Alteration", 0, ""), "Cast Novice level Alteration spells for half magicka");
			initCategory(db, "Alteration Dual Casting", 1, id, "Dual casting an Alteration spell overcharges the effects into an even more powerful version");
			id2 = initCategory(db, "Apprentice Alteration", 1, id, "Cast Apprentice level Alteration spells for half magicka");
			initCategory(db, "Mage Armor", 3, id2, "Protection spells like Stoneflesh are 2x as strong if not wearing armor (+.5x per additional rank)");
			initCategory(db, "Magic Resistance", 3, id2, "Blocks 10% of a spells effect (+10% per additional rank)");
			id2 = initCategory(db, "Adept Alteration", 1, id2, "Cast Adept level Alteration spells for half magicka");
			initCategory(db, "Stability", 1, id2, "Alteration spells have greater duration");
			id2 = initCategory(db, "Expert Alteration", 1, id2, "Cast Expert level Alteration spells for half magicka");
			initCategory(db, "Atronach", 1, id2, "Absorb 30% of the magicka of any spells that hit you");
			initCategory(db, "Master Alteration", 1, id2, "Cast Master level Alteration spells for half magicka");
			//Conjuration
			id = initCategory(db, "Novice Conjuration", 1, initCategory(db, "Conjuration", 0, ""), "Cast Novice level Conjuration spells for half magicka");
			initRelationship(db,
					initCategory(db, "Elemental Potency", 1, initCategory(db, "Astromancy", 1, initCategory(db, "Summoner", 1, id, "Can summon atronachs or raise undead 2x as far away (3x for second rank)"), "Double duration for conjured atronachs"), "Conjured atronachs are 50% more powerful"),
					initCategory(db, "Twin Souls", 1, initCategory(db, "Dark Souls", 1, initCategory(db, "Necromancy", 1, id, "Greater duration for reanimated undead"), "Reanimated undead have 100 points more health"), "You can have two atronachs or reanimated zombies"));
			initCategory(db, "Conjuration Dual Casting", 1, id, "Dual casting a Conjuration spell overcharges the effects into an even more powerful version (Cannot conjure two summons/reanimations)");
			initCategory(db, "Oblivion Binding", 1, initCategory(db, "Soul Stealer", 1, initCategory(db, "Mystic Binding", 1, id, "Bound weapons do more damage"), "Bound weapons cast Soul Trap on targets"), "Bound weapons will banish summoned creatures and turn raised ones");
			initCategory(db, "Master Conjuration", 1, initCategory(db, "Expert Conjuration", 1, initCategory(db, "Adept Conjuration", 1, initCategory(db, "Apprentice Conjuration", 1, id, "Cast Apprentice level Conjuration spells for half magicka"), "Cast Adept level Conjuration spells for half magicka"), "Cast Expert level Conjuration spells for half magicka"), "Cast Master level Conjuration spells for half magicka");
			//Destruction
			id = initCategory(db, "Novice Destruction", 1, initCategory(db, "Destruction", 0, ""), "");
			initCategory(db, "Intense Flames", 1, initCategory(db, "Augmented Flames", 1, id, ""), "");
			initCategory(db, "Deep Freeze", 1, initCategory(db, "Augmented Frost", 1, id, ""), "");
			initCategory(db, "Disintigrate", 1, initCategory(db, "Augmented Shock", 1, id, ""), "");
			id2 = initCategory(db, "Apprentice Destruction", 1, id, "");
			initCategory(db, "Master Destruction", 1, initCategory(db, "Expert Destruction", 1, initCategory(db, "Adept Destruction", 1, id2, ""), ""), "");
			initCategory(db, "Rune Master", 1, id2, "");
			initCategory(db, "Impact", 1, initCategory(db, "Destruction Dual Casting", 1, id, ""), "");
			//Illusion
			id = initCategory(db, "Novice Illusion", 1, initCategory(db, "Illusion", 0, ""), "");
			initCategory(db, "Illusion Dual Casting", 1, id, "");
			initCategory(db, "Master Illusion", 1, initCategory(db, "Expert Illusion", 1, initCategory(db, "Adept Illusion", 1, initCategory(db, "Apprentice Illusion", 1, id, ""), ""), ""), "");
			initRelationship(db,
					initCategory(db, "Rage", 1, initCategory(db, "Aspect of Terror", 1, initCategory(db, "Hypnotic Gaze", 1, id, ""), ""), ""),
					initCategory(db, "Master of the Mind", 1, initCategory(db, "Quiet Casting", 1, initCategory(db, "Kindred Mage", 1, initCategory(db, "Animage", 1, id, ""), ""), ""), ""));
			//Restoration
			id = initCategory(db, "Novice Restoration", 1, initCategory(db, "Restoration", 0, ""), "");
			initCategory(db, "Respite", 1, id, "");
			initCategory(db, "Necromage", 1, initCategory(db, "Regeneration", 1, id, ""), "");
			initCategory(db, "Ward Absorb", 1, id, "");
			initCategory(db, "Master Restoration", 1, initCategory(db, "Expert Restoration", 1, initCategory(db, "Adept Restoration", 1, initCategory(db, "Apprentice Restoration", 1, id, ""), ""), ""), "");
			initCategory(db, "Avoid Death", 1, initCategory(db, "Recovery", 1, id, ""), "");
			initCategory(db, "Restoration Dual Casting", 1, id, "");
			//Enchanting
			id = initCategory(db, "Enchanter", 5, initCategory(db, "Enchanting", 0, ""), "");
			initRelationship(db,
					initCategory(db, "Storm Enchanter", 1, initCategory(db, "Frost Enchanter", 1, initCategory(db, "Fire Enchanter", 1, id, ""), ""), ""),
					initCategory(db, "Extra Effect", 1, initCategory(db, "Corpus Enchanter", 1, initCategory(db, "Insightful Enchanter", 1, id, ""), ""), ""));
			initCategory(db, "Soul Siphon", 1, initCategory(db, "Soul Squeezer", 1, id, ""), "");
			//THE THEIF
			//Alchemy
			id = initCategory(db, "Physician", 1, initCategory(db, "Alchemist", 5, initCategory(db, "Alchemy", 0, ""), ""), "");
			id2 = initCategory(db, "Concetrated Poison", 1, initCategory(db, "Poisoner", 1, id, ""), "");
			initCategory(db, "Green Thumb", 1, id2, "");
			initCategory(db, "Purity", 1,
					initRelationship(db,
							id2,
							initCategory(db, "Snakeblood", 1, initCategory(db, "Experimenter", 3, initCategory(db, "Benefactor", 1, id, ""), ""), "")), "");
			//Light Armor
			id = initCategory(db, "Custom Fit", 1, initCategory(db, "Agile Defender", 5, initCategory(db, "Light Armor", 0, ""), ""), "");
			initRelationship(db,
					initCategory(db, "Wind Walker", 1, initCategory(db, "Unhindered", 1, id, ""), ""),
					initCategory(db, "Deft Movement", 1, initCategory(db, "Matching Set", 1, id, ""), ""));
			//Lockpicking
			id = initCategory(db, "Apprentice Locks", 1, initCategory(db, "Novice Locks", 1, initCategory(db, "Lockpicking", 0, ""), ""), "");
			initCategory(db, "Wax Key", 1, initCategory(db, "Quick Hands", 1, id, ""), "");
			id = initCategory(db, "Adept Locks", 1, id, "");
			initCategory(db, "Treasure Hunter", 1, initCategory(db, "Golden Touch", 1, id, ""), "");
			id = initCategory(db, "Expert Locks", 1, id, "");
			initCategory(db, "Unbreakable", 1, initCategory(db, "Locksmith", 1, id, ""), "");
			initCategory(db, "Master Locks", 1, id, "");
			//Pickpocket
			id = initCategory(db, "Night Theif", 1, initCategory(db, "Light Fingers", 5, initCategory(db, "Pickpocket", 0, ""), ""), "");
			initCategory(db, "Poisoned", 1, id, "");
			initCategory(db, "Extra Pockets", 1, id, "");
			id = initCategory(db, "Cutpurse", 1, id, "");
			initCategory(db, "Keymaster", 1, id, "");
			initCategory(db, "Perfect Touch", 1, initCategory(db, "Misdirection", 1, id, ""), "");
			//Sneak
			id = initCategory(db, "Stealth", 5, initCategory(db, "Sneak", 0, ""), "");
			initCategory(db, "Shadow Warrior", 1, initCategory(db, "Silence", 1, initCategory(db, "Silent Roll", 1, initCategory(db, "Light Foot", 1, initCategory(db, "Muffled Movement", 1, id, ""), ""), ""), ""), "");
			initCategory(db, "Assassin's Blade", 1, initCategory(db, "Deadly Aim", 1, initCategory(db, "Backstab", 1, id, ""), ""), "");
			//Speech
			id = initCategory(db, "Haggling", 5, initCategory(db, "Speech", 0, ""), "");
			initCategory(db, "Master Trader", 1, initCategory(db, "Fence", 1, initCategory(db, "Investor", 1, initCategory(db, "Merchant", 1, initCategory(db, "Allure", 1, id, ""), ""), ""), ""), "");
			initCategory(db, "Intimidation", 1, initCategory(db, "Persuasion", 1, initCategory(db, "Bribery", 1, id, ""), ""), "");
		}
		
		private long initCategory(SQLiteDatabase db, String name, int max, String desc) {
			ContentValues values = new ContentValues();
			values.put(Perks.name, name);
			values.put(Perks.max, max);
			values.put(Perks.desc, desc);
			return db.insert(TABLE_PERKS, Perks._ID, values);
		}
		
		private long initRelationship(SQLiteDatabase db, long parent, long child) {
			ContentValues values = new ContentValues();
			values.put(Perk_relationships.parent, parent);
			values.put(Perk_relationships.child, child);
			db.insert(TABLE_PERK_RELATIONSHIPS, Perk_relationships._ID, values);
			return child;
		}
		
		private long initCategory(SQLiteDatabase db, String name, int max, long parent, String desc) {
			long child = initCategory(db, name, max, desc);
			initRelationship(db, parent, child);
			return child;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	
}
