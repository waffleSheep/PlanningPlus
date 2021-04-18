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
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskPaneFour#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskPaneFour extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskPaneFour() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskPaneFour.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskPaneFour newInstance(String param1, String param2) {
        TaskPaneFour fragment = new TaskPaneFour();
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
        return inflater.inflate(R.layout.fragment_task_pane_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        NavController navController = Navigation.findNavController(view);
        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Tasks");

        TextInputLayout textInputLayout = view.findViewById(R.id.outlinedTextField);
        textInputLayout.getEditText().setText(taskViewModel.days.getValue());
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.days.setValue(textInputLayout.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextInputLayout textInputLayout1 = view.findViewById(R.id.outlinedTextField2);
        textInputLayout1.getEditText().setText(taskViewModel.hours.getValue());
        textInputLayout1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.hours.setValue(textInputLayout1.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_taskPaneFour_to_taskPaneThree);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskViewModel.assignedState.getValue()) {
                    navController.navigate(R.id.action_taskPaneFour_to_assignedTaskPaneFive);
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(Database.username);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            Task task = new Task(taskViewModel.days.getValue() + "d" + taskViewModel.hours.getValue() + "h",
                                    taskViewModel.taskTitle.getValue(),
                                    taskViewModel.taskDescription.getValue(),
                                    taskViewModel.taskDeadlineDate.getValue() + " " + taskViewModel.taskDeadlineTime.getValue(),
                                    false,
                                    "");
                            for (String i : taskViewModel.specificSubTasks.getValue())
                                task.subTasks.add(new SubTask(i));

                            for (int i = 0; i < taskViewModel.tempTags.getValue().size(); ++i)
                                if (taskViewModel.tempOptions.getValue().get(i))
                                    task.tags.add(new Tag(taskViewModel.tempTags.getValue().get(i)));

                            for(Tag i : task.tags){
                                if(i.tagName.equals("Home")){
                                    Plan plan = new Plan("",
                                            "",
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            true,
                                            user.homeAddressLatitude,
                                            user.homeAddressLongitude);
                                    ((NavigationDrawerMenu) getActivity()).proximityAlert(user.homeAddressLatitude,
                                            user.homeAddressLongitude,
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            user.username);
                                    user.plans.add(0, plan);
                                }
                                else if(i.tagName.equals("Work")){
                                    Plan plan = new Plan("",
                                            "",
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            true,
                                            user.workAddressLatitude,
                                            user.workAddressLongitude);
                                    ((NavigationDrawerMenu) getActivity()).proximityAlert(user.workAddressLatitude,
                                            user.workAddressLongitude,
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            user.username);
                                    user.plans.add(0, plan);
                                }
                                else if(i.tagName.equals("Sunrise")) {
                                    Date date = new Date(System.currentTimeMillis());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    String strDate = simpleDateFormat.format(date);
                                    String[] dateTime = strDate.split(" ");
                                    String notificationString = dateTime[0] + " 07:00";
                                    Date date1 = new Date(System.currentTimeMillis());
                                    try {
                                        date1 = simpleDateFormat.parse(notificationString);
                                    } catch (ParseException ex) { }
                                    if("07:00".compareTo(dateTime[1]) <= 0) {
                                        date1 = new Date(date1.getTime() + 1000 * 60 * 60 * 24);
                                        notificationString = simpleDateFormat.format(date1);
                                    }
                                    Plan plan = new Plan(notificationString.split(" ")[0],
                                            "07:00",
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            false,
                                            0.0,
                                            0.0);
                                    ((NavigationDrawerMenu) getActivity()).timedAlert(taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            Database.username, date1.getTime()
                                    );
                                    user.plans.add(plan);
                                }
                                else if(i.tagName.equals("Sunset")){
                                    Date date = new Date(System.currentTimeMillis());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    String strDate = simpleDateFormat.format(date);
                                    String[] dateTime = strDate.split(" ");
                                    String notificationString = dateTime[0] + " 19:00";
                                    Date date1 = new Date(System.currentTimeMillis());
                                    try {
                                        date1 = simpleDateFormat.parse(notificationString);
                                    } catch (ParseException ex) { }
                                    if("19:00".compareTo(dateTime[1]) <= 0) {
                                        date1 = new Date(date1.getTime() + 1000 * 60 * 60 * 24);
                                        notificationString = simpleDateFormat.format(date1);
                                    }
                                    Plan plan = new Plan(notificationString.split(" ")[0],
                                            "19:00",
                                            taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            false,
                                            0.0,
                                            0.0);
                                    ((NavigationDrawerMenu) getActivity()).timedAlert(taskViewModel.taskTitle.getValue(),
                                            taskViewModel.taskDescription.getValue(),
                                            Database.username, date1.getTime()
                                    );
                                    user.plans.add(plan);
                                }
                            }

                            user.tasks.add(task);
                            DocumentReference docRef1 = db.collection("users").document(Database.username);
                            docRef1.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                        }
                    });
                    navController.navigate(R.id.action_taskPaneFour_to_tasksFragment);
                }
            }
        });
    }
}