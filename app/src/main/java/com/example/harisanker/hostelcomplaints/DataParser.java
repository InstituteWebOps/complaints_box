package com.example.harisanker.hostelcomplaints;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by DELL on 7/7/2017.
 */

public class DataParser {

    private InputStream stream;
    private ArrayList<Complaint> complaintArray;
    Context context;

    public DataParser(String string, Context c) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        complaintArray= new ArrayList<>();
        context =c;
    }

    public ArrayList<Complaint> pleasePleaseParseMyData() throws IOException {

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.setLenient(true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return readComplaintsArray(reader);
        } finally {

            reader.close();

        }

    }

    public ArrayList<Complaint> readComplaintsArray(JsonReader reader) throws IOException {
        ArrayList<Complaint> complaints = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            complaints.add(readComplaint(reader));
        }
        reader.endArray();
        return complaints;

    }


    public Complaint readComplaint(JsonReader reader) throws IOException {

        Complaint complaint = new Complaint();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                complaint.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                complaint.setRollNo(reader.nextString());
            } else if (name.equals("roomno")) {
                complaint.setRoomNo(reader.nextString());
            } else if (name.equals("title")) {
                complaint.setTitle(reader.nextString());
            } else if (name.equals("proximity")) {
                complaint.setProximity(reader.nextString());
            } else if (name.equals("description")) {
                complaint.setDescription(reader.nextString());
            } else if (name.equals("upvotes")) {
                complaint.setUpvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("downvotes")) {
                complaint.setDownvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("resolved")) {
                Boolean resolved = false;
                if(reader.nextString().equals("1")) resolved=true;
                complaint.setResolved(resolved);
            } else if (name.equals("uuid")) {
                complaint.setUid(reader.nextString());
            } else if (name.equals("datetime")) {
                complaint.setDate(reader.nextString());
            } else if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                complaint.setTag(reader.nextString());
            } else if (name.equals("comments")) {
                complaint.setComments(Integer.parseInt(reader.nextString()));
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
        return complaint;
    }

}
