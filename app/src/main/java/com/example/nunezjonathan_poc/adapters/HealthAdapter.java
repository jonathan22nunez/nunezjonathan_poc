package com.example.nunezjonathan_poc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.HealthViewHolder> {

    private final List<Health> healthList;
    private ItemClickListener itemClickListener;

    public HealthAdapter(List<Health> healthList) {
        this.healthList = healthList;
    }

    class HealthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView healthType;
        TextView healthDescription;

        public HealthViewHolder(@NonNull View itemView) {
            super(itemView);

            healthType = itemView.findViewById(R.id.textView_childName);
            healthDescription = itemView.findViewById(R.id.textView_childDob);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!= null) {
                itemClickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    public void removeItem(int position) {
        healthList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, healthList.size());
    }
    public void restoreItem(Health health, int position) {
        healthList.add(position, health);
        // notify item added by position
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public HealthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new HealthViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthViewHolder holder, int position) {
        holder.healthDescription.setText(healthList.get(position).toString());

        String datetime = CalendarUtils.toDatetimeString(CalendarUtils.stringToCalendar(healthList.get(position).datetime).getTime());
        holder.healthType.setText(datetime);
    }

    @Override
    public int getItemCount() {
        return healthList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
