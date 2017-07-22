package com.example.harisanker.hostelcomplaints;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dell on 21-06-2017.
 */
public class CustomComplaintActivity extends AppCompatActivity {
    String hostel, room_no, code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_complaint);

        final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/addComplaint.php";
        final String hostel_url = "https://students.iitm.ac.in/studentsapp/studentlist/get_hostel.php";
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        final String name = Utils.getprefString(UtilStrings.NAME, this);


        Button saveCustomCmplnt = (Button) findViewById(R.id.button_save);
        final EditText tv_title = (EditText) findViewById(R.id.editText_complaint_title);
        final EditText tv_description = (EditText) findViewById(R.id.editText_complaint_description);
        final EditText tv_tags = (EditText) findViewById(R.id.editText_tags);

        saveCustomCmplnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = tv_title.getText().toString();
                final String description = tv_description.getText().toString();
                final String tags = tv_tags.getText().toString();
                final String mUUID = UUID.randomUUID().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CustomComplaintActivity.this, "sending complaint...", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("hostel", hostel);
                        params.put("name", name);
                        params.put("rollno", roll_no);
                        params.put("roomno", room_no);
                        params.put("title", title);
                        params.put("proximity", "");
                        params.put("description", description);
                        params.put("tags", tags);
                        params.put("upvotes", "0");
                        params.put("downvotes", "0");
                        params.put("resolved", "0");
                        params.put("uuid", mUUID);
                        //params.put("datetime","");
                        return params;
                    }
                };
                MySingleton.getInstance(CustomComplaintActivity.this).addToRequestQueue(stringRequest);


            }
        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, hostel_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(CustomComplaintActivity.this).addToRequestQueue(jsonObjectRequest);


    }

    public void parseJSON(JSONObject r) {

        try {
            hostel = r.getString("hostel");
            room_no = r.getString("roomno");
            code = r.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}


