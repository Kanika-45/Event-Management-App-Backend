package com.example.app.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.AuthenticationResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.RefreshTokenRequest;
import com.example.app.dto.RegisterRequest;
import com.example.app.exceptions.AppException;
import com.example.app.model.NotificationEmail;
import com.example.app.model.User;
import com.example.app.model.VerificationToken;
import com.example.app.repository.UserRepository;
import com.example.app.repository.VerificationTokenRepository;
import com.example.app.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		user.setRole("User");
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please activate your account",
				user.getEmail(), "Thank you for signing up to Spring Reddit, " +
		"please click on the below url to activate your account : " +
		"http://localhost:8080/api/auth/accountVerification/" + token));
	}

	private String generateVerificationToken(User user) {
		
		String token = UUID.randomUUID().toString();	
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationTokenRepository.save(verificationToken);
		return token;
	}

	/*Optional is a container object which may or may not contain a 
	 * non-null value. If a value is present, isPresent() will return 
	 * true and get() will return the value.
	 */
	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = 
				verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(()->new AppException(
				"Invalid Token")); 
		fetchUserAndEnable(verificationToken.orElseThrow(() -> 
		new AppException("Invalid Token")));
		
	}
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(
						"User not found " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(
				authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken()
                		.getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider
                		.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .role(getCurrentUser().getRole())
                .build();

	}
	
	@Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = 
        		(org.springframework.security.core.userdetails.User) 
        		SecurityContextHolder.getContext().getAuthentication()
        		.getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                		"User name not found - " + principal.getUsername()));
    }
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		return AuthenticationResponse.builder()
		.authenticationToken(token)
		.refreshToken(refreshTokenRequest.getRefreshToken())
		.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
		.username(refreshTokenRequest.getUsername())
		.build();
		}

}
