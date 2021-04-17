package com.example.planningplus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanPaneOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanPaneOneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanPaneOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanPaneOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanPaneOneFragment newInstance(String param1, String param2) {
        PlanPaneOneFragment fragment = new PlanPaneOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_pane_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        NavController navController = Navigation.findNavController(view);
        Button next = view.findViewById(R.id.next);
        Button previous = view.findViewById(R.id.previous);
        TextInputLayout planTitle = view.findViewById(R.id.outlinedTextField);
        planTitle.getEditText().setText(taskViewModel.planTitle.getValue());
        planTitle.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.planTitle.setValue(planTitle.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextInputLayout planDescription = view.findViewById(R.id.outlinedTextField2);
        planDescription.getEditText().setText(taskViewModel.planDescription.getValue());
        planDescription.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.planDescription.setValue(planDescription.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskViewModel.hasProximityAlert.getValue()){
                    navController.navigate(R.id.action_planPaneOneFragment_to_planProximityFragment);
                }
                else{
                    navController.navigate(R.id.action_planPaneOneFragment_to_planTimedFragment);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskViewModel.hasProximityAlert.getValue()){
                    navController.navigate(R.id.action_planPaneOneFragment_to_planPaneTwoProximityFragment);
                }
                else{
                    navController.navigate(R.id.action_planPaneOneFragment_to_planPaneTwoTimedFragment);
                }
            }
        });


    }
}