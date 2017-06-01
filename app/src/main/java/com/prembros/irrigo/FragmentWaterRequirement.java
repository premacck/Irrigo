package com.prembros.irrigo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Spinner;

public class FragmentWaterRequirement extends Fragment {

    private static final String CROP_NAME = "cropName";

    private Spinner spinnerCropName;
    private Spinner spinnerDefaultArea;
    private TextInputEditText peakWaterRequirementTextView;
    private TextInputEditText manualAreaWidthTextView;
    private TextInputEditText manualAreaLengthTextView;
    private RadioButton selectAreaAuto;
    private RadioButton selectAreaManual;

    private String cropName;
    private String sprinklerName;
    private int fieldArea;
    private String previousTitle;

    private ActionBar actionBar;

    private OnWaterRequirementInteractionListener mListener;

    public FragmentWaterRequirement() {
        // Required empty public constructor
    }

    public static FragmentWaterRequirement newInstance(String cropName) {
        FragmentWaterRequirement fragment = new FragmentWaterRequirement();
        Bundle args = new Bundle();
        args.putString(CROP_NAME, cropName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString(CROP_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_water_requirement, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.water_requirement));
        }
        spinnerCropName = (Spinner) rootView.findViewById(R.id.spinner_crop_name);
        spinnerDefaultArea = (Spinner) rootView.findViewById(R.id.spinner_default_area);
        peakWaterRequirementTextView = (TextInputEditText) rootView.findViewById(R.id.peak_water_requirement);
        selectAreaAuto = (RadioButton) rootView.findViewById(R.id.radio_select_area_auto);
        selectAreaManual = (RadioButton) rootView.findViewById(R.id.radio_select_area_manual);
        manualAreaWidthTextView = (TextInputEditText) rootView.findViewById(R.id.manual_area_width);
        manualAreaLengthTextView = (TextInputEditText) rootView.findViewById(R.id.manual_area_length);
        selectAreaAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onWaterRequirementInteraction(new View[] {view, spinnerDefaultArea, manualAreaWidthTextView, manualAreaLengthTextView});
            }
        });
        selectAreaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onWaterRequirementInteraction(new View[] {view, spinnerDefaultArea, manualAreaWidthTextView, manualAreaLengthTextView});
            }
        });
        return rootView;
    }

    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onWaterRequirementInteraction(new View[] {view});
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWaterRequirementInteractionListener) {
            mListener = (OnWaterRequirementInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWaterRequirementInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
        mListener = null;
    }

    interface OnWaterRequirementInteractionListener {
        void onWaterRequirementInteraction(View[] views);
    }
}
