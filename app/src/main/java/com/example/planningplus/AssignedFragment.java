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
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignedFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AssignedRecyclerAdapter adapter;
    private TaskViewModel taskViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssignedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignedFragment newInstance(String param1, String param2) {
        AssignedFragment fragment = new AssignedFragment();
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
        return inflater.inflate(R.layout.fragment_assigned, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        ((NavigationDrawerMenu) getActivity()).getSupportActionBar().setTitle("Assigned Tasks");
        ImageView background = view.findViewById(R.id.imageView6);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        adapter = new AssignedRecyclerAdapter();
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DocumentReference documentReference = db.collection("users").document(Database.username);

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
                taskViewModel.taskDeadlineDate.setValue("");
                taskViewModel.taskDeadlineTime.setValue("");

                for(Tag i : Database.userTagsData){
                    taskViewModel.tempTags.getValue().add(i.tagName);
                    taskViewModel.tempOptions.getValue().add(false);
                }

                taskViewModel.assignedState.setValue(true);
                navController.navigate(R.id.action_assignedFragment_to_taskPaneOne);
            }
        });

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                adapter.setItems(user.assignedTasks);
                if(adapter.getItemCount() == 0){
                    background.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    background.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}