package com.korz.indicator;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.korz.indicator.Dot.State;
import com.korz.indicator.DotRecyclerViewAdapter.DotHolder;
import com.korz.indicator.ViewPageIndicator.ViewParams;

public class DotRecyclerViewAdapter extends RecyclerView.Adapter<DotHolder> {

    ViewParams params;
    int count;

    public DotRecyclerViewAdapter(ViewParams params) {
        this.params = params;
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void updateCount(int newCount) {
        int delta = newCount - this.count;
        this.count = newCount;
        notifyItemRangeInserted(newCount - count, delta);
    }

    @NonNull
    @Override
    public DotHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams =
                new LayoutParams(params.getActiveDotSize(), params.getActiveDotSize());
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.rightMargin = params.getDotMargin();
        layoutParams.leftMargin = params.getDotMargin();
        Dot dot = new Dot(context, params);
        dot.setState(State.OUT_SIDE_STATE);
        frameLayout.addView(dot, layoutParams);
//        TextView textView = new TextView(viewGroup.getContext());
//        textView.setGravity(Gravity.CENTER);
//        frameLayout.addView(textView, layoutParams);
        return new DotHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DotHolder dotHolder, int i) {
        //dotHolder.textView.setText("" + i);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class DotHolder extends ViewHolder {

        Dot dot;
        TextView textView;

        public DotHolder(@NonNull View itemView) {
            super(itemView);
            ViewGroup viewGroup = (ViewGroup) itemView;
            this.dot = (Dot) viewGroup.getChildAt(0);
            //this.textView = (TextView) viewGroup.getChildAt(1);
        }

        public Dot getDot() {
            return dot;
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
