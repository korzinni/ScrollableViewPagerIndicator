package com.example.korz.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    private static final String POSITION = "position";

    private int position;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(int position) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
        }
        Log.d("pagerFragmentState", "onCreate: " + position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("pagerFragmentState", "onCreateView: " + position);
        View view = inflater.inflate(R.layout.fragment_page_activity, container, false);
        View view1 = view.findViewById(R.id.frame);
        View view2 = view.findViewById(R.id.view1);
        View view3 = view.findViewById(R.id.view2);
        View view4 = view.findViewById(R.id.view3);
        view2.setScaleX(0.75f);
        view2.setScaleX(0.75f);
        view3.setScaleX(0.75f);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("pagerFragmentState", "onAttach: " + position);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("pagerFragmentState", "onStart: " + position);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("pagerFragmentState", "onResume: " + position);
    }

    @Override
    public void onPause() {
        Log.d("pagerFragmentState", "onPause: " + position);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("pagerFragmentState", "onStop: " + position);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("pagerFragmentState", "onDestroyView: " + position);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("pagerFragmentState", "onDestroy: " + position);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("pagerFragmentState", "onDetach: " + position);
        super.onDetach();
    }
}
