package com.example.nunezjonathan_poc.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.google.android.material.snackbar.Snackbar;

public abstract class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    public interface SwipeCallBackListener {
        void deleteItem();
    }

    private Activity mActivity;
    private SwipeCallBackListener mListener;
    private Paint p = new Paint();

    public SwipeCallback(Fragment fragment, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);

        this.mActivity = fragment.getActivity();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

//            if(dX <= 0) {
//                p.setColor(Color.parseColor("#388E3C"));
//                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
//                c.drawRect(background, p);
//                icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_delete_forever);
//                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
//                c.drawBitmap(icon, null, icon_dest, p);
//            }
            if (dX <= 0) {
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_delete_forever);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }
        }
    }
}
