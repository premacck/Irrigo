package com.prembros.irrigo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import static com.prembros.irrigo.FragmentCropSelection.totalWaterRequirement;
import static com.prembros.irrigo.FragmentMainLine.newDischargeMainLine;
import static com.prembros.irrigo.FragmentSprinkler.totalFlowOfControl;

public class FragmentPump extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private String cropName;
    private long fieldArea;
    private String previousTitle;

    private TextInputEditText suctionPlusDeliveryHeadEditText;
    private CheckBox primaryFilter;
    private CheckBox discFilter;
    private CheckBox ventury;
    private TextInputEditText horsePowerEditText;
    private TextInputEditText irrigationTimeEditText;

    private int H = 0;
    private int primaryFilterValue = 0;
    private int discFilterValue = 0;
    private int venturyValue = 0;
//    private OnPumpInteractionListener mListener;

    public FragmentPump() {
        // Required empty public constructor
    }

    public static FragmentPump newInstance(String cropName, long fieldArea) {
        FragmentPump fragment = new FragmentPump();
        Bundle args = new Bundle();
        args.putString("cropName", cropName);
        args.putLong("fieldArea", fieldArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString("cropName");
            fieldArea = getArguments().getLong("fieldArea");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pump, container, false);

        ((ScrollView) rootView.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.pump));
        }

        initializeVariables(rootView);

        return rootView;
    }

    private void initializeVariables(View rootView) {
        suctionPlusDeliveryHeadEditText = (TextInputEditText) rootView.findViewById(R.id.suction_delivery_head);
        primaryFilter = (CheckBox) rootView.findViewById(R.id.check_primary_filter);
        discFilter = (CheckBox) rootView.findViewById(R.id.check_disc_filter);
        ventury = (CheckBox) rootView.findViewById(R.id.check_ventury);
        horsePowerEditText = (TextInputEditText) rootView.findViewById(R.id.pump_horse_power);
        irrigationTimeEditText = (TextInputEditText) rootView.findViewById(R.id.irrigation_time);

        setListeners();
    }

    private void setListeners() {
        primaryFilter.setOnCheckedChangeListener(this);
        discFilter.setOnCheckedChangeListener(this);
        ventury.setOnCheckedChangeListener(this);

        suctionPlusDeliveryHeadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    H = Integer.parseInt(editable.toString());
                    findHorsePowerAndIrrigationTime();
                } else {
                    horsePowerEditText.setText("");
                    irrigationTimeEditText.setText("");
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            switch (compoundButton.getId()) {
                case R.id.check_primary_filter:
//                    primaryFilterValue ++;
                    H += 1;
                    findHorsePowerAndIrrigationTime();
                    break;
                case R.id.check_disc_filter:
//                    discFilterValue += 2;
                    H += 2;
                    findHorsePowerAndIrrigationTime();
                    break;
                case R.id.check_ventury:
//                    venturyValue += 3;
                    H += 3;
                    findHorsePowerAndIrrigationTime();
                    break;
                default:
                    break;
            }
        } else {
            switch (compoundButton.getId()) {
                case R.id.check_primary_filter:
//                    primaryFilterValue --;
                    H -= 1;
                    findHorsePowerAndIrrigationTime();
                    break;
                case R.id.check_disc_filter:
//                    discFilterValue -= 2;
                    H -= 2;
                    findHorsePowerAndIrrigationTime();
                    break;
                case R.id.check_ventury:
//                    venturyValue -= 3;
                    H -= 3;
                    findHorsePowerAndIrrigationTime();
                    break;
                default:
                    break;
            }
        }
    }

    private void findHorsePowerAndIrrigationTime(){
//        H += primaryFilterValue + discFilterValue + venturyValue;
        new AsyncTask<Object, Object, Double[]>() {
            @Override
            protected Double[] doInBackground(Object... voids) {
                if (H > 0) {
                    return new Double[]{((
                            newDischargeMainLine * H) / 0.6375),                            /*HORSE POWER*/
                            (totalWaterRequirement / totalFlowOfControl)};                  /*IRRIGATION TIME*/
                } else return null;
            }

            @Override
            protected void onPostExecute(Double[] doubles) {
                if (doubles != null) {
                    horsePowerEditText.setText(String.valueOf(doubles[0]));
                    irrigationTimeEditText.setText(String.valueOf(doubles[1]));
                } else {
                    horsePowerEditText.setText("");
                    irrigationTimeEditText.setText("");
                }
                super.onPostExecute(doubles);
            }
        }.execute();
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onPumpInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnPumpInteractionListener) {
//            mListener = (OnPumpInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnPumpInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
////        actionBar.setTitle(previousTitle);
//        mListener = null;
//    }

//    interface OnPumpInteractionListener {
//        void onPumpInteraction(Uri uri);
//    }
}
