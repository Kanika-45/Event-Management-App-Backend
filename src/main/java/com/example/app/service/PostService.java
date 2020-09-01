package com.example.app.service;

import com.example.app.dto.PostRequest;
import com.example.app.dto.PostResponse;
import com.example.app.exceptions.PostNotFoundException;
import com.example.app.exceptions.SubredditNotFoundException;
import com.example.app.mapper.PostMapper;
import com.example.app.model.Post;
import com.example.app.model.Subreddit;
import com.example.app.model.User;
import com.example.app.repository.PostRepository;
import com.example.app.repository.SubredditRepository;
import com.example.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    //private PostMapper postMapper;
	
	public Post save(Post post) {
        Post save = postRepository.save(post);
		User currentUser = authService.getCurrentUser();
		save.setUser(currentUser);
		Integer count = 0;
		save.setRegisterCount(count);
        return save;
    }
	
	@Transactional(readOnly = true)
    public Post getPost(Long id) {
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new PostNotFoundException(id.toString()));
//        return postMapper.mapToDto(post);
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException(
						"Post Not Found" + id.toString()));
		return post;
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
//        return postRepository.findAll()
//                .stream()
//                .map(postMapper::mapToDto)
//                .collect(toList());
    	return postRepository.findAll();
    }
    
//    @Transactional(readOnly = true)
//    public List<Post> getPostsByUsername(String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//        return postRepository.findByUser(user)
//                .stream()
//                .collect(toList());
//    }
    
    public Post update(Post post, Long id) {
    	Post lastPost = postRepository.findById(id)
              .orElseThrow(() -> new PostNotFoundException(id.toString()));
    	post.setPostId(id);
    	User currentUser = authService.getCurrentUser();
    	post.setUser(currentUser);
    	post.setRegisterCount(lastPost.getRegisterCount());
    	Post update = postRepository.save(post);
		
      return update;
  }
    
    public void delete(Long id) {
    	postRepository.deleteById(id);
    }

}
