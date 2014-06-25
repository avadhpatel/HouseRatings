package com.avadh.houseratings.houseratings.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avadh.houseratings.houseratings.app.HouseRatingsDb.HouseRatingsDbHelper;


public class AddHouseActivity extends ActionBarActivity {

    private boolean editHouse = false;
    private long houseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        editHouse = intent.getBooleanExtra(ViewHouses.VIEW_HOUSE_EDIT, false);
        houseId = intent.getLongExtra(ViewHouses.VIEW_HOUSE_ID, -1);

        if (editHouse && houseId != -1) {
            HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
            Cursor c = dbHelper.getHouse(houseId);

            String houseName = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_NAME));
            String houseAddr = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_ADDR));
            int housePrice = c.getInt(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_PRICE));
            String houseInfo = c.getString(c.getColumnIndex(HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_INFO));
            if (houseInfo == null) {
                houseInfo = "";
            }

            EditText mName = (EditText)findViewById(R.id.editHouseName);
            EditText mAddr = (EditText)findViewById(R.id.editHouseAddr);
            EditText mPrice = (EditText)findViewById(R.id.editHousePrice);
            EditText mInfo = (EditText)findViewById(R.id.editHouseInfo);

            mName.setText(houseName, TextView.BufferType.EDITABLE);
            mAddr.setText(houseAddr, TextView.BufferType.EDITABLE);
            mPrice.setText(String.valueOf(housePrice), TextView.BufferType.EDITABLE);
            mInfo.setText(String.valueOf(houseInfo), TextView.BufferType.EDITABLE);
        }
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNewHouse(View view) {
        HouseRatingsDbHelper dbHelper = new HouseRatingsDbHelper(this);
        EditText mName = (EditText)findViewById(R.id.editHouseName);
        EditText mAddr = (EditText)findViewById(R.id.editHouseAddr);
        EditText mPrice = (EditText)findViewById(R.id.editHousePrice);
        EditText mInfo = (EditText)findViewById(R.id.editHouseInfo);
        int price = 0;

        try {
            price = Integer.parseInt(mPrice.getText().toString());
        } catch(NumberFormatException nfe) {
            price = 0;
        }

        if (editHouse && houseId != -1) {
            dbHelper.updateHouse(houseId, mName.getText().toString(), mAddr.getText().toString(), price, mInfo.getText().toString());
        } else {
            dbHelper.AddNewHouse(mName.getText().toString(), mAddr.getText().toString(), price, mInfo.getText().toString());
        }
        finish();
    }
}
