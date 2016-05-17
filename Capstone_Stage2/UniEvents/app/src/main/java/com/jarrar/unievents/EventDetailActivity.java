package com.jarrar.unievents;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by TOSHIBA on 5/9/2016.
 */
public class EventDetailActivity extends FragmentActivity {

    private String KEY = "data";
    private Parcelable mUri;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY, getIntent().getData());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY))
            mUri = savedInstanceState.getParcelable(KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        mUri = getIntent().getData();
        Bundle arguments = new Bundle();
        arguments.putParcelable(EventDetailFragment.DETAIL_URI, mUri);

        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(arguments);

        replaceFragment(fragment);


    }

    public void replaceFragment(Fragment frag) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null){
            FragmentTransaction t = manager.beginTransaction();
            Fragment currentFrag = manager.findFragmentById(R.id.event_detail_container);

            if (currentFrag != null && currentFrag.getClass().equals(frag.getClass())) {
                t.replace(R.id.event_detail_container, frag).commit();
            } else {
                t.replace(R.id.event_detail_container, frag).commit();
            }
        }
    }
}
