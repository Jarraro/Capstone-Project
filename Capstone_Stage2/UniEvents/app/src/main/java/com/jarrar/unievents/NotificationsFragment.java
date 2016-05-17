package com.jarrar.unievents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jarrar.unievents.data.NotificationAdapter;
import com.jarrar.unievents.data.NotificationsDatabase;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 5/2/2016.
 */
public class NotificationsFragment extends Fragment {
    public ListView mListView;
    public ArrayList<String> arrayList = new ArrayList<>();
    public NotificationAdapter adapter;
    public NotificationsDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        TextView emptyView = (TextView) rootView.findViewById(R.id.textViewEmpty);
        mListView.setEmptyView(emptyView);
        db = new NotificationsDatabase(getActivity());
        arrayList = db.getNotifications();
        if (!arrayList.isEmpty()) {
            adapter = new NotificationAdapter(arrayList, getActivity());
            Log.d("Array length", String.valueOf(arrayList.size()));
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else
            emptyView.setVisibility(View.VISIBLE);
        return rootView;
    }
}