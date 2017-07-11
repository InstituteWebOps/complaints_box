package com.example.harisanker.hostelcomplaints;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
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

public class Comments extends AppCompatActivity {

    List<CommentObj> commentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url = "http://students.iitm.ac.in/studentsapp/complaintbox/hostelcomplaints/searchComment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent i = getIntent();
        Complaint complaint = (Complaint)i.getSerializableExtra("cardData");

        TextView name = (TextView)findViewById(R.id.comment_tv_name);
        TextView hostel = (TextView)findViewById(R.id.comment_tv_hostel);
        TextView resolved = (TextView)findViewById(R.id.comment_tv_is_resolved);
        TextView title = (TextView) findViewById(R.id.comment_tv_title);
        TextView description = (TextView) findViewById(R.id.comment_tv_description);
        final TextView upvote = (TextView)findViewById(R.id.comment_tv_upvote);
        final TextView downvote = (TextView)findViewById(R.id.comment_tv_downvote);
        TextView comment = (TextView)findViewById(R.id.comment_tv_comment);

        name.setText(complaint.getName());
        hostel.setText(complaint.getHostel());
        resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        title.setText(complaint.getTitle());
        description.setText(complaint.getDescription());
        upvote.setText(""+complaint.getUpvotes());
        downvote.setText(""+complaint.getDownvotes());
        comment.setText(""+complaint.getComments());

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        Request request = new JsonRequest<ArrayList<CommentObj>>(Request.Method.POST, url, null, new Response.Listener<ArrayList<CommentObj>>() {
            @Override
            public void onResponse(ArrayList<CommentObj> response) {
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new CommentsAdapter(response);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // error.networkResponse.statusCode
                // error.networkResponse.data

                //put error msg
            }
        }) {
            //to POST params
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //get hostel from prefs
                //put some dummy for now
                params.put("HOSTEL","narmada");
                params.put("UUID","333545");
                return params;
            }

            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                String jsonString = null;
                //copy paste this
                try {
                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //parse your data
               CmntDataParser dataParser = new CmntDataParser(jsonString);
                ArrayList<CommentObj> commentObjArrayList= dataParser.pleaseParseMyData();

                //copy paste this, change commentObjArrayList
                return Response.success(commentObjArrayList, HttpHeaderParser.parseCacheHeaders(response));
            }

        };
        //volley singleton - ensures single request queue in an app
        MySingleton.getInstance(this).addToRequestQueue(request);

        //lite
        int MY_SOCKET_TIMEOUT_MS = 5000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

}
