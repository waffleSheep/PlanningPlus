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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskPaneTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskPaneTwo extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubTaskRecyclerAdapter adapter;
    RequestQueue requestQueue;
    String url = "http://54.251.192.205:8080/suggestSubTask";
    TaskViewModel taskViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskPaneTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskPaneTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskPaneTwo newInstance(String param1, String param2) {
        TaskPaneTwo fragment = new TaskPaneTwo();
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
        return inflater.inflate(R.layout.fragment_task_pane_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubTaskRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItems(taskViewModel.specificSubTasks.getValue());

        taskViewModel.specificSubTasks.setValue(adapter.getSubTaskTitles());

        NavController navController = Navigation.findNavController(view);

        TextInputLayout subTaskTitle = view.findViewById(R.id.outlinedTextField);
        Button add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(subTaskTitle.getEditText().getText().toString());
            }
        });

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_taskPaneTwo_to_taskPaneOne);
            }
        });

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_taskPaneTwo_to_taskPaneThree);
            }
        });

        if(taskViewModel.tempOptions.getValue().get(0) && !taskViewModel.RSLCalculated.getValue().equals(Boolean.TRUE)){
            taskViewModel.RSLCalculated.setValue(true);
            taskViewModel.RSLIntent.setValue(taskViewModel.taskTitle.getValue());
            requestUrl(view, taskViewModel.taskTitle.getValue());
        }

    }

    public void requestUrl(View view, String intent){

        JSONObject js = new JSONObject();
        try {
            js.put("document", intent);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject response1 = response.getJSONObject("result");
                    adapter.addItem("call " + response1.getString("subtask"));
                    taskViewModel.RSLSubTaskName.setValue("call " + response1.getString("subtask"));
                }
                catch (JSONException exception){
                    Snackbar.make(view, "response fail1", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, error.getMessage(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }
}