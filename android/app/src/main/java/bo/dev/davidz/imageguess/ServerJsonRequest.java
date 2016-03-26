package bo.dev.davidz.imageguess;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DavidAhmad on 08/05/2015
 * Fetches the categories from serverBaseAddress.
 * The result is processed in the listener
 */
public class ServerJsonRequest extends AsyncTask<String, Void, String> {

    ServerAnswerListener<String> listener;
    String serverBaseAddress;

    // CHANGE THE SERVER ADDRESS ON "strings.xml" AS APPROPIATE
    public ServerJsonRequest(ServerAnswerListener<String> listener, String serverAddress) {
        this.listener = listener;
        serverBaseAddress = serverAddress;
    }

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String answerJsonStr = null;
        String urlStr = null;

        try {
            Uri.Builder builder = Uri.parse(serverBaseAddress).buildUpon()
                    .appendPath("services");

            if (params.length != 0) {
                for (String param:params) {
                    builder.appendPath(param);
                }
            }
            Uri builtUri = builder.build();
            urlStr = builtUri.toString();
            URL url = new URL(urlStr);
            Log.v(LOG_TAG, "URL: " + urlStr);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                answerJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                //New line makes debugging easier
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                answerJsonStr = null;
            }
            answerJsonStr = buffer.toString();
        } catch (IOException e) {
            int duration = Toast.LENGTH_SHORT;/*
            String toastDescription = "Couldn't find " + urlStr;
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastDescription, duration);
            toast.show();*/
            Log.e(LOG_TAG, "Error while connecting to the server: "+e, e);
            answerJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return answerJsonStr;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        listener.processServerAnswer(json);

    }
}
