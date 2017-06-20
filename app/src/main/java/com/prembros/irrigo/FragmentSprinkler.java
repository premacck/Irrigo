package com.prembros.irrigo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.prembros.irrigo.FragmentCropSelection.fieldArea;
import static com.prembros.irrigo.MainActivity.nextValid;

public class FragmentSprinkler extends Fragment implements AdapterView.OnItemSelectedListener {

    private long totalWaterRequirement;
    private long area;
    private String previousTitle;
    private Context context;

    protected static float totalNumberOfSprinklers = 0;
    protected static String selectedSprinkler;
    protected static int discharge = 0;
    protected static double totalFlowOfControl;

    private String selectedNozzle;
    private float selectedPressure = 0;
    String sprinklerSelectedItem = null;
    String nozzleSelectedItem = null;
    String pressuerSelectedItem = null;

    private Spinner spinnerSelectSprinkler;
    private Spinner spinnerSprinklerNozzle;
    private Spinner spinnerSprinklerPressure;
    private TextInputEditText wettedDiameterEditText;
    private TextInputEditText dischargeEditText;
    private TextInputEditText totalNumberOfSprinklersEditText;
    private TextInputEditText totalFlowOfControlEditText;

//    private OnSprinklerInteractionListener mListener;

    public FragmentSprinkler() {
        // Required empty public constructor
    }

