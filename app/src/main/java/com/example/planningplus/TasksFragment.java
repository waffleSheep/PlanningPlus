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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.net.UnknownServiceException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TaskRecyclerAdapter adapter;
    private TaskViewModel taskViewModel;
    ArrayList<Task> tasks;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
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
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Tasks");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        adapter = new TaskRecyclerAdapter();
        DocumentReference documentReference = db.collection("users").document(Database.username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                recyclerView = view.findViewById(R.id.recyclerView);
                layoutManager = new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewModel.tempTags.setValue(new ArrayList<>());
                taskViewModel.tempTags.setValue(new ArrayList<>());
                taskViewModel.taskTitle.setValue("");
                taskViewModel.taskDescription.setValue("");
                taskViewModel.specificSubTasks.setValue(new ArrayList<>());
                taskViewModel.tempTags.setValue(new ArrayList<>());
                taskViewModel.tempOptions.setValue(new ArrayList<>());
                taskViewModel.days.setValue("0");
                taskViewModel.hours.setValue("0");
                taskViewModel.taskDeadlineTime.setValue("");
                taskViewModel.taskDeadlineTime.setValue("");

                for(Tag i : Database.userTagsData){
                    taskViewModel.tempTags.getValue().add(i.tagName);
                    taskViewModel.tempOptions.getValue().add(false);
                }

                taskViewModel.assignedState.setValue(false);
                navController.navigate(R.id.action_tasksFragment_to_taskPaneOne);
            }
        });

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                adapter.setItems(user.tasks);
            }
        });

    }
}