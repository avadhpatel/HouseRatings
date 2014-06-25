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

    public static final String[] ROOM_FEATURE_PRIORITIES = new String[] {"must have", "high",
                    "mid", "low"};

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    public static abstract class HouseList implements BaseColumns {
        public static final String TABLE_NAME = "house_list";
        public static final String COLUMN_NAME_HOUSE_NAME = "name";
        public static final String COLUMN_NAME_HOUSE_ADDR = "address";
        public static final String COLUMN_NAME_HOUSE_PRICE = "price";
        public static final String COLUMN_NAME_HOUSE_INFO = "info";
    }

    public static abstract class RoomList implements BaseColumns {
        public static final String TABLE_NAME = "room_list";
        public static final String COLUMN_NAME_HOUSE_ID = "house";
        public static final String COLUMN_NAME_ROOM_NAME = "name";
        public static final String COLUMN_NAME_FEATURE_PRIORITY = "priority";
        public static final String COLUMN_NAME_ROOM_FEATURE = "feature";
        public static final String COLUMN_NAME_HAS_FEATURE = "has_feature";
        public static final String COLUMN_NAME_FEATURE_NOTE = "note";
    }

    private static String getHouseListCreateEntries() {
        String ret = "CREATE TABLE " + HouseList.TABLE_NAME + " (" +
                HouseList._ID + " INTEGER PRIMARY KEY, " +
                HouseList.COLUMN_NAME_HOUSE_NAME + TEXT_TYPE + COMMA_SEP +
                HouseList.COLUMN_NAME_HOUSE_ADDR + TEXT_TYPE + COMMA_SEP +
                HouseList.COLUMN_NAME_HOUSE_PRICE + INT_TYPE + COMMA_SEP +
                HouseList.COLUMN_NAME_HOUSE_INFO + TEXT_TYPE +
                " )";
        return ret;
    }

    private static String getRoomListCreateEntries() {
        String ret = "CREATE TABLE " + RoomList.TABLE_NAME + " (" +
                RoomList._ID + " INTEGER PRIMARY KEY, " +
                RoomList.COLUMN_NAME_HOUSE_ID + INT_TYPE + COMMA_SEP +
                RoomList.COLUMN_NAME_ROOM_NAME + TEXT_TYPE + COMMA_SEP +
                RoomList.COLUMN_NAME_ROOM_FEATURE + TEXT_TYPE + COMMA_SEP +
                RoomList.COLUMN_NAME_FEATURE_PRIORITY + TEXT_TYPE + COMMA_SEP +
                RoomList.COLUMN_NAME_HAS_FEATURE + INT_TYPE + COMMA_SEP +
                RoomList.COLUMN_NAME_FEATURE_NOTE + TEXT_TYPE +
                " )";
        return ret;
    }

    private static String getHouseListDeleteTableStr() {
        String ret = "DROP TABLE IF EXISTS " + HouseList.TABLE_NAME;
        return ret;
    }

    private static String getRoomListDeleteTableStr() {
        String ret = "DROP TABLE IF EXISTS " + RoomList.TABLE_NAME;
        return ret;
    }

    public static class HouseRatingsDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "HouseRatings.db";

        public HouseRatingsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(getHouseListCreateEntries());
            db.execSQL(getRoomListCreateEntries());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                String alterQuery = "ALTER TABLE " + HouseList.TABLE_NAME + " ADD COLUMN " +
                        HouseList.COLUMN_NAME_HOUSE_INFO + " " + TEXT_TYPE + ";";
                db.execSQL(alterQuery);
            } else {
                db.execSQL(getHouseListDeleteTableStr());
                db.execSQL(getRoomListDeleteTableStr());
                onCreate(db);
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void fillDummy() {
            AddNewHouse("Dream House 1", "1332 N St, Austin, TX 78729", 433250, "Test Info");
        }

        public Cursor getListOfHouses() {
            SQLiteDatabase db = getReadableDatabase();

            Cursor c = db.query(HouseList.TABLE_NAME, null, null, null, null, null, HouseList._ID);
            c.moveToFirst();
            return c;
        }

        public Cursor getListOfRoomsOfHouse(long houseId) {
            SQLiteDatabase db = getReadableDatabase();

            String[] selection_args = new String[] {"" + houseId};
            Cursor c = db.query(RoomList.TABLE_NAME, null, RoomList.COLUMN_NAME_HOUSE_ID + " = " + houseId,
                    null, RoomList.COLUMN_NAME_ROOM_NAME, null, RoomList.COLUMN_NAME_ROOM_NAME);
            c.moveToFirst();
            return c;
        }

        public Cursor getFeaturesOfRoomOfHouse(long houseId, String roomName) {
            SQLiteDatabase db = getReadableDatabase();
            roomName = roomName.replace("'", "''");

            Cursor c = db.query(RoomList.TABLE_NAME, null,
                    RoomList.COLUMN_NAME_HOUSE_ID + " = " + houseId + " AND " +
                    RoomList.COLUMN_NAME_ROOM_NAME + " = '" + roomName + "'",
                    null, null, null, null);
            c.moveToFirst();
            return c;
        }

        public void setFeaturePresent(long featureId, boolean present) {
            SQLiteDatabase db = getWritableDatabase();
            int i_present = (present) ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put(RoomList.COLUMN_NAME_HAS_FEATURE, i_present);
            String selection = RoomList._ID + " LIKE ?";
            String [] selectionArgs = { String.valueOf(featureId) };

            int count = db.update(RoomList.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        public Cursor getHouse(long houseId) {
            SQLiteDatabase db = getWritableDatabase();

            Cursor c = db.query(HouseList.TABLE_NAME, null, HouseList._ID + " = " + houseId,
                    null, null, null, null);
            c.moveToFirst();
            return c;
        }

        public void updateHouse(long houseId, String name, String address, int price, String info) {
            SQLiteDatabase db = getWritableDatabase();

            name = name.replace("'", "''");
            address = address.replace("'", "''");
            info = info.replace("'", "''");

            ContentValues values = new ContentValues();
            values.put(HouseList.COLUMN_NAME_HOUSE_NAME, name);
            values.put(HouseList.COLUMN_NAME_HOUSE_ADDR, address);
            values.put(HouseList.COLUMN_NAME_HOUSE_PRICE, price);
            values.put(HouseList.COLUMN_NAME_HOUSE_INFO, info);
            String selection = HouseList._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(houseId) };

            int count = db.update(HouseList.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        public void deleteHouse(long houseId) {
            // First delete all the entries in RoomList
            SQLiteDatabase db = getWritableDatabase();
            String selection = RoomList.COLUMN_NAME_HOUSE_ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(houseId) };
            db.delete(RoomList.TABLE_NAME, selection, selectionArgs);

            selection = HouseList._ID + " LIKE ?";
            db.delete(HouseList.TABLE_NAME, selection, selectionArgs);
        }

        public void AddNewHouse(String name, String address, int price, String info) {
            SQLiteDatabase db = getWritableDatabase();

            name = name.replace("'", "''");
            address = address.replace("'", "''");
            info = info.replace("'", "''");

            ContentValues newHouse = new ContentValues();
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_NAME, name);
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_ADDR, address);
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_PRICE, price);
            newHouse.put(HouseList.COLUMN_NAME_HOUSE_INFO, info);

            long houseId = db.insert(HouseList.TABLE_NAME, HouseList.COLUMN_NAME_HOUSE_NAME, newHouse);
//            Cursor houseC = db.query(HouseList.TABLE_NAME, null,
//                    HouseList._ID, new String[]{"" + newRowId}, null, null, null);
//            houseC.moveToFirst();

            // Add all room/feature entries
            AddMasterBedroom(houseId);
            AddMasterBathroom(houseId);
            AddKidRoom(houseId);
            AddKitchenToNewHouse(houseId);
            AddGuestRoom(houseId);
            AddMediaRoom(houseId);
            AddOffice(houseId);
            AddFloorPlan(houseId);
            AddLaundryRoom(houseId);
        }

        private void AddNewRoomEntry(long houseId, String name, String feature, String priority) {
            int has_feature = 0;
            String notes = "";
            SQLiteDatabase db = getWritableDatabase();

            ContentValues newRoomFeature = new ContentValues();
            newRoomFeature.put(RoomList.COLUMN_NAME_HOUSE_ID, houseId);
            newRoomFeature.put(RoomList.COLUMN_NAME_ROOM_NAME, name);
            newRoomFeature.put(RoomList.COLUMN_NAME_ROOM_FEATURE, feature);
            newRoomFeature.put(RoomList.COLUMN_NAME_FEATURE_PRIORITY, priority);
            newRoomFeature.put(RoomList.COLUMN_NAME_HAS_FEATURE, has_feature);
            newRoomFeature.put(RoomList.COLUMN_NAME_FEATURE_NOTE, notes);

            long newRowId = db.insert(RoomList.TABLE_NAME, null, newRoomFeature);
        }

        private void AddKitchenToNewHouse(long houseId) {
            String roomName = "Kitchen";

            AddNewRoomEntry(houseId, roomName, "Granite counter tops & backsplash", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Steel Appliances", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Dark Cabinets", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Cooktop (electric preferred)", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Wall mount microwave & oven", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "No sink/cooktop on island", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Tile Floor", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Breakfast bar", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Large pantry", ROOM_FEATURE_PRIORITIES[3]);
        }

        private void AddMasterBedroom(long houseId) {
            String roomName = "Master Bedroom";

            AddNewRoomEntry(houseId, roomName, "Bathroom door", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Big to fit king bed and sitting area", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Large windows", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Closet pre-installed with cabinets", ROOM_FEATURE_PRIORITIES[3]);
            AddNewRoomEntry(houseId, roomName, "Room not facing west", ROOM_FEATURE_PRIORITIES[0]);
        }

        private void AddMasterBathroom(long houseId) {
            String roomName = "Master Bathroom";

            AddNewRoomEntry(houseId, roomName, "Tiled shower", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Granite counter tops", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "5 piece bathroom", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Good lighting/windows", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Big shower area", ROOM_FEATURE_PRIORITIES[3]);
        }

        private void AddKidRoom(long houseId) {
            String roomName = "Kid's Room & Bath";

            AddNewRoomEntry(houseId, roomName, "Big - to have bed, play area, toys and table", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Closer to master bedroom", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Attached bath", ROOM_FEATURE_PRIORITIES[3]);
            AddNewRoomEntry(houseId, roomName, "Tiled shower", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "No sharing", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Room not facing west", ROOM_FEATURE_PRIORITIES[0]);
        }

        private void AddGuestRoom(long houseId) {
            String roomName = "Guest Room & Bath";

            AddNewRoomEntry(houseId, roomName, "Large to hold queen bed", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Full bath with shower", ROOM_FEATURE_PRIORITIES[0]);
        }

        private void AddMediaRoom(long houseId) {
            String roomName = "Media Room";

            AddNewRoomEntry(houseId, roomName, "Is there mediaroom?", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Pre-wired for Audio/Video", ROOM_FEATURE_PRIORITIES[2]);
        }

        private void AddOffice(long houseId) {
            String roomName = "Office";

            AddNewRoomEntry(houseId, roomName, "Is there office?", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Separate room", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Space for 2 tables", ROOM_FEATURE_PRIORITIES[1]);
        }

        private void AddLaundryRoom(long houseId) {
            String roomName = "Laundry Room";

            AddNewRoomEntry(houseId, roomName, "Closed room", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Sink", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Storage", ROOM_FEATURE_PRIORITIES[1]);
        }

        private void AddFloorPlan(long houseId) {
            String roomName = "Floor plan Details";

            AddNewRoomEntry(houseId, roomName, "Stairs not visible from enterance", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "High Ceilings", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Wide front door", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Upgraded/Metal entry door", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Big backyard", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Open/Close door for backyard - not sliding", ROOM_FEATURE_PRIORITIES[2]);
            AddNewRoomEntry(houseId, roomName, "Tiled floor in living and high traffic area", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Big family room, tv, sofa distance should be large", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "Front porch or sitting area", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Covered backyard porch with ceiling fan", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "North-South plot, south entrance", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "2 car garage", ROOM_FEATURE_PRIORITIES[0]);
            AddNewRoomEntry(houseId, roomName, "2 story", ROOM_FEATURE_PRIORITIES[1]);
            AddNewRoomEntry(houseId, roomName, "Balcony/patio on 2nd floor", ROOM_FEATURE_PRIORITIES[2]);
        }
    }

}
