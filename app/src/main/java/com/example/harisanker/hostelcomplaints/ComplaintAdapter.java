package com.example.harisanker.hostelcomplaints;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by harisanker on 22/6/17.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;
    private  int mstatus;
    private Activity activity;
    private UUID mUUID;


    public ComplaintAdapter(ArrayList<Complaint> myDataset , Activity a) {
        mDataset = myDataset;
        activity = a;
    }

    @Override
    public ComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.mTextView.setText(mDataset[position]);
        TextView name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView resolved = (TextView) holder.view.findViewById(R.id.tv_is_resolved);
        TextView title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView description = (TextView) holder.view.findViewById(R.id.tv_description);
        final TextView upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        final TextView downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        Button bn_upvote= (Button)holder.view.findViewById(R.id.bn_upvote);
        Button bn_downvote= (Button)holder.view.findViewById(R.id.bn_downvote);
        Button bn_comment = (Button)holder.view.findViewById(R.id.bn_comment);


        final Complaint complaint = mDataset.get(position);

        name.setText(complaint.getName());
        hostel.setText(complaint.getHostel());
        resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        title.setText(complaint.getTitle());
        description.setText(complaint.getDescription());
        upvote.setText(""+complaint.getUpvotes());
        downvote.setText(""+complaint.getDownvotes());
        comment.setText(""+complaint.getComments());

        mUUID = UUID.randomUUID();

        bn_upvote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String url = "http://students.iitm.ac.in/studentsapp/complaintbox/hostelcomplaints/vote";
                Request request = new JsonRequest<ArrayList<Complaint>>(Request.Method.POST, url,null, new Response.Listener<ArrayList<Complaint>>() {
                    @Override
                    public void onResponse(ArrayList<Complaint> response) {
                        switch (mstatus) {
                            case 1:
                                int currentUpvote = Integer.parseInt(upvote.getText().toString());
                                upvote.setText("" + (currentUpvote + 1));
                                break;
                            case 0:
                                Toast.makeText(activity, "only 1 upvote allowed per person", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //to POST params
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        //get hostel from prefs
                        //put some dummy for now
                        params.put("HOSTEL","narmada");
                        params.put("UUID","" + mUUID);
                        params.put("VOTE","1");
                        params.put("ROLL_NO","ae11d001");
                        return params;
                    }

                    @Override
                    protected Response<ArrayList<Complaint>> parseNetworkResponse(NetworkResponse response) {
                        if(response != null){
                            mstatus = response.statusCode;
                        }
                        //return super.parseNetworkResponse(response);
                        return null;
                    }
                };
                MySingleton.getInstance(activity).addToRequestQueue(request);
            }
        });

        bn_downvote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String url = "http://students.iitm.ac.in/studentsapp/complaintbox/hostelcomplaints/vote";
                Request request = new JsonRequest<ArrayList<Complaint>>(Request.Method.POST, url,null, new Response.Listener<ArrayList<Complaint>>() {
                    @Override
                    public void onResponse(ArrayList<Complaint> response) {
                        switch (mstatus) {
                            case 1:
                                int currentDownvote = Integer.parseInt(downvote.getText().toString());
                                downvote.setText("" + (currentDownvote + 1));
                                break;
                            case 0:
                                Toast.makeText(activity, "only 1 downvote allowed per person", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //to POST params
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        //get hostel from prefs
                        //put some dummy for now
                        params.put("HOSTEL","narmada");
                        params.put("UUID","1324");
                        params.put("VOTE","1");
                        params.put("ROLL_NO","ae11d001");
                        return params;
                    }

                    @Override
                    protected Response<ArrayList<Complaint>> parseNetworkResponse(NetworkResponse response) {
                        if(response != null){
                            mstatus = response.statusCode;
                        }
                        //return super.parseNetworkResponse(response);
                        return null;
                    }
                };
                MySingleton.getInstance(activity).addToRequestQueue(request);
            }
        });

        bn_comment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), Comments.class);
                intent.putExtra("cardData",complaint);
                activity.getApplicationContext().startActivity(intent);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;


        }
    }
}

