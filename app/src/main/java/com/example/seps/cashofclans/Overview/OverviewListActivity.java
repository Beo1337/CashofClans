package com.example.seps.cashofclans.Overview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cashify.overview.Entry;
import com.cashify.overview.PictureActivity;
import com.cashify.database.DatabaseHelper;
import com.cashify.R;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse stellt die Einträge der Datenbank als Liste dar.
 */
public class OverviewListActivity extends AppCompatActivity {

    private static final String TAG = "OverviewListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_list);
        Log.d(TAG,"Beginn öffnen Tabelle");
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

        // Reference to TableLayout
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"€", "TEXT", "KATEGORIE", "DATUM", "FOTO", "   "};
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 0, 5, 0);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        //Liste aller Einträge aus der Datenbank holen.
        List<Entry> entryList = new LinkedList<>();
        entryList.addAll(myDb.getEntries());
        Collections.sort(entryList, new Comparator<Entry>() {//Sortiert nach Datum
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return o1.getDatum().compareTo(o2.getDatum());
                    }
        });

        Iterator<Entry> i = entryList.iterator();
        while(i.hasNext()){//Für jeden Eintrag
            Entry e = i.next();
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            tv.setPadding(5, 0, 5, 0);

            tv.setText(String.valueOf(e.getAmount()));
            row.addView(tv);

            TextView tv1 = new TextView(this);
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(16);
            tv1.setPadding(5, 0, 5, 0);

            tv1.setText(e.getTitle());
            row.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(16);
            tv2.setPadding(5, 0, 5, 0);

            tv2.setText(e.getCategory());
            row.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setGravity(Gravity.CENTER);
            tv3.setTextSize(16);
            tv3.setPadding(5, 0, 5, 0);

            tv3.setText(e.getDatum());
            row.addView(tv3);

            Log.d(TAG,"Bild Speicherort "+e.getFoto());
            if(e.getFoto()==null){
                TextView t = new TextView(this);
                t.setText("");
                row.addView(t);
            }
            else
            {
                ImageButton bild = new ImageButton(this);

                bild.setImageResource(R.drawable.image);
                bild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pic = new Intent(v.getContext(),PictureActivity.class);
                        pic.putExtra("picture",(String)v.getTag());
                        startActivity(pic);
                    }
                });
                bild.setTag(e.getFoto());
                row.addView(bild);
            }


            //Button for deleting entries
            ImageButton btn_del = new ImageButton(this);
            btn_del.setImageResource(R.mipmap.delete_x);
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myDb.removeEntry((int)v.getTag())) {
                        Toast.makeText(OverviewListActivity.this, "Erfolgreich gelöscht!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    else
                        Toast.makeText(OverviewListActivity.this, "Fehler beim Löschen!", Toast.LENGTH_SHORT).show();
                }
            });
            btn_del.setTag(e.getId());
            row.addView(btn_del);


            //Button for editing entries
            ImageButton btn_edit = new ImageButton(this);
            btn_edit.setImageResource(R.mipmap.edit);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO -IMPLEMENT
                }
            });
            //row.addView(btn_edit);

            tableLayout.addView(row);

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
