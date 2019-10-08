package com.example.nunezjonathan_poc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.List;

public class DiaperListAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x016283;

    private List<Event> diaperList;

    public DiaperListAdapter(List<Event> diaperList) {
        this.diaperList = diaperList;
    }

    @Override
    public int getCount() {
        return diaperList.size();
    }

    @Override
    public Object getItem(int position) {
        if (diaperList.size() > 0 && position < diaperList.size()) {
            return diaperList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diaper_log_item, parent, false);
        }

        Event diaperEvent = (Event) getItem(position);
        switch (diaperEvent.eventType) {
            case Event.EventType.WET:
                ((TextView) convertView.findViewById(R.id.textView_diaperType)).setText("Wet");
                break;
            case Event.EventType.POOPY:
                ((TextView) convertView.findViewById(R.id.textView_diaperType)).setText("Poopy");
                break;
            case Event.EventType.MIXED:
                ((TextView) convertView.findViewById(R.id.textView_diaperType)).setText("Mixed");
                break;
        }

        ((TextView) convertView.findViewById(R.id.textView_time)).setText(CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(diaperEvent.datetime)));
        if (diaperEvent.color != Event.Color.NONE) {
            ((ImageView) convertView.findViewById(R.id.imageView_diaper_color)).setImageDrawable(parent.getResources().getDrawable(diaperEvent.color, null));
        }
        ((TextView) convertView.findViewById(R.id.textView_hardness)).setText(diaperEvent.hardness);

        return convertView;
    }
}
