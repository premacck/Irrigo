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

public class FragmentBenefitCost extends Fragment {

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;
    private String previousTitle;

    private ActionBar actionBar;

    private OnBenefitCostInteractionListener mListener;

    public FragmentBenefitCost() {
        // Required empty public constructor
    }

    public static FragmentBenefitCost newInstance() {
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return new FragmentBenefitCost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_benefit_cost, container, false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.getTitle() != null) {
            previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle(getString(R.string.benefit_cost));
        }
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBenefitCostInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBenefitCostInteractionListener) {
            mListener = (OnBenefitCostInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBenefitCostInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        actionBar.setTitle(previousTitle);
        mListener = null;
    }

    interface OnBenefitCostInteractionListener {
        void onBenefitCostInteraction(Uri uri);
    }
}
