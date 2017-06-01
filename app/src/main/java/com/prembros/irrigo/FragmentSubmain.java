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

public class FragmentSubmain extends Fragment {
    private static final String CROP_NAME = "cropName";
    private static final String TYPE_OF_SPRINKLER = "typeOfSprinkler";
    private static final String AREA_OF_FIELD = "areaOfField";

    private String cropName;
    private String typeOfSprinkler;
    private int areaOfField;
    private String previousTitle;

    private ActionBar actionBar;

    private OnSubmainInteractionListener mListener;

    public FragmentSubmain() {
        // Required empty public constructor
    }

    public static FragmentSubmain newInstance(String cropName, String typeOfSprinkler, int areaOfField) {
        FragmentSubmain fragment = new FragmentSubmain();
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
        View rootView = inflater.inflate(R.layout.fragment_submain, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.submain));
        }
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSubmainInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmainInteractionListener) {
            mListener = (OnSubmainInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSubmainInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
        mListener = null;
    }

    interface OnSubmainInteractionListener {
        void onSubmainInteraction(Uri uri);
    }
}
