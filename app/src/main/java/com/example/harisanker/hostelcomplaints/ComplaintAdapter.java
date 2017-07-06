package com.example.harisanker.hostelcomplaints;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by harisanker on 22/6/17.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;


    public ComplaintAdapter(ArrayList<Complaint> myDataset) {
        mDataset = myDataset;
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
        TextView upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        TextView downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView comment = (TextView) holder.view.findViewById(R.id.tv_comment);

        Complaint complaint = mDataset.get(position);

        name.setText(complaint.getName());
        hostel.setText(complaint.getHostel());
        resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        title.setText(complaint.getTitle());
        description.setText(complaint.getDescription());
        upvote.setText("" + complaint.getUpvotes());
        downvote.setText("" + complaint.getDownvotes());
        comment.setText("" + complaint.getComments());


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

