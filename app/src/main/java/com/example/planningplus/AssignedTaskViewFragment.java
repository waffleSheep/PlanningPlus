package com.example.planningplus;

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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignedTaskViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignedTaskViewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubTaskInTaskRecyclerAdapter adapter;
    private TaskViewModel taskViewModel;

    RecyclerView recyclerView2;
    RecyclerView.LayoutManager layoutManager2;
    AssigneeRecyclerAdapter adapter2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssignedTaskViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignedTaskViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignedTaskViewFragment newInstance(String param1, String param2) {
        AssignedTaskViewFragment fragment = new AssignedTaskViewFragment();
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
        return inflater.inflate(R.layout.fragment_assigned_task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Assigned Tasks");

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubTaskInTaskRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        NavController navController = Navigation.findNavController(view);

        recyclerView2 = view.findViewById(R.id.recyclerView2);
        layoutManager2 = new LinearLayoutManager(view.getContext());
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new AssigneeRecyclerAdapter();
        recyclerView2.setAdapter(adapter2);

        TextView taskTitle = view.findViewById(R.id.textView3);
        TextView taskDescription = view.findViewById(R.id.textView4);
        TextView timeRequired = view.findViewById(R.id.textView5);
        TextView taskDeadline = view.findViewById(R.id.textView6);
        Button deleteTask = view.findViewById(R.id.delete);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(Database.username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                AssignedTask assignedTask = null;
                for(AssignedTask i : user.assignedTasks)
                    if(i.task.id.equals(Database.id))
                        assignedTask = i;
                taskTitle.setText(assignedTask.task.taskTitle);
                taskDescription.setText("Task description: " + assignedTask.task.taskDescription);
                timeRequired.setText("Time required: " + assignedTask.task.timeRequired);
                taskDeadline.setText("Deadline: " + assignedTask.task.taskDeadline);
                ArrayList<String> stringArrayList = new ArrayList<>();
                for(SubTask i : assignedTask.task.subTasks)
                    stringArrayList.add(i.subTaskTitle);
                adapter.setItems(stringArrayList);
                adapter2.setItems(assignedTask.assignees, assignedTask.completed);
            }
        });

        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        for(int i = 0; i < user.assignedTasks.size(); ++i){
                            if(user.assignedTasks.get(i).task.id.equals(Database.id)){
                                for(String j : user.assignedTasks.get(i).assignees){
                                    DocumentReference documentReference1 = db.collection("users").document(j);
                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user1 = documentSnapshot.toObject(User.class);
                                            for(int k = 0; k < user1.tasks.size(); ++k){
                                                if(user1.tasks.get(k).id.equals(Database.id)){
                                                    user1.tasks.remove(k);
                                                    break;
                                                }
                                            }
                                            documentReference1.set(user1);
                                        }
                                    });
                                }
                                user.assignedTasks.remove(i);
                                break;
                            }
                        }
                        documentReference.set(user);
                    }
                });
                navController.navigate(R.id.action_assignedTaskViewFragment_to_assignedFragment);
            }
        });



    }
}