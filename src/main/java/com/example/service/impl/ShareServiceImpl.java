package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Share;
import com.example.repository.ShareRepository;
import com.example.service.ShareService;

@Service
public class ShareServiceImpl implements ShareService {
	@Autowired
	ShareRepository shareRepository;

	@Override
	public Share save(Share entity) {
		Share share = shareRepository.save(entity);
		return share;
	}

	@Override
	public Share findById(String id) {
		if (id == null)
			return null;
		Share share = shareRepository.findById(id).get();
		return share;
	}
}
