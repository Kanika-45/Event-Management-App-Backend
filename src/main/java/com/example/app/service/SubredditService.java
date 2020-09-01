package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.SubredditDto;
import com.example.app.exceptions.AppException;
//import com.example.app.mapper.SubredditMapper;
import com.example.app.model.Subreddit;
import com.example.app.model.User;
import com.example.app.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	//private final SubredditMapper subredditMapper;
	
//	@Transactional
//	public SubredditDto save(SubredditDto subredditDto) {
//		Subreddit save = subredditRepository.save(
//				mapSubredditDto(subredditDto));
//		subredditDto.setId(save.getId());
//		return subredditDto;
//	}
	
	@Transactional
	public Subreddit save(Subreddit subreddit) {
		Subreddit save = subredditRepository.save(subreddit);
				//mapSubredditDto(subredditDto));
		//subredditDto.setId(save.getId());
		User currentUser = authService.getCurrentUser(); 
		save.setUser(currentUser);
		return save;
	}
	
//	private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//		return Subreddit.builder().name(subredditDto.getName())
//				.description(subredditDto.getDescription()).build();
//	}

	@Transactional(readOnly = true)
	public List<Subreddit> getAll() {
		return subredditRepository.findAll().stream().collect(toList());
	}
	
//	private SubredditDto mapToDto(Subreddit subreddit) {
//		return SubredditDto.builder().name(subreddit.getName())
//				.id(subreddit.getId()).numberOfPosts(
//						subreddit.getPosts().size()).build();
//	}
	
	public Subreddit getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new AppException(
						"No subreddit found with id : " + id));
		//return mapToDto(subreddit);
		return subreddit;
	}
	
}
