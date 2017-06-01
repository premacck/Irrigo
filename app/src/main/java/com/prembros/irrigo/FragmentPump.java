package com.prembros.irrigo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPump extends Fragment {
    private static final String CROP_NAME = "cropName";
    private static final String AREA_OF_FIELD = "areaOfField";

    private String cropName;
    private int areaOfField;
    private String previousTitle;

    private ActionBar actionBar;

    private OnPumpInteractionListener mListener;

    public FragmentPump() {
        // Required empty public constructor
    }

    public static FragmentPump newInstance(String cropName, int areaOfField) {
        FragmentPump fragment = new FragmentPump();
        Bundle args = new Bundle();
        args.putString(CROP_NAME, cropName);
        args.putInt(AREA_OF_FIELD, areaOfField);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString(CROP_NAME);
            areaOfField = getArguments().getInt(AREA_OF_FIELD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pump, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.pump));
        }
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPumpInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPumpInteractionListener) {
            mListener = (OnPumpInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPumpInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
        mListener = null;
    }

    interface OnPumpInteractionListener {
        void onPumpInteraction(Uri uri);
    }
}
