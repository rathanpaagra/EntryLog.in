package in.entrylog.entrylog.main.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import in.entrylog.entrylog.R;

public class SavingActivity extends AppCompatActivity {

    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        im= (ImageView) findViewById(R.id.imageTilt);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        im= (ImageView) findViewById(R.id.imageTilt);
        im.setImageBitmap(bmp);


        //method3
/*
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        im= (ImageView) findViewById(R.id.imageTilt);
        im.setImageBitmap(bmp);*/


        //method2
        /*if(getIntent().hasExtra("byteArray")) {
           // ImageView previewThumbnail = new ImageView(this);
            im= (ImageView) findViewById(R.id.imageTilt);

            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            im.setImageBitmap(b);
        }*/



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}
