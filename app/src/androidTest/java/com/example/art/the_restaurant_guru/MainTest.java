package com.example.art.the_restaurant_guru;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Lewis Sandoval on 4/19/2015.
 */
public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mMainActivity;
    private Button mButton;

    public MainTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        mMainActivity = getActivity();
        mButton = (Button) mMainActivity.findViewById(R.id.btn_random);
    }

    public void testLandscapeOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "MainTest - testLandscapeOrientation()";
                CharSequence expected = mButton.getText();
                Log.d(TAG, "Changing to Landscape orientation...");
                mMainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Log.d(TAG, "Finished!");
                CharSequence actual = mButton.getText();
                assertEquals(expected, actual); //See if text rebuilds correctly
            }
        });
    }

    public void testPortraitOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "MainTest - testPortraitOrientation()";
                CharSequence expected = mButton.getText();
                Log.d(TAG, "Changing to Portrait orientation...");
                mMainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                CharSequence actual = mButton.getText();
                Log.d(TAG, "Finished!");
                assertEquals(expected, actual); //See if text rebuilds correctly
            }
        });
    }
}
