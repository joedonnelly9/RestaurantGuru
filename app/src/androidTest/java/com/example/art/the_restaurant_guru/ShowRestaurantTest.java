package com.example.art.the_restaurant_guru;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Lewis Sandoval on 4/19/2015.
 */
public class ShowRestaurantTest extends ActivityInstrumentationTestCase2<ShowRestaurant> {
    private ShowRestaurant mActivity;
    private TextView mTextView;

    public ShowRestaurantTest(){
        super(ShowRestaurant.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        mActivity = getActivity();
        mTextView = (TextView) mActivity.findViewById(R.id.restaurant_name);
    }

    public void testLandscapeOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "ShowRestaurantTest - testLandscapeOrientation()";
                CharSequence expected = "TEST";
                mTextView.setText(expected);
                Log.d(TAG, "Changing to Landscape orientation...");
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Log.d(TAG, "Finished!");
                CharSequence actual = mTextView.getText();
                assertEquals(expected, actual); //See if text rebuilds correctly
            }
        });
    }

    public void testPortraitOrientation() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "ShowRestaurantTest - testPortraitOrientation()";
                CharSequence expected = "TEST";
                mTextView.setText(expected);
                Log.d(TAG, "Changing to Portrait orientation...");
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                CharSequence actual = mTextView.getText();
                Log.d(TAG, "Finished!");
                assertEquals(expected, actual); //See if text rebuilds correctly
            }
        });
    }
}
