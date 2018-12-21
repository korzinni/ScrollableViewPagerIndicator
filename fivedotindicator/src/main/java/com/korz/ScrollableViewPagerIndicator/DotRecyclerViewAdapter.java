package com.korz.ScrollableViewPagerIndicator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.korz.ScrollableViewPagerIndicator.Dot.State;
import com.korz.ScrollableViewPagerIndicator.DotRecyclerViewAdapter.DotHolder;
import com.korz.ScrollableViewPagerIndicator.ViewPageIndicator.ViewParams;

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
        TextView textView = new TextView(viewGroup.getContext());
        textView.setGravity(Gravity.CENTER);
        dot.setState(State.INACTIVE_STATE);
        frameLayout.addView(dot, layoutParams);
        frameLayout.addView(textView, layoutParams);
        return new DotHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DotHolder dotHolder, int i) {
        dotHolder.textView.setText("" + i);
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
            this.textView = (TextView) viewGroup.getChildAt(1);
        }

        public Dot getDot() {
            return dot;
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
