package com.lewall.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.lewall.db.collections.UserCollection;
import com.lewall.db.collections.PostCollection;
import com.lewall.db.models.Comment;
import com.lewall.db.models.Post;
import com.lewall.db.models.User;
import com.lewall.helpers.CommentSort;
import com.lewall.db.collections.CommentCollection;
import com.lewall.api.BadRequest;
import com.lewall.common.AggregatedComment;
import com.lewall.common.AggregatedPost;

/**
 * A class that implements Post-managing services
 * 
 * @author Shrey Agarwal, Ates Isfendiyaroglu
 * @version 8 December 2024
 */
public class PostService implements IService {
    private static final UserCollection users = db.getUserCollection();
    private static final PostCollection posts = db.getPostCollection();
    private static final CommentCollection comments = db.getCommentCollection();

    public static boolean createPost(UUID userId, String messagePost, String date, int likes, int dislikes,
            String imageURL, String classId) {
        Post post = new Post(userId, messagePost, date, likes, dislikes, imageURL, classId);
        return posts.addElement(post);
    }

    public static List<AggregatedPost> aggregatePosts(Set<UUID> postIds) {
        List<Post> modelPosts = posts.findAll((post) -> postIds.contains(post.getId()));
        return UserService.getAggregatedPosts(modelPosts);
    }

    /**
     * Delete a post
     * 
     * @param userId
     * @param postId
     * @return
     */
    public static boolean deletePost(UUID userId, UUID postId) {
        User user = users.findOne(u -> u.getId().equals(userId));
        if (user == null) {
            throw new BadRequest("User not found");
        }
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }
        if (!post.getUserId().equals(userId)) {
            throw new BadRequest("Not users post");
        }
        return posts.removeElement(postId);
    }

    /**
     * Toggle like
     * 
     * @param userId
     * @param postId
     * @return updated Post
     */
    public static Post toggleLike(UUID userId, UUID postId) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }

        post.toggleLike(userId.toString());

        if (!posts.updateElement(post.getId(), post)) {
            return null;
        }

        return posts.findById(postId);
    }

    /**
     * Toggle dislike
     * 
     * @param userId
     * @param postId
     * @return updated Post
     */
    public static Post dislikePost(UUID userId, UUID postId) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }

        post.toggleDislike(userId.toString());

        if (!posts.updateElement(post.getId(), post)) {
            return null;
        }

        return posts.findById(postId);
    }

    /**
     * Hide a post
     * 
     * @param userId
     * @param postId
     * @return
     */
    public static boolean hidePost(UUID userId, UUID postId) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }
        User user = users.findOne(u -> u.getId().equals(userId));
        if (user == null) {
            throw new BadRequest("User not found");
        }

        return user.hidePost(postId.toString()) && users.updateElement(user.getId(), user);
    }

    /**
     * Unhide a post, however shouldn't ever be able to unhide a post
     * 
     * @param userId
     * @param postId
     * @return
     */
    public static boolean unhidePost(UUID userId, UUID postId) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }
        User user = users.findOne(u -> u.getId().equals(userId));
        if (user == null) {
            throw new BadRequest("User not found");
        }

        return user.unhidePost(postId.toString()) && users.updateElement(user.getId(), user);
    }

    /**
     * Get all comments for a post
     * 
     * @param postId
     * @return
     */
    public static List<Comment> getComments(UUID postId) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }
        List<Comment> comments1 = comments.commentsByPostId(postId);
        CommentSort.quickSort(comments1, 0, comments1.size() - 1);

        return comments1;
    }

    public static AggregatedComment aggregatedComment(Comment comment) {
        User user = users.findById(comment.getUserId());
        return new AggregatedComment(comment, user);
    }

    public static List<AggregatedComment> aggregatedComments(List<Comment> comments) {
        List<AggregatedComment> aggregatedComments = new ArrayList<>();

        for (Comment comment : comments) {
            aggregatedComments.add(aggregatedComment(comment));
        }

        return aggregatedComments;
    }

    /**
     * Make a post private
     * 
     * @param userId
     * @param postId
     * @param isPrivate
     * @return
     */
    public static boolean makePostPrivate(UUID userId, UUID postId, boolean isPrivate) {
        Post post = posts.findOne(p -> p.getId().equals(postId));
        if (post == null) {
            throw new BadRequest("Post not found");
        }
        User user = users.findOne(u -> u.getId().equals(userId));
        if (user == null) {
            throw new BadRequest("User not found");
        }

        if (!post.getUserId().equals(userId)) {
            throw new BadRequest("Not users post");
        }

        post.setIsPrivate(isPrivate);

        return posts.updateElement(post.getId(), post);
    }
}
