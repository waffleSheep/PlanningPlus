package com.example.planningplus;

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
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanPaneTwoProximityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanPaneTwoProximityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanPaneTwoProximityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanPaneTwoProximityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanPaneTwoProximityFragment newInstance(String param1, String param2) {
        PlanPaneTwoProximityFragment fragment = new PlanPaneTwoProximityFragment();
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
        return inflater.inflate(R.layout.fragment_plan_pane_two_proximity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Plans");
        NavController navController = Navigation.findNavController(view);
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        TextView latitude = view.findViewById(R.id.textview2);
        latitude.setText("Latitude: " + taskViewModel.planLatitude.getValue());

        TextView longitude = view.findViewById(R.id.textview3);
        longitude.setText("Longitude: " + taskViewModel.planLongitude.getValue());

        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/main.html");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String[] coordinates = consoleMessage.message().split(",");
                taskViewModel.planLatitude.setValue(Double.parseDouble(coordinates[0]));
                taskViewModel.planLongitude.setValue(Double.parseDouble(coordinates[1]));
                latitude.setText("Latitude: " + taskViewModel.planLatitude.getValue());
                longitude.setText("Longitude: " + taskViewModel.planLongitude.getValue());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        Button next = view.findViewById(R.id.next);
        Button previous = view.findViewById(R.id.previous);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(Database.username);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        Plan plan = new Plan(taskViewModel.planTimeDate.getValue(),
                                taskViewModel.planTimeTime.getValue(),
                                taskViewModel.planTitle.getValue(),
                                taskViewModel.planDescription.getValue(),
                                taskViewModel.hasProximityAlert.getValue(),
                                taskViewModel.planLatitude.getValue(),
                                taskViewModel.planLongitude.getValue());
                        user.plans.add(0, plan);
                        ((NavigationDrawerMenu) getActivity()).proximityAlert(taskViewModel.planLatitude.getValue(), taskViewModel.planLongitude.getValue(), taskViewModel.planTitle.getValue(), taskViewModel.planDescription.getValue(), Database.username);
                        documentReference.set(user);
                    }
                });
                navController.navigate(R.id.action_planPaneTwoProximityFragment_to_plansFragment);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_planPaneTwoProximityFragment_to_planPaneOneFragment);
            }
        });
    }
}