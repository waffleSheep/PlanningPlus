package com.example.planningplus;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
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

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationPaneTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationPaneTwoFragment extends Fragment {
    private AuthenticationViewModel authenticationViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public RegistrationPaneTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationPaneTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationPaneTwoFragment newInstance(String param1, String param2) {
        RegistrationPaneTwoFragment fragment = new RegistrationPaneTwoFragment();
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
        return inflater.inflate(R.layout.fragment_registration_pane_two, container, false);
    }

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        NavController navController = Navigation.findNavController(view);

        TextView latitude = view.findViewById(R.id.textview2);
        latitude.setText("Latitude: " + authenticationViewModel.homeAddressLatitude.getValue());

        TextView longitude = view.findViewById(R.id.textview3);
        longitude.setText("Longitude: " + authenticationViewModel.homeAddressLongitude.getValue());

        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/main.html");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String[] coordinates = consoleMessage.message().split(",");
                authenticationViewModel.homeAddressLatitude.setValue(Double.parseDouble(coordinates[0]));
                authenticationViewModel.homeAddressLongitude.setValue(Double.parseDouble(coordinates[1]));
                latitude.setText("Latitude: " + authenticationViewModel.homeAddressLatitude.getValue());
                longitude.setText("Longitude: " + authenticationViewModel.homeAddressLongitude.getValue());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registrationPaneTwoFragment_to_registrationPaneOneFragment);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registrationPaneTwoFragment_to_registrationPaneThreeFragment);
            }
        });
    }
}