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

public class FragmentMainLine extends Fragment {
    private static final String CROP_NAME = "cropName";
    private static final String TYPE_OF_SPRINKLER = "typeOfSprinkler";
    private static final String AREA_OF_FIELD = "areaOfField";

    private String cropName;
    private String typeOfSprinkler;
    private int areaOfField;
    private String previousTitle;

    private ActionBar actionBar;

    private OnMainLineInteractionListener mListener;

    public FragmentMainLine() {
        // Required empty public constructor
    }

    public static FragmentMainLine newInstance(String cropName, String typeOfSprinkler, int areaOfField) {
        FragmentMainLine fragment = new FragmentMainLine();
        Bundle args = new Bundle();
        args.putString(CROP_NAME, cropName);
        args.putString(TYPE_OF_SPRINKLER, typeOfSprinkler);
        args.putInt(AREA_OF_FIELD, areaOfField);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString(CROP_NAME);
            typeOfSprinkler = getArguments().getString(TYPE_OF_SPRINKLER);
            areaOfField = getArguments().getInt(AREA_OF_FIELD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_line, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.main_line));
        }
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainLineInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainLineInteractionListener) {
            mListener = (OnMainLineInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainLineInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
        mListener = null;
    }

    interface OnMainLineInteractionListener {
        void onMainLineInteraction(Uri uri);
    }
}
