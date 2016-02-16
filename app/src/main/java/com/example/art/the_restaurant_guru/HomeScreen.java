package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeScreen extends Activity {
    private Spinner first_spinner;
    private Spinner second_spinner;
    private Spinner third_spinner;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        first_spinner = (Spinner) findViewById(R.id.first_spinner);
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_spinner.setAdapter(catAdapter);

        second_spinner = (Spinner) findViewById(R.id.second_spinner);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.price_array, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second_spinner.setAdapter(priceAdapter);

        third_spinner = (Spinner) findViewById(R.id.third_spinner);
        ArrayAdapter<CharSequence> rangeAdapter = ArrayAdapter.createFromResource(this, R.array.range_array, android.R.layout.simple_spinner_item);
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        third_spinner.setAdapter(rangeAdapter);


        // Button click Listener
        addListenerOnButton();

    }

    //get the selected dropdown list value

    public void addListenerOnButton() {
        submit_btn = (Button) findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                intent.putExtra("category", String.valueOf(first_spinner.getSelectedItem()));

                int price = -1;
                String priceString = String.valueOf(second_spinner.getSelectedItem());
                switch (priceString){
                    case "$":
                        price = 0;
                        break;
                    case "$$":
                        price = 1;
                        break;
                    case "$$$":
                        price = 2;
                        break;
                    case "$$$$":
                        price = 3;
                        break;
                    case "$$$$$":
                        price = 4;
                        break;
                    case "Any":
                        price = -1;
                        break;
                    default:
                        Toast.makeText(HomeScreen.this,
                                "Price Error : " +
                                        "\n" + String.valueOf(second_spinner.getSelectedItem()) ,
                                Toast.LENGTH_LONG).show();
                }
                intent.putExtra("price", price);

                int range = 50000;
                String rangeString = String.valueOf(third_spinner.getSelectedItem());
                switch (rangeString){
                    case "1 Mile":
                        range = 1609;
                        break;
                    case "3 Miles":
                        range = 1609 * 3;
                        break;
                    case "5 Miles":
                        range = 1609 * 5;
                        break;
                    case "10 Miles":
                        range = 1609 * 10;
                        break;
                    case "Any":
                        range = 50000;
                        break;
                    default:
                        Toast.makeText(HomeScreen.this,
                                "Range Error : " +
                                        "\n" + String.valueOf(third_spinner.getSelectedItem()) ,
                                Toast.LENGTH_LONG).show();
                }
                intent.putExtra("range", range);
                if(hasNetworkConnection())
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(HomeScreen.this,"Sorry, you are not connected to the internet...\nPlease connect your device and try again.",Toast.LENGTH_LONG).show();
                }

            }

        });

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
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