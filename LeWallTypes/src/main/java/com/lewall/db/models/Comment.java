package com.lewall.db.models;

import java.util.UUID;

/**
 * Comment
 * 
 * This class represents a comment object that can be added to a post or another
 * comment.
 * 
 * 
 * @author Shrey Agarwal and Mahit Mehta
 * @version 11/12/2024
 * 
 */
public class Comment extends Model {
    private UUID userId;
    private UUID postId;
    private String messageComment;
    private String date;
    private int likes;
    private String[] likedBy;
    private Comment[] comments;

    /**
     * Constructor for Comment
     * 
     * @param postId
     * @param userId
     * @param messageComment
     * @param date
     * @param likes
     * @param comments
     */
    public Comment(UUID userId, UUID postId, String messageComment, String date, int likes) {
        super();

        this.postId = postId;
        this.userId = userId;
        this.messageComment = messageComment;
        this.date = date;
        this.likes = likes;
        this.likedBy = new String[0];
        this.comments = new Comment[0];
    }

    /**
     * Getter for messageComment
     * 
     * @return messageComment
     */
    public String getMessageComment() {
        return messageComment;
    }

    /**
     * Getter for userId
     * 
     * @return userId
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Getter for date
     * 
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for postId
     * 
     * @return postId
     */
    public UUID getPostId() {
        return postId;
    }

    /**
     * Getter for likes
     * 
     * @return likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Getter for comments
     * 
     * @return comments
     */
    public Comment[] getComments() {
        return comments;
    }

    /**
     * Getter for likedBy
     * 
     * @return likedBy
     */
    public String[] getLikedBy() {
        return likedBy;
    }

    /**
     * Setter for comments
     * 
     * @param comments
     */
    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    /**
     * Setter for messageComment
     * 
     * @param messageComment
     */
    public void setMessageComment(String messageComment) {
        this.messageComment = messageComment;
    }

    /**
     * Setter for userId
     * 
     * @param userId
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Setter for date
     * 
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Setter for postId
     * 
     * @param postId
     */
    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    /**
     * Setter for likes
     * 
     * @param likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Setter for likedBy
     * 
     * @param likedBy
     */
    public void setLikedBy(String[] likedBy) {
        this.likedBy = likedBy;
    }

    /**
     * Add a like to the comment
     * 
     * @param userId
     */
    public void addLike(String userId) {
        this.likes++;
        String[] newLikedBy = new String[likedBy.length + 1];
        for (int i = 0; i < likedBy.length; i++) {
            newLikedBy[i] = likedBy[i];
        }
        newLikedBy[likedBy.length] = userId.toString();
        likedBy = newLikedBy;
    }

    /**
     * Remove a like from the comment
     * 
     * @param userId
     */
    public void removeLike(String userId) {
        this.likes--;
        String[] newLikedBy = new String[likedBy.length - 1];
        int j = 0;
        for (int i = 0; i < likedBy.length; i++) {
            if (!likedBy[i].equals(userId.toString())) {
                newLikedBy[j] = likedBy[i];
                j++;
            }
        }
        likedBy = newLikedBy;
    }

    /**
     * Equals method for Comment
     * 
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Comment) {
            Comment other = (Comment) obj;
            return this.userId.equals(other.userId) && this.postId.equals(other.postId)
                    && this.messageComment.equals(other.messageComment) && this.date.equals(other.date)
                    && this.likes == other.likes;
        }
        return false;
    }

    /**
     * toString method for Comment
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "Commented by " + userId + " on post " + postId + " that said " + messageComment + " on " + date
                + " with " + likes + " likes";
    }

}
