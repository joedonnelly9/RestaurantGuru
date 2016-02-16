package com.example.art.the_restaurant_guru;

/**
 * MainActivity.java
 *
 * This class will show the map of the restaurants that fit the parameters given in
 * HomeScreen.java. If there is a network outage the user will be sent back to
 * HomeScreen.java and shown an error message.
 *
 * CSE 5236
 * Group 6
 * 4/20/2015
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends FragmentActivity implements LocationListener{

    GoogleMap mGoogleMap;

    String path = "";
    double mLatitude=40.0017311;
    double mLongitude=-83.0196284;
    String key ="AIzaSyAg3ptiV1rlkHrijfSa0WVMOt2UJUnF7Ng"; // This is the browser key
    private String category;
    private int price;
    private int range;
    private List<String> placeArray;
    private List<String> addressArray;
    private List<LatLng> locationArray;
    private List<String> idArray;
    private int pointer;
    private String theRandomPlace;
    private boolean done_loading = false;
    /* Accelerometer stuff */
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 12) {
                View btn =	(Button)findViewById(R.id.btn_random);
                randomPlace(btn);
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    public void back_to_home_btn(View view)
    {
        Intent i = new Intent(getApplicationContext(),HomeScreen.class);
        startActivity(i);
    }

    public void randomPlace(View view)
    {
        if(done_loading) {
            if(placeArray != null && placeArray.size()== 0)
            {

                Toast.makeText(MainActivity.this, "Sorry, there are no restaurants that fix these parameters\n Please try again..", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(i);

            }
            else {
                Random random = new Random();
                pointer = (random.nextInt(placeArray.size()));
                if (placeArray.size() > 0) {
                    theRandomPlace = placeArray.get(pointer);
                    ((Button) view).setText(theRandomPlace);
                } else {
                    ((Button) view).setText("No places found :(");
                }
            }
        }
    }
    //  This is a button to ShowRestaurant.java
    public void moreInfo(View view){
        if(done_loading) {
            if(placeArray != null && placeArray.size()== 0)
            {
                Toast.makeText(MainActivity.this, "Sorry, there are no restaurants that fix these parameters\n Please try again..", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(i);
            }
            else {
                Button button = (Button) view;
                ((Button) view).setText("getting more info....");
                String id = idArray.get(pointer);
                String name = placeArray.get(pointer);
                String address = addressArray.get(pointer);
                LatLng location = locationArray.get(pointer);
                Intent i = new Intent(getApplicationContext(), ShowRestaurant.class);
                i.putExtra("id", String.valueOf(id));
                i.putExtra("name", String.valueOf(name));
                i.putExtra("address", String.valueOf(address));
                i.putExtra("location", String.valueOf(location));
                startActivity(i);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            category = extras.getString("category");
            price = extras.getInt("price");
            range = extras.getInt("range");
        }

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        placeArray = new Vector<String>();
        addressArray = new Vector<String>();
        locationArray = new Vector<LatLng>();
        idArray = new Vector<String>();

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Google Map
            mGoogleMap = fragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);



            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location="+mLatitude+","+mLongitude);
                    sb.append("&radius="+range);
                    sb.append("&types=restaurant|food|bakery|cafe");
                    if(price >= 0){ sb.append("&minprice="+price); sb.append("&maxprice=" + price);}
                    sb.append("&sensor=true");
                    if(category != null) if(category.compareTo("Any") != 0){ sb.append("&keyword=" + category);}
                    sb.append("&key="+key);

                    int index = sb.indexOf(" ");
                    if(index > 0)
                    {
                        sb.replace(index, index+1,"%20");
                    }

                    // Creating a new non-ui thread task to download Google place json data
                    PlacesTask placesTask = new PlacesTask();

                    if(hasNetworkConnection())
                    {
                        // Invokes the "doInBackground()" method of the class PlaceTask
                        placesTask.execute(sb.toString());
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Sorry, you are not connected to the internet...\nPlease connect your device and try again.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                        startActivity(i);
                    }
                }

    }


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Error  downloading url", e.toString());
        }finally{
            if(iStream != null){
                iStream.close();
                urlConnection.disconnect();
            }
        }

        return data;
    }


    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            mGoogleMap.clear();

            for(int i=0;i<list.size();i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                // Getting the type
                String type = hmPlace.get("type");

                // Getting the price_level
                String price = hmPlace.get("price");
                int price_level = Integer.parseInt(price);
                int user_price = Integer.parseInt(price);
                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                if (price_level <= user_price) {
                    // Placing a marker on the touched position
                   if(theRandomPlace != null && !theRandomPlace.equals(name))
                    {
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }
                    Marker marker = mGoogleMap.addMarker(markerOptions);
                    String id = marker.getId();
                    placeArray.add(name);
                    addressArray.add(vicinity);
                    locationArray.add(latLng);
                    idArray.add(id);
                }
            }
            done_loading = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnected = true;
        boolean isWifiAvailable = networkInfo.isAvailable();
        boolean isWifiConnected = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvailable = networkInfo.isAvailable();
        boolean isMobileConnected = networkInfo.isConnected();
        isConnected = (isMobileAvailable&&isMobileConnected) || (isWifiAvailable&&isWifiConnected);
        return isConnected;
    }
}