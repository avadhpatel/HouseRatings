package com.avadh.houseratings.houseratings.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ViewRooms extends ActionBarActivity {

    public static String VIEW_ROOM_NAME = "room_name";
    private static long houseId = -1;
    private static String houseAddr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        if (savedInstanceState != null) {
            houseId = savedInstanceState.getLong("houseId");
        } else if(intent != null) {
            houseId = intent.getLongExtra(ViewHouses.VIEW_HOUSE_ID, -1);
        }

        SetupHouseInfo(houseId);
        ConnectViewDb(houseId);
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putLong("houseId", houseId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle instanceState) {
        houseId = instanceState.getLong("houseId");
    }

    private void SetupHouseInfo(long houseId) {
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        Cursor c = dbHelper.getHouse(houseId);

        String houseName = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_NAME));
        houseAddr = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_ADDR));
        setTitle(houseName);
        getActionBar().setSubtitle(houseAddr);

        houseAddr = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_ADDR));
    }

    private void ConnectViewDb(final long houseId) {
        // Get list of rooms for selected House
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        final Cursor c = dbHelper.getListOfRoomsOfHouse(houseId);

        String[] from = {"name"};
        int[] to = {R.id.room_name};
        ListView roomList = (ListView)findViewById(android.R.id.list);

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_view_rooms, c, from, to, 0);
        roomList.setAdapter(adapter);
        startManagingCursor(c);
        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Call View Room Features Activity
                Intent intent = new Intent(getApplicationContext(), ViewOneRoom.class);
                String roomName = c.getString(c.getColumnIndex(HouseRatingsDb.RoomList.COLUMN_NAME_ROOM_NAME));
                intent.putExtra(ViewHouses.VIEW_HOUSE_ID, houseId);
                intent.putExtra(VIEW_ROOM_NAME, roomName);
                startActivity(intent);
            }
        });
    }

    private void openMapHouse() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri.Builder builder = new Uri.Builder();
        String mapUri = "geo:0,0?q=";
        mapUri += houseAddr.replace(" ", "+");
        intent.setData(Uri.parse(mapUri));
        Log.d(ViewRooms.class.getSimpleName(), "map uri " + mapUri);
        startActivity(intent);
//        builder.appendPath(houseAddr);
//        Uri houseAddrUri = builder.build();
//        intent.setData(houseAddrUri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    private void openHouseEdit() {
        Intent intent = new Intent(this, AddHouseActivity.class);
        intent.putExtra(ViewHouses.VIEW_HOUSE_ID, houseId);
        intent.putExtra(ViewHouses.VIEW_HOUSE_EDIT, true);
        startActivity(intent);
    }

    private void deleteHouse() {
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        dbHelper.deleteHouse(houseId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_rooms, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_map_house:
                openMapHouse();
                return true;
            case R.id.action_edit_house:
                openHouseEdit();
                return true;
            case R.id.action_delete_house:
                deleteHouse();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
