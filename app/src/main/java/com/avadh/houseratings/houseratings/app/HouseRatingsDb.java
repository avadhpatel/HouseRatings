package com.avadh.houseratings.houseratings.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by apatel6 on 6/6/2014.
 */



public final class HouseRatingsDb {
    public HouseRatingsDb() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    public static abstract class HouseList implements BaseColumns {
        public static final String TABLE_NAME = "house_list";
        public static final String COLUMN_NAME_HOUSE_NAME = "name";
        public static final String COLUMN_NAME_HOUSE_ADDR = "address";
        public static final String COLUMN_NAME_HOUSE_PRICE = "price";
    }

    private static String getHouseListCreateEntries() {
        String ret = "CREATE TABLE " + HouseList.TABLE_NAME + " (" +
                HouseList._ID + " INTEGER PRIMARY KEY, " +
                HouseList.COLUMN_NAME_HOUSE_NAME + TEXT_TYPE + COMMA_SEP +
                HouseList.COLUMN_NAME_HOUSE_ADDR + TEXT_TYPE + COMMA_SEP +
                HouseList.COLUMN_NAME_HOUSE_PRICE + INT_TYPE +
                " )";
        return ret;
    }

    private static String getHouseListDeleteTableStr() {
        String ret = "DROP TABLE IF EXISTS " + HouseList.TABLE_NAME;
        return ret;
    }

    public static class HouseRatingsDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "HouseRatings.db";

        public HouseRatingsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(getHouseListCreateEntries());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(getHouseListDeleteTableStr());
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void fillDummy() {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues oneHouseEntry = new ContentValues();
            oneHouseEntry.put(HouseList.COLUMN_NAME_HOUSE_NAME, "Dream House 1");
            oneHouseEntry.put(HouseList.COLUMN_NAME_HOUSE_ADDR, "1332 North St, Austin, TX 78729");
            oneHouseEntry.put(HouseList.COLUMN_NAME_HOUSE_PRICE, 433250);

            long newRowId = db.insert(HouseList.TABLE_NAME, HouseList.COLUMN_NAME_HOUSE_NAME, oneHouseEntry);
        }

        public Cursor getListOfHouses() {
            SQLiteDatabase db = getReadableDatabase();

            Cursor c = db.query(HouseList.TABLE_NAME, null, null, null, null, null, HouseList._ID);
            c.moveToFirst();
            return c;
        }

        public void AddNewHouse(String name, String address, int price) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues newHouse = new ContentValues();
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_NAME, name);
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_ADDR, address);
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_PRICE, price);

            long newRowId = db.insert(HouseList.TABLE_NAME, HouseList.COLUMN_NAME_HOUSE_NAME, newHouse);
        }
    }

}
