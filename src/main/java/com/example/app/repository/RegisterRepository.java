package com.example.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.Post;
import com.example.app.model.Register;
import com.example.app.model.User;

public interface RegisterRepository extends JpaRepository<Register, Long> {
	
	Optional<Register> findByPostAndUser(Post post, User currentUser);

	List<Register> findByPost(Post post);

	List<Register> findByUser(User user);
}
