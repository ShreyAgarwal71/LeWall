package com.lewall.resolvers;

import java.util.UUID;

import com.lewall.api.InternalServerError;
import com.lewall.api.Request;
import com.lewall.resolvers.ResolverTools.AuthGuard;
import com.lewall.resolvers.ResolverTools.BaseResolver;
import com.lewall.resolvers.ResolverTools.Endpoint;
import com.lewall.resolvers.ResolverTools.Resolver;
import com.lewall.services.PostService;
import com.lewall.dtos.CreatePostDTO;
import com.lewall.dtos.DeletePostDTO;
import com.lewall.dtos.UnlikePostDTO;
import com.lewall.dtos.LikePostDTO;
import com.lewall.dtos.CommentsDTO;
import com.lewall.dtos.PostCommentsDTO;

@Resolver(basePath = "/post")
public class PostResolver implements BaseResolver {
    @AuthGuard()
    @Endpoint(endpoint = "/create", method = Request.EMethod.POST, requestBodyType = CreatePostDTO.class)
    public void createPost(Request<CreatePostDTO> request) {
        UUID userId = request.getUserId();
        String messagePost = request.getBody().getMessagePost();
        String date = request.getBody().getDate();
        int likes = 0;
        String imageURL = request.getBody().getImageURL();

        if (!PostService.createPost(userId, messagePost, date, likes, imageURL)) {
            throw new InternalServerError("Failed to Create Post");
        }
    }

    @AuthGuard()
    @Endpoint(endpoint = "/delete", method = Request.EMethod.POST, requestBodyType = DeletePostDTO.class)
    public void deletePost(Request<DeletePostDTO> request) {
        UUID postId = request.getBody().getPostId();

        if (!PostService.deletePost(postId)) {
            throw new InternalServerError("Failed to Delete Post");
        }
    }

    @AuthGuard()
    @Endpoint(endpoint = "/like", method = Request.EMethod.POST, requestBodyType = LikePostDTO.class)
    public void likePost(Request<LikePostDTO> request) {
        UUID postId = request.getBody().getPostId();
        UUID userId = request.getUserId();

        if (!PostService.likePost(userId, postId)) {
            throw new InternalServerError("Failed to Like Post");
        }
    }

    @AuthGuard()
    @Endpoint(endpoint = "/unlike", method = Request.EMethod.POST, requestBodyType = UnlikePostDTO.class)
    public void unlikePost(Request<UnlikePostDTO> request) {
        UUID postId = request.getBody().getPostId();
        UUID userId = request.getUserId();

        if (!PostService.unlikePost(userId, postId)) {
            throw new InternalServerError("Failed to Unlike Post");
        }
    }

    @AuthGuard()
    @Endpoint(endpoint = "/getComments", method = Request.EMethod.GET, requestBodyType = PostCommentsDTO.class, responseBodyType = CommentsDTO.class)
    public CommentsDTO getPosts(Request<PostCommentsDTO> request) {
        UUID postId = request.getBody().getPostId();

        CommentsDTO comments = new CommentsDTO(PostService.getComments(postId));
        if (PostService.getComments(postId) == null) {
            throw new InternalServerError("Failed to get Comments");
        }

        return comments;
    }
}
