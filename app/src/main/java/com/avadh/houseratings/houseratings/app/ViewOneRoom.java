package com.avadh.houseratings.houseratings.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;


public class ViewOneRoom extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_room);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        long houseId = intent.getLongExtra(ViewHouses.VIEW_HOUSE_ID, -1);
        String roomName = intent.getStringExtra(ViewRooms.VIEW_ROOM_NAME);

        if (houseId == -1 || roomName.compareTo("") == 0) {
            finish();
        }

        setTitle(roomName);
        ConnectViewDb(houseId, roomName);
    }

    private void ConnectViewDb(long houseId, String roomName) {
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        Cursor c = dbHelper.getFeaturesOfRoomOfHouse(houseId, roomName);

        String[] from = {HouseRatingsDb.RoomList.COLUMN_NAME_ROOM_FEATURE};
        int[] to = {R.id.feature};
        ListView featureList = (ListView)findViewById(android.R.id.list);

        ListAdapter adapter = new OneRoomCursorAdapter(this, R.layout.room_feature_row, c, from, to, 0);
        featureList.setAdapter(adapter);
        startManagingCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_one_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OneRoomCursorAdapter extends SimpleCursorAdapter {
        private Cursor cursor = null;

        public OneRoomCursorAdapter(Context context, int layout, Cursor c, String[] from,
                                    int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            cursor = c;
            cursor.moveToFirst();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            int cur_pos = cursor.getPosition();
            cursor.moveToPosition(position);
            final long feature_id = cursor.getLong(cursor.getColumnIndex(HouseRatingsDb.RoomList._ID));
            final int is_selected = cursor.getInt(cursor.getColumnIndex(HouseRatingsDb.RoomList.COLUMN_NAME_HAS_FEATURE));
            cursor.moveToPosition(cur_pos);

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.featurePresent);
            checkBox.setChecked((is_selected > 0) ? true : false);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Update the DB.
                    CheckBox box = (CheckBox)view;
                    boolean b = box.isChecked();
                    HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(getApplicationContext());
                    dbHelper.setFeaturePresent(feature_id, b);
                }
            });
            return view;
        }
    }
}
