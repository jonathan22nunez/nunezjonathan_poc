package com.example.nunezjonathan_poc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.sql.Time;
import java.util.List;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    private Context mContext;
    private List<Event> eventsList;

    public OverviewAdapter(Context context, List<Event> eventsList) {
        this.mContext = context;
        this.eventsList = eventsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventIdentifier;
        TextView eventDatetime, eventDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventIdentifier = itemView.findViewById(R.id.imageView_eventIdentifier);
            eventDatetime = itemView.findViewById(R.id.textView_eventDatetime);
            eventDetails = itemView.findViewById(R.id.textView_eventDetails);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (eventsList.get(position).eventType) {
            case Event.EventType.SLEEP:
                holder.eventIdentifier.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_sleep, null));
                holder.eventDetails.setText(TimeUtils.timerHMS(eventsList.get(position).duration));
                break;
            case Event.EventType.NURSE:
            case Event.EventType.BOTTLE:
                holder.eventIdentifier.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bottle, null));
                holder.eventDetails.setText(TimeUtils.timerMS(eventsList.get(position).duration));
                break;
            case Event.EventType.WET:
                holder.eventDetails.setText("Wet");
            case Event.EventType.POOPY:
                holder.eventDetails.setText("Poopy");
            case Event.EventType.MIXED:
                holder.eventIdentifier.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_diaper, null));
                holder.eventDetails.setText("Mixed");
                break;
        }

        holder.eventDatetime.setText(CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(eventsList.get(position).datetime)));

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
