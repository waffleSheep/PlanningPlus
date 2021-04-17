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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskViewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubTaskInTaskRecyclerAdapter adapter;
    private TaskViewModel taskViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskViewFragment newInstance(String param1, String param2) {
        TaskViewFragment fragment = new TaskViewFragment();
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
        return inflater.inflate(R.layout.fragment_task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubTaskInTaskRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        NavController navController = Navigation.findNavController(view);

        TextView taskTitle = view.findViewById(R.id.textView3);
        TextView taskDescription = view.findViewById(R.id.textView4);
        TextView timeRequired = view.findViewById(R.id.textView5);
        TextView taskDeadline = view.findViewById(R.id.textView6);
        TextView assignedBy = view.findViewById(R.id.textView7);
        Button markAsComplete = view.findViewById(R.id.complete);
        Button askQuestion = view.findViewById(R.id.question);

        markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(Database.username);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        for(int i = 0 ; i < user.tasks.size(); ++i){
                            if(user.tasks.get(i).id.equals(Database.id)){
                                if(user.tasks.get(i).isAssigned){
                                    DocumentReference documentReference1 = db.collection("users").document(user.tasks.get(i).assignedBy);
                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                            User user1 = documentSnapshot1.toObject(User.class);
                                            for(int j = 0; j < user1.assignedTasks.size(); ++j){
                                                if(user1.assignedTasks.get(j).task.id.equals(Database.id)){
                                                    for(int k = 0; k < user1.assignedTasks.get(j).assignees.size(); ++k){
                                                        if(user1.assignedTasks.get(j).assignees.get(k).equals(Database.username)){
                                                            user1.assignedTasks.get(j).completed.set(k, Boolean.TRUE);
                                                        }
                                                    }
                                                }
                                            }
                                            documentReference1.set(user1);
                                        }
                                    });
                                }

                                user.tasks.remove(i);
                                break;
                            }
                        }
                        documentReference.set(user);
                    }
                });
                navController.navigate(R.id.action_taskViewFragment_to_tasksFragment);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(Database.username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Task task = null;
                for(Task i : user.tasks)
                    if(i.id.equals(Database.id))
                        task = i;
                taskTitle.setText(task.taskTitle);
                taskDescription.setText("Task description: " + task.taskDescription);
                Database.taskDesc = task.taskDescription;
                timeRequired.setText("Time required: " + task.timeRequired);
                taskDeadline.setText("Deadline: " + task.taskDeadline);
                assignedBy.setText("Assigned by: " + task.assignedBy);
                ArrayList<String> stringArrayList = new ArrayList<>();
                for(SubTask i : task.subTasks)
                    stringArrayList.add(i.subTaskTitle);
                adapter.setItems(stringArrayList);
                if(!task.isAssigned){
                    askQuestion.setVisibility(View.GONE);
                    assignedBy.setVisibility(View.GONE);
                }

                if(task.isAssigned){
                    taskViewModel.taskDescription.setValue(task.taskDescription);
                    taskViewModel.assignerName.setValue(task.assignedBy);
                }
            }
        });

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_taskViewFragment_to_askQuestionFragment);
            }
        });
    }
}