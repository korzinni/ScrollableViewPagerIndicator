package com.example.korz.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageFragment extends Fragment {

    private static final String ARG_COLOR = "color";

    private int color;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(int color) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_activity, container, false);
        View view1 = view.findViewById(R.id.frame);
        View view2 = view.findViewById(R.id.view1);
        View view3 = view.findViewById(R.id.view2);
        View view4 = view.findViewById(R.id.view3);
        view2.setScaleX(0.75f);
        view2.setScaleX(0.75f);
        view3.setScaleX(0.75f);
        view1.setBackgroundColor(color);
        return view;
    }

}
