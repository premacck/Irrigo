package com.prembros.irrigo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import static com.prembros.irrigo.MainActivity.nextValid;

public class FragmentCropSelection extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String CROP_NAME = "cropName";
    protected static long fieldArea = 0;
    protected static long totalWaterRequirement = 0;
    protected static String cropName;

    private Spinner spinnerCropName;
    private Spinner spinnerDefaultArea;
    private TextInputEditText dailyWaterRequirementTextView;
    private TextInputEditText manualAreaWidthTextView;
    private TextInputEditText manualAreaLengthTextView;
    private RadioButton selectAreaAuto;
    private RadioButton selectAreaManual;
    private TextView totalWaterRequirementTextView;
    private String manualLength = "";
    private String manualWidth = "";

    private String dailyWaterRequirement = "";
//    private String previousTitle;
    private DatabaseHolder db;

//    private OnCropSelectionInteractionListener mListener;

    public FragmentCropSelection() {
        // Required empty public constructor
    }

    public static FragmentCropSelection newInstance(String cropName) {
        FragmentCropSelection fragment = new FragmentCropSelection();
        Bundle args = new Bundle();
        args.putString(CROP_NAME, cropName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHolder(getContext());
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if (getArguments() != null) {
            cropName = getArguments().getString(CROP_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crop_selection, container, false);

        ((ScrollView) rootView.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
//            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.crop_selection));
        }

        initializeVariables(rootView);
        

        return rootView;
    }

    void initializeVariables(View rootView){
        spinnerCropName = (Spinner) rootView.findViewById(R.id.spinner_crop_name);
        dailyWaterRequirementTextView = (TextInputEditText) rootView.findViewById(R.id.daily_water_requirement);
        selectAreaAuto = (RadioButton) rootView.findViewById(R.id.radio_select_area_auto);
        spinnerDefaultArea = (Spinner) rootView.findViewById(R.id.spinner_default_area);
        selectAreaManual = (RadioButton) rootView.findViewById(R.id.radio_select_area_manual);
        manualAreaWidthTextView = (TextInputEditText) rootView.findViewById(R.id.manual_area_width);
        manualAreaLengthTextView = (TextInputEditText) rootView.findViewById(R.id.manual_area_length);
        totalWaterRequirementTextView = (TextView) rootView.findViewById(R.id.total_water_requirement);

        setListeners();
    }

    void setListeners(){
        spinnerCropName.setOnItemSelectedListener(this);
        selectAreaAuto.setOnClickListener(this);
        spinnerDefaultArea.setOnItemSelectedListener(this);
        selectAreaManual.setOnClickListener(this);

        addTextChangeListeners();
    }
    
    void addTextChangeListeners() {
        manualAreaLengthTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                manualLength = charSequence.toString();
                if (charSequence.toString().equals("")) {
                    updateTotalWaterRequirement(true);
                } else updateTotalWaterRequirement(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        
        manualAreaWidthTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                manualWidth = charSequence.toString();
                if (charSequence.toString().equals("")) {
                    updateTotalWaterRequirement(true);
                } else updateTotalWaterRequirement(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    void updateTotalWaterRequirement(boolean clearContents) {
        if (!clearContents) {
            new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    if (!manualLength.equals("") && !manualWidth.equals("")) {
                        try {
                            fieldArea = Integer.parseInt(manualLength) * (Integer.parseInt(manualWidth));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            fieldArea = 0;
                        }
                    }
                    if (fieldArea == 0) {
                        return null;
                    } else return fieldArea;
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    if (aLong == null) {
                        totalWaterRequirementTextView.setText(R.string.invalid_range);
                    } else {
                        if (dailyWaterRequirement.equals("") || aLong == 0) {
                            totalWaterRequirementTextView.setText("");
                            nextValid = false;
                        } else {
                            totalWaterRequirement = aLong * Integer.parseInt(dailyWaterRequirement);
                            totalWaterRequirementTextView.setText(String.valueOf(totalWaterRequirement));
                            nextValid = true;
                        }
                    }
                    super.onPostExecute(aLong);
                }
            }.execute();
        } else {
            totalWaterRequirementTextView.setText("");
            nextValid = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_select_area_auto:
                toggleRadio(view);
                break;
            case R.id.radio_select_area_manual:
                toggleRadio(view);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_crop_name:
                assignValueToDailyWaterRequirement();
                break;
//            case R.id.radio_select_area_auto:
//                toggleRadio(new View[] {view, spinnerDefaultArea, manualAreaWidthTextView, manualAreaLengthTextView});
//                break;
            case R.id.spinner_default_area:
                if (i > 0) selectDefaultArea(spinnerDefaultArea.getSelectedItem().toString());
                break;
//            case R.id.radio_select_area_manual:
//                toggleRadio(new View[] {view, spinnerDefaultArea, manualAreaWidthTextView, manualAreaLengthTextView});
//                break;
            default:
                break;
        }
    }

    void assignValueToDailyWaterRequirement() {
        cropName = spinnerCropName.getSelectedItem().toString();
        if (spinnerCropName.getSelectedItemPosition() == 0) {
            dailyWaterRequirementTextView.setText("");
        }
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    db.open();
                    dailyWaterRequirement = db.returnDailyWaterRequirement(cropName);
                    if (dailyWaterRequirement != null) {
                        dailyWaterRequirementTextView.setText(dailyWaterRequirement);
                        updateTotalWaterRequirement(false);
                    }
                }
            });
        }
    }
    
    void toggleRadio(View view) {
        if (spinnerDefaultArea != null && manualAreaWidthTextView != null && manualAreaLengthTextView != null) {
            switch (view.getId()) {
                case R.id.radio_select_area_auto:
                    if (!spinnerDefaultArea.isEnabled()) {
                        spinnerDefaultArea.setEnabled(true);
                        manualWidth = "";
                        manualLength = "";
                        fieldArea = 0;
                        manualAreaWidthTextView.setText("");
                        manualAreaLengthTextView.setText("");
                        manualAreaWidthTextView.setEnabled(false);
                        manualAreaLengthTextView.setEnabled(false);
                    }
                    break;
                case R.id.radio_select_area_manual:
                    if (spinnerDefaultArea.isEnabled()) {
                        spinnerDefaultArea.setEnabled(false);
                        spinnerDefaultArea.setSelection(0, true);
                        manualWidth = "";
                        manualLength = "";
                        fieldArea = 0;
                        manualAreaWidthTextView.setEnabled(true);
                        manualAreaLengthTextView.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
            updateTotalWaterRequirement(true);
        }
    }
    
    void selectDefaultArea(String areaString) {
        switch (areaString) {
            case "50 X 50":
                fieldArea = 2500;
                break;
            case "100 X 50":
                fieldArea = 5000;
                break;
            case "100 X 100":
                fieldArea = 10000;
                break;
            case "100 X 150":
                fieldArea = 15000;
                break;
            case "150 X 150":
                fieldArea = 22500;
                break;
            case "200 X 200":
                fieldArea = 40000;
                break;
            default:
                break;
        }
        updateTotalWaterRequirement(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    public void onButtonPressed(View view) {
//        if (mListener != null) {
//            mListener.onCropSelectionInteraction(new View[] {view});
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnCropSelectionInteractionListener) {
//            mListener = (OnCropSelectionInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnCropSelectionInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        actionBar.setTitle(previousTitle);
//        mListener = null;
//    }

//    interface OnCropSelectionInteractionListener {
//        void onCropSelectionInteraction(long totalWaterRequirement, long area);
//    }
}
