package com.example.planningplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSignInFragment extends Fragment {
    private AuthenticationViewModel authenticationViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSignInFragment newInstance(String param1, String param2) {
        UserSignInFragment fragment = new UserSignInFragment();
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

        return inflater.inflate(R.layout.fragment_user_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //comment
        super.onViewCreated(view, savedInstanceState);
        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        Button register = view.findViewById(R.id.register);
        NavController navController = Navigation.findNavController(view);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userSignInFragment_to_registrationPaneOneFragment);
            }
        });

        TextInputLayout loginText = view.findViewById(R.id.outlinedTextField);
        TextInputLayout passwordText = view.findViewById(R.id.outlinedTextField2);
        Button login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginText.getEditText().getText().toString().equals("") || passwordText.getEditText().getText().toString().equals("")){
                    Snackbar.make(view, "Areas not filled", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    return;
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(loginText.getEditText().getText().toString());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                User user = document.toObject(User.class);
                                if(user.password.equals(passwordText.getEditText().getText().toString())){
                                    Database.username = user.username;
                                    Database.userTagsData = user.tags;
                                    Intent intent = new Intent(requireActivity(), NavigationDrawerMenu.class);
                                    startActivity(intent);
                                }
                                else{
                                    Snackbar.make(view, "Password Incorrect", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                }

                            }
                            else{
                                Snackbar.make(view, "No such user exists", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }
                        }
                        else{
                            Snackbar.make(view, "Internet error", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                });
            }
        });
    }
}