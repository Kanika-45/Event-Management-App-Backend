package com.example.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.dto.SubredditDto;
import com.example.app.model.Subreddit;
import com.example.app.service.SubredditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {
	
	private final SubredditService subredditService; 
	
	@PostMapping
	public ResponseEntity<Subreddit> createSubreddit(@RequestBody Subreddit subreddit) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(subredditService.save(subreddit));
	}
	
	@GetMapping
	public ResponseEntity<List<Subreddit>> getAllSubreddits() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(subredditService.getAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Subreddit> getSubreddit(
			@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(subredditService.getSubreddit(id));
	}
	
}