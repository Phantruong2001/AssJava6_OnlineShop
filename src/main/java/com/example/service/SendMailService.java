package com.example.service;

import com.example.model.SendMail;

public interface SendMailService {

	SendMail save(SendMail entity);

	SendMail findById(String id);

}
