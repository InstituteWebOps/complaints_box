package com.example.harisanker.hostelcomplaints;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddYourComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_comment);

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
        final EditText CmntDesc = (EditText)findViewById(R.id.editText);
        Button save = (Button)findViewById(R.id.bn_save);

        name.setText(complaint.getName());
        hostel.setText(complaint.getHostel());
        resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        title.setText(complaint.getTitle());
        description.setText(complaint.getDescription());
        upvote.setText(""+complaint.getUpvotes());
        downvote.setText(""+complaint.getDownvotes());
        comment.setText(""+complaint.getComments());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmntDescStr = CmntDesc.getText().toString();
                //write code here to send the comment description to the database, increase the number of comments in database by 1
            }
        });
    }

}
