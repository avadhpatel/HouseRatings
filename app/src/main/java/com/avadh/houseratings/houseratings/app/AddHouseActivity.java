package com.avadh.houseratings.houseratings.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.avadh.houseratings.houseratings.app.HouseRatingsDb.HouseRatingsDbHelper;


public class AddHouseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_house, menu);
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

    public void saveNewHouse(View view) {
        HouseRatingsDbHelper dbHelper = new HouseRatingsDbHelper(this);
        EditText mName = (EditText)findViewById(R.id.editHouseName);
        EditText mAddr = (EditText)findViewById(R.id.editHouseAddr);
        EditText mPrice = (EditText)findViewById(R.id.editHousePrice);
        int price = 0;

        try {
            price = Integer.parseInt(mPrice.getText().toString());
        } catch(NumberFormatException nfe) {
            price = 0;
        }
        dbHelper.AddNewHouse(mName.getText().toString(), mAddr.getText().toString(), price);
        finish();
    }
}
