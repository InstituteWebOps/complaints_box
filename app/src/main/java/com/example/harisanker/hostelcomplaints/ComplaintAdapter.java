package com.example.harisanker.hostelcomplaints;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by harisanker on 22/6/17.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;
    private  int mstatus;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;


    public ComplaintAdapter(ArrayList<Complaint> myDataset , Activity a, Context c) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.mTextView.setText(mDataset[position]);
        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView tv_resolved = (TextView) holder.view.findViewById(R.id.tv_is_resolved);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        final TextView tv_upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        final TextView tv_downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView tv_comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        TextView tv_tag = (TextView)holder.view.findViewById(R.id.tv_tags);
        Button bn_upvote= (Button)holder.view.findViewById(R.id.bn_upvote);
        Button bn_downvote= (Button)holder.view.findViewById(R.id.bn_downvote);
        Button bn_comment = (Button)holder.view.findViewById(R.id.bn_comment);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout =(LinearLayout) holder.view.findViewById(R.id.ll_comment);
        ImageView iv_comp_image = (ImageView) holder.view.findViewById(R.id.iv_complaint_image);
        TextView tv_comp_photo  = (TextView)holder.view.findViewById(R.id.tv_complaint_image);


        final Complaint complaint = mDataset.get(position);

        tv_name.setText(complaint.getName());
        //TODO change narmada to IITM
        tv_hostel.setText(sharedPref.getString("hostel", "Narmada"));
        tv_resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        tv_title.setText(complaint.getTitle());
        tv_description.setText(complaint.getDescription());
        tv_upvote.setText(""+complaint.getUpvotes());
        tv_downvote.setText(""+complaint.getDownvotes());
        tv_comment.setText(""+complaint.getComments());
        //todo use glide and get profile picture
        if (complaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            tv_hostel.setText(complaint.getHostel());
        }

        if(!complaint.getTag().isEmpty()){
            tv_tag.setText(complaint.getTag());
        }else{
            tv_tag.setVisibility(View.INVISIBLE);
        }

        final String mUUID = complaint.getUid();

        if(!complaint.getImageUrl().isEmpty()){
            Log.i("AdapterImage",complaint.getImageUrl());
            tv_comp_photo.setVisibility(View.VISIBLE);
            iv_comp_image.setVisibility(View.VISIBLE);
            Uri url = Uri.parse(complaint.getImageUrl());
            Glide.with(context)
                    .load(url)
                    .placeholder(R.color.cardview_light_background)
                    .error(null)
                    .crossFade(500)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_comp_image);
            iv_comp_image.setImageURI(null);
            iv_comp_image.setImageURI(url);
        }else{
            iv_comp_image.setImageURI(null);
        }

        if(complaint.isResolved()){
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.resolved_colour));
            bn_upvote.setClickable(false);
            bn_downvote.setClickable(false);
            bn_comment.setClickable(false);
        }else {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unresolved_colour));

            bn_upvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/vote.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status =="1"){
                                    increaseUpvotes();
                                    notifyItemChanged(position);
                                }else {
                                    Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        //to POST params
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            //get hostel from prefs
                            //put some dummy for now
                            params.put("HOSTEL", "narmada");
                            params.put("UUID", mUUID);
                            params.put("VOTE", "1");
                            params.put("ROLL_NO", "ae11d001");
                            return params;
                        }
                    };
                    MySingleton.getInstance(activity).addToRequestQueue(request);
                }

                private void increaseUpvotes() {
                    int upvote_no = complaint.getUpvotes();
                    complaint.setUpvotes(upvote_no+1);
                }
            });

            bn_downvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/vote.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status =="1"){
                                    increaseDownvotes();
                                    notifyItemChanged(position);
                                }else {
                                    Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        //to POST params
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            //get hostel from prefs
                            //put some dummy for now
                            params.put("HOSTEL", "narmada");
                            params.put("UUID", mUUID);
                            params.put("VOTE", "0");
                            params.put("ROLL_NO", "ae11d001");
                            return params;
                        }

                    };
                    MySingleton.getInstance(activity).addToRequestQueue(request);
                }

                private void increaseDownvotes() {
                    int downvote_no =complaint.getDownvotes();
                    complaint.setDownvotes(downvote_no+1);
                }
            });
        }

        bn_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comments.class);
                intent.putExtra("cardData", complaint);
                activity.startActivity(intent);
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

