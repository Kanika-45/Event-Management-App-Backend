package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.model.Post;
//import com.example.app.model.Subreddit;
import com.example.app.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	//List<Post> findAllBySubreddit(Subreddit subreddit);
	
	List<Post> findByUser(User user);

}
