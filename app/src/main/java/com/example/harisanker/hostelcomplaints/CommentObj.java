package com.example.harisanker.hostelcomplaints;

/**
 * Created by harshitha on 11/7/17.
 */

public class CommentObj {
    private String name;
    private String roomNo;
    private  String date;
    private String commentStr;
    private  String rollNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {this.roomNo = roomNo;}

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentStr() { return commentStr;}

    public void setCommentStr(String commentStr){ this.commentStr = commentStr;}

}
