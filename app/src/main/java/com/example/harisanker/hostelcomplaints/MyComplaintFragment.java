package com.example.harisanker.hostelcomplaints;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyComplaintFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    List<Complaint> complaintList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url = "http://students.iitm.ac.in/studentsapp/complaintbox/hostelcomplaints/myComplaints";
    public MyComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_complaint, container, false);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_my_complaint);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.general_complaint_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        getMyComplaints();

        return view;
    }

    public void getMyComplaints(){
        Request request = new JsonRequest< ArrayList<Complaint> >(Request.Method.POST, url, null, new Response.Listener< ArrayList<Complaint> >() {

            //here get the parsed data and show it in UI
            @Override
            public void onResponse(ArrayList<Complaint> response) {
                //code the ui here
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new ComplaintAdapter(response,getActivity());
                mRecyclerView.setAdapter(mAdapter);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // error.networkResponse.statusCode
                // error.networkResponse.data

                //put error msg
                Toast.makeText(getActivity(), "No internet connectivity", Toast.LENGTH_SHORT).show();

            }

        }
        ) {
            //to POST params
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //get hostel from prefs
                //put some dummy for now
                params.put("HOSTEL","narmada");
                params.put("ROLL_NO","ae11b001");
                return params;
            }

            // here you parse your json, this is on worker(non UI) thread
            @Override
            protected Response< ArrayList<Complaint> > parseNetworkResponse(NetworkResponse response) {
                String jsonString = null;
                //copy paste this
                try {
                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //parse your data
                DataParser dataParser = new DataParser(jsonString);
                ArrayList<Complaint> complaintArray = dataParser.pleasePleaseParseMyData();

                //copy paste this, change complaintArray
                return Response.success(complaintArray, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        //volley singleton - ensures single request queue in an app
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

        //lite
        int MY_SOCKET_TIMEOUT_MS = 5000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onRefresh() {

        //code here to load new data and setRefreshing to false
        //Below is only sample code
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(2000); //Refreshing is seen for 2 seconds

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        }.start();
    }
}
