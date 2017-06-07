package com.cashify.overview;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Diese Klasse stellt die alten Werte eines Eintrags dar und schreibt die geänderten Werte zurück in die Datenbank
 */
public class ChangeEntryActivity extends AppCompatActivity {

    /**Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.*/
    private static final String TAG = "AddActivity";
    /**In diesem Textfeld wird der aktuelle Kontostand angezeigt.*/
    private EditText betrag;
    /**In diesem Textfeld wird der aktuelle Title angezeigt.*/
    private EditText title;
    /**Wird benötigt um die gewählte Zahl in das Textfeld zu schreiben.*/
    private String s;
    /**Datenbank*/
    private DatabaseHelper myDb;
    /**Wird benötigt um negative Einträge zu verbuchen*/
    private int vorzeichen = 1;
    /**Speichert eine Auswahlliste der verfügbaren Kategorien.*/
    private Spinner spin;
    /**Speichername des Fotos*/
    private String foto = null;
    /**File in dem das Foto gespeichert wird*/
    private File photoFile = null;
    /**Jahr des Eintrags*/
    private int mYear = -1;
    /**Monat des Eintrags*/
    private int mMonth = -1;
    /**Tag des Eintrags*/
    private int mDay = -1;
    /**Wird benötigt um Werte die der Aktivity mitgegeben wurden auszulesen.*/
    private Bundle bundle;
    /**Format für ein Datum*/
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**Button zum Löschen eines Bildes*/
    private ImageButton delete;
    /**Button zum Machen eines Fotos*/
    private ImageButton cam;

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_entry);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        betrag = (EditText) findViewById(R.id.Betrag);
        title = (EditText) findViewById(R.id.title);

        myDb = new DatabaseHelper(this);

        //Über bundle können zusätzliche Infos aus dem Intent ausgelesen werden.
        bundle = getIntent().getExtras();


        Button commit = (Button) findViewById(R.id.ButtonCommit);

        //Cursor von der Kategorietabelle holen.
        Cursor c = myDb.getReadableDatabase().rawQuery("SELECT ID AS _id, NAME FROM category", null);
        //Adapter aus dem Cursor erstellen.
        String[] from = new String[]{"NAME"};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to);

        //Vordefiniertes Layout dem Spinner zuweisen.
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin = (Spinner) this.findViewById(R.id.category);
        spin.setAdapter(sca);

        betrag.setText(bundle.getString("betrag"));
        selectValue(spin, bundle.getString("kategorie"));
        title.setText(bundle.getString("titel"));
        foto = bundle.getString("foto");
        delete = (ImageButton) findViewById(R.id.imageButton7);
        cam = (ImageButton) findViewById(R.id.imageButton);
        if (foto != null) {

            cam.setImageResource(R.drawable.cameracheckicon);
            delete.setVisibility(View.VISIBLE);
        }
        else
            delete.setVisibility(View.INVISIBLE);


        try {
            Date date = sdf.parse(bundle.getString("datum"));
            Log.d(TAG, "DATUM: " + bundle.getString("datum"));
            mYear = Integer.valueOf(bundle.getString("datum").substring(0, 4));
            Log.d(TAG, "JAHR: " + mYear);
            mMonth = date.getMonth();
            Log.d(TAG, "TAG aus altem Eintrag: " + bundle.getString("datum").substring(8, 10));
            mDay = Integer.valueOf(bundle.getString("datum").substring(8, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    /**Diese Methode setzt den Spinner auf eine Kategorie.*/
    private void selectValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Cursor) spinner.getItemAtPosition(i)).getString(1).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**Diese Methode speichert die eingegebenen Werte in die Datenbank.*/
    public void eintragen(View v) {

        if (!betrag.getText().toString().equals("")) {//Wenn kein Betrag eingeben wurde, wird kein Eintrag in der Datenbank erstellt.

            EditText title = (EditText) findViewById(R.id.title);
            //Kategorie holen
            String s = ((Cursor) spin.getSelectedItem()).getString(1);
            //Datum festlegen
            String strDate;
            if (mYear == -1) {//Wenn noch kein Datum vergeben wurde, nimm das jetztige Datum.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                strDate = sdf.format(new Date());
            } else {//Wenn ein Datum vergeben wurde, nimm dieses
                String day;

                if (mDay < 10)
                    day = "0" + mDay;
                else
                    day = "" + mDay;

                mMonth++;//Monat starte bei 0 daher vorher ++ und nacher --
                if (mMonth < 10)
                    strDate = mYear + "-0" + mMonth + "-" + day + " 00:00:00";
                else
                    strDate = mYear + "-" + mMonth + "-" + day + " 00:00:00";

                mMonth--;
            }
            Log.d(TAG, "Eingetragenes Datum: " + strDate);

            OverviewManager manager = new OverviewManager(myDb);

            //addEntry(Betrag,Titel,Foto,Kategorie,Datum)
            if (!manager.changeEntry(Integer.valueOf(bundle.getString("id")), Double.valueOf(betrag.getText().toString()) * vorzeichen, title.getText().toString(), foto, s, strDate))
                Toast.makeText(this, "Fehler beim Ändern!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**Diese Methode macht ein Foto welches zum Eintrag hinzugefügt wird*/
    public void cam(View v) {

        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Wenn eine Kamera gefunden wurde:
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                //Neues File für das Foto anlegen.
                try {
                    photoFile = createImageFile();
                    foto = photoFile.getAbsolutePath();
                } catch (IOException ex) {
                    Log.d(TAG, "Fehler beim Erstellen des Files.");
                    ex.printStackTrace();
                }
                //Wenn ein File für das Foto erstellt werden konnte:
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    cam.setImageResource(R.drawable.cameracheckicon);
                }
            }
        } catch (Exception e1) {//Falls irgend ein Fehler mit der Camera auftreten sollte, wird dieser gefangen.
            Toast.makeText(ChangeEntryActivity.this, "Foto aufnehmen nicht möglich!", Toast.LENGTH_LONG).show();
        }
    }

    /**Diese Methode wird aufgerufen sobald das Foto gemacht wurde. Es wird der Fotobutton durch das Bild ersetzt.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        if (myBitmap != null) {
            Log.d(TAG, "Foto da");
            ImageButton im = (ImageButton) findViewById(R.id.imageButton7);
            im.setImageResource(R.drawable.cameracheckicon);
        } else
            Log.d(TAG, "Foto nicht da!");
    }

    /**Diese Methode erstellt ein File in dem das Foto gespeichert wird und liefert dieses als Rückgabewert.*/
    private File createImageFile() throws IOException {
        //Ein neues File für ein Bild anlegen.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CashifyPicture_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    /**Diese Methode ruft einen Auswahldialog für einen Datumspicker auf.*/
    public void showStartDateDialog(View v) {
        if (mYear == -1) {//Wenn noch kein Tag gewählt wurde.
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog dialog = new DatePickerDialog(ChangeEntryActivity.this,
                new ChangeEntryActivity.mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    /**Dieser Listener reagiert auf Änderungen des Datumspickers und schreibt das geänderte Datum in die dafür vorgesehnen Felder*/
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            StringBuilder date = new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" ");
            Log.d(TAG, date.toString());


        }
    }

    /**Diese Methode löscht ein Bild von einem Eintrag*/
    public void deletecam(View v){
        Toast.makeText(ChangeEntryActivity.this, "Foto gelöscht!", Toast.LENGTH_LONG).show();
        foto = null;
        delete.setVisibility(View.INVISIBLE);
        cam.setImageResource(R.drawable.cameraicon);

    }
}
