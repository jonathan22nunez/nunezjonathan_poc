package com.example.nunezjonathan_poc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;

import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private static final int BASE_ID = 0x02918348;

    private List<Event> eventList;

    public EventListAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return eventList.get(position).eventType;
    }

    @Override
    public Object getItem(int position) {
        if (eventList.size() > 0 && position < eventList.size()) {
            return eventList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SleepViewHolder holder = new SleepViewHolder();
        int eventType = getItemViewType(position);

        if (convertView == null) {
            if (eventType == Event.EventType.SLEEP) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sleep_log_item, parent, false);
                holder = new SleepViewHolder();
                holder.duration = convertView.findViewById(R.id.textView_sleep_duration);

                convertView.setTag(holder);
            }
        } else {
            holder = (SleepViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);

        holder.duration.setText(String.valueOf(event.duration));


        return convertView;
    }

    private class SleepViewHolder {

        TextView duration, datetime;
    }
}
