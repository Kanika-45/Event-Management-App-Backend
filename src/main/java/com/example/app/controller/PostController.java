package com.example.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.dto.PostRequest;
import com.example.app.dto.PostResponse;
import com.example.app.model.Post;
import com.example.app.service.PostService;

import lombok.AllArgsConstructor;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
	
	private final PostService postService;
		
	@PostMapping
	public ResponseEntity<Post> save(@RequestBody Post post) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postService.save(post));
	}
		
	@GetMapping
  public ResponseEntity<List<Post>> getAllPosts() {
      return status(HttpStatus.OK).body(postService.getAllPosts());
  }
	
	@GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
		System.out.println("In get");
        return status(HttpStatus.OK).body(postService.getPost(id));
    }
	
//	@GetMapping("by-user/{username}")
//    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {
//        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
//    }
	
	@PutMapping("update/{id}")
	public ResponseEntity<Post> update(@RequestBody Post post, @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postService.update(post, id));
	}
	
	@DeleteMapping("delete/{id}")
	public void delete(@PathVariable Long id) {
		System.out.println("In delete");
		postService.delete(id);
	}
	
}
