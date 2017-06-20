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

import static com.prembros.irrigo.FragmentLateral.sectionalFlow;

public class FragmentMainLine extends Fragment implements AdapterView.OnItemSelectedListener {

    private String cropName;
    private String selectedSprinkler;
    private long fieldArea;
    private String previousTitle;

    private TextInputEditText totalSectionsRunAtATimeEditText;
    private TextInputEditText dischargeEditText;
    private Spinner spinnerSelectMainLineSize;
    private TextInputEditText enterLengthEditText;
    private TextInputEditText headlossEditText;
    private TextInputEditText mainLineLengthEditText;
    private TextInputEditText mainLineDiameterEditText;

    protected  static float newDischargeMainLine = 0;

    private int totalSectionsRunAtATime;
    private float discharge;
    private int mainLineSize;
    private int length;
    private float innerDiameter;

//    private OnMainLineInteractionListener mListener;

    public FragmentMainLine() {
        // Required empty public constructor
    }

    public static FragmentMainLine newInstance(String cropName, String typeOfSprinkler, long fieldArea) {
        FragmentMainLine fragment = new FragmentMainLine();
        Bundle args = new Bundle();
        args.putString("cropName", cropName);
        args.putString("selectedSprinkler", typeOfSprinkler);
        args.putLong("fieldArea", fieldArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropName = getArguments().getString("cropName");
            selectedSprinkler = getArguments().getString("selectedSprinkler");
            fieldArea = getArguments().getLong("fieldArea");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_line, container, false);

        ((ScrollView) rootView.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.main_line));
        }

        initializeVariables(rootView);

        return rootView;
    }

    private void initializeVariables(View rootView) {
        totalSectionsRunAtATimeEditText = (TextInputEditText) rootView.findViewById(R.id.total_sections_run_at_time);
        dischargeEditText = (TextInputEditText) rootView.findViewById(R.id.discharge);
        spinnerSelectMainLineSize = (Spinner) rootView.findViewById(R.id.select_main_line_size);
        enterLengthEditText = (TextInputEditText) rootView.findViewById(R.id.enter_length);
        headlossEditText = (TextInputEditText) rootView.findViewById(R.id.headloss);
        mainLineLengthEditText = (TextInputEditText) rootView.findViewById(R.id.main_line_length);
        mainLineDiameterEditText = (TextInputEditText) rootView.findViewById(R.id.main_line_diameter);

        setListeners();
    }

    private void setListeners() {
        spinnerSelectMainLineSize.setOnItemSelectedListener(this);

        totalSectionsRunAtATimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    try {
                        totalSectionsRunAtATime = Integer.parseInt(editable.toString());
                        newDischargeMainLine = totalSectionsRunAtATime * sectionalFlow;
                    } catch (NumberFormatException e) {
                        Log.e("ERROR: ", e.getMessage());
                        totalSectionsRunAtATime = 0;
                    }
                    if (newDischargeMainLine > 0) {
                        dischargeEditText.setText(String.valueOf(newDischargeMainLine));
                    } else dischargeEditText.setText(R.string.invalid_value);
                } else dischargeEditText.setText("");
            }
        });

        enterLengthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                            return 1.21 * Math.pow(10, 10)
                                    * Math.pow((newDischargeMainLine / 150), 1.857)
                                    * Math.pow(innerDiameter, -4.871) * 0.36 * length;
                        }

                        @Override
                        protected void onPostExecute(Double aDouble) {
                            if (aDouble != 0) {
                                headlossEditText.setText(String.valueOf(aDouble));
                                mainLineLengthEditText.setText(String.valueOf(length));
                            } else {
                                headlossEditText.setText(R.string.invalid_value);
                                mainLineLengthEditText.setText("");
                            }
                            super.onPostExecute(aDouble);
                        }
                    }.execute();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
//                    dischargeEditText.setText(String.valueOf(newDischargeMainLine));
                    mainLineDiameterEditText.setText(String.valueOf(innerDiameter));
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else mainLineDiameterEditText.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onMainLineInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnMainLineInteractionListener) {
//            mListener = (OnMainLineInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnMainLineInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
////        actionBar.setTitle(previousTitle);
//        mListener = null;
//    }

//    interface OnMainLineInteractionListener {
//        void onMainLineInteraction(Uri uri);
//    }
}
