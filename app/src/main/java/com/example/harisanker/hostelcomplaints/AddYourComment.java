package com.example.harisanker.hostelcomplaints;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddYourComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_comment);

        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/newComment.php";
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        final String NAME = Utils.getprefString(UtilStrings.NAME, this);

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
        //todo change narmad
        hostel.setText(sharedPref.getString("hostel", "narmada"));
        resolved.setText(complaint.isResolved()?"Resolved":"Unresolved");
        title.setText(complaint.getTitle());
        description.setText(complaint.getDescription());
        upvote.setText(""+complaint.getUpvotes());
        downvote.setText(""+complaint.getDownvotes());
        comment.setText(""+complaint.getComments());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmntDescStr = CmntDesc.getText().toString();
                //write code here to send the comment description to the database, increase the number of comments in database by 1
                final String mUUID= UUID.randomUUID().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddYourComment.this, "sending comment...", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String hostel_name = sharedPref.getString("hostel","");
                        String room = sharedPref.getString("roomno","");
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        params.put("hostel",hostel_name);
                        params.put("name",NAME);
                        params.put("rollno",roll_no);
                        params.put("roomno",room);
                        params.put("comment",cmntDescStr);
                        params.put("uuid",mUUID);
                        params.put("datetime",date);
                        return params;
                    }
                };
                MySingleton.getInstance(AddYourComment.this).addToRequestQueue(stringRequest);
            }
        });
    }

}
