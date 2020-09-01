package com.example.app.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.dto.AuthenticationResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.RefreshTokenRequest;
import com.example.app.dto.RegisterRequest;
import com.example.app.service.AuthService;
import com.example.app.service.RefreshTokenService;
import static org.springframework.http.HttpStatus.OK;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	
	/*@RequestBody and @ResponseBody annotations are used to bind the 
	*HTTP request/response body with a domain object in method parameter 
	*or return type. Behind the scenes, these annotation uses HTTP 
	*Message converters to convert the body of HTTP request/response to 
	*domain objects.
	*/
	@PostMapping("signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", 
				HttpStatus.OK);
		
	}
	
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		authService.verifyAccount(token);
		return new ResponseEntity<>("Account Activated Successfully!!", 
				HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
	
	@PostMapping("refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
	return authService.refreshToken(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
	refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
	return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
	}

}
