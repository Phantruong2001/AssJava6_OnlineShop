package com.example.service;

import com.example.model.Share;

public interface ShareService {

	Share save(Share entity);

	Share findById(String id);
}
