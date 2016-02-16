package com.example.art.the_restaurant_guru;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * Created by Lewis Sandoval on 4/17/2015.
 */
public class HomeTest extends ActivityInstrumentationTestCase2<HomeScreen>
{
    private HomeScreen mHomeScreen;

    public HomeTest(){
        super(HomeScreen.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        mHomeScreen = getActivity();
    }

    public void testLandscapeOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "HomeTest - testLandscapeOrientation()";
                Log.d(TAG, "Changing to Landscape orientation...");
                mHomeScreen.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Log.d(TAG, "Finished!");
                assertEquals(1, 1); //Just seeing if the app crashes
            }
        });
    }

    public void testPortraitOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "HomeTest - testPortraitOrientation()";
                Log.d(TAG, "Changing to Portrait orientation...");
                mHomeScreen.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Log.d(TAG, "Finished!");
                assertEquals(1, 1); //Just seeing if the app crashes
            }
        });
    }
}

