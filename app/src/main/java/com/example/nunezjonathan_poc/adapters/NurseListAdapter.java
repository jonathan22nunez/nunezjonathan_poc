package com.example.nunezjonathan_poc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.List;

public class NurseListAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x018283;

    private final List<Event> feedingList;

    public NurseListAdapter(List<Event> feedingList) {
        this.feedingList = feedingList;
    }

    @Override
    public int getCount() {
        return feedingList.size();
    }

    @Override
    public Object getItem(int position) {
        if (feedingList.size() > 0 && position < feedingList.size()) {
            return feedingList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nurse_log_item, parent, false);
        }

        Event feedingEvent = (Event) getItem(position);
        ((TextView) convertView.findViewById(R.id.textView_duration)).setText(TimeUtils.timerMS(feedingEvent.duration));
        ((TextView) convertView.findViewById(R.id.textView_time))
                .setText(CalendarUtils.toDatetimeString(
                        CalendarUtils.stringToCalendar(feedingEvent.datetime).getTime()));
        String leftSubtext = "";
        String rightSubtext = "";
        if (feedingEvent.eventType == Event.EventType.NURSE) {
            leftSubtext = "Left: " + TimeUtils.timerMS(feedingEvent.leftSideDuration);
            rightSubtext = "Right: " + TimeUtils.timerMS(feedingEvent.rightSideDuration);
        } else if (feedingEvent.eventType == Event.EventType.BOTTLE) {
            leftSubtext = "Start Amount: " + feedingEvent.startAmount;
            rightSubtext = "End Amount: " + feedingEvent.endAmount;
        }
        ((TextView) convertView.findViewById(R.id.textView_left_subtext)).setText(leftSubtext);
        ((TextView) convertView.findViewById(R.id.textView_right_subtext)).setText(rightSubtext);

        return convertView;
    }
}
