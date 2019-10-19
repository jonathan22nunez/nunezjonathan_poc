package com.example.nunezjonathan_poc.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ChildViewHolder> {

    private final List<Child> children;
    private final Context mContext;
    private ItemClickListener itemClickListener;
    private int lastPosition = -1;

    public class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView childImage_container;
        private final ImageView childImage, selectedChild;
        private final TextView childName;
        private final TextView childDob;

        ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            childImage_container = itemView.findViewById(R.id.childImage_container);
            childImage = itemView.findViewById(R.id.childImage);
            childName = itemView.findViewById(R.id.textView_childName);
            childDob = itemView.findViewById(R.id.textView_childDob);
            selectedChild = itemView.findViewById(R.id.selected_child);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!= null) {
                itemClickListener.onClick(itemView, getAdapterPosition());


            }
        }
    }

    public ChildRecyclerAdapter(Context context, List<Child> children) {
        this.mContext = context;
        this.children = children;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ChildViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        holder.selectedChild.setVisibility(View.GONE);

        SharedPreferences sharedPrefs = mContext.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        long childId = sharedPrefs.getLong("childId", -1);
        String childDocumentId = sharedPrefs.getString("childDocumentId", null);
        if (childId != -1) {
            if (childId == children.get(position)._id) holder.selectedChild.setVisibility(View.VISIBLE);
        } else if (childDocumentId != null) {
            if (childDocumentId.equals(children.get(position).documentId)) holder.selectedChild.setVisibility(View.VISIBLE);
        }

        if (!children.get(position).imageStringUri.isEmpty()) {
            holder.childImage_container.setVisibility(View.VISIBLE);
            holder.childImage.setImageURI(Uri.parse(children.get(position).imageStringUri));
        }
        holder.childName.setText(children.get(position).name);
        holder.childDob.setText(children.get(position).dob);

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    public List<Child> getChildren() {
        return children;
    }
}
