package com.example.restarun.SearchActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL( strings[0] );
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput( true );
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream( input );
            connection.disconnect();
            return myBitmap;
        } catch (IOException e) {
            Log.d("DEBUG","" + strings[0]);
            e.printStackTrace();
        }

        return null;
    }

}
