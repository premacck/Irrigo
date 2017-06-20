package com.prembros.irrigo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Spinner;

import static com.prembros.irrigo.FragmentCropSelection.totalWaterRequirement;
import static com.prembros.irrigo.FragmentSprinkler.discharge;
import static com.prembros.irrigo.FragmentSprinkler.totalNumberOfSprinklers;

public class FragmentLateral extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextInputEditText numberOfFieldSectionEditText;
    private TextInputEditText sectionalFlowEditText;
    private TextInputEditText sprinklerInEachSectionEditText;
    private Spinner spinnerSelectLateralSize;
    private TextInputEditText lengthEditText;
    private TextInputEditText dischargeEditText;
    private TextInputEditText headlossEditText;
    private TextInputEditText lateralLengthEditText;
    private TextInputEditText lateralDiameterEditText;

    private String cropName;
    private String typeOfSprinkler;
    private long areaOfField;
    private double headloss;

    protected static float sectionalFlow;
    protected static double innerDiameter;

    private long numberOfFieldSection;
    private float sprinklerInEachSection;
    private int length;

    private String previousTitle;

    private ActionBar actionBar;

//    private OnLateralInteractionListener mListener;

    public FragmentLateral() {
        // Required empty public constructor
    }

    public static FragmentLateral newInstance(String cropName, String typeOfSprinkler, long fieldArea) {
        FragmentLateral fragment = new FragmentLateral();
        Bundle args = new Bundle();
        args.putString("cropName", cropName);
        args.putString("typeOfSprinkler", typeOfSprinkler);
        args.putLong("areaOfField", fieldArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString("cropName");
            typeOfSprinkler = getArguments().getString("typeOfSprinkler");
            areaOfField = getArguments().getLong("areaOfField");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lateral, container, false);

        ((ScrollView) rootView.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);

        initializeVariables(rootView);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.lateral));
        }

        return rootView;
    }

    private void initializeVariables(View rootView) {
        numberOfFieldSectionEditText = (TextInputEditText) rootView.findViewById(R.id.num_of_field_section);
        sectionalFlowEditText = (TextInputEditText) rootView.findViewById(R.id.sectional_flow);
        sprinklerInEachSectionEditText = (TextInputEditText) rootView.findViewById(R.id.sprinkler_in_each_section);
        spinnerSelectLateralSize = (Spinner) rootView.findViewById(R.id.select_lateral_size);
        lengthEditText = (TextInputEditText) rootView.findViewById(R.id.enter_length);
        dischargeEditText = (TextInputEditText) rootView.findViewById(R.id.discharge);
        headlossEditText = (TextInputEditText) rootView.findViewById(R.id.headloss);
        lateralLengthEditText = (TextInputEditText) rootView.findViewById(R.id.lateral_length);
        lateralDiameterEditText = (TextInputEditText) rootView.findViewById(R.id.lateral_diameter);

        setListeners();
    }

    private void setListeners() {
        spinnerSelectLateralSize.setOnItemSelectedListener(this);

        numberOfFieldSectionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    final String charSequence = editable.toString();
                    new AsyncTask<Void, Void, Float[]>() {
                        @Override
                        protected Float[] doInBackground(Void... voids) {
                            Float[] result = new Float[2];

                            try {
                                numberOfFieldSection = Integer.parseInt(charSequence);
                            } catch (NumberFormatException e) {
                                Log.e("ERROR: ", e.getMessage());
                                return null;
                            }
                            sectionalFlow = (float)totalWaterRequirement / numberOfFieldSection;
                            sprinklerInEachSection = totalNumberOfSprinklers * discharge;

                            result[0] = sectionalFlow;
                            result [1] = sprinklerInEachSection;
                            return result;
                        }

                        @Override
                        protected void onPostExecute(Float[] floats) {
                            if (floats != null) {
                                sectionalFlowEditText.setText(String.valueOf(floats[0]));
                                sprinklerInEachSectionEditText.setText(String.valueOf(floats[1]));
                            } else {
                                sectionalFlowEditText.setText(R.string.invalid_value);
                                sprinklerInEachSectionEditText.setText("");
                            }
                            super.onPostExecute(floats);
                        }
                    }.execute();
                } else {
                    numberOfFieldSection = 0;
                    sectionalFlow = 0;
                    sectionalFlowEditText.setText("");
                    sprinklerInEachSectionEditText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        lengthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    try {
                        length = Integer.parseInt(editable.toString());
                    } catch (NumberFormatException e) {
                        Log.e("ERROR: ", e.getMessage());
                        length = 0;
                    }
                    new AsyncTask<Void, Void, Double>() {
                        @Override
                        protected Double doInBackground(Void... voids) {
                            return 1.21 * Math.pow(10, 12)
                                    * Math.pow(((float) discharge / 150), 1.857)
                                    * Math.pow(innerDiameter, -4.871) * 0.36 * length;
                        }

                        @Override
                        protected void onPostExecute(Double aDouble) {
                            if (aDouble != 0) {
                                headloss = aDouble;
                                headlossEditText.setText(String.valueOf(headloss));
                                lateralLengthEditText.setText(String.valueOf(length));
                                lateralDiameterEditText.setText(String.valueOf(innerDiameter));
                            } else {
                                headlossEditText.setText(R.string.invalid_value);
                                lateralLengthEditText.setText("");
                                lateralDiameterEditText.setText("");
                            }
                            super.onPostExecute(aDouble);
                        }
                    }.execute();
                } else {
                    headloss = 0;
                    headlossEditText.setText("");
                    lateralLengthEditText.setText("");
                    lateralDiameterEditText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            int outerDiameter;
            try {
            outerDiameter = Integer.parseInt(adapterView.getSelectedItem().toString());
            } catch (NumberFormatException e) {
                Log.e("ERROR: ", e.getMessage());
                outerDiameter = 0;
            }
            final int finalOuterDiameter = outerDiameter;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    DatabaseHolder db = new DatabaseHolder(getContext());
                    db.open();
                    innerDiameter = db.returnPipeInnerDiameter(finalOuterDiameter);
                    db.close();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    dischargeEditText.setText(String.valueOf(discharge));
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onLateralFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnLateralInteractionListener) {
//            mListener = (OnLateralInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnLateralInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
////        actionBar.setTitle(previousTitle);
//        mListener = null;
//    }

//    interface OnLateralInteractionListener {
//        void onLateralFragmentInteraction(Uri uri);
//    }
}
