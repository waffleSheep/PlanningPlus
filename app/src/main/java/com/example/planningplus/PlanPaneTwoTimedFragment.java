package com.example.planningplus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.versionedparcelable.VersionedParcel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanPaneTwoTimedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanPaneTwoTimedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanPaneTwoTimedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanPaneTwoTimedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanPaneTwoTimedFragment newInstance(String param1, String param2) {
        PlanPaneTwoTimedFragment fragment = new PlanPaneTwoTimedFragment();
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
        return inflater.inflate(R.layout.fragment_plan_pane_two_timed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        NavController navController = Navigation.findNavController(view);


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select date");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        Button date = view.findViewById(R.id.button);
        Button time = view.findViewById(R.id.button2);
        TextView dateTextView = view.findViewById(R.id.date);
        dateTextView.setText("Date: " + taskViewModel.planTimeDate.getValue());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getFragmentManager(), "tag");
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date date = new Date((Long) selection);
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                    dateTextView.setText("Date: " + simpleFormat.format(date));
                    taskViewModel.planTimeDate.setValue(simpleFormat.format(date));
                });

            }
        });

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build();

        TextView timeTextView = view.findViewById(R.id.time);
        timeTextView.setText("Time: " + taskViewModel.planTimeTime.getValue());
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(getFragmentManager(), "tag");
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hour = materialTimePicker.getHour() + "";
                        if(hour.length() == 1){
                            hour = "0" + hour;
                        }
                        String minute = materialTimePicker.getMinute() + "";
                        if(minute.length() == 1){
                            minute = "0" + minute;
                        }
                        timeTextView.setText("Time: " + hour + ":" + minute);
                        taskViewModel.planTimeTime.setValue(hour + ":" + minute);
                    }
                });
            }
        });

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_planPaneTwoTimedFragment_to_planPaneOneFragment);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskViewModel.planTimeDate.getValue().equals("") || taskViewModel.planTimeTime.getValue().equals("")){
                    Log.i("not work", "reason");
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date notificationDate;
                try{
                    notificationDate = simpleDateFormat.parse(taskViewModel.planTimeDate.getValue() + " " + taskViewModel.planTimeTime.getValue());
                }
                catch (ParseException parseException){
                    Log.i("not work", "reason1");
                    return;
                }
                Long currTime = System.currentTimeMillis();
                Long notificationTime = notificationDate.getTime();
                if(notificationTime <= currTime){
                    Log.i("not work", "reason2");
                    Log.i("curr", currTime + "");
                    Log.i("curr", notificationTime + "");
                    return;
                }

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
                        user.plans.add(plan);

                        ((NavigationDrawerMenu) getActivity()).timedAlert(taskViewModel.planTitle.getValue(), taskViewModel.planDescription.getValue(), Database.username, notificationTime);
                        documentReference.set(user);
                    }
                });
                navController.navigate(R.id.action_planPaneTwoTimedFragment_to_plansFragment);
            }
        });
    }
}