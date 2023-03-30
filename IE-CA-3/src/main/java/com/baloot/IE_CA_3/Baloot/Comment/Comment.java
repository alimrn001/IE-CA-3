package com.baloot.IE_CA_3.Baloot.Comment;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Comment {

    private int commentId;

    @SerializedName(value = "userEmail")
    private String username;

    private int commodityId;

    private String text;

    private LocalDate date;

    private int likesNo;

    private int dislikesNo;


    public Comment(int commentId, String username, int commodityId, String text, String date) {
        this.commentId = commentId;
        this.username = username;
        this.commodityId = commodityId;
        this.text = text;
        this.date = LocalDate.parse(date);
        this.likesNo = 0;
        this.dislikesNo = 0;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public void setLikesNo(int likesNo) {
        this.likesNo = likesNo;
    }

    public void setDislikesNo(int dislikesNo) {
        this.dislikesNo = dislikesNo;
    }

    public void addLike() {
        likesNo++;
    }

    public void removeLike() {
        likesNo--;
    }

    public void addDislike() {
        dislikesNo++;
    }

    public void removeDislike() {
        dislikesNo--;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getLikesNo() {
        return likesNo;
    }

    public int getDislikesNo() {
        return dislikesNo;
    }

}
