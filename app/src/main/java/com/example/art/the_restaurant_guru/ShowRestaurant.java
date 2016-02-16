package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by art on 4/2/2015.
 */
public class ShowRestaurant extends Activity {
    private String path = "ratings";
    private String id = "";
    private String name = "";
    private String address = "";
    private String location = "";
    private float rating = 0;
    public String favRestaurants = "";

    public void back_to_home_btn(View view) {
        Intent i = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            name = extras.getString("name");
            address = extras.getString("address");
            location = extras.getString("location");
        }
        TextView restName = (TextView) findViewById(R.id.restaurant_name);
        restName.setText(name + "\n" + address);
        ReadDate readDate = new ReadDate();
        readDate.execute();
    }

    public void save_btn(View view) {
        RatingBar mBar = (RatingBar) findViewById(R.id.show_rating_bar);
        rating = mBar.getRating();
        try {
            saveData();
            Toast.makeText(ShowRestaurant.this, "Data was Saved!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() throws IOException {

        String data = "";
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Context context = getBaseContext();
        data = "[" + id + "," + name + "," + address + "," + location + "," + rating + "," + dateFormat.format(new Date()) + "]";
        FileOutputStream out = null;
        try {
            out = openFileOutput(path, context.MODE_APPEND);
            out.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void deleteFile(View view) {
        Context context = getBaseContext();
        boolean flag = context.deleteFile(path);

        if (flag) {
            Toast.makeText(ShowRestaurant.this, "Saved Data was Deleted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ShowRestaurant.this, "Saved Data was Not Deleted!", Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(i);
    }

    public void showSavedData(View view) {
        Button showSavedData = (Button) findViewById(R.id.show_save_data_btn);
        showSavedData.setVisibility(view.GONE);
        TextView title = (TextView) findViewById(R.id.restaurant_name);
        title.setVisibility(view.GONE);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.show_rating_bar);
        ratingBar.setVisibility(View.GONE);
        Button save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setVisibility(View.GONE);
        TextView show_data = (TextView) findViewById(R.id.showSaveData);
        show_data.setVisibility(view.VISIBLE);
        Button hideSavedData = (Button) findViewById(R.id.hide_saved_data);
        hideSavedData.setVisibility(view.VISIBLE);
        show_data.setMovementMethod(new ScrollingMovementMethod());
        show_data.setText(favRestaurants);
    }

    public void hideSavedData(View view) {
        TextView title = (TextView) findViewById(R.id.restaurant_name);
        title.setVisibility(view.VISIBLE);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.show_rating_bar);
        ratingBar.setVisibility(View.VISIBLE);
        Button save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setVisibility(View.VISIBLE);
        Button showSavedData = (Button) findViewById(R.id.show_save_data_btn);
        showSavedData.setVisibility(view.VISIBLE);
        Button hideSavedData = (Button) findViewById(R.id.hide_saved_data);
        hideSavedData.setVisibility(view.GONE);
        TextView show_data = (TextView) findViewById(R.id.showSaveData);
        show_data.setVisibility(view.GONE);
    }

    public class ReadDate extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader in;
            ArrayList<String[]> favList;
            String names = "";
            try {
                in = new BufferedReader(new InputStreamReader(openFileInput(path)));
                String inputStr;
                StringBuffer stringBuffer = new StringBuffer();
                while ((inputStr = in.readLine()) != null) {
                    stringBuffer.append(inputStr);
                }
                in.close();
                favList = parseStr(stringBuffer);
                for (int i = 0; i < favList.size(); i++) {
                    names = names + "NAME: " + favList.get(i)[1] + "\nRATING: " + favList.get(i)[6] + "\n\n";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return names;
        }

        protected void onPostExecute(String result) {
            favRestaurants = doInBackground();
        }

        private ArrayList<String[]> parseStr(StringBuffer str) {
            ArrayList<String[]> answer = new ArrayList<>();
            String the_str = str.toString();
            int finish = 0;
            String[] info;
            while (the_str.length() > 0) {
                finish = the_str.indexOf("]");
                info = the_str.substring(1, finish).split(",");
                info[4] = info[4].substring(10);
                info[5] = info[5].substring(0, info[5].length() - 1);
                answer.add(info);
                the_str = the_str.substring(finish + 1);
            }
            return answer;
        }
    }
}
