package com.avadh.houseratings.houseratings.app;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.avadh.houseratings.houseratings.app.HouseRatingsDb.HouseRatingsDbHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ConnectViewToDb();
    }

//    private void ConnectViewToDb() {
//        HouseRatingsDbHelper dbHelper = new HouseRatingsDbHelper(this);
//        Cursor c = dbHelper.getListOfHouses();
//
//        /* TEST CODE */
//        if (c.getCount() == 0 || c.getInt(0) == 0) {
//            // Empty list - Add dummy entries
//            dbHelper.fillDummy();
//            c = dbHelper.getListOfHouses();
//        }
//
//        /* Now attach the cursor to the list of houses */
//        String[] from = {"name", "price"};
//        int[] to = {R.id.houseName, R.id.housePrice};
//        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.house_list_row, c, from, to, 0);
//        setListAdapter(adapter);
//    }

    public void viewHouses(View view) {
        Intent intent = new Intent(this, ViewHouses.class);
        startActivity(intent);
    }

    public void addNewHouse(View view) {
        Intent intent = new Intent(this, AddHouseActivity.class);
        startActivity(intent);
    }

    public void emailData(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
