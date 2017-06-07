package com.cashify.overview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.cashify.R;

/**
 * Diese Klasse stellt ein gemachtes Bild grafisch dar.
 * Der Speicherort des Bildes wird dem Intent als Extra übergeben
 * Aufruf:
 * Intent pic = new Intent(v.getContext(),PictureActivity.class);
 * pic.putExtra("name_des_Fotos",(String)v.getTag());
 * startActivity(pic);
 */
public class PictureActivity extends AppCompatActivity {

    /**Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.*/
    private static final String TAG = "PictureActivity";
    /**Die Imageview in welche das Foto geladen wird.*/
    private ImageView bild;
    /**Das Bitmap das aus dem gespeicherten Bild erstellt wird.*/
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        //Die ImageView aus dem Layout holen.
        bild = (ImageView) findViewById(R.id.picture);
        //Ein Bitmap aus dem gespeicherten Foto erstellen.
        myBitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("picture"));
        if (myBitmap != null) {//Wenn das Foto geladen werden konnte.
            Log.d(TAG, "Foto da");
            bild.setImageBitmap(RotateBitmap(myBitmap, 90));
        } else
            Log.d(TAG, "Foto nicht da!");
    }

    /**Diese Methode dreht das Bitmap um eine übergebene Anzahl an Grad.*/
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