    public static FragmentSprinkler newInstance(long totalWaterRequirement, long area) {
        FragmentSprinkler fragmentSprinkler = new FragmentSprinkler();
        Bundle args = new Bundle();
        args.putLong("totalWaterRequirement", totalWaterRequirement);
        args.putLong("area", area);
        fragmentSprinkler.setArguments(args);
        return fragmentSprinkler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if (getArguments() != null) {
            totalWaterRequirement = getArguments().getLong("totalWaterRequirement");
            area = getArguments().getLong("area");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sprinkler, container, false);
        context = getContext();

        ((ScrollView) rootView.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);

        initializeVariables(rootView);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.sprinkler));
        }

        return rootView;
    }

    void initializeVariables(View rootView) {
        spinnerSelectSprinkler = (Spinner) rootView.findViewById(R.id.select_sprinkler);
        spinnerSprinklerNozzle = (Spinner) rootView.findViewById(R.id.select_nozzle_type);
        spinnerSprinklerPressure = (Spinner) rootView.findViewById(R.id.select_pressure);
        wettedDiameterEditText = (TextInputEditText) rootView.findViewById(R.id.wetted_diameter);
        dischargeEditText = (TextInputEditText) rootView.findViewById(R.id.discharge);
        totalNumberOfSprinklersEditText = (TextInputEditText) rootView.findViewById(R.id.total_num_sprinklers);
        totalFlowOfControlEditText = (TextInputEditText) rootView.findViewById(R.id.total_flow_of_control);

        spinnerSelectSprinkler.setOnItemSelectedListener(this);
        spinnerSprinklerNozzle.setOnItemSelectedListener(this);
        spinnerSprinklerPressure.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            final String selectedItem = adapterView.getSelectedItem().toString();
            final int id = adapterView.getId();
            switch (id) {
                case R.id.select_sprinkler:
                    sprinklerSelectedItem = adapterView.getSelectedItem().toString();
                    break;
                case R.id.select_nozzle_type:
                    nozzleSelectedItem = adapterView.getSelectedItem().toString();
                    break;
                case R.id.select_pressure:
                    pressuerSelectedItem = adapterView.getSelectedItem().toString();
                    break;
            }
//            switch (adapterView.getId()) {
//                case R.id.select_sprinkler:
//                    ArrayList<String> nozzlesList = db.returnSprinklerSpecs(adapterView.getSelectedItem().toString(), null, null, "NozzleType", true);
//                    setAdapter(spinnerSprinklerNozzle, nozzlesList, getString(R.string.select_nozzle_type));
//                    break;
//                case R.id.select_nozzle_type:
//                    ArrayList<String> pressuresList = db.returnSprinklerSpecs(spinnerSelectSprinkler.getSelectedItem().toString(),
//                            adapterView.getSelectedItem().toString(), null, "Pressure", true);
//                    setAdapter(spinnerSprinklerPressure, pressuresList, getString(R.string.select_pressure));
//                    break;
//                case R.id.select_pressure:
//                    ArrayList<String> wettedDiametersList = db.returnSprinklerSpecs(
//                            spinnerSelectSprinkler.getSelectedItem().toString(),
//                            spinnerSprinklerNozzle.getSelectedItem().toString(),
//                            spinnerSprinklerPressure.getSelectedItem().toString(), "WettedDiameter", true);
//                    String[] wettedDiameters = wettedDiametersList.toArray(new String[wettedDiametersList.size()]);
////                    float wettedDiameter = Float.parseFloat(Arrays.copyOf(wettedDiameters, wettedDiameters.length, String[].class)[0]);
//
//                    ArrayList<String> dischargesList = db.returnSprinklerSpecs(
//                            spinnerSelectSprinkler.getSelectedItem().toString(),
//                            spinnerSprinklerNozzle.getSelectedItem().toString(),
//                            spinnerSprinklerPressure.getSelectedItem().toString(), "Discharge", false);
//                    String[] discharges = dischargesList.toArray(new String[dischargesList.size()]);
//                    discharge = Integer.parseInt(discharges[0]);
//
//                    setWettedDiameterAndDischarge(wettedDiameterEditText, wettedDiameters);
//                    setWettedDiameterAndDischarge(dischargeEditText, discharges);
//                    updateResults(wettedDiameterEditText.getText().toString(), dischargeEditText.getText().toString());
//                    break;
//                default:
//                    break;
//            }
            new AsyncTask<Void, Void, Integer>() {
                ArrayList<String> nozzlesList;
                ArrayList<String> pressuresList;
                String[] wettedDiameters;
                String[] discharges;
                @Override
                protected Integer doInBackground(Void... voids) {
                    int index = 0;
                    DatabaseHolder db = new DatabaseHolder(context);
                    db.open();
                    switch (id) {
                        case R.id.select_sprinkler:
                            nozzlesList = db.returnSprinklerSpecs(sprinklerSelectedItem, null, null, "NozzleType", true);
                            index = 1;
                            break;
                        case R.id.select_nozzle_type:
                            pressuresList = db.returnSprinklerSpecs(sprinklerSelectedItem,
                                    nozzleSelectedItem, null, "Pressure", true);
                            index = 2;
                            break;
                        case R.id.select_pressure:
                            ArrayList<String> wettedDiametersList = db.returnSprinklerSpecs(
                                    sprinklerSelectedItem, nozzleSelectedItem, pressuerSelectedItem, "WettedDiameter", true);
                            wettedDiameters = wettedDiametersList.toArray(new String[wettedDiametersList.size()]);
//                    float wettedDiameter = Float.parseFloat(Arrays.copyOf(wettedDiameters, wettedDiameters.length, String[].class)[0]);

                            ArrayList<String> dischargesList = db.returnSprinklerSpecs(
                                    sprinklerSelectedItem,
                                    nozzleSelectedItem,
                                    pressuerSelectedItem, "Discharge", false);
                            discharges = dischargesList.toArray(new String[dischargesList.size()]);
                            discharge = Integer.parseInt(discharges[0]);
                            index = 3;
                            break;
                        default:
                            break;
                    }
                    db.close();
                    return index;
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    switch (integer) {
                        case 1:
                            setAdapter(spinnerSprinklerNozzle, nozzlesList, getString(R.string.select_nozzle_type));
                            break;
                        case 2:
                            setAdapter(spinnerSprinklerPressure, pressuresList, getString(R.string.select_pressure));
                            break;
                        case 3:
                            setWettedDiameterAndDischarge(wettedDiameterEditText, wettedDiameters);
                            setWettedDiameterAndDischarge(dischargeEditText, discharges);
                            updateResults(wettedDiameterEditText.getText().toString(), dischargeEditText.getText().toString());
                            break;
                        default:
                            break;
                    }
                    super.onPostExecute(integer);
                }
            }.execute();
        }
    }

    void setAdapter(Spinner spinner, ArrayList<String> dataList, String firstElement) {
        dataList.add(0, firstElement);
//        String[] data = dataList.toArray(new String[dataList.size()]);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(context, R.layout.spinner_list_item, dataList);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.startAnimation(AnimationUtils.loadAnimation(context, R.anim.float_down));
        spinner.setVisibility(View.VISIBLE);
    }

    void setWettedDiameterAndDischarge(TextInputEditText editText, String[] data) {
        editText.setText(Arrays.copyOf(data, data.length, String[].class)[0]);
    }

    void updateResults(String wettedDiameter, String discharge) {
        totalNumberOfSprinklers = fieldArea / Float.parseFloat(wettedDiameter);
        totalFlowOfControl = totalNumberOfSprinklers * Integer.parseInt(discharge);

        if (totalNumberOfSprinklersEditText != null) {
            totalNumberOfSprinklersEditText.setText(String.valueOf(totalNumberOfSprinklers));
            if (totalFlowOfControlEditText != null)
                totalFlowOfControlEditText.setText(String.valueOf(totalFlowOfControl));
            nextValid = true;
        } else nextValid = false;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onSprinklerInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnSprinklerInteractionListener) {
//            mListener = (OnSprinklerInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnSprinklerInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
//        mListener = null;
    }

//    interface OnSprinklerInteractionListener {
//        void onSprinklerInteraction(Uri uri);
//    }
}
