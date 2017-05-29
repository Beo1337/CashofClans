package com.example.seps.cashofclans.Overview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.R;

public class OverviewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_list);
        showDataTable();
    }

    public void showDataTable() {
        //Get Date for Query
        /*Intent i = getParent().getIntent();
        String start = i.getStringExtra("start");
        String end = i.getStringExtra("end");
        boolean noDate = true;
        if (start.equals(null) || end.equals(null)){
            noDate = true;
        } else {
            noDate = false;
        }*/

        // Create DatabaseHelper instance
        DatabaseHelper myDb = new DatabaseHelper(this);
        SQLiteDatabase db = myDb.getReadableDatabase();

        // Reference to TableLayout
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"ID", "€", "TEXT", "KATEGORIE", "DATUM", "FOTO", "   "};
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM uebersicht", null);

            /*if (noDate) {
            }else{
                cursor = db.rawQuery("SELECT * FROM uebersicht WHERE DATUM >= " + start +" AND DATUM <= " +end, null);
            }*/

            String[] entry = new String[6];
            while (cursor.moveToNext()) {

                entry[0] = String.valueOf(cursor.getInt(0));
                entry[1] = String.valueOf(cursor.getDouble(1));
                entry[2] = cursor.getString(2);
                entry[3] = String.valueOf(cursor.getInt(3)); //getCategoryName() -> funktioniert noch nicht
                entry[4] = cursor.getString(4);
                entry[5] = cursor.getString(6);

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                for (String text : entry) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }
                //Button for deleting entries
                ImageButton btn_del = new ImageButton(this);
                btn_del.setImageResource(R.mipmap.delete_x);
                    btn_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //myDb.removeEntry(cursor.getInt(0));
                            Toast.makeText(OverviewListActivity.this, "Erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
                        }
                    });
                row.addView(btn_del);
                //Button for editing entries
                ImageButton btn_edit = new ImageButton(this);
                btn_edit.setImageResource(R.mipmap.edit);
                    btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TO-IMPLEMENT
                        }
                    });
                row.addView(btn_edit);

                tableLayout.addView(row);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //Method which return the name of a category !TO-IMPLEMENT!
    public String getCategoryName(int id) {
        DatabaseHelper myDb = new DatabaseHelper(this);
        SQLiteDatabase db = myDb.getReadableDatabase();

        Cursor kategorie_cursor = db.rawQuery("SELECT * FROM category", null);

        while (kategorie_cursor.moveToNext() || !(kategorie_cursor.getInt(0) == id)) {
            if (kategorie_cursor.getInt(0) == id) {
                return kategorie_cursor.getString(1);
            }
        }
        kategorie_cursor.close();
        return null;
    }
}
