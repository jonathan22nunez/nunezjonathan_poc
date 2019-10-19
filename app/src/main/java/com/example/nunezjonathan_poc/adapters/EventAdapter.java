package com.example.nunezjonathan_poc.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Event> eventsList;
    private ItemClickListener itemClickListener;

    public EventAdapter(Context context, List<Event> eventsList) {
        this.mContext = context;
        this.eventsList = eventsList;
    }

    public EventAdapter(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    class SleepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView duration, timeStartEnd;

        public SleepViewHolder(@NonNull View itemView) {
            super(itemView);

            duration = itemView.findViewById(R.id.textView_duration);
            timeStartEnd = itemView.findViewById(R.id.textView_timeStartEnd);
            itemView.setOnClickListener(this);
        }

        public void setSleepViewHolder(Event event) {
            duration.setText(TimeUtils.timerHMS(event.duration));
            String startTime = CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(event.datetime));
            String endTime = CalendarUtils.getEndDatetime(event.datetime, event.duration);
            String time = startTime + " - " + endTime;
            timeStartEnd.setText(time);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!= null) {
                itemClickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    class FeedingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView duration, time, leftSubtext, rightSubtext;

        public FeedingViewHolder(@NonNull View itemView) {
            super(itemView);

            duration = itemView.findViewById(R.id.textView_duration);
            time = itemView.findViewById(R.id.textView_time);
            leftSubtext = itemView.findViewById(R.id.textView_left_subtext);
            rightSubtext = itemView.findViewById(R.id.textView_right_subtext);
            itemView.setOnClickListener(this);
        }

        public void setFeedingViewHolder(Event event) {
            duration.setText(TimeUtils.timerMS(event.duration));
            time.setText(CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(event.datetime)));
            String leftText = "";
            String rightText = "";
            if (event.eventType == Event.EventType.NURSE) {
                leftText = "Left: " + TimeUtils.timerMS(event.leftSideDuration);
                rightText = "Right: " + TimeUtils.timerMS(event.rightSideDuration);
            } else if (event.eventType == Event.EventType.BOTTLE) {
                leftText = "Start Amount: " + event.startAmount;
                rightText = "End Amount: " + event.endAmount;
            }
            leftSubtext.setText(leftText);
            rightSubtext.setText(rightText);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!= null) {
                itemClickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    class DiaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView type, time, hardness;
        ImageView color;

        public DiaperViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.textView_diaperType);
            time = itemView.findViewById(R.id.textView_time);
            color = itemView.findViewById(R.id.imageView_diaper_color);
            hardness = itemView.findViewById(R.id.textView_hardness);
            itemView.setOnClickListener(this);
        }

        public void setDiaperViewHolder(Event event) {
            switch (event.eventType) {
                case Event.EventType.WET:
                    type.setText("Wet");
                    break;
                case Event.EventType.POOPY:
                    type.setText("Poopy");
                    break;
                case Event.EventType.MIXED:
                    type.setText("Mixed");
                    break;
            }

            time.setText(CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(event.datetime)));
            if (event.color != Event.Color.NONE) {
                color.setImageDrawable(mContext.getResources().getDrawable(event.color, null));
            }
            hardness.setText(event.hardness);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!= null) {
                itemClickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    public void removeItem(int position) {
        eventsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, eventsList.size());
    }
    public void restoreItem(Event event, int position) {
        eventsList.add(position, event);
        // notify item added by position
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Event.EventType.SLEEP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sleep_log_item, parent, false);
                return new SleepViewHolder(view);
            case Event.EventType.NURSE:
            case Event.EventType.BOTTLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nurse_log_item, parent, false);
                return new FeedingViewHolder(view);
            case Event.EventType.WET:
            case Event.EventType.POOPY:
            case Event.EventType.MIXED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diaper_log_item, parent, false);
                return new DiaperViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Event.EventType.SLEEP:
                ((SleepViewHolder) holder).setSleepViewHolder(eventsList.get(position));
                break;
            case Event.EventType.NURSE:
            case Event.EventType.BOTTLE:
                ((FeedingViewHolder) holder).setFeedingViewHolder(eventsList.get(position));
                break;
            case Event.EventType.WET:
            case Event.EventType.POOPY:
            case Event.EventType.MIXED:
                ((DiaperViewHolder) holder).setDiaperViewHolder(eventsList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return eventsList.get(position).eventType;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
