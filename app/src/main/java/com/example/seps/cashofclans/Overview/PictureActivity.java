package com.example.seps.cashofclans.Overview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.seps.cashofclans.R;

import static com.example.seps.cashofclans.Overview.OverviewListActivity.RotateBitmap;

public class PictureActivity extends AppCompatActivity {

    private static final String TAG = "PictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        ImageView bild = (ImageView) findViewById(R.id.picture) ;
        Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("picture"));
        if(myBitmap != null) {
            Log.d(TAG, "Foto da");
            bild.setImageBitmap(RotateBitmap(myBitmap,90));
        }
        else
            Log.d(TAG,"Foto nicht da!");
    }
}
