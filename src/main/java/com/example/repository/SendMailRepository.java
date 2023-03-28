package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.SendMail;

@Repository
public interface SendMailRepository extends JpaRepository<SendMail, String> {
	Optional<SendMail> findByEmailTo(String emailTo);
}