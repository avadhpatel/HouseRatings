package com.avadh.houseratings.houseratings.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ViewHouses extends ListActivity {

    public static final String VIEW_HOUSE_ID = "house_id";
    public static final String VIEW_HOUSE_EDIT = "house_edit";

    private SimpleCursorAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_houses);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ConnectViewToDb();
    }

    private void ConnectViewToDb() {
        HouseRatingsDb.HouseRatingsDbHelper dbHelper = new HouseRatingsDb.HouseRatingsDbHelper(this);
        Cursor c = dbHelper.getListOfHouses();

        /* TEST CODE */
//        if (c.getCount() == 0 || c.getInt(0) == 0) {
//            // Empty list - Add dummy entries
//            dbHelper.fillDummy();
//            c = dbHelper.getListOfHouses();
//        }

        /* Now attach the cursor to the list of houses */
        String[] from = {HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_NAME,
                HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_PRICE,
                HouseRatingsDb.HouseList.COLUMN_NAME_HOUSE_ADDR};
        int[] to = {R.id.houseName, R.id.housePrice, R.id.houseAddr};
        ListView house_list = (ListView)findViewById(android.R.id.list);

        adapter = new ViewHouseCursorAdapter(this, R.layout.house_list_row, c, from, to, 0);
        house_list.setAdapter(adapter);
        startManagingCursor(c);
        house_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewRooms.class);
                intent.putExtra(VIEW_HOUSE_ID, id);
//                startActivity(intent);
                startActivityForResult(intent, -1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewHouseCursorAdapter extends SimpleCursorAdapter {
        public ViewHouseCursorAdapter(Context context, int layout, Cursor c, String[] from,
                                      int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView priceVIew = (TextView)view.findViewById(R.id.housePrice);
            String price = priceVIew.getText().toString();
            String new_price = "";
            int len = price.length();
            for (int i=0; i < len; i++) {
                if(i > 0 && i % 3 == 0)
                    new_price = "," + new_price;
                new_price = price.charAt(len - i - 1) + new_price;
            }

            priceVIew.setText("$" + new_price);
            return view;
        }
    }
}
