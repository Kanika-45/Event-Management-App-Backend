package com.example.app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.RegisterDto;
import com.example.app.dto.RegisterResponse;
import com.example.app.exceptions.AppException;
import com.example.app.exceptions.PostNotFoundException;
import com.example.app.model.NotificationEmail;
import com.example.app.model.Post;
import com.example.app.model.Register;
import com.example.app.model.User;
import com.example.app.repository.PostRepository;
import com.example.app.repository.RegisterRepository;
import com.example.app.repository.UserRepository;

import lombok.AllArgsConstructor;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RegisterService {
	
	private final RegisterRepository registerRepository;
	private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
	
	@Transactional
	public void register(RegisterDto registerDto) {
		Post post = postRepository.findById(registerDto.getPostId())
				.orElseThrow(() -> 
				new PostNotFoundException("Post Not Found with ID - " 
				+ registerDto.getPostId()));
		
		Optional<Register> registerByPostAndUser = registerRepository
				.findByPostAndUser(post, authService.getCurrentUser());
		if(registerByPostAndUser.isPresent()) {
			registerRepository.deleteById(registerByPostAndUser.get().getRegisterId());
			post.setRegisterCount(post.getRegisterCount() - 1);
		}
		else {
			registerRepository.save(mapToRegister(registerDto, post));
			String message = mailContentBuilder.build("Dear" + 
			authService.getCurrentUser().getUsername() + 
			" thank you for registering for " + post.getPostName());
			post.setRegisterCount(post.getRegisterCount() + 1);
	        sendCommentNotification(message, authService.getCurrentUser());
			
		}
		
		//registerRepository.save();
		//post.setRegisterCount(post.getRegisterCount() + 1);
		postRepository.save(post);
		
	}
	
	private void sendCommentNotification(String message, User currentUser) {
		mailService.sendMail(new NotificationEmail("Resgistration mail", 
				currentUser.getEmail(), message));
		
	}

	private Register mapToRegister(RegisterDto registerDto, Post post) {
		return Register.builder().post(post).user(authService.getCurrentUser()).build();
	}

	public List<RegisterResponse> getAllRegistrationsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> 
        new PostNotFoundException(postId.toString()));
        List<RegisterResponse> list = registerRepository
        		.findByPost(post).stream().map(this::mapToDto).collect(toList());
        return list;
	}
	
	private RegisterResponse mapToDto(Register register) {
		return RegisterResponse.builder().username(register
				.getUser().getUsername()).email(
						register.getUser().getEmail()).build();

	}
	
	public List<Register> getAllRegistrations() {
        List<Register> list = registerRepository.findAll().stream()
        		.collect(toList());
        //map(this::mapToDto).
        return list;
	}
	
	public List<Post> getAllRegistrationsForUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(
						"User does not exist with username " + username));
		ArrayList<Post> posts = new ArrayList<Post>();
		List<Register> register = registerRepository.findByUser(user);
		register.forEach((r) -> posts.add(r.getPost()));
		return posts;
		//return registerRepository.findByUser(user).stream()
		//.collect(toList());
		
	}
	
	public boolean isRegisteredForPost(String username, Long id) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(
						"User does not exist with username " + username));
		Post post = postRepository.findById(id)
				.orElseThrow(() -> 
				new PostNotFoundException("Post Not Found with ID - " 
				+ id));
		Optional<Register> registerByPostAndUser = registerRepository
				.findByPostAndUser(post, user);
		if(registerByPostAndUser.isPresent()) {
			return true;
		}
		else return false;
	}
	
}
