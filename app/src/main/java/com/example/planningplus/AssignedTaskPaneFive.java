package com.example.planningplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignedTaskPaneFive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignedTaskPaneFive extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubTaskRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssignedTaskPaneFive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignedTaskPaneFive.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignedTaskPaneFive newInstance(String param1, String param2) {
        AssignedTaskPaneFive fragment = new AssignedTaskPaneFive();
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
        return inflater.inflate(R.layout.fragment_assigned_task_pane_five, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubTaskRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItems(taskViewModel.userNames.getValue());
        taskViewModel.userNames.setValue(adapter.getSubTaskTitles());

        NavController navController = Navigation.findNavController(view);

        TextInputLayout subTaskTitle = view.findViewById(R.id.outlinedTextField);
        Button add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(subTaskTitle.getEditText().getText().toString());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                User user = document.toObject(User.class);
                                adapter.addItem(subTaskTitle.getEditText().getText().toString());
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

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_assignedTaskPaneFive_to_taskPaneFour);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                true,
                                Database.username);
                        for (String i : taskViewModel.specificSubTasks.getValue())
                            task.subTasks.add(new SubTask(i));

                        for (int i = 0; i < taskViewModel.tempTags.getValue().size(); ++i)
                            if (taskViewModel.tempOptions.getValue().get(i))
                                task.tags.add(new Tag(taskViewModel.tempTags.getValue().get(i)));

                        user.assignedTasks.add(new AssignedTask(task, taskViewModel.userNames.getValue()));
                        DocumentReference docRef1 = db.collection("users").document(Database.username);
                        docRef1.set(user);

                        for(String i : taskViewModel.userNames.getValue()){
                            DocumentReference docRef2 = db.collection("users").document(i);
                            docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                    User user = documentSnapshot1.toObject(User.class);
                                    user.tasks.add(task);
                                    docRef2.set(user);
                                }
                            });
                        }
                    }
                });
                navController.navigate(R.id.action_assignedTaskPaneFive_to_assignedFragment);
            }
        });
    }
}