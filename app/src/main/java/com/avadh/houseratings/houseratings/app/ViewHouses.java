package com.avadh.houseratings.houseratings.app;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ViewHouses extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_houses);

        ConnectViewToDb();
    }

    private void ConnectViewToDb() {
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        Cursor c = dbHelper.getListOfHouses();

        /* TEST CODE */
        if (c.getCount() == 0 || c.getInt(0) == 0) {
            // Empty list - Add dummy entries
            dbHelper.fillDummy();
            c = dbHelper.getListOfHouses();
        }

        /* Now attach the cursor to the list of houses */
        String[] from = {"name", "price"};
        int[] to = {R.id.houseName, R.id.housePrice};
        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.house_list_row, c, from, to, 0);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_houses, menu);
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
