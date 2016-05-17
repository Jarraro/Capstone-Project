package com.jarrar.unievents.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarrar.unievents.R;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 5/8/2016.
 */
public class NotificationAdapter extends BaseAdapter {

    ArrayList<String> notificationsArray = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    NotificationsDatabase db;

    public NotificationAdapter(ArrayList<String> notifications, Context baseContext) {
        notificationsArray = notifications;
        context = baseContext;
    }

    @Override
    public int getCount() {
        return notificationsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_item_notification, parent, false);
        TextView textNotification = (TextView) row.findViewById(R.id.textViewNotification);
        ImageView btnDelete = (ImageView) row.findViewById(R.id.imageDelete);
        btnDelete.setTag(position);
        textNotification.setText(notificationsArray.get(position));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new NotificationsDatabase(context);
                Integer index = (Integer) v.getTag();
                db.deleteNotification(notificationsArray.get(index.intValue()).toString());
                notificationsArray.remove(index.intValue());
                db.close();
                notifyDataSetChanged();
            }
        });
        return (row);
    }
}