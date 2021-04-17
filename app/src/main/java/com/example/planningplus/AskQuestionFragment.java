package com.example.planningplus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskQuestionFragment extends Fragment {
    RequestQueue requestQueue;
    String url = "http://54.255.81.93:8080/questionAnswer";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AskQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AskQuestionFragment newInstance(String param1, String param2) {
        AskQuestionFragment fragment = new AskQuestionFragment();
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
        return inflater.inflate(R.layout.fragment_ask_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout textInputLayout = view.findViewById(R.id.outlinedTextField);
        Button button = view.findViewById(R.id.button5);
        RelativeLayout loadingPanel = view.findViewById(R.id.loadingPanel);
        TextView answerText = view.findViewById(R.id.textView15);
        loadingPanel.setVisibility(View.GONE);
        answerText.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPanel.setVisibility(View.VISIBLE);
                JSONObject js = new JSONObject();
                try {
                    js.put("document", Database.taskDesc);
                    System.out.println(Database.taskDesc);
                    js.put("question", textInputLayout.getEditText().getText().toString());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject response1 = response.getJSONObject("result");
                            loadingPanel.setVisibility(View.GONE);
                            answerText.setVisibility(View.VISIBLE);
                            answerText.setText("Answer: " + response1.getString("answer"));
                        }
                        catch (JSONException exception){
                            loadingPanel.setVisibility(View.GONE);
                            answerText.setVisibility(View.VISIBLE);
                            answerText.setText("How did we get here?");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingPanel.setVisibility(View.GONE);
                        answerText.setVisibility(View.VISIBLE);
                        answerText.setText("Internet Error");
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                requestQueue.add(jsonObjectRequest);
            }
        });

    }
}