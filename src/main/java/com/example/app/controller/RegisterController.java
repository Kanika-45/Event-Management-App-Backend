package com.example.app.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.dto.RegisterDto;
import com.example.app.dto.RegisterResponse;
import com.example.app.model.Post;
import com.example.app.model.Register;
import com.example.app.service.RegisterService;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegisterController {
	
	private final RegisterService registerService;
	
	@PostMapping
	public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
		registerService.register(registerDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/by-post/{postId}")
    public ResponseEntity<List<RegisterResponse>> getAllRegisterationsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(registerService.getAllRegistrationsForPost(postId));
    }
	
	@GetMapping("")
    public ResponseEntity<List<Register>> getAllRegisterations() {
        return ResponseEntity.status(OK)
                .body(registerService.getAllRegistrations());
    }

	@GetMapping("/by-username/{username}")
	public ResponseEntity<List<Post>> getAllRegistartionForUser(
			@PathVariable String username) {

		return ResponseEntity.status(OK).body(
				registerService.getAllRegistrationsForUser(username));
	}
	@GetMapping("/{username}/{id}")
	public ResponseEntity<Boolean> isRegisteredForPost(@PathVariable
			String username, @PathVariable Long id) {
		return ResponseEntity.status(OK).body(
				registerService.isRegisteredForPost(username, id));
	}
}
