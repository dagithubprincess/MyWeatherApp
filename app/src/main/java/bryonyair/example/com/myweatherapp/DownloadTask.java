package bryonyair.example.com.myweatherapp;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static bryonyair.example.com.myweatherapp.MainActivity.count;

class DownloadTask extends AsyncTask<String, Void, String>  {

    Context context2;
    TextView date1, min_tem, max_tem, myplace;
    ImageView myimg;
    String result;

    public DownloadTask(Context context, TextView dateo, TextView min_tempo, TextView max_tempo, TextView place, ImageView image) {
        context2 = context;
        this.date1 = dateo;
        this.min_tem = min_tempo;
        this.max_tem = max_tempo;
        this.myplace = place;
        this.myimg = image;
    }

    @Override
    protected String doInBackground(String... urls) {

        Log.d("Downloadtask", "do in background");
        result = "";
        URL url;
        HttpURLConnection myconnection;

        try {
            url = new URL(urls[0]);
            myconnection = (HttpURLConnection) url.openConnection();
            InputStream in = myconnection.getInputStream();
            InputStreamReader myStreamReader = new InputStreamReader(in);
            int data = myStreamReader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = myStreamReader.read();
            }

            return result;
        } catch (Exception e) {
            //webconnection error
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("DownloadTask", "onPostExecute");

        String icon = null;
        try {
            JSONObject myObject = new JSONObject(result);
            JSONObject main = new JSONObject(myObject.getString("main"));
            String min_temp = main.getString("temp_min");
            String max_temp = main.getString("temp_max");
            String placeName = myObject.getString("name");
            // Display date with day name in a short format
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("EEE, dd/MM/yyyy");
            String today = formatter.format(date);

            JSONArray weatherArray = myObject.getJSONArray("weather");

            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject obj = weatherArray.getJSONObject(i);
                icon = obj.getString("icon");
            }

            date1.setText(today);
            min_tem.setText(min_temp);
            max_tem.setText(max_temp);
            myplace.setText(placeName);

            Picasso.with(context2).load("http://openweathermap.org/img/w/"+icon+ ".png").into(myimg);
            count++;


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
