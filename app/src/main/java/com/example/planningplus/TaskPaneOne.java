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
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskPaneOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskPaneOne extends Fragment {

    public TaskViewModel taskViewModel;
    public int i;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskPaneOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskPaneOne.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskPaneOne newInstance(String param1, String param2) {
        TaskPaneOne fragment = new TaskPaneOne();
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
        return inflater.inflate(R.layout.fragment_task_pane_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Tasks");

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        ChipGroup chipGroup = view.findViewById(R.id.chip_group);

        if(!taskViewModel.assignedState.getValue()) {
            for (i = 0; i < taskViewModel.tempTags.getValue().size(); ++i) {
                Chip chip = new Chip(requireContext());
                chip.setText(taskViewModel.tempTags.getValue().get(i));
                chip.setCheckable(true);
                chip.setChecked(taskViewModel.tempOptions.getValue().get(i));
                chipGroup.addView(chip);
            }
        }

        NavController navController = Navigation.findNavController(view);


        TextInputLayout textInputLayout = view.findViewById(R.id.outlinedTextField);
        textInputLayout.getEditText().setText(taskViewModel.taskTitle.getValue());
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.taskTitle.setValue(textInputLayout.getEditText().getText().toString());
                if(taskViewModel.RSLCalculated.getValue().equals(Boolean.TRUE)){
                    if(!taskViewModel.RSLIntent.getValue().equals(taskViewModel.taskTitle.getValue())){
                        taskViewModel.RSLCalculated.setValue(false);
                        for(int i = 0; i < taskViewModel.specificSubTasks.getValue().size(); ++i){
                            if(taskViewModel.specificSubTasks.getValue().get(i).equals(taskViewModel.RSLSubTaskName.getValue())){
                                taskViewModel.specificSubTasks.getValue().remove(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskViewModel.assignedState.getValue()){
                    navController.navigate(R.id.action_taskPaneOne_to_assignedFragment);
                }
                else {
                    navController.navigate(R.id.action_taskPaneOne_to_tasksFragment);
                }
            }
        });

        TextInputLayout textInputLayout1 = view.findViewById(R.id.outlinedTextField2);
        textInputLayout1.getEditText().setText(taskViewModel.taskDescription.getValue());
        textInputLayout1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.taskDescription.setValue(textInputLayout1.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!taskViewModel.assignedState.getValue()) {
                    for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                        Chip chip = (Chip) chipGroup.getChildAt(i);
                        taskViewModel.tempOptions.getValue().set(i, chip.isChecked());
                    }
                }
                navController.navigate(R.id.action_taskPaneOne_to_taskPaneTwo);
            }
        });
    }
}