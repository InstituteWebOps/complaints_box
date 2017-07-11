package com.example.harisanker.hostelcomplaints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harshitha on 11/7/17.
 */

public class CmntDataParser {
    private String jsonString;
    private ArrayList<CommentObj> commentArray;

    public CmntDataParser(String string){
        jsonString=string;
        commentArray= new ArrayList<>();
    }

    public ArrayList<CommentObj> pleaseParseMyData(){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CommentObj commentObj = new CommentObj();

                commentObj.setName(jsonObject.getString("name"));
                commentObj.setCommentStr(jsonObject.getString("comment"));
                commentObj.setRollNo(jsonObject.getString("rollno"));
                commentObj.setRoomNo(jsonObject.getString("roomno"));

                //this will put error as the value is not a string but a datetime
                // ask me to change the format on the database
                commentObj.setDate(jsonObject.getString("datetime"));
                commentArray.add(commentObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentArray;
    }
}
