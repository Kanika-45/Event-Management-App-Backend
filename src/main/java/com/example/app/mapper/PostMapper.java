package com.example.app.mapper;

//import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.example.app.dto.PostRequest;
import com.example.app.dto.PostResponse;
import com.example.app.model.*;
//import com.programming.techie.springredditclone.repository.CommentRepository;
//import com.programming.techie.springredditclone.repository.VoteRepository;
import com.example.app.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

//import static com.programming.techie.springredditclone.model.VoteType.DOWNVOTE;
//import static com.programming.techie.springredditclone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public interface PostMapper {    

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    Posti map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);

}
