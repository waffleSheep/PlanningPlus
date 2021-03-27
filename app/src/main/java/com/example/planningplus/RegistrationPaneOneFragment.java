package com.example.planningplus;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationPaneOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationPaneOneFragment extends Fragment {
    private AuthenticationViewModel authenticationViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationPaneOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationPaneOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationPaneOneFragment newInstance(String param1, String param2) {
        RegistrationPaneOneFragment fragment = new RegistrationPaneOneFragment();
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
        return inflater.inflate(R.layout.fragment_registration_pane_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View model and nav controller
        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        NavController navController = Navigation.findNavController(view);

        EditText username = view.findViewById(R.id.editTextTextPersonName2);
        username.setText(authenticationViewModel.username.getValue());
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authenticationViewModel.username.setValue(username.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText password = view.findViewById(R.id.editTextTextPassword2);
        password.setText(authenticationViewModel.password.getValue());
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authenticationViewModel.password.setValue(password.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText confirmPassword = view.findViewById(R.id.editTextTextPassword3);
        confirmPassword.setText(authenticationViewModel.confirmPassword.getValue());
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authenticationViewModel.confirmPassword.setValue(confirmPassword.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SwitchMaterial isStudentSwitch = view.findViewById(R.id.switch1);
        isStudentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registrationPaneOneFragment_to_userSignInFragment);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registrationPaneOneFragment_to_registrationPaneTwoFragment);
            }
        });
    }
}