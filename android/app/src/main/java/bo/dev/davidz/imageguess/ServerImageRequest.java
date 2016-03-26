package bo.dev.davidz.imageguess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DavidAhmad on 10/05/2015.
 */
public class ServerImageRequest extends AsyncTask<String, Void, Bitmap>  {
    //ServerAnswerListener <Bitmap> listener;
    ImageView image;

    // CHANGE THE SERVER ADDRESS ON "strings.xml" AS APPROPIATE
    ServerImageRequest(/*ServerAnswerListener<Bitmap> listener*/ ImageView image) {
        //this.listener = listener;
        this.image = image;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        URL urlObject = null;
        String url = params[0] + params[1];

        Bitmap bmp = null;
        try {
            urlObject = new URL(url);
            bmp = BitmapFactory.decodeStream(urlObject.openConnection().getInputStream());
            Log.v("url1", url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("url2", url);
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        image.setImageBitmap(bitmap);
    }
}

