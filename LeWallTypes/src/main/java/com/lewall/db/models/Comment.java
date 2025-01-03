package com.lewall.db.models;

import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

/**
 * Comment
 * 
 * This class represents a comment object that can be added to a post or another
 * comment.
 * 
 * 
 * @author Shrey Agarwal, Mahit Mehta, Ates Isfendiyaroglu
 * @version 8 December 2024
 * 
 */
public class Comment extends Model {
    private UUID userId;
    private UUID postId;
    private String messageComment;
    private String date;
    private int likes;
    private Set<String> likedBy;
    private int dislikes;
    private Set<String> dislikedBy;
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
    public Comment(UUID userId, UUID postId, String messageComment, String date, int likes, int dislikes) {
        super();

        this.postId = postId;
        this.userId = userId;
        this.messageComment = messageComment;
        this.date = date;
        this.likes = likes;
        this.likedBy = new HashSet<>();
        this.dislikes = dislikes;
        this.dislikedBy = new HashSet<>();
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
     * Getter for dislikes
     * 
     * @return dislikes
     */
    public int getDislikes() {
        return dislikes;
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
    public Set<String> getLikedBy() {
        return likedBy;
    }

    /**
     * Getter for dislikedBy
     *
     * @return dislikedBy
     */
    public Set<String> getDislikedBy() {
        return dislikedBy;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setDislikedBy(Set<String> dislikedBy) {
        this.dislikedBy = dislikedBy;
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
    public void setLikedBy(Set<String> likedBy) {
        this.likedBy = likedBy;
    }

    /**
     * Toggle like of a post
     * 
     * @param userId
     */
    public void toggleLike(String userId) {
        if (likedBy.contains(userId)) {
            likedBy.remove(userId);
            this.likes--;
            return;
        }

        if (dislikedBy.contains(userId)) {
            dislikedBy.remove(userId);
            this.dislikes--;
        }

        likedBy.add(userId);
        this.likes++;
    }

    /**
     * Toggle dislike of a post
     * 
     * @param userId
     * @return boolean
     */
    public void toggleDislike(String userId) {
        if (dislikedBy.contains(userId)) {
            dislikedBy.remove(userId);
            this.dislikes--;
            return;
        }

        if (likedBy.contains(userId)) {
            likedBy.remove(userId);
            this.likes--;
        }

        dislikedBy.add(userId);
        this.dislikes++;
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
     * compereTo method for Comment
     * 
     * @param comment
     */
    public int compareTo(Comment comment) {
        String[] date1 = this.date.split("/");
        String[] date2 = comment.getDate().split("/");
        if (Integer.parseInt(date1[2]) > Integer.parseInt(date2[2])) {
            return 1;
        } else if (Integer.parseInt(date1[2]) < Integer.parseInt(date2[2])) {
            return -1;
        } else {
            if (Integer.parseInt(date1[0]) > Integer.parseInt(date2[0])) {
                return 1;
            } else if (Integer.parseInt(date1[0]) < Integer.parseInt(date2[0])) {
                return -1;
            } else {
                if (Integer.parseInt(date1[1]) > Integer.parseInt(date2[1])) {
                    return 1;
                } else if (Integer.parseInt(date1[1]) < Integer.parseInt(date2[1])) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
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
