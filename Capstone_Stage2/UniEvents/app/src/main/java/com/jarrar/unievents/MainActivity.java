package com.jarrar.unievents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements EventsFragment.Callback {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    FrameLayout mContainer;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newUserCheck();

        if (findViewById(R.id.event_detail_container) != null) {
            mTwoPane = true;
            mContainer = (FrameLayout) findViewById(R.id.event_detail_container);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.event_detail_container, new EventDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0f);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
    }

    private void newUserCheck() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("app_conf", MODE_PRIVATE);
        if (!sharedPreferences.contains("First_use")) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("First_use", false);
            edit.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!mTwoPane) {
            MenuItem item = menu.findItem(R.id.action_notifications);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra("reset", true);
            startActivity(intent);
        }
        if (id == R.id.action_notifications) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, new NotificationsFragment(), DETAILFRAGMENT_TAG)
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri uri) {
        if (mTwoPane) {

            if (mContainer.getVisibility() != View.VISIBLE)
                mContainer.setVisibility(View.VISIBLE);
            Bundle args = new Bundle();
            args.putParcelable(EventDetailFragment.DETAIL_URI, uri);

            EventDetailFragment fragment = new EventDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new EventsFragment();
            else return new NotificationsFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Events";
                case 1:
                    return "Notifications";
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }
}