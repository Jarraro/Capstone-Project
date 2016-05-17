package com.jarrar.unievents;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jarrar.unievents.data.EventLoader;
import com.jarrar.unievents.data.EventsContract;
import com.jarrar.unievents.data.UpdaterService;

/**
 * Created by TOSHIBA on 5/1/2016.
 */
public class EventsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private boolean mIsRefreshing = false;
    Callback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        if(getLoaderManager() != null)
        getLoaderManager().initLoader(0, null, this);
        if (savedInstanceState == null) {
            refresh();
        }
        return rootView;
    }

    private void refresh() {
        getActivity().startService(new Intent(getActivity(), UpdaterService.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mRefreshingReceiver);
    }

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return EventLoader.newAllEventsInstance(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Adapter adapter = new Adapter(data);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(EventLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_event, parent, false);
            final ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemSelected(EventsContract.Events.buildEventUri(getItemId(vh.getAdapterPosition())));
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            Glide.with(getActivity()).load(mCursor.getString(EventLoader.Query.PHOTO_URL))
                    .into(holder.imageView);
            holder.titleView.setText(mCursor.getString(EventLoader.Query.TITLE));
            holder.dateView.setText(mCursor.getString(EventLoader.Query.DATE));
            holder.targetView.setText(getActivity().getString(R.string.event_major) + mCursor.getString(EventLoader.Query.TARGET));
            holder.locationView.setText(getActivity().getString(R.string.event_location) + mCursor.getString(EventLoader.Query.LOCATION));
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public ImageView imageView;
        public TextView dateView;
        public TextView targetView;
        public TextView locationView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageViewEvent);
            titleView = (TextView) view.findViewById(R.id.textViewTitle);
            dateView = (TextView) view.findViewById(R.id.textViewDate);
            targetView = (TextView) view.findViewById(R.id.textViewMajor);
            locationView = (TextView) view.findViewById(R.id.textViewLocation);
        }
    }

    public interface Callback {
        void onItemSelected(Uri uri);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }
}
