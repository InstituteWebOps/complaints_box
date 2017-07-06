package com.example.harisanker.hostelcomplaints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 7/7/2017.
 */

public class DataParser {

    private String jsonString;
    private ArrayList<Complaint> complaintArray;

    public DataParser(String string) {
        jsonString=string;
        complaintArray= new ArrayList<>();
    }

    public ArrayList<Complaint> pleasePleaseParseMyData(){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Complaint complaint = new Complaint();
                complaint.setName(jsonObject.getString("name"));
                complaint.setComments(jsonObject.getInt("comments"));
                complaint.setDescription(jsonObject.getString("description"));
                complaint.setDownvotes(jsonObject.getInt("downvotes"));
                complaint.setHostel(jsonObject.getString("hostel"));
                complaint.setProximity(jsonObject.getString("proximity"));
                complaint.setResolved(jsonObject.getBoolean("resolved"));
                complaint.setRollNo(jsonObject.getString("rollno"));
                complaint.setTag(jsonObject.getString("tag"));
                complaint.setRoomNo(jsonObject.getString("roomno"));
                complaint.setUpvotes(jsonObject.getInt("upvotes"));
                complaint.setUid(jsonObject.getString("uuid"));

                //this will put error as the value is not a string but a datetime
                // ask me to change the format on the database
                complaint.setDate(jsonObject.getString("datetime"));
                complaintArray.add(complaint);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return complaintArray;

    }

}
