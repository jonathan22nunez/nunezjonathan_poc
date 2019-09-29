package com.example.nunezjonathan_poc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Sleep;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import org.w3c.dom.Text;

import java.util.List;

public class SleepListAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x019283;

    private List<Sleep> sleepList;

    public SleepListAdapter(List<Sleep> sleepList) {
        this.sleepList = sleepList;
    }

    @Override
    public int getCount() {
        return sleepList.size();
    }

    @Override
    public Object getItem(int position) {
        if (sleepList.size() > 0 && position < sleepList.size()) {
            return sleepList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sleep_log_item, parent, false);
        }

        Sleep sleep = sleepList.get(position);
        ((TextView) convertView.findViewById(R.id.textView_sleep_duration)).setText(String.valueOf(sleep.duration));
        String startTime = CalendarUtils.toTimeHMSString(CalendarUtils.stringToCalendar(sleep.start_datetime));
        String endTime = sleep.getEndDatetime();
        String timeStartEnd = startTime + " - " + endTime;
        ((TextView) convertView.findViewById(R.id.textView_sleep_timeStartEnd)).setText(timeStartEnd);

        return convertView;
    }
}
