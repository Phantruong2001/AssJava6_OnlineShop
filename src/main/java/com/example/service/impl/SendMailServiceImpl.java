package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.SendMail;
import com.example.repository.SendMailRepository;
import com.example.service.SendMailService;

@Service
public class SendMailServiceImpl implements SendMailService {
	@Autowired
	SendMailRepository sendMailRepository;

	@Override
	public SendMail save(SendMail entity) {
		SendMail sendMail = sendMailRepository.save(entity);
		return sendMail;
	}

	@Override
	public SendMail findById(String id) {
		SendMail sendMail = sendMailRepository.findById(id).get();
		return sendMail;
	}

}
