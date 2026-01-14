package ro.pub.cs.systems.eim.practicaltest02v1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestTask extends AsyncTask<String, Void, String> {

    private Context context;

    public WebRequestTask(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(String... params) {
        String prefix = params[0];
        String urlString =
                "https://www.google.com/complete/search?client=chrome&q=" + prefix;

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            connection.disconnect();

        } catch (Exception e) {
            Log.e("WEB_ERROR", e.toString());
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("WEB_RESPONSE", result);

        //3b
        try {
            String thirdSuggestion=
                    new JSONArray(result)
                    .getJSONArray(1)
                    .getString(2);


            Log.d("AUTOCOMPLETE_3", thirdSuggestion);

            //3c
            Intent intent = new Intent("ro.pub.cs.systems.eim.practicaltest02v1.AUTOCOMPLETE");
            intent.setPackage(context.getPackageName());
            intent.putExtra("output", thirdSuggestion);
            context.sendBroadcast(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
