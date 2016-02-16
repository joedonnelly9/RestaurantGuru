package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;


public class SplashScreen extends Activity {

    private final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the next
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Next. */
            //    Intent mainIntent = new Intent(SplashScreen.this,HomeScreen.class);
                Intent mainIntent = new Intent(SplashScreen.this,HomeScreen.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DELAY);
    }
}
