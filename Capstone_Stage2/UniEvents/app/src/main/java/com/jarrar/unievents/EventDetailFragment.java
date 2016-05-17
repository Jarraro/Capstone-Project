package com.jarrar.unievents;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jarrar.unievents.data.EventLoader;
import com.jarrar.unievents.data.EventsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TOSHIBA on 5/9/2016.
 */
public class EventDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDateView;
    private TextView mTargetView;
    private TextView mLocationView;
    private TextView mDescriptionView;

    String imageURL;
    String title;
    String date;
    String target;
    String location;
    String description;

    int PHOTO_URL = 1;
    int TITLE = 3;
    int DESCRIPTION = 4;
    int DATE = 5;
    int TARGET = 6;
    int LOCATION = 7;

    private static final String[] DETAIL_COLUMNS = {
            EventsContract.Events._ID,
            EventsContract.Events.PHOTO_URL,
            EventsContract.Events.TITLE,
            EventsContract.Events.DESCRIPTON,
            EventsContract.Events.DATE,
            EventsContract.Events.TARGET,
            EventsContract.Events.LOCATION,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }
        if(getLoaderManager().getLoader(0) == null)
        getLoaderManager().initLoader(0, null, this);

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);
        mImageView = (ImageView) root.findViewById(R.id.photo);
        mTitleView = (TextView) root.findViewById(R.id.textViewTitle);
        mDateView = (TextView) root.findViewById(R.id.textViewDate);
        mTargetView = (TextView) root.findViewById(R.id.textViewTarget);
        mLocationView = (TextView) root.findViewById(R.id.textViewLocation);
        mDescriptionView = (TextView) root.findViewById(R.id.textViewDescription);
        Button mCalenderButton = (Button) root.findViewById(R.id.buttonSave);
        Button mShareButton = (Button) root.findViewById(R.id.buttonShare);
        mCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveToCalender();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareEvent();
            }
        });

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return EventLoader.loadFromUri(getActivity(), mUri);
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            imageURL = data.getString(PHOTO_URL);
            title = data.getString(TITLE);
            date = data.getString(DATE);
            target = data.getString(TARGET);
            location = data.getString(LOCATION);
            description = data.getString(DESCRIPTION);
            Glide.with(getActivity()).load(imageURL)
                    .into(mImageView);
            mTitleView.setText(title);
            mDateView.setText(date);
            mTargetView.setText(target);
            mDescriptionView.setText(description);
            mLocationView.setText(location);
            data.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void shareEvent() {
        String shareBody = getActivity().getString(R.string.share_text_title)
                + title
                + getActivity().getString(R.string.share_text_date) + date
                + getActivity().getString(R.string.share_text_location) + location;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    public void saveToCalender() throws ParseException {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d = f.parse(date);
        long milliseconds = d.getTime();
        intent.putExtra("beginTime", milliseconds);
        intent.putExtra("allDay", true);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("eventLocation", location);
        startActivity(intent);
    }
}
