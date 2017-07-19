package com.example.harisanker.hostelcomplaints;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by harshitha on 11/7/17.
 */

public class CmntDataParser {

    private InputStream stream;
    private ArrayList<CommentObj> commentArray;
    Context context;

    public CmntDataParser(String string, Context c){
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        commentArray= new ArrayList<>();
        context = c;
    }

    public ArrayList<CommentObj> pleaseParseMyData() throws IOException{

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.setLenient(true);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return readCommentsArray(reader);
        } finally {

            reader.close();

        }

    }

    public ArrayList<CommentObj> readCommentsArray(JsonReader reader) throws IOException {
        ArrayList<CommentObj> comments = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            comments.add(readComment(reader));
        }
        reader.endArray();
        return comments;

    }
    public CommentObj readComment(JsonReader reader) throws IOException {

        CommentObj commentObj =new CommentObj();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                commentObj.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                commentObj.setRollNo(reader.nextString());
            } else if (name.equals("roomno")) {
                commentObj.setRoomNo(reader.nextString());
            } else if (name.equals("comment")) {
                commentObj.setCommentStr(reader.nextString());
            }  else if (name.equals("datetime")) {
                commentObj.setDate(reader.nextString());
            } else if (name.equals("status")){
                if(reader.nextString() != "1"){
                    Toast.makeText(context, "Changes are yet to be made in the database", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Database updated", Toast.LENGTH_SHORT).show();
                }
            } else if (name.equals("error")){
                Toast.makeText(context, "Error in input", Toast.LENGTH_SHORT).show();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return commentObj;
    }

}
